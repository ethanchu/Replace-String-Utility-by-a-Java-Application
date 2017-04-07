package edu.gatech.seclass.replace;

import java.io.File;
import java.io.IOException;

public class Main {

 /*
Do not alter this class or implement it.
 */

    public static void main(String[] args) {
        // Empty Skeleton Method
        replace rpl = new replace();
        if (args == null || args.length<3) {
            usage();
            return;
        }
        for (int i = 0; i<args.length; i++) {
            if (args[i].equals("-f")) {
                try {
                    rpl.setfir();
                }catch (Exception e){
                    usage();
                    return;
                }
            }
            else if (args[i].equals("-i")) {
                try {
                    rpl.setinsen();
                }catch (Exception e){
                    usage();
                    return;
                }
            }
            else if (args[i].equals("-w")) {
                try {
                    rpl.setwhole();
                }catch (Exception e){
                    usage();
                    return;
                }
            }
            else {
                if (i+2 == args.length -1) {
                    rpl.setconf(args[i], args[i+1], new File(args[i+2]));
                    try {
                        rpl.replacedone();
                    } catch (Exception e){
                        usage();
                        return;
                    }
                    return;
                }
                else {
                    usage();
                    return;
                }
            }
        }
    }

    private static void usage() {
        System.err.println("Usage: Replace [-f] [-i] [-w] <from> <to> <filename>" );
    }

}