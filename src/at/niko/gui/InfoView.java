package at.niko.gui;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

import javax.swing.*;

public class InfoView extends JInternalFrame {

    public InfoView(ClassNode cn){
        this.setLayout(null);
        JLabel nameLabel = new JLabel(cn.name);
        JLabel superClass = new JLabel(cn.superName);
        JLabel accessLabel = new JLabel(String.valueOf(cn.access));
        JLabel fields = new JLabel(String.valueOf(cn.fields.size()));
        JLabel methods = new JLabel(String.valueOf(cn.methods.size()));
        JLabel interfaces = new JLabel(String.valueOf(cn.interfaces.size()));
        JLabel[] labels = new JLabel[]{nameLabel, superClass, accessLabel, fields, methods, interfaces};
        int posx = 20;
        int posy = 20;
        for(int n = 0; n < labels.length; n++){
            JLabel label = labels[n];
            label.setLocation(posx, posy);
            this.add(label);
            posy += 20;
        }
    }

}
