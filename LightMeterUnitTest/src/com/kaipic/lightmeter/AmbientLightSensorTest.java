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
	
	@Test
	public void shouldStopBroadCastingWhenPaused() throws Exception {
		AmbientLightSensor sensor = new AmbientLightSensor();
		LightSensorListener listener = mock(LightSensorListener.class);
		sensor.register(listener);
		sensor.togglePause();
		sensor.broadcast();
		verify(listener, never()).onLightSensorChange();
	}
	
	@Test
	public void togglePausedShouldTogglePauseStatus() throws Exception {
		LightSensor sensor = new AmbientLightSensor();
		assertFalse(sensor.isPaused());
		sensor.togglePause();
		assertTrue(sensor.isPaused());
		sensor.togglePause();
		assertFalse(sensor.isPaused());
	}
}