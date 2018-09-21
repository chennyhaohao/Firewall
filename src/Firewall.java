import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
		FileReader fr = null;
		BufferedReader br = null;
		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(fpath);
			br = new BufferedReader(fr);

			String line;
			int ports, porte;
			String ips, ipe;
			
			//Parse CSV and insert all rules
			while ((line = br.readLine()) != null) { 
				String[] args = line.split(",");
				String[] portArgs = args[2].split("-");
				ports = Integer.parseInt(portArgs[0]);
				if (portArgs.length == 2)
					porte = Integer.parseInt(portArgs[1]);
				else
					porte = ports;
				String[] ipArgs = args[3].split("-");
				ips = ipArgs[0];
				if (ipArgs.length == 2)
					ipe = ipArgs[1];
				else
					ipe = ips;
				insertRule(args[0], args[1], ports, porte, ips, ipe);
			}
			
			// Then merge ranges for fast queries
			for (List<List<TreeSet<Range>>> i: allowedList) { 
				for (List<TreeSet<Range>> j: i) {
					for (TreeSet<Range> s: j)
						mergeRanges(s);
				}
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	
	public boolean accept_packet(String direction, String protocol, int port, 
			String ip) {
		TreeSet<Range> s = allowedList.get(convertDirection(direction))
				.get(convertProtocol(protocol)).get(port-1);
		Range r = new Range(convertIP(ip), convertIP(ip));
		Range f = s.floor(r);
		if (f == null)
			return false;
		return (f.end >= r.end);
	}
	
	protected void insertRule(String direction, String protocol, int ports, int porte,
			String ips, String ipe) {
		long s = convertIP(ips);
		long e = convertIP(ipe);
		List<TreeSet<Range>> l = allowedList.get(convertDirection(direction))
				.get(convertProtocol(protocol));
		for (int i=ports-1; i<= porte-1; i++) {
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
		for (int i=digits.length-1; i>=0; i--) {
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
