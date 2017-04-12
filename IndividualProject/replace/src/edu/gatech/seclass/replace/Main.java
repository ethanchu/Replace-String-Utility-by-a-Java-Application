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
            usage(0);
            return;
        }
        for (int i = 0; i<args.length-3; i++) {
            if (args[i].equals("-f")) {
                    rpl.setfir();
            }
            else if (args[i].equals("-i")) {
                    rpl.setinsen();
             }
            else if (args[i].equals("-w")) {
                    rpl.setwhole();
                    if (args[i+1].length()==1 && (i+1 <(args.length-3))) { // Make sure -w is not the latest control command.
                        i = i+1;
                        rpl.setdelimiter(args[i]);
                    }
            }
            else if (args[i].equals("-x")){
                    if (args[i+1].length()==1 && (i+1 <(args.length-3))) {
                        i = i+1;
                        rpl.setwildcard(args[i]);

                    }
                    else {
                        rpl.setwildcard("x");
                    }
            }
            else {
                usage(0);
                return;
            }
        }
            rpl.setconf(args[args.length-3], args[args.length-2], new File(args[args.length-1]));
            try {
                rpl.replacedone();
            } catch (Exception e){
                usage(1);
                return;
            }
            return;
        }


    private static void usage(int i) {
        if (i==0)
            System.err.println("Usage: Replace [-f] [-i] [-w[char]] [-x[char]] <from> <to> <filename>" );
        if (i==1)
            System.err.println("File Not Found" );
    }

}