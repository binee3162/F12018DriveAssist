package data;

import data.elements.CarStatusData;

import java.util.List;

public class PacketCarStatusData extends Packet {

	private List<CarStatusData> carStatuses;

	public PacketCarStatusData() {}

	public List<CarStatusData> getCarStatuses() {
		return carStatuses;
	}

	public void setCarStatuses(List<CarStatusData> carStatuses) {
		this.carStatuses = carStatuses;
	}

}
