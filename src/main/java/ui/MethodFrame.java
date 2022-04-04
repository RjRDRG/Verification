package ui;

import contract.IContract;
import contract.OpenApiContract;
import contract.structures.Endpoint;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;

public class MethodFrame extends JFrame {

    IContract contract;
    IContract priorContract;

    List<MethodBuilder> methodPanels;

    public MethodFrame(IContract contract, IContract priorContract) {
        this.contract = contract;
        this.priorContract = priorContract;
        this.methodPanels = new LinkedList<>();

        BBucket bBucket = new BBucket(20,20);

        for (Endpoint e : contract.getEndpoints()) {
            MethodBuilder mp = new MethodBuilder(e);
            methodPanels.add(mp);
            bBucket.add(mp.getPanel());
        }

        JScrollPane mainPanel = new JScrollPane(bBucket.close());

        JFrame frame = new JFrame();
        frame.setTitle("Contract Methods");
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
        public JComboBox<String> priorEndpointCombo;
        public Map<String, JComboBox<String>> responses;

        private final JPanel panel;

        public JPanel getPanel() {
            return panel;
        }

        public MethodBuilder(Endpoint endpoint) {
            this.endpoint = endpoint;

            BColumn bColumn = new BColumn(0,5);

            String[] priorEndpoints = priorContract.getEndpoints().stream().map(Endpoint::toString).toArray(String[]::new);
            priorEndpointCombo = new JComboBox<>(priorEndpoints);
            priorEndpointCombo.setSelectedItem(endpoint);
            priorEndpointCombo.addActionListener(this::priorEndpointChanged);

            bColumn.add(new BRow(5,5).add(priorEndpointCombo).close());

            this.responses = new HashMap<>();
            for(String rs : contract.getResponses(endpoint)) {

                BRow bRow = new BRow(5,5);

                JLabel rl = new JLabel(rs);
                rl.setFont(new Font("Arial", Font.PLAIN, 15));

                bRow.add(rl);

                JComboBox<String> rc = new JComboBox<>(new String[0]);
                if(this.priorEndpointCombo.getSelectedItem() != null)
                    priorEndpointChanged(null);
                bRow.add(rc);

                responses.put(rs, rc);

                bColumn.add(bRow.close());
            }

            panel = bColumn.close();

            TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), endpoint.toString());
            border.setTitleJustification(TitledBorder.RIGHT);
            panel.setBorder(border);
        }

        private void priorEndpointChanged(ActionEvent e) {
            String[] responseStatus = priorContract.getResponses(Endpoint.fromString((String) priorEndpointCombo.getSelectedItem())).toArray(new String[0]);
            for(Map.Entry<String, JComboBox<String>> p : responses.entrySet()) {
                p.getValue().setModel(new DefaultComboBoxModel<>(responseStatus));
                p.getValue().setSelectedItem(p.getKey());
            }
        }
    }

    public static void main(String[] args)
    {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        OpenAPI newV = new OpenAPIParser().readLocation("./src/main/resources/new.yaml", null, parseOptions).getOpenAPI();
        OpenAPI oldV = new OpenAPIParser().readLocation("./src/main/resources/old.yaml", null, parseOptions).getOpenAPI();

        new MethodFrame(new OpenApiContract(newV), new OpenApiContract(oldV));
    }
}
