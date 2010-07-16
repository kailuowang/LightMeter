package com.kaipic.lightmeter.lib;

import android.content.Context;
import org.junit.Test;

import static com.kaipic.lightmeter.lib.ExposureValueTest.assertEVEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class LightSensorRepoTest {
  @Test
  public void shouldCreateAmbientLightSensorForTypeAuto() throws Exception {
    Context context = mock(Context.class);
    LightSensorRepo repo = new LightSensorRepo(context);
    LightSensor sensor = repo.createSensor(LightSensorType.AUTO);
    assertTrue(sensor instanceof AmbientLightSensor);
  }

  @Test
  public void shouldCreateManualLightSensorForTypeManual() throws Exception {
    LightSensorRepo repo = new LightSensorRepo(null);
    LightSensor sensor = repo.createSensor(LightSensorType.MANUAL);
    assertTrue(sensor instanceof ManualLightSensor);    
  }

  @Test
  public void shouldGetAutoSensorByString() throws Exception {
    Context context = mock(Context.class);
    LightSensorRepo repo = new LightSensorRepo(context);
    LightSensor sensor = repo.getSensor(LightSensorType.AUTO.toString());
    assertTrue(sensor instanceof AmbientLightSensor);
    assertTrue(sensor == repo.getSensor(LightSensorType.AUTO.toString()));
  }

  @Test
  public void shouldGetAutoSensorByStringZero() throws Exception {
    Context context = mock(Context.class);
    LightSensorRepo repo = new LightSensorRepo(context);
    LightSensor sensor = repo.getSensor("0");
    assertTrue(sensor instanceof AmbientLightSensor);
    assertTrue(sensor == repo.getSensor("0"));
  }

  @Test
  public void shouldGetManualSensorByString() throws Exception {
    LightSensorRepo repo = new LightSensorRepo(null);
    LightSensor sensor = repo.getSensor("13");
    assertTrue(sensor instanceof ManualLightSensor);
    sensor.setISO(100);
    assertEVEquals(new ExposureValue(13f), sensor.getEV());
  }

}
