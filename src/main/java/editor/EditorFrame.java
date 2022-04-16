package editor;

import contract.IContract;
import contract.structures.Endpoint;
import ui.JGridBagPanel;
import ui.JViewerPanel;
import io.ResultIO;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EditorFrame extends JFrame {

    public static IContract CONTRACT;
    public static IContract PRIOR_CONTRACT;

    JViewerPanel v0;
    EndpointPanel endpointPanel;
    ResolutionPanel resolutionPanel;

    public EditorFrame(IContract contract, IContract priorContract) {
        super();

        CONTRACT = contract;
        PRIOR_CONTRACT = priorContract;

        endpointPanel = getEndpointPanel(contract,priorContract);
        endpointPanel.next.addActionListener(e0 -> {
            resolutionPanel = endpointPanel.getNextPanel();
            resolutionPanel.back.addActionListener(e1 -> {
                getContentPane().remove(resolutionPanel);
                getContentPane().add(endpointPanel, BorderLayout.CENTER);
                repaint();
                revalidate();
            });
            resolutionPanel.submit.addActionListener(e1 -> {
                try {
                    ResultIO.writeToYaml(resolutionPanel.getResult());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.exit(1);
            });
            getContentPane().remove(endpointPanel);
            getContentPane().add(resolutionPanel, BorderLayout.CENTER);
            repaint();
            revalidate();
        });

        setTitle("Contract Evolution Editor");

        getContentPane().setLayout(new BorderLayout());

        JGridBagPanel top = new JGridBagPanel();
        top.load(0,0, new JSeparator(SwingConstants.HORIZONTAL)).add();
        top.load(0,1, Box.createRigidArea(new Dimension(0,20))).add();
        getContentPane().add(top, BorderLayout.PAGE_START);
        getContentPane().add(Box.createRigidArea(new Dimension(0,20)), BorderLayout.PAGE_END);
        getContentPane().add(Box.createRigidArea(new Dimension(20,0)), BorderLayout.LINE_START);
        getContentPane().add(Box.createRigidArea(new Dimension(20,0)), BorderLayout.LINE_END);
        getContentPane().add(endpointPanel, BorderLayout.CENTER);

        setSize(new Dimension(1000, 1000));
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }



    public EndpointPanel getEndpointPanel(IContract contract, IContract priorContract) {
        Set<Endpoint> e0 = contract.getEndpoints();
        Set<Endpoint> e1 = priorContract.getEndpoints();

        Set<Endpoint> intersection = new HashSet<>(e0);
        intersection.retainAll(e1);

        e0.removeAll(intersection);
        e1.removeAll(intersection);

        Map<String,Set<String>> ele0 = new HashMap<>();
        for(Endpoint e : e0) {
            ele0.put(e.toString(), new HashSet<>(contract.getResponses(e)));
        }

        Map<String,Set<String>> ele1 = new HashMap<>();
        for(Endpoint e : e1) {
            ele1.put(e.toString(), new HashSet<>(priorContract.getResponses(e)));
        }

        Map<String,Set<String>> ele2 = new HashMap<>();
        for(Endpoint e : intersection) {
            ele2.put(e.toString(), new HashSet<>(contract.getResponses(e)));
        }

        return new EndpointPanel(
                "Endpoints: " + "./src/main/resources/new.yaml", ele0,
                "Prior Endpoints: " + "./src/main/resources/old.yaml", ele1,
                ele2
        );
    }
}