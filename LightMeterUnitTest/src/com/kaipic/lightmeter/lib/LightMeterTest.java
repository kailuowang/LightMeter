package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.ShutterSpeedTest.assertShutterSpeedEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class LightMeterTest {

  @Test
  public void shouldCalculateShutterSpeed() throws Exception {

    LightSensor lightSensor = mock(LightSensor.class);
    when(lightSensor.getEV()).thenReturn(new ExposureValue(10f));
    ShutterSpeed result = new LightMeter(lightSensor).setAperture(4f)
        .calculateShutterSpeed();
    assertShutterSpeedEquals(new ShutterSpeed(1f / 64f), result);
  }

  @Test
  public void shouldCreateWithInitialSettings() throws Exception {
    LightMeter lightMeter = new LightMeter(mock(LightSensor.class));
    assertFalse(lightMeter.getAperture() == null);
  }

  @Test
  public void shouldNotifySubscribedListenersWhenSensorBroadcast() throws Exception {
    LightMeterListener listener = mock(LightMeterListener.class);
    LightSensor sensor = new ManualLightSensor();
    LightMeter meter = new LightMeter(sensor);

    meter.subscribe(listener);
    sensor.broadcast();
    verify(listener, times(1)).onLightMeterChange();
    meter.unsubscribe(listener);
    sensor.broadcast();
    verify(listener, times(1)).onLightMeterChange();
  }

  @Test
  public void shouldUnsubscribeFromSensorIfChanged() throws Exception {
    LightSensor lightSensor1 = mock(LightSensor.class);
    LightSensor lightSensor2 = mock(LightSensor.class);
    LightMeter meter = new LightMeter(lightSensor1);
    meter.setLightSensor(lightSensor2);
    verify(lightSensor1).unsubscribe(meter);
    verify(lightSensor2).subscribe(meter);
  }

}
