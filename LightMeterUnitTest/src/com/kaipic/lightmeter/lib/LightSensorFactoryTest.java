package com.kaipic.lightmeter.lib;

import android.content.Context;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class LightSensorFactoryTest {
  @Test
  public void shouldCreateAmbientLightSensorForTypeAuto() throws Exception {
    Context context = mock(Context.class);
    LightSensorFactory repo = new LightSensorFactory(context);
    LightSensor sensor = repo.createSensor(LightSensorType.AUTO);
    assertTrue(sensor instanceof AmbientLightSensor);
  }

  @Test
  public void shouldCreateManualLightSensorForTypeManual() throws Exception {
    LightSensorFactory repo = new LightSensorFactory(null);
    LightSensor sensor = repo.createSensor(LightSensorType.MANUAL);
    assertTrue(sensor instanceof ManualLightSensor);
  }

}
