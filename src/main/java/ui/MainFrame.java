package ui;

import contract.IContract;
import contract.OpenApiContract;
import contract.structures.Endpoint;
import contract.structures.Property;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.apache.commons.lang3.tuple.Pair;
import resolution.LinkResolutionAdviser;
import resolution.ValueResolutionAdviser;
import resolution.structures.Resolution;
import ui.utils.BBucket;
import ui.utils.BColumn;
import ui.utils.BRow;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {

    private final IContract contract;
    private final IContract priorContract;

    private final ValueResolutionAdviser valueResolutionAdviser;
    private final LinkResolutionAdviser linkResolutionAdviser;

    public final JButton submit;
    public final JButton cancel;

    List<MethodBuilder> methodBuilders;

    public MainFrame(IContract contract, IContract priorContract, InitFrame initFrame) {
        this.contract = contract;
        this.priorContract = priorContract;

        this.valueResolutionAdviser = new ValueResolutionAdviser();
        this.linkResolutionAdviser = new LinkResolutionAdviser();

        this.methodBuilders = new LinkedList<>();

        BBucket bBucket = new BBucket(20,20);

        BRow bRow = new BRow(0,5);

        this.submit = new JButton("Submit");
        this.submit.addActionListener(this::initCompleted);
        this.cancel = new JButton("Cancel");
        this.cancel.addActionListener(this::initCanceled);

        bBucket.add(bRow.add(submit).add(cancel).close());

        for (InitFrame.MethodBuilder m : initFrame.methodBuilders) {
            MethodBuilder mp = new MethodBuilder(m);
            methodBuilders.add(mp);
            bBucket.add(mp.getPanel());
        }

        JScrollPane mainPanel = new JScrollPane(bBucket.close());

        setTitle("Compatibly Generator");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(600, 600));
        pack();
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void initCompleted(ActionEvent e) {
        this.setVisible(false);
    }

    public void initCanceled(ActionEvent e) {
        this.dispose();
        System.exit(1);
    }

    class MethodBuilder {
        public Endpoint endpoint;
        public JLabel priorEndpoint;
        public MessageBuilder requestBuilder;
        public List<MessageBuilder> responseBuilders;

        private final JPanel panel;

        public JPanel getPanel() {
            return panel;
        }

        public MethodBuilder(InitFrame.MethodBuilder imb) {
            this.endpoint = imb.endpoint;

            BColumn bColumn = new BColumn(0,5);

            priorEndpoint = new JLabel("prior: " + (String) imb.priorEndpointCombo.getSelectedItem());
            priorEndpoint.setFont(new Font("Arial", Font.PLAIN, 15));
            priorEndpoint.setBorder(BorderFactory.createLineBorder(Color.black));
            bColumn.add(priorEndpoint);

            requestBuilder = new MessageBuilder("request", "request");
            bColumn.add(requestBuilder.getPanel());

            List<Pair<String, String>> responses =
                    imb.responses.entrySet().stream().map(e -> Pair.of(e.getKey(),(String)e.getValue().getSelectedItem())).collect(Collectors.toList());

            responseBuilders = new ArrayList<>(responses.size());

            for(Pair<String, String> rs : responses) {
                MessageBuilder rb = new MessageBuilder(rs.getKey(), rs.getValue());
                responseBuilders.add(rb);
                bColumn.add(rb.getPanel());
            }

            panel = bColumn.close();

            TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), endpoint.toString());
            border.setTitleJustification(TitledBorder.RIGHT);
            panel.setBorder(border);
        }

        class MessageBuilder {
            public final String message;
            public final JLabel priorMessage;
            public List<PropertyBuilder> propertyBuilders;

            public final Set<Property> properties;
            public final Set<Property> priorProperties;

            private final JPanel panel;

            public JPanel getPanel() {
                return panel;
            }

            public MessageBuilder(String message, String priorMessage) {
                this.message = message;

                BColumn bColumn = new BColumn(0,5);

                this.priorMessage = new JLabel("prior: " + priorMessage);
                this.priorMessage.setFont(new Font("Arial", Font.PLAIN, 15));
                bColumn.add(this.priorMessage);

                if(message.contains("request")) {
                    this.properties = contract.getRequestProperties(endpoint);
                    this.priorProperties = priorContract.getRequestProperties(endpoint);
                }
                else {
                    this.properties = contract.getResponseProperties(endpoint, message);
                    this.priorProperties = priorContract.getResponseProperties(endpoint, priorMessage);
                }

                this.propertyBuilders = new ArrayList<>(this.properties.size());

                for (Property property : this.properties) {
                    PropertyBuilder rb = new PropertyBuilder(property);
                    propertyBuilders.add(rb);
                    bColumn.add(rb.getPanel());
                }

                panel = bColumn.close();

                TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), message);
                border.setTitleJustification(TitledBorder.RIGHT);
                panel.setBorder(border);
            }

            class PropertyBuilder {
                public final JCheckBox custom;
                public final JTextField resolution;
                public final JComboBox<String> suggestion;

                private final List<Resolution> suggestions;

                private final JPanel panel;

                public JPanel getPanel() {
                    return panel;
                }

                public PropertyBuilder(Property property) {
                    BColumn bColumn = new BColumn(0,5);

                    BRow bRow = new BRow(0, 5);

                    this.custom = new JCheckBox("Custom");
                    this.custom.addActionListener(this::checkBoxToggled);
                    bRow.add(this.custom);

                    this.resolution = new JTextField("filter");
                    this.resolution.setFont(new Font("Arial", Font.PLAIN, 15));
                    this.resolution.addActionListener(this::resolutionSet);
                    bRow.add(this.resolution);

                    bColumn.add(bRow.close());

                    this.suggestions = linkResolutionAdviser.solve(property, priorProperties);
                    suggestions.addAll(valueResolutionAdviser.solve(property));

                    String[] sgs = suggestions.stream().map(s -> s.resolution).toArray(String[]::new);
                    this.suggestion = new JComboBox<>(sgs);
                    this.suggestion.setSelectedItem(Resolution.LINK + property.key.toString());
                    bColumn.add(suggestion);

                    panel = bColumn.close();

                    TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), property.key.toString());
                    border.setTitleJustification(TitledBorder.RIGHT);
                    panel.setBorder(border);
                }

                private void checkBoxToggled(ActionEvent e) {
                    if(!custom.isSelected()) {
                        this.resolution.setText("filter");
                    }
                    else {
                        this.resolution.setText("rule");
                    }
                }

                private void resolutionSet(ActionEvent e) {
                    if(!custom.isSelected()) {
                        List<String> filter = Arrays.stream(this.resolution.getText().split(" ")).collect(Collectors.toList());

                        String[] sgs = suggestions.stream()
                                .filter(s -> filter.containsAll(s.tags))
                                .map(s -> s.resolution)
                                .toArray(String[]::new);

                        suggestion.setModel(new DefaultComboBoxModel<>(sgs));
                    }
                }
            }
        }
    }
}
