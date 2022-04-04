package ui;

import contract.IContract;
import contract.OpenApiContract;
import contract.structures.Endpoint;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.apache.commons.lang3.tuple.Pair;
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

    List<MethodBuilder> methodBuilders;

    public MainFrame(IContract contract, IContract priorContract, InitFrame initFrame) {
        this.contract = contract;
        this.priorContract = priorContract;
        this.methodBuilders = new LinkedList<>();

        BBucket bBucket = new BBucket(20,20);

        for (InitFrame.MethodBuilder m : initFrame.methodBuilders) {
            MethodBuilder mp = new MethodBuilder(m);
            methodBuilders.add(mp);
            bBucket.add(mp.getPanel());
        }

        JScrollPane mainPanel = new JScrollPane(bBucket.close());

        JFrame frame = new JFrame();
        frame.setTitle("Compatibly Generator");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
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

        private void priorEndpointChanged(ActionEvent e) {
            List<String> responseStatus = priorContract.getResponses(Endpoint.fromString((String) priorEndpointCombo.getSelectedItem()));
            for(Map.Entry<String, JComboBox<String>> p : responses.entrySet()) {
                p.getValue().setModel(new DefaultComboBoxModel<>(responseStatus.toArray(new String[0])));
                p.getValue().setSelectedItem(p.getKey());
            }
        }
    }

    class MessageBuilder {
        public final JLabel message;
        public final JLabel priorMessage;
        public List<PropertyBuilder> messageBuilders;

        private final JPanel panel;

        MessageBuilder(String message, String priorMessage) {
            this.message = message;
            this.priorMessage = priorMessage;

            this.panel = new JPanel();
        }

        public JPanel getPanel() {
            return panel;
        }
    }

    class PropertyBuilder {
        JLabel property;
        JTextField resolution;
        JComboBox<String> suggestion;
        JCheckBox custom;
    }

    public static void main(String[] args)
    {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        OpenAPI newV = new OpenAPIParser().readLocation("./src/main/resources/new.yaml", null, parseOptions).getOpenAPI();
        OpenAPI oldV = new OpenAPIParser().readLocation("./src/main/resources/old.yaml", null, parseOptions).getOpenAPI();

        InitFrame initFrame = new InitFrame(new OpenApiContract(newV), new OpenApiContract(oldV));

        new MainFrame(new OpenApiContract(newV), new OpenApiContract(oldV), initFrame);
    }
}
