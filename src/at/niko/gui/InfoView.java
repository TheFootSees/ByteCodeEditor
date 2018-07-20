package at.niko.gui;

import at.niko.utils.StringUtils;
import org.objectweb.asm.tree.ClassNode;

import javax.swing.*;
import java.awt.*;

public class InfoView extends JPanel {

    private JLabel nameLabel = new JLabel();
    private JLabel superClass = new JLabel();
    private JLabel accessLabel = new JLabel();
    private JLabel fields = new JLabel();
    private JLabel methods = new JLabel();
    private JLabel interfaces = new JLabel();

    public InfoView(){
        this.setLayout(null);

        int yPos = 5;
        nameLabel.setBounds(5, yPos, 200, 20);
        yPos += 20;
        superClass.setBounds(5, yPos, 200, 20);
        yPos += 20;
        accessLabel.setBounds(5, yPos, 200, 20);
        yPos += 20;
        fields.setBounds(5, yPos, 200, 20);
        yPos += 20;
        methods.setBounds(5, yPos, 200, 20);
        yPos += 20;
        interfaces.setBounds(5, yPos, 200, 20);
//        this.add(nameLabel);
//        this.add(superClass);
//        this.add(accessLabel);
//        this.add(fields);
//        this.add(methods);
//        this.add(interfaces);
        JLabel[] labels = new JLabel[]{nameLabel, superClass, accessLabel, fields, methods, interfaces};
        for(int n = 0; n < labels.length; n++){
            JLabel label = labels[n];
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            this.add(label);
        }
    }

    public void update(ClassNode cn){
        nameLabel.setText("Name: " + cn.name);
        superClass.setText("Supername: " + cn.superName);
        accessLabel.setText("Access: " + StringUtils.getAccessStringComma(cn.access));
        fields.setText("Fields: " + String.valueOf(cn.fields.size()));
        methods.setText("Methods: " + String.valueOf(cn.methods.size()));
        interfaces.setText("Interfaces " + String.valueOf(cn.interfaces.size()));
    }

}
