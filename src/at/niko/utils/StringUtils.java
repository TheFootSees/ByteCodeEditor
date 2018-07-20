package at.niko.utils;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public final class StringUtils {

    public static String getAccessStringComma(int access){
        List<AccessManager.AccessType> types = AccessManager.getAccesses(access);
        String out = "";

        if(!types.isEmpty()){
            out = types.get(0).name().toLowerCase();
            for(int n = 1; n < types.size(); n++){
                out += ", " + types.get(n).name().toLowerCase();
            }
        }

        return out;
    }

    public static String getAccessStringSpaces(int access){
        List<AccessManager.AccessType> types = AccessManager.getAccesses(access);
        String out = "";

        for(AccessManager.AccessType type : types){
            out += type.name().toLowerCase() + " ";
        }

        return out;
    }

    public static String getType(String desc){
        if(desc.equals("I") || desc.endsWith(")I")){
            return "int";
        }
        if(desc.equals("F") || desc.endsWith(")F")){
            return "float";
        }
        if(desc.equals("D") || desc.endsWith(")D")){
            return "double";
        }
        if(desc.equals("Z") || desc.endsWith(")Z")){
            return "boolean";
        }
        if(desc.equals("J") || desc.endsWith(")J")){
            return "long";
        }
        if(desc.equals("S") || desc.endsWith(")S")){
            return "short";
        }
        if(desc.equals("B") || desc.endsWith(")B")){
            return "byte";
        }
        if(desc.equals("Ljava/lang/String;") || desc.endsWith(")Ljava/lang/String;")){
            return "String";
        }
        if(desc.endsWith(")V")){
            return "void";
        }
        return desc;
    }

    public static String getParamtersString(MethodNode mn){
        String out = "(";
        String desc = mn.desc;

        Type[] types = Type.getArgumentTypes(mn.desc);
        if(types.length != 0){
            out += getType(types[0].toString());
            for(int n = 1; n < types.length; n++){
                out += ", " + getType(types[n].toString());
            }
        }

        out += ")";
        return out;
    }

}
