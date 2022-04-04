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
import ui.utils.BBucket;
import ui.utils.BColumn;
import ui.utils.BRow;

public class InitFrame extends JFrame {

    private final IContract contract;
    private final IContract priorContract;

    public final JButton submit;
    public final JButton cancel;

    List<MethodBuilder> methodBuilders;

    public InitFrame(IContract contract, IContract priorContract) {
        this.contract = contract;
        this.priorContract = priorContract;
        this.methodBuilders = new LinkedList<>();

        BBucket bBucket = new BBucket(20,20);

        BRow bRow = new BRow(0,5);

        this.submit = new JButton("Submit");
        this.submit.addActionListener(this::initCompleted);
        this.cancel = new JButton("Cancel");
        this.cancel.addActionListener(this::initCanceled);

        bBucket.add(bRow.add(submit).add(cancel).close());

        for (Endpoint e : contract.getEndpoints()) {
            MethodBuilder mp = new MethodBuilder(e);
            methodBuilders.add(mp);
            bBucket.add(mp.getPanel());
        }

        JScrollPane mainPanel = new JScrollPane(bBucket.close());

        setTitle("Compatibly Generator Init");
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
        new MainFrame(contract, priorContract, this);
    }

    public void initCanceled(ActionEvent e) {
        this.dispose();
        System.exit(1);
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

            BColumn bColumn = new BColumn(5,5);

            String[] priorEndpoints = priorContract.getEndpoints().stream().map(Endpoint::toString).toArray(String[]::new);
            priorEndpointCombo = new JComboBox<>(priorEndpoints);
            priorEndpointCombo.setSelectedItem(endpoint.toString());
            priorEndpointCombo.addActionListener(this::priorEndpointChanged);

            bColumn.add(new BRow(5,5).add(priorEndpointCombo).close());

            this.responses = new HashMap<>();
            for(String rs : contract.getResponses(endpoint)) {

                BRow bRow = new BRow(5,5);

                JLabel rl = new JLabel(rs);
                rl.setFont(new Font("Arial", Font.PLAIN, 15));
                bRow.add(rl);

                JComboBox<String> rc;
                if(this.priorEndpointCombo.getSelectedItem() != null) {
                    rc = new JComboBox<>(priorContract.getResponses(Endpoint.fromString((String) priorEndpointCombo.getSelectedItem())).toArray(new String[0]));
                    rc.setSelectedItem(rs);
                }
                else {
                    rc = new JComboBox<>(new String[0]);
                }
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
            List<String> responseStatus = priorContract.getResponses(Endpoint.fromString((String) priorEndpointCombo.getSelectedItem()));
            for(Map.Entry<String, JComboBox<String>> p : responses.entrySet()) {
                p.getValue().setModel(new DefaultComboBoxModel<>(responseStatus.toArray(new String[0])));
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

        new InitFrame(new OpenApiContract(newV), new OpenApiContract(oldV));
    }
}
