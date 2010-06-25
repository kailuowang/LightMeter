package com.kaipic.lightmeter.lib;

public interface LightSensor {
	float read();

	void start();

	void stop();
	
	void register(LightSensorListener listener);

	public void togglePause();

	public boolean isPaused();

	public String getStatus();
	
}
