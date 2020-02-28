package data;

import data.elements.Header;
//import com.eh7n.f1telemetry.gui.codegeek.RealtimeInt;
import com.fasterxml.jackson.databind.ObjectMapper;


public abstract class Packet {

	private Header header;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public void demo(){

	}
	/*public void setInterface(RealtimeInt realtimeInt){

	}*/
	public String toJSON() {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(this);
		}catch(Exception e) {
			//TODO: Handle this exception
		}
		return json;
	}

}
