package logic;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import gui.graph.GraphInt;
import gui.realtime.RealtimeInt;
import sun.nio.cs.Surrogate;


//uncomment when using raspberry pi
public class RaspberryPiInterface {


    static GpioController gpiocontroller = GpioFactory.getInstance();


    static GpioPinDigitalOutput greenLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_06); //create a output pin
    static GpioPinDigitalOutput redLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_07); //create a output pin
    static GpioPinDigitalOutput brakeLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_08); //create a output pin
    //todo: we still have two leds for other error leds

    final GpioPinDigitalInput changIntPBlast = gpiocontroller.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
    final GpioPinDigitalInput changIntPBnext = gpiocontroller.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);
    final GpioPinDigitalInput radioPB = gpiocontroller.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
    final GpioPinDigitalInput changeMode = gpiocontroller.provisionDigitalInputPin(RaspiPin.GPIO_10, PinPullResistance.PULL_DOWN);


    private RealtimeInt realtimeInt;

    private GraphInt graphInt;

    static GpioPinDigitalInput[] inputPins;
    private int currentInt;

    public  RaspberryPiInterface(RealtimeInt realtimeInt, GraphInt graphInt) {
        this.realtimeInt = realtimeInt;

        this.graphInt = graphInt;
        inputPins = new GpioPinDigitalInput[]{changIntPBlast, radioPB,changIntPBnext,changeMode};

        GpioPinListenerDigital listener  = new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                //System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin().getName() + " = " + event.getState());
                if(event.getPin().getName().equals("GPIO 2") && event.getState() == PinState.HIGH) {
                    //interface button pressed
                    if(--currentInt < 0) currentInt = 3;
                    changeInterface();
                }
                else if(event.getPin().getName().equals("GPIO 3") && event.getState() == PinState.HIGH){
                    if(++currentInt >= 4) currentInt = 0;
                    changeInterface();
                }
                else if(event.getPin().getName().equals("GPIO 5") && event.getState() == PinState.HIGH) {
                    //radio button pressed
                    toggleRadioLED();
                }
                else if(event.getPin().getName().equals("GPIO 10") && event.getState() == PinState.HIGH) {
                    //changeMode  button pressed
                    graphInt.getChangeMode().doClick();
                }
            }
        };
        gpiocontroller.addListener(listener, inputPins);


    }

    private void changeInterface(){
        switch (currentInt) {
            case 0:
                graphInt.setVisible(false);
                realtimeInt.setVisible(true);
                break;
            case 1:
                realtimeInt.setVisible(false);
                graphInt.setVisible(true);
                break;
            case 2:
                realtimeInt.setVisible(true);
                graphInt.setVisible(true);
                break;
        }
    }



    public void toggleRadioLED() {
        if(realtimeInt.getRadioIndicator().getText().equals("ON"))
            realtimeInt.setIndicator(false,realtimeInt.getRadioIndicator());
        else realtimeInt.setIndicator(true,realtimeInt.getRadioIndicator());

    }

    public void setTSAL( boolean paused) {
        if(!paused){
            highVoltage();
        }else{
            lowVoltage();
        }
    }
    public void setBrakeLED(float brake){
        if(brake>15) brakeLED.high();
        else brakeLED.low();
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