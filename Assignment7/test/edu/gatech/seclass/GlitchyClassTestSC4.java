package edu.gatech.seclass;

import org.junit.Test;

/**
 * Created by Yichen Zhu on 2017/3/27.
 */
public class GlitchyClassTestSC4 {
    @Test
    public void testMethod4() throws Exception {
        GlitchyClass buggyClass = new GlitchyClass();
        buggyClass.glitchyMethod4(1);
    }
}
