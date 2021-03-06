package at.niko.gui;

import at.niko.Niko;
import at.niko.utils.Icons;
import org.objectweb.asm.tree.ClassNode;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * The file navigation pane.
 *
 * @author Konloch
 * @author WaterWolf
 * @author afffsdd
 */

@SuppressWarnings("serial")
public class JarTree extends JInternalFrame {

    /**
     * The orginal source is from the ByteCodeViewer maintained by Konloch
     * source:
     * https://github.com/Konloch/bytecode-viewer/blob/master/src/the/bytecode/club/bytecodeviewer/gui/FileNavigationPane.java
     */

    JCheckBox exact = new JCheckBox("Exact");
    JButton open = new JButton("+");
    JButton close = new JButton("-");

    MyTreeNode treeRoot = new MyTreeNode("Files:");
    MyTree tree = new MyTree(treeRoot);
    final String quickSearchText = "Quick file search (no file extension)";
    final JTextField quickSearch = new JTextField(quickSearchText);
    boolean cancel = false;

    public KeyAdapter search = new KeyAdapter() {
        @Override
        public void keyPressed(final KeyEvent ke) {
            if (ke.getKeyCode() == KeyEvent.VK_ENTER) {

                final String qt = quickSearch.getText();
                quickSearch.setText("");


                String[] path = null;

                if (qt.contains(".")) {
                    path = qt.split("\\.");
                    String[] path2 = new String[path.length];
                    for (int i = 0; i < path.length; i++) {
                        path2[i] = path[i];
                        if (i + 2 == path.length) {
                            path2[i + 1] = "." + path[i + 1];
                        }
                    }
                } else {
                    path = new String[]{qt};
                }

                MyTreeNode curNode = treeRoot;
                if (exact.isSelected()) {
                    pathLoop:
                    for (int i = 0; i < path.length; i++) {
                        final String pathName = path[i];
                        final boolean isLast = i == path.length - 1;

                        for (int c = 0; c < curNode.getChildCount(); c++) {
                            final MyTreeNode child = (MyTreeNode) curNode.getChildAt(c);
                            System.out.println(pathName + ":" + ((String) child.getUserObject()));

                            if (((String) child.getUserObject()).equals(pathName)) {
                                curNode = child;
                                if (isLast) {
                                    final TreePath pathn = new TreePath(curNode.getPath());
                                    tree.setSelectionPath(pathn);
                                    tree.makeVisible(pathn);
                                    tree.scrollPathToVisible(pathn);
                                    openPath(pathn); //auto open
                                    System.out.println("Found! " + curNode);
                                    break pathLoop;
                                }
                                continue pathLoop;
                            }
                        }

                        System.out.println("Could not find " + pathName);
                        break;
                    }
                } else {
                    {
                        @SuppressWarnings("unchecked")
                        Enumeration<MyTreeNode> enums = curNode.depthFirstEnumeration();
                        while (enums != null && enums.hasMoreElements()) {

                            MyTreeNode node = enums.nextElement();
                            if (node.isLeaf()) {
                                if (((String) (node.getUserObject())).toLowerCase().contains(path[path.length - 1].toLowerCase())) {
                                    TreeNode pathArray[] = node.getPath();
                                    int k = 0;
                                    StringBuffer fullPath = new StringBuffer();
                                    while (pathArray != null
                                            && k < pathArray.length) {
                                        MyTreeNode n = (MyTreeNode) pathArray[k];
                                        String s = (String) (n.getUserObject());
                                        fullPath.append(s);
                                        if (k++ != pathArray.length - 1) {
                                            fullPath.append(".");
                                        }
                                    }
                                    String fullPathString = fullPath.toString();
                                    if (fullPathString != null && fullPathString.toLowerCase().contains(qt.toLowerCase())) {
                                        System.out.println("Found! " + node);
                                        final TreePath pathn = new TreePath(node.getPath());
                                        tree.setSelectionPath(pathn.getParentPath());
                                        tree.setSelectionPath(pathn);
                                        tree.makeVisible(pathn);
                                        tree.scrollPathToVisible(pathn);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    };

    public JarTree() {
        super("Jar Tree");
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        setTitle("Files");

        this.open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final TreeNode root = (TreeNode) tree.getModel().getRoot();
                expandAll(tree, new TreePath(root), true);
            }
        });

        this.close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final TreeNode root = (TreeNode) tree.getModel().getRoot();
                expandAll(tree, new TreePath(root), false);
                tree.expandPath(new TreePath(root));
            }
        });

        this.tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                openPath(tree.getPathForLocation(e.getX(), e.getY()));
            }
        });

        this.tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(final TreeSelectionEvent arg0) {
                if (cancel) {
                    cancel = false;
                    return;
                }
                openPath(arg0.getPath());
            }
        });

        this.tree.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (arg0.getSource() instanceof MyTree) {
                        MyTree tree = (MyTree) arg0.getSource();
                        openPath(tree.getSelectionPath());
                    }
                } else {
                    cancel = true;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                quickSearch.grabFocus();
                quickSearch.setText("" + e.getKeyChar()); // fuck
                cancel = true;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                quickSearch.grabFocus();
                quickSearch.setText("" + e.getKeyChar()); // fuck
                cancel = true;
            }
        });

        quickSearch.addKeyListener(search);

        quickSearch.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(final FocusEvent arg0) {
                if (quickSearch.getText().equals(quickSearchText)) {
                    quickSearch.setText("");
                    quickSearch.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(final FocusEvent arg0) {
                if (quickSearch.getText().isEmpty()) {
                    quickSearch.setText(quickSearchText);
                    quickSearch.setForeground(Color.gray);
                }
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);

        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(quickSearch, BorderLayout.NORTH);
        JPanel p3 = new JPanel(new BorderLayout());
        p3.add(exact, BorderLayout.WEST);
        JPanel p4 = new JPanel(new BorderLayout());
        p4.add(open, BorderLayout.EAST);
        p4.add(close, BorderLayout.WEST);
        p3.add(p4, BorderLayout.EAST);
        p2.add(p3, BorderLayout.SOUTH);

        getContentPane().add(p2, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void openClassFileToWorkSpace(final String name, final ClassNode node) {
        Niko.getInstance().openClassNode(node);
    }

    public void openFileToWorkSpace(String name, byte[] contents) {
        Niko.getInstance().openResource(name, contents);
    }

    public void updateTree() {
        try {
            treeRoot.removeAllChildren();
            MyTreeNode root = new MyTreeNode(Niko.getInstance().getJarFile().getFile().getName());
            treeRoot.add(root);
            ImageRenderer renderer = new ImageRenderer();
            tree.setCellRenderer(renderer);

            if (!Niko.getInstance().getJarFile().getContent().classes.isEmpty()) {
                for (ClassNode c : Niko.getInstance().getJarFile().getContent().classes.values()) {
                    String name = c.name;
                    final String[] spl = name.split("/");
                    if (spl.length < 2) {
                        root.add(new MyTreeNode(name + ".class"));
                    } else {
                        MyTreeNode parent = root;
                        for (int i1 = 0; i1 < spl.length; i1++) {
                            String s = spl[i1];
                            if (i1 == spl.length - 1)
                                s += ".class";
                            MyTreeNode child = null;
                            for (int i = 0; i < parent.getChildCount(); i++) {
                                if (((MyTreeNode) parent.getChildAt(i)).getUserObject()
                                        .equals(s)) {
                                    child = (MyTreeNode) parent.getChildAt(i);
                                    break;
                                }
                            }
                            if (child == null) {
                                child = new MyTreeNode(s);
                                parent.add(child);
                            }
                            parent = child;
                        }
                    }
                }
            }

            if (!Niko.getInstance().getJarFile().getContent().resources.isEmpty()) {
                for (final Map.Entry<String, byte[]> entry : Niko.getInstance().getJarFile().getContent().resources.entrySet()) {
                    String name = entry.getKey();
                    final String[] spl = name.split("/");
                    if (spl.length < 2) {
                        root.add(new MyTreeNode(name));
                    } else {
                        MyTreeNode parent = root;
                        for (final String s : spl) {
                            MyTreeNode child = null;
                            for (int i = 0; i < parent.getChildCount(); i++) {
                                if (((MyTreeNode) parent.getChildAt(i)).getUserObject()
                                        .equals(s)) {
                                    child = (MyTreeNode) parent.getChildAt(i);
                                    break;
                                }
                            }
                            if (child == null) {
                                child = new MyTreeNode(s);
                                parent.add(child);
                            }
                            parent = child;
                        }
                    }
                }
            }



            treeRoot.sort();
            tree.expandPath(new TreePath(tree.getModel().getRoot()));
            tree.updateUI();
        } catch (java.util.ConcurrentModificationException e) {
            //ignore, the last file will reset everything
        }
        // expandAll(tree, true);
    }

    @SuppressWarnings("rawtypes")
    private void expandAll(final JTree tree, final TreePath parent,
                           final boolean expand) {
        // Traverse children
        final TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (final Enumeration e = node.children(); e.hasMoreElements(); ) {
                final TreeNode n = (TreeNode) e.nextElement();
                final TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    public class MyTree extends JTree {

        private static final long serialVersionUID = -2355167326094772096L;
        DefaultMutableTreeNode treeRoot;

        public MyTree(final DefaultMutableTreeNode treeRoot) {
            super(treeRoot);
            this.treeRoot = treeRoot;
        }

        StringMetrics m = null;
    }

    public class MyTreeNode extends DefaultMutableTreeNode {

        private static final long serialVersionUID = -8817777566176729571L;

        public MyTreeNode(final Object o) {
            super(o);
        }

        @Override
        public void insert(final MutableTreeNode newChild, final int childIndex) {
            super.insert(newChild, childIndex);
        }

        public void sort() {
            recursiveSort(this);
        }

        @SuppressWarnings("unchecked")
        private void recursiveSort(final MyTreeNode node) {
            if(node.children == null){
                return;
            }
            Collections.sort(node.children, nodeComparator);
            final Iterator<MyTreeNode> it = node.children.iterator();
            while (it.hasNext()) {
                final MyTreeNode nextNode = it.next();
                if (nextNode.getChildCount() > 0) {
                    recursiveSort(nextNode);
                }
            }
        }

        protected Comparator<MyTreeNode> nodeComparator = new Comparator<MyTreeNode>() {
            @Override
            public int compare(final MyTreeNode o1, final MyTreeNode o2) {
                // To make sure nodes with children are always on top
                final int firstOffset = o1.getChildCount() > 0 ? -1000 : 0;
                final int secondOffset = o2.getChildCount() > 0 ? 1000 : 0;
                return o1.toString().compareToIgnoreCase(o2.toString())
                        + firstOffset + secondOffset;
            }

            @Override
            public boolean equals(final Object obj) {
                return false;
            }

            @Override
            public int hashCode() {
                final int hash = 7;
                return hash;
            }
        };
    }

    /**
     * @author http://stackoverflow.com/a/18450804
     */
    class StringMetrics {

        Font font;
        FontRenderContext context;

        public StringMetrics(Graphics2D g2) {

            font = g2.getFont();
            context = g2.getFontRenderContext();
        }

        Rectangle2D getBounds(String message) {

            return font.getStringBounds(message, context);
        }

        double getWidth(String message) {

            Rectangle2D bounds = getBounds(message);
            return bounds.getWidth();
        }

        double getHeight(String message) {

            Rectangle2D bounds = getBounds(message);
            return bounds.getHeight();
        }

    }

    public void resetWorkspace() {
        treeRoot.removeAllChildren();
        tree.repaint();
        tree.updateUI();
    }

    public void openPath(TreePath path) {
        if (path == null)
            return;
        final StringBuffer nameBuffer = new StringBuffer();
        for (int i = 2; i < path.getPathCount(); i++) {
            nameBuffer.append(path.getPathComponent(i));
            if (i < path.getPathCount() - 1) {
                nameBuffer.append("/");
            }
        }

        String name = nameBuffer.toString();
        if (name.endsWith(".class")) {
            final ClassNode cn = Niko.getInstance().getJarFile().getContent().classes.get(name.substring(0, name.length() - ".class".length()));
            if (cn != null) {
                openClassFileToWorkSpace(nameBuffer.toString(), cn);
            }
        } else {
            openFileToWorkSpace(nameBuffer.toString(),  Niko.getInstance().getJarFile().getContent().resources.get(nameBuffer.toString()));
        }
    }

    /**
     * @author http://stackoverflow.com/questions/14968005
     * @author Konloch
     */
    public class ImageRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(
                JTree tree,
                Object value,
                boolean sel,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) { //called every time there is a pane update, I.E. whenever you expand a folder

            super.getTreeCellRendererComponent(tree, value,
                    selected, expanded, leaf, row, hasFocus);

            if (value instanceof MyTreeNode) {
                MyTreeNode node = (MyTreeNode) value;
                String name = node.toString().toLowerCase();

                if (name.endsWith(".jar")) {
                    //setIcon(Resources.jarIcon);
                } else if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".bmp") || name.endsWith(".gif")) {
                    //setIcon(Resources.imageIcon);
                } else if (name.endsWith(".class")) {
                    setIcon(Icons.CLASS_ICON);
                } else if (name.endsWith(".java")) {
                    setIcon(Icons.CLASS_ICON);
                } else if (name.endsWith(".txt") || name.endsWith(".md")) {
                    setIcon(Icons.FILE_ICON);
                } else {
                    setIcon(Icons.PACKAGE_ICON);
                }
            }

            return this;
        }
    }
}