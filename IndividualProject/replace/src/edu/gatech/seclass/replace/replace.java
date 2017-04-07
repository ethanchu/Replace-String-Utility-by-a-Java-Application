package edu.gatech.seclass.replace;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Created by Yichen Zhu on 2017/4/5.
 */
public class replace {

    private boolean firchoice;
    private int fircount;
    private boolean insenchoice;
    private int insencount;
    private boolean wholechoice;
    private int wholecount;

    private String fromside;
    private String toside;
    private File filepath;


    public replace () {
        firchoice = false;
        fircount = 0;
        insenchoice = false;
        insencount = 0;
        wholechoice = false;
        wholecount = 0;
    }

    public void setfir() throws Exception {
        firchoice = true;
        fircount ++;
        if (fircount > 1)
            throw new Exception("multiple -f arguments");
    }

    public void setinsen() throws Exception {
        insenchoice = true;
        insencount ++;
        if (insencount > 1)
            throw new Exception("multiple -i arguments");
    }

    public void setwhole() throws Exception {
        wholechoice = true;
        wholecount ++;
        if (wholecount > 1)
            throw new Exception("multiple -w arguments");
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
        if (wholechoice) {
            procecontent = replacewhole(content);
        }
        else procecontent = replacenonwhole(content);
        // Write back into file
        FileWriter fileWriter = new FileWriter(filepath);
        fileWriter.write(procecontent);
        fileWriter.close();
    }

    private String replacewhole(String tmp) {
        // Handle with first replace
        if (firchoice) {
            if (insenchoice)
                tmp = tmp.replaceFirst("(?<!\\S)"+"(?i)"+fromside+"(?!\\S)", toside);
            else
                tmp = tmp.replaceFirst("(?<!\\S)"+fromside+"(?!\\S)", toside);
            return tmp;
        }
            // Handle replace
        else {
            if (insenchoice)
                tmp = tmp.replaceAll("(?<!\\S)"+"(?i)"+fromside+"(?!\\S)", toside);
            else
                tmp = tmp.replaceAll("(?<!\\S)"+fromside+"(?!\\S)", toside); //(?<!\\S)  (?!\\S)
            return tmp;
        }
    }

    private String replacenonwhole(String tmp) {
        // Handle with first replace
        if (firchoice) {
            if (insenchoice)
                tmp = tmp.replaceFirst("(?i)"+fromside, toside);
            else
                tmp = tmp.replaceFirst(fromside, toside);
            return tmp;
        }
        // Handle replace
        else {
            if (insenchoice)
                tmp = tmp.replaceAll("(?i)"+fromside, toside);
            else
                tmp = tmp.replaceAll(fromside, toside);
            return tmp;
        }
    }
}
