package at.niko.jar;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class JarFileChoosers {

    public static JFileChooser inputChooser = new JFileChooser();
    public static JFileChooser outputChooser = new JFileChooser();

    public static File open(){
        inputChooser.addChoosableFileFilter(new FileNameExtensionFilter(".jars", ".jar"));
        inputChooser.showOpenDialog(null);
        return inputChooser.getSelectedFile();
    }

    public static File export(){
        outputChooser.addChoosableFileFilter(new FileNameExtensionFilter(".jars", ".jar"));
        outputChooser.showSaveDialog(null);
        return outputChooser.getSelectedFile();
    }

}
