package edu.gatech.seclass;

import org.junit.Test;

/**
 * Created by Yichen Zhu on 2017/3/27.
 */
public class GlitchyClassTestBC1 {
    @Test
    public void testMethod1() throws Exception {
        GlitchyClass buggyClass = new GlitchyClass();
        buggyClass.glitchyMethod1(1);
        buggyClass.glitchyMethod1(3);
    }
}
