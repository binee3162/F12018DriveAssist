package data;

import data.elements.ParticipantData;

import java.util.List;

public class PacketParticipantsData extends Packet {
	
	private int numCars;
	private List<ParticipantData> participants;
	
	public PacketParticipantsData() {}
	
	public int getNumCars() {
		return numCars;
	}
	public void setNumCars(int numCars) {
		this.numCars = numCars;
	}
	public List<ParticipantData> getParticipants() {
		return participants;
	}
	public void setParticipants(List<ParticipantData> participants) {
		this.participants = participants;
	}
}
