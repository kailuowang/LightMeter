package com.kaipic.lightmeter.lib;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class LightSensorTest {
    @Test
    public void shouldGetEVfromISOCalibrationAndIlluminationRead() throws Exception {
       LightSensor sensor = new LightSensor(){
           public float read() {
               return 160f;
           }
       }.setISO(100)
       .setCalibration(250);
       assertEquals(6f, sensor.getEV().getValue(), 0.00001);
    }

    @Test
    public void shouldCreateWithDefaultValues() throws Exception {
       LightSensor sensor = new LightSensor(){ public float read() { return 0; } };
       assertTrue(sensor.getISO() > 0);
       assertTrue(sensor.getCalibration() > 0);
    }

    @Test
	public void shouldLockEVWhenPaused() throws Exception {
		LightSensor lightSensor = spy(new LightSensor(){
            public float read() { return 0; }
        }.setCalibration(250).setISO(100));

		when(lightSensor.read()).thenReturn(160f, 320f, 640f);
		Assert.assertEquals(6f, lightSensor.getEV().getValue(), 0.0001);
	    Assert.assertEquals(7f, lightSensor.getEV().getValue(), 0.0001);
	    lightSensor.togglePause();
	    Assert.assertEquals(7f, lightSensor.getEV().getValue(), 0.0001);
	    lightSensor.togglePause();
	    Assert.assertEquals(8f, lightSensor.getEV().getValue(), 0.0001);
	}
}
