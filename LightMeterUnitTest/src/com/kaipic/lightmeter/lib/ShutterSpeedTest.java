package com.kaipic.lightmeter.lib;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ShutterSpeedTest {
    @Test
    public void shouldConvertToCorrectFormatForValueLessThan1() throws Exception {
        assertEquals("1/50", new ShutterSpeed(1f / 50f).toString());
    }


    @Test
    public void shouldConvertToCorrectFormatForValueLagerThan1() throws Exception {
        assertEquals("2s", new ShutterSpeed(2f).toString());
        assertEquals("1s", new ShutterSpeed(1f).toString());
    }

    @Test
    public void shouldCreateFromApertureCalibrationISO() throws Exception {
        ShutterSpeed shutterSpeed = new ShutterSpeed(new Aperture(4f), 250, 100, 400f);
        assertShutterSpeedEquals(new ShutterSpeed(0.1f), shutterSpeed);
    }

    @Test
    public void shouldCreateFromEVandApertureAndISO() throws Exception {
        ShutterSpeed shutterSpeed = new ShutterSpeed(new Aperture(4f), new ExposureValue(10));
        assertShutterSpeedEquals(new ShutterSpeed(1f/64f), shutterSpeed);
    }

    public static void assertShutterSpeedEquals(ShutterSpeed expected, ShutterSpeed actual) {
        assertEquals(expected.getValue(), actual.getValue(), 0.00001);
    }
}
