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
import ui.utils.*;

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
        public Endpoint priorEndpoint;
        public MessageBuilder requestBuilder;
        public List<MessageBuilder> responseBuilders;

        private final JPanel panel;

        public JPanel getPanel() {
            return panel;
        }

        public MethodBuilder(InitFrame.MethodBuilder imb) {
            this.endpoint = imb.endpoint;
            this.priorEndpoint = Endpoint.fromString((String) Objects.requireNonNull(imb.priorEndpointCombo.getSelectedItem()));

            BBucket bBucket = new BBucket(10,5);

            requestBuilder = new MessageBuilder("request", "request");
            bBucket.add(requestBuilder.getPanel());

            List<Pair<String, String>> responses =
                    imb.responses.entrySet().stream().map(e -> Pair.of(e.getKey(),(String)e.getValue().getSelectedItem())).collect(Collectors.toList());

            responseBuilders = new ArrayList<>(responses.size());

            for(Pair<String, String> rs : responses) {
                MessageBuilder rb = new MessageBuilder(rs.getKey(), rs.getValue());
                responseBuilders.add(rb);
                bBucket.add(rb.getPanel());
            }

            panel = bBucket.close();

            String t = priorEndpoint.toString() + "  to  " + endpoint.toString();
            TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(),t);
            border.setTitleJustification(TitledBorder.CENTER);
            panel.setBorder(border);
        }

        class MessageBuilder {
            public final String message;
            public final String priorMessage;
            public List<PropertyBuilder> propertyBuilders;

            public final Set<Property> properties;
            public final Set<Property> priorProperties;

            private final JPanel panel;

            public JPanel getPanel() {
                return panel;
            }

            public MessageBuilder(String message, String priorMessage) {
                this.message = message;
                this.priorMessage = priorMessage;

                BBucket bBucket = new BBucket(10,5);

                if(message.contains("request")) {
                    this.properties = contract.getRequestProperties(endpoint);
                    this.priorProperties = priorContract.getRequestProperties(endpoint);
                }
                else {
                    this.properties = contract.getResponseProperties(endpoint, message);
                    this.priorProperties = priorContract.getResponseProperties(endpoint, priorMessage);
                }

                this.propertyBuilders = new ArrayList<>(this.properties.size());

                if(this.properties.isEmpty()) {
                   bBucket.add(BFill.of(new JLabel("NONE")));
                }

                for (Property property : this.properties) {
                    PropertyBuilder rb = new PropertyBuilder(property);
                    propertyBuilders.add(rb);
                    bBucket.add(rb.getPanel());
                }

                panel = bBucket.close();

                String t = priorMessage + " to " + message;
                TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), t);
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
                    BBucket bBucket = new BBucket(5,5);

                    BRow bRow = new BRow(0, 5);

                    this.custom = new JCheckBox("Custom");
                    this.custom.addActionListener(this::checkBoxToggled);
                    bRow.add(this.custom);

                    this.resolution = new JTextField();
                    this.resolution.setFont(new Font("Arial", Font.PLAIN, 15));
                    this.resolution.addActionListener(this::resolutionSet);
                    bRow.add(this.resolution);

                    bBucket.add(bRow.close());

                    this.suggestions = linkResolutionAdviser.solve(property, priorProperties);
                    suggestions.addAll(valueResolutionAdviser.solve(property));

                    String[] sgs = suggestions.stream().map(s -> s.resolution).toArray(String[]::new);
                    this.suggestion = new JComboBox<>(sgs);
                    this.suggestion.setSelectedItem(Resolution.LINK + property.key.toString());
                    bBucket.add(suggestion);

                    panel = bBucket.close();

                    TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), property.key.toString());
                    border.setTitleJustification(TitledBorder.LEFT);
                    panel.setBorder(border);
                }

                private void checkBoxToggled(ActionEvent e) {

                }

                private void resolutionSet(ActionEvent e) {
                    if(!custom.isSelected()) {
                        List<String> filter = Arrays.stream(this.resolution.getText().split(" ")).collect(Collectors.toList());

                        String[] sgs = suggestions.stream()
                                .filter(s -> s.tags.containsAll(filter))
                                .map(s -> s.resolution)
                                .toArray(String[]::new);

                        suggestion.setModel(new DefaultComboBoxModel<>(sgs));
                    }
                }
            }
        }
    }
}
