package io.silverspoon.bulldog.beagleboneblack.io;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cSignalType;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.linux.io.LinuxI2cBus;

public class BBBI2cBus extends LinuxI2cBus {

   private Pin sdaPin;
   private Pin sclPin;
   private int frequency;

   public BBBI2cBus(String name, String deviceFilePath, Pin sdaPin, Pin sclPin, int frequency) {
      super(name, deviceFilePath);
      this.sdaPin = sdaPin;
      this.sclPin = sclPin;
      sdaPin.addFeature(new BBBI2cPinFeature(this, sdaPin, I2cSignalType.SDA));
      sclPin.addFeature(new BBBI2cPinFeature(this, sclPin, I2cSignalType.SCL));
   }

   public Pin getSCL() {
      return sclPin;
   }

   public Pin getSDA() {
      return sdaPin;
   }

   @Override
   public int getFrequency() {
      return frequency;
   }
}
