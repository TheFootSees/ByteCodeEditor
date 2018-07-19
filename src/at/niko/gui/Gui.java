package at.niko.gui;

import at.niko.Niko;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame {

    private JarTree jarTree;
    private TabView tabView;

    public Gui(){
        this.setTitle("ByteCodeEditor by Niko");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(900, 700);
        this.setLayout(new BorderLayout());

        this.add(createMenu(), BorderLayout.NORTH);
        this.add(createSplitPane(), BorderLayout.CENTER);

        this.setVisible(true);
    }

    public JSplitPane createSplitPane(){
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(150);

        jarTree = new JarTree();
        tabView = new TabView();

        splitPane.setLeftComponent(jarTree);
        splitPane.setRightComponent(tabView);
        return splitPane;
    }

    private JMenuBar createMenu(){
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open Jar");
        JMenuItem exportItem = new JMenuItem("Export Jar");
        fileMenu.add(openItem);
        fileMenu.add(exportItem);
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Niko.getInstance().openNewFile();
            }
        });
        exportItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Niko.getInstance().saveFile();
            }
        });

        bar.add(fileMenu);

        return bar;
    }

    public JarTree getJarTree() {
        return jarTree;
    }

    public TabView getTabView() {
        return tabView;
    }
}
