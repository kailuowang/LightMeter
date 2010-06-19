package com.kaipic.lightmeter;

public interface LightSensor {
	float read();

	void start();

	void stop();
	
	void register(LightSensorListener listener);
	
}
