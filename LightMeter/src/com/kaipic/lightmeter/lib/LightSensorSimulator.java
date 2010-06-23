package com.kaipic.lightmeter.lib;

import java.util.Random;

public class LightSensorSimulator extends AmbientLightSensor {

	Random r = new Random();
	public float read() {
		return (float)Math.pow(2f, (float)r.nextInt(14));
	}

	public void start() { }

	public void stop() { }

	
}
