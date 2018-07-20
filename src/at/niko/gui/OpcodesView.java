package at.niko.gui;

import at.niko.utils.HtmlUtils;
import at.niko.utils.OpcodeUtil;
import at.niko.utils.StringUtils;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;
import java.awt.*;

public final class OpcodesView extends JList<String> {

    public OpcodesView(){
        this.setFont(new Font("Arial", Font.PLAIN, 13));
    }

    public void update(ClassNode cn){
        DefaultListModel<String> model = new DefaultListModel<>();

        for(Object mo : cn.methods){
            MethodNode mn = (MethodNode) mo;
            if(mn.name.equals("<init>")){
                continue;
            }
            if(mn.name.isEmpty()){
                continue;
            }
            String title = new HtmlUtils().start().color(StringUtils.getAccessStringSpaces(mn.access) + StringUtils.getType(mn.desc) + " " + mn.name + " " + StringUtils.getParamtersString(mn), "black").stop().get();
            model.addElement(title);
            for(Object ao : mn.instructions.toArray()){
                AbstractInsnNode ain = (AbstractInsnNode) ao;
                model.addElement(OpcodeUtil.getName(ain.getOpcode()));
            }
        }

        this.setModel(model);
    }

}
