package com.kaipic.lightmeter.lib;

import static org.junit.Assert.*;

import org.junit.Test;

import com.kaipic.lightmeter.lib.AmbientLightSensor;
import com.kaipic.lightmeter.lib.LightSensor;
import com.kaipic.lightmeter.lib.LightSensorListener;

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
		LightSensor sensor = new MockLightSensor();
		assertFalse(sensor.isPaused());
		sensor.togglePause();
		assertTrue(sensor.isPaused());
		sensor.togglePause();
		assertFalse(sensor.isPaused());
	}
	
	@Test
	public void togglePausedShouldRestart() throws Exception {
		LightSensor sensor = spy(new MockLightSensor());
		assertFalse(sensor.isPaused());
		sensor.togglePause();
		verify(sensor, never()).start();
		sensor.togglePause();
		verify(sensor).start();
	}
}
