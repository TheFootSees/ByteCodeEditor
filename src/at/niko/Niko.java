package at.niko;

import at.niko.gui.Gui;
import at.niko.jar.JarFile;
import at.niko.jar.JarFileChoosers;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;

public class Niko {

    private static Niko instance;
    private Gui gui;

    private JarFile jarFile;

    public Niko(){
        instance = this;
        gui = new Gui();
    }

    public void openNewFile(){
        File file = JarFileChoosers.open();
        if(jarFile == null){
            jarFile = new JarFile(file);
        }else{
            try {
                jarFile.changeInput(file);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        gui.getJarTree().updateTree();
    }

    public void saveFile(){
        File file = JarFileChoosers.export();
        try {
            jarFile.saveToJar(file);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Niko();
    }

    public static Niko getInstance() {
        return instance;
    }

    public Gui getGui() {
        return gui;
    }

    public JarFile getJarFile() {
        return jarFile;
    }

    public void openClassNode(ClassNode node) {
        gui.getTabView().update(node);
    }

    public void openResource(String name, byte[] data){

    }
}
