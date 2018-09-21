# Firewall
by Chenhao Xu  

## Tests
Tested implementation on common cases, large input (1M rules) and some edge cases (minimum/maximum values of port & ip address). Test code (in Main.java) and test files are included.  

## Design and Complexity Analysis  
The majority of the Firewall implementation is in `Firewall.java`. `Range.java` implements a helpful data structure that stores intervals of IP addresses.

The overall design: allowed intervals are stored in a three-dimensional array of BBSTs containing IP ranges. The 3 dimensions are, by order, direction, protocol and port number, and the BBSTs (Java's `TreeSet` is used in my implementation) at the end store intervals of IP addresses, sorted by their starting addresses. Importantly, the intervals are non-overlapping, which allows efficient logrithmic time queries. (We also could have chosen IP addresses as the 3rd dimension of the array instead of port numbers, but the range of possible IP addresses is bigger than that of port numbers by several orders of magnitude, and therefore using port number as the 3rd dimension would reduce time complexity of the constructor and overall memory complexity in the worst cases.)  
  
In the constructor, all allowed intervals are inserted one-by-one. The insertions gives us a worst-case time-complexity of `O(nr*log(nr)*np)`, where `nr` is the total number of rules and `np` is the number of distinct port numbers. This can take a relatively long time for large numbers of rules, but I consider this a reasonable tradeoff, as the constructor is called only once for a given set of rules and efficient queries are far more important in production. The space complexity is `O(np*nr)` (which, depending on the amount of memory we have, might potentially be overwhelming in the worst possible cases. In average cases, however, the implementation seems to work fine on my laptop as verified by the large input test.)  
  
Finally after the insertions, we run an important preprocessing step in which for each BBST, we merge all overlapping intervals so that only non-overlapping merged intervals are left in the tree. (This can be done in O(n) time for each BBST, where n is the size of the tree.) This preprocessing allows us to search for an IP address among the intervals by simply running a tree search. Therefore, after running the constructor, the time complexity for each query is `O(log(nr))` in the worst case, which is reasonably fast and doesn't produce significant latency.  

## Future improvements
The main space for improvement would be the space complexity of this implementation, which could be overwhelming in the worst cases. A more compact rule representation I can conceive of is to store the rules as non-overlapping 2-dimensional "intervals" of port numbers and ip addresses (we can visualize them as rectangles on a plane with port numbers as the x-axis and ip addresses as the y-axis). The actual way to construct such a representation might be complicated, as we most probably need the rectangles to be non-overlapping to support efficient queries (naive linear time queries on overlapping rectangles would be too slow for large number of rules).