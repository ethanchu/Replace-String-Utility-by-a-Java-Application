package edu.gatech.seclass;

/**
 * Created by Yichen Zhu on 2017/3/27.
 */
public class GlitchyClass {

    public int glitchyMethod1(int input) {
        if (input == 3) {
            System.out.println("Catched this Statement");
        }
        return (input / (input - 2));
    }

    public void glitchyMethod2(int input) {
        /*
         If a test suite can achieves 100% branch coverage, it must can achieve 100% statement coverage.
           Thus, it will reveals the fault, based on assumption(2). But it will cause the conflict with the assertion in assumption(1).
         */
    }

    public int glitchyMethod3(int input) {
        int b = 1;
        if (input == 2) {
            b = 0 ;
        }
        return (input/b);
    }

    public int glitchyMethod4(int input) {
        if (input != 2) {
            System.out.println("Catched this Statement");
        }
        return (input / (input - 2));
    }

    public boolean glitchyMethod5 (boolean a, boolean b) {
        int x = 1;
        int y = 1;
        if(a)
            x -=1;
        else
            y+=1;
        if(b)
            x -=1;
        else
            y +=1;
        return (y/x > 0);
    }
// | a | b |output|
// ================
// | T | T | F|
// | T | F | E|
// | F | T | E|
// | F | F | T|
// ================
// Coverage required: path coverage
}

