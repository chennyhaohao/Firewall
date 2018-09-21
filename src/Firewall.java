import java.util.*;

public class Firewall {
	protected static int portRange = 65535;	
	protected static String[] directions = {"inbound", "outbound"};
	protected static String[] protocols = {"tcp", "udp"};
	
	protected List<List<List<TreeSet<Range>>>> allowed;
	
	public Firewall(String fpath) {
		allowed = new ArrayList<List<List<TreeSet<Range>>>>(directions.length);
		for (int i=0; i<directions.length; i++) {
			allowed.add(new ArrayList<List<TreeSet<Range>>>(protocols.length));
			for (int j=0; j<protocols.length; j++) {
				allowed.get(i).add(new ArrayList<TreeSet<Range>>(portRange));
				for (int k=0; k<portRange; k++) {
					allowed.get(i).get(j).add(new TreeSet<Range>());
				}
			}
		}
		System.out.println("Firewall initialized");
	}
	
	public boolean accept_packet(String direction, String protocol, int port, 
			String ip) {
		return true;
	}
	
	protected long convertIP(String ip) {
		return 0;
	}
	
	protected int convertDirection(String dir) {
		for (int i=0; i<directions.length; i++) {
			if (dir.equals(directions[i]))
				return i;
		}
		return -1;
	}
	
	protected int convertProtocol(String pro) {
		for (int i=0; i<protocols.length; i++) {
			if (pro.equals(protocols[i]))
				return i;
		}
		return -1;
	}
}
