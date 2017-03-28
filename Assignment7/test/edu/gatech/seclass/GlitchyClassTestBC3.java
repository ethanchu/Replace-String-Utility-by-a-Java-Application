package edu.gatech.seclass;

import org.junit.Test;

/**
 * Created by Yichen Zhu on 2017/3/28.
 */
public class GlitchyClassTestBC3 {
    @Test
    public void testMethod3() throws Exception {
        GlitchyClass buggyClass = new GlitchyClass();
        buggyClass.glitchyMethod3(2);
    }
}
