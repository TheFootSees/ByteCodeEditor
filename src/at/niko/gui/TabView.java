package at.niko.gui;

import javax.swing.*;
import java.awt.*;

public class TabView extends JPanel {

    private JTabbedPane tabPane;
    private JPanel content;

    public TabView(){
        this.setLayout(new BorderLayout());
        tabPane = new JTabbedPane();
        content = new JPanel();
        this.add(tabPane, BorderLayout.NORTH);
        this.add(content, BorderLayout.CENTER);
    }

}
