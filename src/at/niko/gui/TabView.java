package at.niko.gui;

import javax.swing.*;
import java.awt.*;

public class TabView extends JPanel {

    private JTabbedPane tabPane;
    private JPanel content;

    public TabView(){
        this.setLayout(new BorderLayout());
        tabPane = new JTabbedPane();

        tabPane.addTab("Info", new JPanel());
        tabPane.addTab("Opcodes", new JPanel());
        tabPane.addTab("Decompiled", new JPanel());

        content = new JPanel();
        this.add(tabPane, BorderLayout.NORTH);
        this.add(content, BorderLayout.CENTER);
    }

}
