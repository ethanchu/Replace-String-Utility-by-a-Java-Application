package edu.gatech.seclass;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Yichen Zhu on 2017/3/27.
 */
public class GlitchyClassTestSC1 {
    @Test
    public void testMethod1() throws Exception {
        GlitchyClass buggyClass = new GlitchyClass();
        buggyClass.glitchyMethod1(2);
    }
}
