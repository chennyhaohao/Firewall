
public class Range implements Comparable<Range> {
	public long start;
	public long end;
	public Range(long s, long e) {
		start = s;
		end = e;
	}
	@Override
	public int compareTo(Range r) { //sort by starting position, and distinguish between different intervals
		if (start < r.start)
			return -1;
		if (start > r.start)
			return 1;
		return (int)(end - r.end);
	}
}
