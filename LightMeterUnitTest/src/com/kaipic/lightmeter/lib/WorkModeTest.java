package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorkModeTest {

  @Test
  public void lightValueStringWhenSensorNotReady() {
    LightMeter meter = mock(LightMeter.class);
    LightSensor sensor = mock(LightSensor.class);
    when(meter.getLightSensor()).thenReturn(sensor);
    when(sensor.isReady()).thenReturn(false);
    when(sensor.getStatus()).thenReturn("a status");
    WorkMode workMode = new ManualMode(meter);
    assertEquals("a status", workMode.getLightValueString());
  }

  @Test
  public void lightValueStringWhenSensorIsReady() {
    LightMeter meter = mock(LightMeter.class);
    LightSensor sensor = mock(LightSensor.class);
    when(meter.getLightSensor()).thenReturn(sensor);
    when(sensor.isReady()).thenReturn(true);
    ExposureValue exposureValue = new ExposureValue(12);
    when(meter.getISO100EV()).thenReturn(exposureValue);
    WorkMode workMode = new AvMode(meter);
    assertEquals(exposureValue.toDetailString(), workMode.getLightValueString());
  }


}
