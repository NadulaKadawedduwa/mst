// A class to represent a graph edge

public class Edge implements Comparable<Edge> {

	int src, dest, weight;

	// Comparator function used for
	// sorting edges based on their weight
	public int compareTo(Edge compareEdge)
	{
		return this.weight - compareEdge.weight;
	}
}

