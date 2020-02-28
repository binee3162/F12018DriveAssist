package data;


import data.elements.ButtonStatus;
import data.elements.CarTelemetryData;
//import gui.codegeek.RealtimeInt;

import javax.swing.*;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class PacketCarTelemetryData extends Packet {

	private List<CarTelemetryData> carTelemetryData;
	private ButtonStatus buttonStatus; // TODO, create a representation of this data properly
	//private RealtimeInt realtimeInt;
	public PacketCarTelemetryData() {

	}

	@Override
	public void demo() {
		CarTelemetryData ctd = null;
		int i=0;
		float speed=carTelemetryData.get(getHeader().getPlayerCarIndex()).getSpeed();
		log.println( "speed: "+speed);
		//realtimeInt.setSpeedLabel((int)speed);
	}
/*	public void setInterface(RealtimeInt realtimeInt){
		this.realtimeInt=realtimeInt;
	}*/


	public List<CarTelemetryData> getCarTelemetryData() {
		return carTelemetryData;
	}
	public void setCarTelemetryData(List<CarTelemetryData> carTelemetryData) {
		this.carTelemetryData = carTelemetryData;
	}

	public ButtonStatus getButtonStatus() {
		return buttonStatus;
	}

	public void setButtonStatus(ButtonStatus buttonStatus) {
		this.buttonStatus = buttonStatus;
	}

}
