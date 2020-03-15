package logic;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import gui.graph.GraphInt;
import gui.realtime.RealtimeInt;
import gui.statistic.StatisticInterface;

//uncomment when using raspberry pi
public class RaspberryPiInterface {
    /*

    static GpioController gpiocontroller = GpioFactory.getInstance();
    static GpioPinDigitalOutput seatBeltLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_01); //create a output pin
    static GpioPinDigitalOutput radioLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_06); //create a output pin
    static GpioPinDigitalOutput greenLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_04); //create a output pin
    static GpioPinDigitalOutput redLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_05); //create a output pin
    //todo: we still have two leds for other error leds

    final GpioPinDigitalInput changIntPB = gpiocontroller.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
    final GpioPinDigitalInput radioPB = gpiocontroller.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);

    private RealtimeInt realtimeInt;
    private StatisticInterface statisticInt;
    private GraphInt graphInt;

    static GpioPinDigitalInput[] inputPins;
    private int currentInt;

    public  RaspberryPiInterface(RealtimeInt realtimeInt, StatisticInterface statisticInt, GraphInt graphInt) {
        this.realtimeInt = realtimeInt;
        this.statisticInt = statisticInt;
        this.graphInt = graphInt;
        inputPins = new GpioPinDigitalInput[]{changIntPB, radioPB};

        GpioPinListenerDigital listener  = new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                //System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin().getName() + " = " + event.getState());
                if(event.getPin().getName().equals("GPIO 2") && event.getState() == PinState.HIGH) {
                    //interface button pressed
                    if(currentInt++ > 1) currentInt = 0;
                    changeInterface();
                }
                else if(event.getPin().getName().equals("GPIO 3") && event.getState() == PinState.HIGH) {
                    //radio button pressed
                    toggleRadioLED();
                }
            }
        };
        gpiocontroller.addListener(listener, inputPins);

        //led test
        setSeatBeltLED(true);
    }

    private void changeInterface(){
        switch (currentInt) {
            case 0:
                graphInt.setVisible(false);
                realtimeInt.setVisible(true);
                break;
            case 1:
                realtimeInt.setVisible(false);
                statisticInt.setVisible(true);
                break;
            case 2:
                statisticInt.setVisible(false);
                graphInt.setVisible(true);
                break;
        }
    }

    public void setSeatBeltLED(boolean state) {
        if(state) seatBeltLED.high();
        else seatBeltLED.low();
    }

    public void toggleRadioLED() {
        radioLED.toggle();
    }

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

     */
}