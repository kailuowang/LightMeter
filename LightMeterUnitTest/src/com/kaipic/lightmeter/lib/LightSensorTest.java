package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.ExposureValueTest.assertEVEquals;
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
       assertEVEquals(new ExposureValue(6f), sensor.getEV());
    }

    @Test
    public void shouldGetEV100ValuefromISOCalibrationAndIlluminationRead() throws Exception {
       LightSensor sensor = mockSensor();
       sensor.setISO(200);
       when(sensor.getEV()).thenReturn(new ExposureValue(6f));
       assertEVEquals(new ExposureValue(5f), sensor.getISO100EV());
    }

    @Test
    public void shouldCreateWithDefaultValues() throws Exception {
       LightSensor sensor = new LightSensor(){ public float read() { return 0; } };
       assertTrue(sensor.getISO() > 0);
       assertTrue(sensor.getCalibration() > 0);
    }

    @Test
	public void shouldLockEVWhenPaused() throws Exception {
        LightSensor lightSensor = mockSensor().setCalibration(250).setISO(100);
		when(lightSensor.read()).thenReturn(160f, 320f, 640f);
		assertEVEquals(new ExposureValue(6f), lightSensor.getEV());
	    assertEVEquals(new ExposureValue(7f), lightSensor.getEV());
	    lightSensor.togglePause();
	    assertEVEquals(new ExposureValue(7f), lightSensor.getEV());
	    lightSensor.togglePause();
	    assertEVEquals(new ExposureValue(8f), lightSensor.getEV());
	}

    protected LightSensor mockSensor(){
        return spy(new LightSensor(){ public float read() { return 0; }});
    }


}
