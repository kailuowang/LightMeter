package com.kaipic.lightmeter;

public class MockLightSensor extends AmbientLightSensor {

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}
	
	public MockLightSensor setRead(float mockRead)
	{
		this.mRead = mockRead;
		return this;
	}
}
