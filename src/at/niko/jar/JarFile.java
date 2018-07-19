package at.niko.jar;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JarFile {

    private File file;
    private JarContent content;

    public JarFile(File file) {
        this.file = file;
        content = new JarContent();
        try {
            reload();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void reload() throws Throwable{
        content.reset();
        Path path = Paths.get(file.getAbsolutePath());
        JarInputStream jis = new JarInputStream(new BufferedInputStream(Files.newInputStream(path)));

        JarEntry je;
        while((je = jis.getNextJarEntry()) != null){
            if(je.getName().endsWith(".class")){
                String name = je.getName();
                name = name.replaceAll(".class$", "");
                ClassNode node = new ClassNode();
                ClassReader reader = new ClassReader(jis);
                reader.accept(node, ClassReader.SKIP_DEBUG);
                content.classes.put(name, node);
            }
        }
        ZipEntry ze;
        while((ze = jis.getNextEntry()) != null){
            if(!ze.getName().endsWith(".class")){
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;
                while ((count = jis.read(buffer)) != -1) {
                    bos.write(buffer, 0, count);
                }
                content.resources.put(ze.getName(), bos.toByteArray());
            }
        }
    }

    public void saveToJar(File jarFile) throws Throwable{
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile));
        HashMap<String, byte[]> outPut = new HashMap<>(content.resources);
        for(ClassNode cn : content.classes.values()){
            ClassWriter cw = new ClassWriter(0);
            cn.accept(cw);
            outPut.put(cn.name, cw.toByteArray());
        }
        for(String entry : outPut.keySet()){
            String addon = entry.contains(".") ? "" : ".class";
            jos.putNextEntry(new ZipEntry(entry + addon));
            jos.write(outPut.get(entry));
            jos.closeEntry();
        }
        jos.close();
    }

    public void changeInput(File newFile) throws Throwable{
        this.file = newFile;
        reload();
    }


    public File getFile() {
        return file;
    }

    public JarContent getContent() {
        return content;
    }

    public static class JarContent {

        public Map<String, ClassNode> classes = new HashMap<>();
        public Map<String, byte[]> resources = new HashMap<>();

        public void reset(){
            classes.clear();
            resources.clear();
        }

    }

}
