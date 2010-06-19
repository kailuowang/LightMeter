package com.kaipic.lightmeter;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class AmbientLightSensorTest {

	@Test
	public void shouldBeAbleToRegisterListenerAndListenToBroadCast(){
		AmbientLightSensor sensor = new AmbientLightSensor();
		LightSensorListener listener = mock(LightSensorListener.class);
		sensor.register(listener);
		sensor.broadcast();
		verify(listener).onLightSensorChange();
	}
	@Test
	public void shouldBeAbleToRegisterListenerMultipleTimesWithoutDuplicatingBroadcasting(){
		AmbientLightSensor sensor = new AmbientLightSensor();
		LightSensorListener listener = mock(LightSensorListener.class);
		sensor.register(listener);
		sensor.register(listener);
		sensor.broadcast();
		verify(listener, times(1)).onLightSensorChange();
	}
}
