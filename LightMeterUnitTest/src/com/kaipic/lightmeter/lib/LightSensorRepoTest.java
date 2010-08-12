package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.ExposureValueTest.assertEVEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class LightSensorRepoTest {
  @Test
  public void shouldUseLightSensorFactoryToCreateLightSensor() throws Exception {
    LightSensorFactory factory = mock(LightSensorFactory.class);
    when(factory.createSensor(LightSensorType.AUTO)).thenReturn(new MockLightSensor());
    LightSensorRepo repo = new LightSensorRepo(factory);
    repo.getSensor(LightSensorType.AUTO.toString());
    verify(factory).createSensor(LightSensorType.AUTO);
  }

  @Test
  public void shouldNotCreateTheSensorWithTheSameTypeMoreThanOnce() throws Exception {
    LightSensorFactory factory = mock(LightSensorFactory.class);
    LightSensor sensor = mock(LightSensor.class);
    when(factory.createSensor(LightSensorType.AUTO)).thenReturn(sensor);
    LightSensorRepo repo = new LightSensorRepo(factory);
    repo.getSensor(LightSensorType.AUTO.toString());
    repo.getSensor(LightSensorType.AUTO.toString());
    verify(factory, times(1)).createSensor(LightSensorType.AUTO);
  }

  @Test
  public void shouldStartTheSensorWhenCreated() throws Exception {
    LightSensorFactory factory = mock(LightSensorFactory.class);
    LightSensor sensor = mock(LightSensor.class);
    when(factory.createSensor(LightSensorType.AUTO)).thenReturn(sensor);
    LightSensorRepo repo = new LightSensorRepo(factory);
    repo.getSensor(LightSensorType.AUTO.toString());
    verify(sensor, times(1)).start();
  }

  @Test
  public void shouldGetManualSensorByString() throws Exception {
    LightSensorRepo repo = new LightSensorRepo(new LightSensorFactory(null));
    LightSensor sensor = repo.getSensor("13");
    assertTrue(sensor instanceof ManualLightSensor);
    sensor.setISO(100);
    assertEVEquals(new ExposureValue(13f), sensor.getEV());
  }

  @Test
  public void shouldStartEveryExisitingSensorWhenStart() throws Exception {
    LightSensorFactory factory = mock(LightSensorFactory.class);
    LightSensor autoSensor = mock(LightSensor.class);
    LightSensor manualSensor = spy(new ManualLightSensor());
    when(factory.createSensor(LightSensorType.AUTO)).thenReturn(autoSensor);
    when(factory.createSensor(LightSensorType.MANUAL)).thenReturn(manualSensor);
    LightSensorRepo repo = new LightSensorRepo(factory);
    repo.getSensor(LightSensorType.AUTO.toString());
    repo.getSensor("10");
    verify(autoSensor, times(1)).start();
    verify(manualSensor, times(1)).start();

    repo.start();

    verify(autoSensor, times(2)).start();
    verify(manualSensor, times(2)).start();
  }

  @Test
  public void shouldStopEveryExisitingSensorWhenStop() throws Exception {
    LightSensorFactory factory = mock(LightSensorFactory.class);
    LightSensor autoSensor = mock(LightSensor.class);
    LightSensor manualSensor = spy(new ManualLightSensor());
    when(factory.createSensor(LightSensorType.AUTO)).thenReturn(autoSensor);
    when(factory.createSensor(LightSensorType.MANUAL)).thenReturn(manualSensor);
    LightSensorRepo repo = new LightSensorRepo(factory);
    repo.getSensor(LightSensorType.AUTO.toString());
    repo.getSensor("10");

    repo.stop();

    verify(autoSensor).stop();
    verify(manualSensor).stop();
  }


}
