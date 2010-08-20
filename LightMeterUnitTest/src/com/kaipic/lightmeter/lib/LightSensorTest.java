package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.ExposureValueTest.assertEVEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class LightSensorTest {
  @Test
  public void shouldGetEVfromISOCalibrationAndIlluminationRead() throws Exception {
    LightSensor sensor = new LightSensor() {
      public float read() {
        return 160f;
      }
    }.setISO(new Iso(100))
        .setCalibration(250);
    assertEVEquals(new ExposureValue(6f), sensor.getEV());
  }

  @Test
  public void shouldGetEV100ValuefromISOCalibrationAndIlluminationRead() throws Exception {
    LightSensor sensor = mockSensor();
    sensor.setISO(new Iso(200));
    when(sensor.getEV()).thenReturn(new ExposureValue(6f));
    assertEVEquals(new ExposureValue(5f), sensor.getISO100EV());
  }

  @Test
  public void shouldCreateWithDefaultValues() throws Exception {
    LightSensor sensor = new LightSensor() {
      public float read() {
        return 0;
      }
    };
    assertTrue(sensor.getISO() != null);
    assertTrue(sensor.getCalibration() > 0);
  }

  @Test
  public void shouldLockEVWhenPaused() throws Exception {
    LightSensor lightSensor = mockSensor().setCalibration(250).setISO(new Iso(100));
    when(lightSensor.read()).thenReturn(160f, 320f, 640f);
    assertEVEquals(new ExposureValue(6f), lightSensor.getEV());
    assertEVEquals(new ExposureValue(7f), lightSensor.getEV());
    lightSensor.togglePause();
    assertEVEquals(new ExposureValue(7f), lightSensor.getEV());
    lightSensor.togglePause();
    assertEVEquals(new ExposureValue(8f), lightSensor.getEV());
  }

  @Test
  public void shouldCallRegisteredListener() throws Exception {
    LightSensor lightSensor = mockSensor();
    LightSensorListener listener = mock(LightSensorListener.class);
    lightSensor.subscribe(listener);
    lightSensor.broadcast();
    verify(listener).onLightSensorChange();
  }

  @Test
  public void shouldStopCallUnregisteredListener() throws Exception {
    LightSensor lightSensor = mockSensor();
    LightSensorListener listener = mock(LightSensorListener.class);
    lightSensor.subscribe(listener);
    lightSensor.broadcast();
    verify(listener, times(1)).onLightSensorChange();
    lightSensor.unsubscribe(listener);
    lightSensor.broadcast();
    verify(listener, times(1)).onLightSensorChange();
  }

  @Test
  public void shouldCalibrateByExposureValue() throws Exception {
    LightSensor sensor = new LightSensor() {
      public float read() {
        return 100;
      }
    };
    sensor.calibrate(new ExposureValue(13));
    assertEVEquals(new ExposureValue(13), sensor.getISO100EV());
  }

  protected LightSensor mockSensor() {
    return spy(new LightSensor() {
      public float read() {
        return 0;
      }
    });
  }

}
