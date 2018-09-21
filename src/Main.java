import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;
import java.util.Random;

public class Main {
	public static void main(String[] args) throws java.io.IOException {
		small_test("./small_test.txt");	
		edge_test("./edge_test.txt");
		generateLargeTest("./large_test_.txt");
		large_test("./large_test_.txt");
	}
	
	public static void small_test(String fpath) {
		Firewall f = new Firewall(fpath);
		
		System.out.println(f.accept_packet("inbound", "tcp", 80, "192.168.1.2"));
		 
		System.out.println(f.accept_packet("inbound", "udp", 53, "192.168.2.1"));
		System.out.println(f.accept_packet("outbound", "tcp", 10234, "192.168.10.11"));
		System.out.println(f.accept_packet("inbound", "tcp", 81, "192.168.1.2"));
		System.out.println(f.accept_packet("inbound", "udp", 24, "52.12.48.92"));
	}
	
	public static void edge_test(String fpath) {
		Firewall f = new Firewall(fpath);
		assert(f.accept_packet("inbound", "tcp", 53, "192.168.2.1"));
		assert(f.accept_packet("inbound", "tcp", 1, "255.255.255.255"));
		assert(f.accept_packet("inbound", "tcp", 65535, "0.0.0.0"));
		assert(f.accept_packet("outbound", "udp", 53, "192.168.2.1"));
		assert(f.accept_packet("outbound", "udp", 1, "255.255.255.255"));
		assert(f.accept_packet("outbound", "udp", 65535, "0.0.0.0"));
		assert(!f.accept_packet("inbound", "udp", 65535, "0.0.0.0"));
		assert(!f.accept_packet("outbound", "tcp", 53, "192.168.2.1"));
	}
	
	public static void generateLargeTest(String fpath) throws IOException {
		Random r = new Random();
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fpath));
	    
	    for (int i=0; i<500000; i++) {
	    	int ports = r.nextInt(65534) + 1;
		    int porte = r.nextInt(65535 - ports) + ports + 1;
	    	//int porte = ports+1;
		    int ip0 = r.nextInt(256);
		    int ip1 = r.nextInt(256);
	    	writer.write(String.format("inbound,tcp,%d-%d,0.0.0.%d-255.255.255.%d\r\n",
	    			ports, porte, ip0, ip1));
		    
	    }
	    writer.close();
	}
	
	public static void large_test(String fpath) {
		System.out.println("test started");
		Firewall f = new Firewall(fpath);
		System.out.println("start queries");
		for (int i=0; i< 500000; i++) {
			f.accept_packet("inbound", "tcp", 53, "192.168.2.1");
			f.accept_packet("inbound", "udp", 65535, "0.0.0.0");
			f.accept_packet("outbound", "tcp", 53, "192.168.2.1");
		}
		System.out.println("queries completed");
	}
}
