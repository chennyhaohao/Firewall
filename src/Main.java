import java.util.TreeSet;

public class Main {
	public static void main(String[] args) {
		//small_test("./small_test.txt");	
		edge_test("./edge_test.txt");
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
}
