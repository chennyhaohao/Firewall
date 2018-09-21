import java.util.TreeSet;

public class Main {
	public static void main(String[] args) {
		Firewall f = new Firewall("");
		int[] tests = {0, 2, 1, 4, 5, 7, 7, 8, 7, 7, 9, 12, 15, 17};
		TreeSet<Range> s = new TreeSet<Range>();
		for (int i=0; i<tests.length; i+=2) {
			s.add(new Range(tests[i], tests[i+1]));
		}
		f.mergeRanges(s);
		for (Range r: s) {
			System.out.printf("%d %d\n", r.start, r.end);
		}
	}
}
