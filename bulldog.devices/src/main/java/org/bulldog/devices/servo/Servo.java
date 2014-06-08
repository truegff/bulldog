package org.bulldog.devices.servo;

import org.bulldog.core.gpio.Pwm;
import org.bulldog.core.util.BulldogUtil;

public class Servo {

	private static final float MIN_ANGLE_DEFAULT = 0.05f;
	private static final float MAX_ANGLE_DEFAULT = 0.10f;
	private static final float FREQUENCY_HZ = 50.0f;
	private static final float INITIAL_POSITION_DEFAULT = 0.0f;
	private static final int TIME_PER_DEGREE_DEFAULT = (int)(0.1f / 60.0f * 1000);
	
	private Pwm pwm;
	private float angle;
	private float minAngleDuty;
	private float maxAngleDuty;
	private int degreeMilliseconds; 

	public Servo (Pwm pwm) {
		this(pwm, INITIAL_POSITION_DEFAULT);
	}
	
	public Servo(Pwm pwm, float initialAngle) {
		this(pwm, initialAngle, MIN_ANGLE_DEFAULT, MAX_ANGLE_DEFAULT, TIME_PER_DEGREE_DEFAULT);
	}
	
	public Servo(Pwm pwm, float initialAngle, float minAngleDuty, float maxAngleDuty, int degreeMilliseconds) {
		this.pwm = pwm;
		this.minAngleDuty = minAngleDuty;
		this.maxAngleDuty = maxAngleDuty;
		this.degreeMilliseconds = degreeMilliseconds;
		this.pwm.setFrequency(FREQUENCY_HZ);
		this.pwm.setDuty(getDutyForAngle(initialAngle));
		if(!this.pwm.isEnabled()) {
			this.pwm.enable();
		}
	}
	
	public void setAngle(float degrees) {
		angle = degrees;
		if(angle < 0.0f) {
			angle = 0.0f;
		}
		
		if(angle > 180.0f) {
			angle = 180.0f;
		}
		
		pwm.setDuty(getDutyForAngle(angle));
	}

	public void moveTo(float desiredAngle, int milliseconds) {
		
	}

	public void moveSmoothTo(float desiredAngle, int milliseconds) {
		double delta = Math.abs(angle - desiredAngle);
		int amountSteps = (int)(milliseconds / degreeMilliseconds);
		
		double stepSize = delta / amountSteps;
		double minAngle = Math.min(angle, desiredAngle);
		float[] discreteSteps = new float[amountSteps];
		
		boolean isInverse = angle > desiredAngle;
		for(int i = 0; i < amountSteps; i++) {
			discreteSteps[i] = calculateStep(i * stepSize, delta, minAngle, isInverse);	
			setAngle(discreteSteps[i]);
			BulldogUtil.sleepMs((int)degreeMilliseconds);
		}
	}
		
	private static float calculateStep(double value, double angleDifference, double angleOffset, boolean invert) {
		
		float smoothValue = (float)Math.round(angleDifference / Math.PI * ((Math.PI * value) / angleDifference 
									- Math.cos((Math.PI * value) / angleDifference) 
									* Math.sin((Math.PI * value) / angleDifference))) ;
		if(invert) {
			return (float)(angleDifference + angleOffset - smoothValue);
		} 
		
		return (float)(smoothValue + angleOffset);
	}
	
	
	public float getAngle() {
		return angle;
	}
	
	protected float getDutyForAngle(float angle) {
		float maxAngle = 180.0f;
		float anglePercent = angle/maxAngle;
		float dutyLength = (maxAngleDuty - minAngleDuty) * anglePercent;
		return minAngleDuty + dutyLength;
	}
	
	public Pwm getPwm() {
		return this.pwm;
	}
	
}
