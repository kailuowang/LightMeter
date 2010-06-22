package com.kaipic.lightmeter.lib;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;

public class LightMeterTest {
	
	@Test
	public void shouldCalculateShutterSpeed() throws Exception {
		
		LightSensor lightSensor = mock(LightSensor.class);
		when(lightSensor.read()).thenReturn(400f);
		float result = new LightMeter(lightSensor).setAperture(4f)
												.setCalibration(250)
												.setISO(100)
												.calculateShutterSpeed();
		assertEquals(10f, result, 0.0001);
	}

	@Test
	public void shouldCreateWithInitialSettings() throws Exception {
		LightMeter lightMeter = new LightMeter(mock(LightSensor.class));
		assertTrue(lightMeter.getAperture() > 0);
		assertTrue(lightMeter.getCalibration() > 0);
		assertTrue(lightMeter.getISO() > 0);

	}
}
