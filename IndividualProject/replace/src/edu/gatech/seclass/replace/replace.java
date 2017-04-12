package edu.gatech.seclass.replace;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by Yichen Zhu on 2017/4/5.
 */
public class replace {

    private boolean firchoice;
    private boolean insenchoice;
    private boolean wholechoice;
    private Character delim;
    private Character wildcar;

    private String fromside;
    private String toside;
    private File filepath;


    public replace () {
        firchoice = false;
        insenchoice = false;
        wholechoice = false;
        delim = null;
        wildcar = null;
    }

    public void setfir(){
        firchoice = true;
    }

    public void setinsen() {
        insenchoice = true;
    }

    public void setwhole() {
        wholechoice = true;
    }


    public void setdelimiter(String delimiter) {
        delim = delimiter.charAt(0);
    }

    public void setwildcard(String wildcard) {
        wildcar = wildcard.charAt(0);
        /*vildcarcount ++;
        if (vildcarcount > 1)
            throw new Exception("multiple -x arguments");*/
    }

    public void setconf (String from, String to, File file) {
        fromside = from ;
        toside = to;
        filepath = file;
    }

    public void replacedone() throws Exception {
        String procecontent = "";
        Scanner in = new Scanner(filepath);
        String content = in.useDelimiter("\\Z").next();
        addregex();
        procecontent = replacestring(content);
        // Write back into file
        FileWriter fileWriter = new FileWriter(filepath);
        fileWriter.write(procecontent);
        fileWriter.close();
    }


    private void addregex() {
        if (wildcar != null) {
            char[] fromarray=fromside.toCharArray();
            fromside="";
            for(int i= 0; i< fromarray.length; i++){
                if (fromarray[i] == wildcar){
                    fromside +=".";
                }
                else
                    fromside +=fromarray[i];
            }
        }
        if (insenchoice)
            fromside = "(?i)"+fromside;
        if (wholechoice) {
            if (delim == null)
                fromside = "(?<!\\S)" + fromside + "(?!\\S)";
            else
                fromside = "((?<!(.|\\n|\\r))|(?<=" + delim +"))" + fromside + "((?=" + delim + ")|(?!(.|\\n|\\r)))";
        }
    }

    private String replacestring(String tmp) {
        if (firchoice)
            tmp = tmp.replaceFirst(fromside, toside);
        else
            tmp = tmp.replaceAll(fromside, toside);
        return tmp;
    }

}
