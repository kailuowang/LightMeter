package com.kaipic.lightmeter.lib;

public class MockLightSensorFactory extends LightSensorFactory {
  public MockLightSensorFactory(float mockReading) {
    this.mockReading = mockReading;
  }

  private float mockReading;

  protected LightSensor createAutoLightSensor() {
    return new MockLightSensor().setRead(mockReading);
  }
}
