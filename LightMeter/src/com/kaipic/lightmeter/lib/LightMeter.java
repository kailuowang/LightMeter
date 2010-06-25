package com.kaipic.lightmeter.lib;

public class LightMeter {
	private LightSensor lightSensor;
	private Aperture aperture = new Aperture(8.0f);
	private int iso = 100;
	private int calibration = 250;
	private boolean locked;
	private float lastRead;

	public LightMeter(LightSensor lightSensor) {
		this.lightSensor = lightSensor;
	}

	public boolean isLocked(){
		return locked;
	}

	public LightSensor getLightSensor() {
		return lightSensor;
	}

	public Aperture getAperture() {
		return aperture;
	}
	
	public LightMeter setAperture(Aperture aperture) {
		this.aperture = aperture;
		return this;
	}

	public LightMeter setAperture(float apertureValue) {
		return setAperture(new Aperture(apertureValue));
	}
	
	public int getISO() {
		return iso;
	}

	public LightMeter setISO(int iso) {
		this.iso = iso;
		return this;
	}

	public int getCalibration() {
		return calibration;
	}

	public LightMeter setCalibration(int calibration) {
		this.calibration = calibration;
		return this;
	}

	public ShutterSpeed calculateShutterSpeed() {
		return new ShutterSpeed( aperture, calibration, iso, readLight());
	}

	public float readLight() {
		if(!locked) lastRead = lightSensor.read();
		return lastRead;
	}

	public void lock() {
		locked = true;
	}

	public void unlock() {
		locked = false;
	}

}
