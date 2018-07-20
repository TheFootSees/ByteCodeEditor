package at.niko.utils;

public final class HtmlUtils {

    private HtmlUtils instance;
    private String outPut = "";

    public HtmlUtils() {
        instance = this;
    }

    public HtmlUtils start(){
        outPut += "<html>";
        return instance;
    }

    public  HtmlUtils stop(){
        outPut += "</html>";
        return instance;
    }

    public HtmlUtils color(String s, String color){
        String template = "<font color='%s'>%s</font>";
        outPut += String.format(template, color, s);
        return instance;
    }

    public String get(){
        return outPut;
    }

}
