import java.util.*;

public class Firewall {
	protected static int portRange = 65535;	
	protected static String[] directions = {"inbound", "outbound"};
	protected static String[] protocols = {"tcp", "udp"};
	
	protected List<List<List<TreeSet<Range>>>> allowedList;
	
	public Firewall(String fpath) {
		allowedList = new ArrayList<List<List<TreeSet<Range>>>>(directions.length);
		for (int i=0; i<directions.length; i++) {
			allowedList.add(new ArrayList<List<TreeSet<Range>>>(protocols.length));
			for (int j=0; j<protocols.length; j++) {
				allowedList.get(i).add(new ArrayList<TreeSet<Range>>(portRange));
				for (int k=0; k<portRange; k++) {
					allowedList.get(i).get(j).add(new TreeSet<Range>());
				}
			}
		}
		System.out.println("Firewall initialized");
	}
	
	public boolean accept_packet(String direction, String protocol, int port, 
			String ip) {
		return true;
	}
	
	protected void insert(String direction, String protocol, int ports, int porte,
			String ips, String ipe) {
		long s = convertIP(ips);
		long e = convertIP(ipe);
		List<TreeSet<Range>> l = allowedList.get(convertDirection(direction))
				.get(convertProtocol(protocol));
		for (int i=ports; i<= porte; i++) {
			l.get(i).add(new Range(s, e));
		}
	}
	
	protected void mergeRanges(TreeSet<Range> ranges) {
		if (ranges.size() == 0)
			return;
		Iterator<Range> iter = ranges.iterator();
		Range curr = iter.next();
		Range next;
	    while (iter.hasNext()) {
	    	next = iter.next();
	    	while (curr.end >= next.start) {
	    		curr.end = Math.max(curr.end, next.end);
	    		iter.remove();
	    		if (iter.hasNext()) 
	    			next = iter.next();
	    		else
	    			break;
	    	}
	    	curr = next;
	    }
	}
	
	protected long convertIP(String ip) {
		int base = 256;
		long power = 1;
		long result = 0;
		String[] digits = ip.split(".");
		for (int i=3; i>=0; i--) {
			int d = Integer.parseInt(digits[i]);
			result += power * d;
			power *= base;
		}
		return result;
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
