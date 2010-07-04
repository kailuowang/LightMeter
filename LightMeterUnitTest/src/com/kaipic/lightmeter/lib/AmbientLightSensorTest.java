package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AmbientLightSensorTest {

  @Test
  public void shouldBeAbleToRegisterListenerAndListenToBroadCast() {
    LightSensor sensor = new AmbientLightSensor();
    LightSensorListener listener = mock(LightSensorListener.class);
    sensor.register(listener);
    sensor.broadcast();
    verify(listener).onLightSensorChange();
  }

  @Test
  public void shouldBeAbleToRegisterListenerMultipleTimesWithoutDuplicatingBroadcasting() {
    LightSensor sensor = new AmbientLightSensor();
    LightSensorListener listener = mock(LightSensorListener.class);
    sensor.register(listener);
    sensor.register(listener);
    sensor.broadcast();
    verify(listener, times(1)).onLightSensorChange();
  }

  @Test
  public void shouldStopBroadCastingWhenPaused() throws Exception {
    LightSensor sensor = new AmbientLightSensor();
    LightSensorListener listener = mock(LightSensorListener.class);
    sensor.register(listener);
    sensor.togglePause();
    sensor.broadcast();
    verify(listener, never()).onLightSensorChange();
  }

  @Test
  public void togglePausedShouldTogglePauseStatus() throws Exception {
    LightSensor sensor = new AmbientLightSensor() {
      public void start() {
      }

      public void stop() {
      }
    };
    assertFalse(sensor.isPaused());
    sensor.togglePause();
    assertTrue(sensor.isPaused());
    sensor.togglePause();
    assertFalse(sensor.isPaused());
  }

  @Test
  public void togglePausedShouldRestart() throws Exception {
    LightSensor sensor = spy(new AmbientLightSensor() {
      public void start() {
      }

      public void stop() {
      }
    });
    assertFalse(sensor.isPaused());
    sensor.togglePause();
    verify(sensor, never()).start();
    sensor.togglePause();
    verify(sensor).start();
  }
}
