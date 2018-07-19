package at.niko.utils;

import at.niko.Niko;

import javax.swing.*;

public class Icons {

    private static final String BASIC_PATH = Niko.class.getResource("../../rsc/iconpack_2018_1/").getPath();

    public static final ImageIcon FILE_ICON;
    public static final ImageIcon CLASS_ICON;
    public static final ImageIcon PACKAGE_ICON;

    static {
        FILE_ICON = new ImageIcon(BASIC_PATH + "fileTypes/text.png");
        CLASS_ICON = new ImageIcon(BASIC_PATH + "nodes/class.png");
        PACKAGE_ICON = new ImageIcon(BASIC_PATH + "nodes/package.png");
    }

}
