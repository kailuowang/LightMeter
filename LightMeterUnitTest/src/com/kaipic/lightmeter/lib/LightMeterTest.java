package com.kaipic.lightmeter.lib;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.kaipic.lightmeter.lib.ShutterSpeedTest.*;
import org.junit.Test;

public class LightMeterTest {
	
	@Test
	public void shouldCalculateShutterSpeed() throws Exception {
		
		LightSensor lightSensor = mock(LightSensor.class);
		when(lightSensor.read()).thenReturn(400f);
		ShutterSpeed result = new LightMeter(lightSensor).setAperture(4f)
												.setCalibration(250)
												.setISO(100)
												.calculateShutterSpeed();
		assertShutterSpeedEquals(new ShutterSpeed(0.1f), result);
	}

	@Test
	public void shouldCreateWithInitialSettings() throws Exception {
		LightMeter lightMeter = new LightMeter(mock(LightSensor.class));
		assertTrue(lightMeter.getAperture() > 0);
		assertTrue(lightMeter.getCalibration() > 0);
		assertTrue(lightMeter.getISO() > 0);
	}
}
