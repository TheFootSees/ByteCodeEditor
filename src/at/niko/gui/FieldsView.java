package at.niko.gui;

import at.niko.utils.AccessManager;
import at.niko.utils.HtmlUtils;
import at.niko.utils.StringUtils;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import javax.swing.*;
import java.awt.*;

public final class FieldsView extends JList<String> {

    public FieldsView(){
        this.setFont(new Font("Arial", Font.PLAIN, 13));
    }

    public void update(ClassNode cn){
        DefaultListModel<String> model = new DefaultListModel<>();
        for(Object o : cn.fields){
            FieldNode fn = (FieldNode) o;
            String str = new HtmlUtils().start().color(StringUtils.getAccessStringSpaces(fn.access) + StringUtils.getType(fn.desc) + " ", "rgb(255,99,71)").color(fn.name, "red").stop().get();
            model.addElement(str);
        }
        this.setModel(model);
    }

}
