package com.kaipic.lightmeter.lib;

public class LightMeter {
	private LightSensor mLightSensor;
	private float mAperture = 8.0f;
	private int mISO = 100;
	private int mCalibration = 250;

	public LightMeter(LightSensor lightSensor) {
		this.mLightSensor = lightSensor;
	}

	public LightSensor getLightSensor() {
		return mLightSensor;
	}

	public float getAperture() {
		return mAperture;
	}

	public LightMeter setAperture(float aperture) {
		this.mAperture = aperture;
		return this;
	}

	public int getISO() {
		return mISO;
	}

	public LightMeter setISO(int iso) {
		this.mISO = iso;
		return this;
	}

	public int getCalibration() {
		return mCalibration;
	}

	public LightMeter setCalibration(int calibration) {
		this.mCalibration = calibration;
		return this;
	}

	public ShutterSpeed calculateShutterSpeed() {
		return new ShutterSpeed( mAperture, mCalibration, mISO, mLightSensor.read());
	}

}
