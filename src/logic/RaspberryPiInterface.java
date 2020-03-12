package logic;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class RaspberryPiInterface {
    static GpioController gpiocontroller = GpioFactory.getInstance();
    static GpioPinDigitalOutput greenLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_04); //create a output pin
    static GpioPinDigitalOutput redLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_05); //create a output pin


    public void setTSAL(boolean game, boolean paused) {
        if(game && !paused){
            highVoltage();
        }else{
            lowVoltage();
        }
    }

    static void lowVoltage() {
        System.out.println("Low voltage is on.");
        greenLED.high();
        redLED.low();
    }

    static void highVoltage() {
        System.out.println("High voltage is on.");
        greenLED.low();
        redLED.blink(300);

    }
}