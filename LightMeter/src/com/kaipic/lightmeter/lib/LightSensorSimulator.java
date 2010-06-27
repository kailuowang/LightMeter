package com.kaipic.lightmeter.lib;

import java.util.Date;
import java.util.Random;

public class LightSensorSimulator extends AmbientLightSensor {
	Date lastReadAt = new Date();
	Random r = new Random();
	
	public float read() {
		long sinceLastRead = (new Date().getTime() - lastReadAt.getTime())/1000;
		
		if(sinceLastRead > 1 )
		{
			mRead = (float)Math.pow(2f, (float)r.nextInt(14));
			lastReadAt = new Date();
		}
		
		return mRead;
	}

    public LightSensorSimulator setRead(float mockRead)
    {
        mRead = mockRead;
        lastReadAt = new Date();
        return this;
    }

    public void broadCast()
	{
		super.broadcast();
	}

	public void start() { }

	public void stop() { }

}
