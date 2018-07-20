package at.niko.utils;

import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public final class AccessManager {

    public static enum AccessType  {
        PRIVATE,
        PROTECTED,
        PUBLIC,
        FINAL,
        STATIC;
    }

    public static List<AccessType> getAccesses(int access){
        List<AccessType> types = new ArrayList<>();
        if(isPublic(access)){
            types.add(AccessType.PUBLIC);
        }
        if(isPrivate(access)){
            types.add(AccessType.PRIVATE);
        }
        if(isProtected(access)){
            types.add(AccessType.PROTECTED);
        }
        if(isStatic(access)){
            types.add(AccessType.STATIC);
        }
        if(isFinal(access)){
            types.add(AccessType.FINAL);
        }
        return types;
    }

    public static boolean isPublic(int access){
        return (access & ACC_PUBLIC) != 0;
    }

    public static boolean isPrivate(int access){
        return (access & ACC_PRIVATE) != 0;
    }

    public static boolean isProtected(int access){
        return (access & ACC_PROTECTED) != 0;
    }

    public static boolean isFinal(int access){
        return (access & ACC_FINAL) != 0;
    }

    public static boolean isStatic(int access){
        return (access & ACC_STATIC) != 0;
    }

}
