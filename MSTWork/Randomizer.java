//The code for the Graph object and Kruskal's is contributed by Aakash Hasija
//Code for random Graph generation and experimentation 
//contributed by Nadula Kadawedduwa

enum Type {SPARSE, DENSE}

public class Randomizer {
	
	public static Graph generate(int n, int weight, Type t) {
		// Cap and min on number of edges
		int cap = (n*(n-1))/2;
		int min = (n-1);
		
		if (t.equals(Type.SPARSE))
			cap = (n*(n-1))/4;
		else if (t.equals(Type.DENSE))
			min = (n*(n-1))/4;
		
		// Random number of edges between min and cap
		int num_e = (int) (Math.random()*cap + min);
		
		// Create empty graph with n vertices and num_e edges while
		// accounting for the n-1 edges we always add for connectivity
		Graph graph = new Graph(n, num_e);

		// Iterate from last to first vertex
		// Ensures connectivity and the order doesn't really matter since
		// edges are added randomly afterwards
		// 1 -> 2 -> 3 is essentially the same as 3 -> 1 -> 2
		for (int i = 0; i < (n-1); i++) {
			graph.edge[i].src = i;
			graph.edge[i].dest = i+1;
			// Weight is random value from 1 to specified weight
			graph.edge[i].weight = (int) (Math.random()*weight)+1;
		}

		// Add (num_e - (n - 1)) more random edges
		for (int i = (n-1); i < num_e; i++) {
			graph.edge[i].src = (int) (Math.random()*(n-1));
			graph.edge[i].dest = (int) (Math.random()*(n-1));
			graph.edge[i].weight = (int) (Math.random()*weight)+1;
		}
		return graph;		
	}
}
