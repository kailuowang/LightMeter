package com.kaipic.lightmeter.lib;

public class ShutterSpeed {
	private float value;

	public ShutterSpeed(float value) {
		this.value = value;
	}
	
	public ShutterSpeed(float aperture, int calibration, int iso, float illumination) {
		this( calibration * aperture * aperture / (illumination * iso));
	}

	public String toString() {
		if(value < 1)
			return "1/" + (int)(1f/value);
		else
			return ((int)value) + "s";
	}

	float getValue() {
		return value;
	}
}
