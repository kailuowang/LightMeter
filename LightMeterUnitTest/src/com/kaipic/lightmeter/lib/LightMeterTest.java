package com.kaipic.lightmeter.lib;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.kaipic.lightmeter.lib.ShutterSpeedTest.*;
import org.junit.Test;

public class LightMeterTest {
	
	@Test
	public void shouldCalculateShutterSpeed() throws Exception {
		
		LightSensor lightSensor = mock(LightSensor.class);
		when(lightSensor.getEV()).thenReturn(new ExposureValue(10f));
		ShutterSpeed result = new LightMeter(lightSensor).setAperture(4f)
												.calculateShutterSpeed();
		assertShutterSpeedEquals(new ShutterSpeed(1f/64f), result);
	}

	@Test
	public void shouldCreateWithInitialSettings() throws Exception {
		LightMeter lightMeter = new LightMeter(mock(LightSensor.class));
		assertFalse(lightMeter.getAperture() == null);
	}
	
}
