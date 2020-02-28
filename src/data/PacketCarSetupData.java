package data;

import data.elements.CarSetupData;

import java.util.List;

public class PacketCarSetupData extends Packet {

	private List<CarSetupData> carSetups;
	
	public PacketCarSetupData() {}
	
	public List<CarSetupData> getCarSetups() {
		return carSetups;
	}
	public void setCarSetups(List<CarSetupData> carSetups) {
		this.carSetups = carSetups;
	}
	
}
