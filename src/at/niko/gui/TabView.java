package at.niko.gui;

import org.objectweb.asm.tree.ClassNode;

import javax.swing.*;
import java.awt.*;

public class TabView extends JPanel {

    private JTabbedPane tabPane;
    private InfoView infoView;
    private FieldsView fieldsView;
    private OpcodesView opcodesView;

    private JScrollPane scrollPaneOpcodes;
    private JScrollPane scrollPaneFields;

    public TabView(){
        this.setLayout(new BorderLayout());
        tabPane = new JTabbedPane();

        infoView = new InfoView();
        fieldsView = new FieldsView();
        opcodesView = new OpcodesView();

        scrollPaneFields = new JScrollPane(fieldsView);
        scrollPaneOpcodes = new JScrollPane(opcodesView);

        tabPane.addTab("Info", infoView);
        tabPane.addTab("Opcodes", scrollPaneOpcodes);
        tabPane.addTab("Decompiled", new JPanel());
        tabPane.addTab("Fields", scrollPaneFields);

        this.add(tabPane, BorderLayout.CENTER);
    }

    public void update(ClassNode cn){
        infoView.update(cn);
        fieldsView.update(cn);
        opcodesView.update(cn);
    }
}
