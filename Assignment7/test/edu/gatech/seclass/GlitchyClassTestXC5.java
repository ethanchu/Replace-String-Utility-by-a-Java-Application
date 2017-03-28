package edu.gatech.seclass;

import org.junit.Test;

/**
 * Created by Yichen Zhu on 2017/3/27.
 */
public class GlitchyClassTestXC5 {
    @Test
    public void testMethod5() throws Exception {
        GlitchyClass buggyClass = new GlitchyClass();
        buggyClass.glitchyMethod5(true, true);
        buggyClass.glitchyMethod5(false, false);
        buggyClass.glitchyMethod5(true, false);
        buggyClass.glitchyMethod5(false, true);
    }
}
