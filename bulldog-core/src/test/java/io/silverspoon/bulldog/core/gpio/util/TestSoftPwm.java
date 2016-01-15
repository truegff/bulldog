package io.silverspoon.bulldog.core.gpio.util;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.mocks.MockedDigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.pwm.SoftPwm;
import io.silverspoon.bulldog.core.util.BulldogUtil;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestSoftPwm {

	private Pin pin;
	
	@Before
	public void setup() {
		pin = new Pin("Testpin", 0, "A", 0);
		MockedDigitalOutput output = new MockedDigitalOutput(pin);
		pin.addFeature(output);
	}
	
	@Test
	@Ignore("Disabled until https://github.com/px3/bulldog/issues/12 is fixed")
	public void testSoftPwm() {
		DigitalOutput output  = pin.as(DigitalOutput.class);
		SoftPwm pwm = new SoftPwm(output);
		pwm.setDuty(0.2);
		pwm.setFrequency(10.0);
		pwm.enable();
	
		BulldogUtil.sleepMs(100);
		//Measure 100 times for one second.
		//20 readings should be high; 
		int highCount = 0;
		int lowCount = 0;
		for(int i = 0; i < 100; i++) {
			Signal signal = output.getAppliedSignal();
			if(signal == Signal.High) {
				highCount++;
			} else {
				lowCount++;
			}
			BulldogUtil.sleepMs(1);
		}
		
		TestCase.assertEquals(80, lowCount);
		TestCase.assertEquals(20, highCount);
		
		pwm.disable();
		pwm.disable(); //should have no effect
		BulldogUtil.sleepMs(50);
	
		Signal signal = output.getAppliedSignal();
		for(int i = 0; i < 100; i++) {
			TestCase.assertEquals(signal, output.getAppliedSignal());
			BulldogUtil.sleepMs(1);
		}
		
	}
	
	
}
