package com.kaipic.lightmeter.lib;

import com.kaipic.lightmeter.lib.AmbientLightSensor;

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
	
	public void broadCast()
	{
		super.broadcast();
	}
}
