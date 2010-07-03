package com.kaipic.lightmeter.lib;

public class LightMeter {
    private LightSensor lightSensor;
    private Aperture aperture = new Aperture(8.0f);
    private boolean locked;
    private float lastRead;

    public LightMeter(LightSensor lightSensor) {
        this.lightSensor = lightSensor;
    }

    public boolean isLocked() {
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

    public LightMeter setISO(int iso) {
        lightSensor.setISO(iso);
        return this;
    }

    public LightMeter setCalibration(int calibration) {
        lightSensor.setCalibration(calibration);
        return this;
    }

    public ShutterSpeed calculateShutterSpeed() {
        return new ShutterSpeed(aperture, lightSensor.getEV());
    }

    public float readLight() {
        if (!locked) lastRead = lightSensor.read();
        return lastRead;
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

}
