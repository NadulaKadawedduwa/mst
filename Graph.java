import java.io.*;
import java.lang.*;
import java.util.*;

//The code for the Graph object and Kruskal's is contributed by Aakash Hasija
//Code for random Graph generation contributed by Nadula Kadawedduwa
class Graph {

	enum Type {SPARSE, DENSE, RANDOM};

	class Edge implements Comparable<Edge> {
		int src, dest, weight;
		public int compareTo(Edge compareEdge)
		{
			return this.weight - compareEdge.weight;
		}
	};

	class subset {
		int parent, rank;
	};

	int V, E;
	Edge edge[];

	Graph(int v, int e)
	{
		V = v;
		E = e;
		edge = new Edge[E];
		for (int i = 0; i < e; ++i)
			edge[i] = new Edge();
	}

	int find(subset subsets[], int i)
	{
		if (subsets[i].parent != i)
			subsets[i].parent
			= find(subsets, subsets[i].parent);

		return subsets[i].parent;
	}

	void Union(subset subsets[], int x, int y)
	{
		int xroot = find(subsets, x);
		int yroot = find(subsets, y);

		if (subsets[xroot].rank < subsets[yroot].rank)
			subsets[xroot].parent = yroot;
		else if (subsets[xroot].rank > subsets[yroot].rank)
			subsets[yroot].parent = xroot;

		else {
			subsets[yroot].parent = xroot;
			subsets[xroot].rank++;
		}
	}

	void KruskalMST()
	{
		Edge result[] = new Edge[V];
		int e = 0;
		int i = 0;
		for (i = 0; i < V; ++i)
			result[i] = new Edge();

		Arrays.sort(edge);

		subset subsets[] = new subset[V];
		for (i = 0; i < V; ++i)
			subsets[i] = new subset();

		for (int v = 0; v < V; ++v) {
			subsets[v].parent = v;
			subsets[v].rank = 0;
		}

		i = 0;

		while (e < V - 1) {
			Edge next_edge = edge[i++];

			int x = find(subsets, next_edge.src);
			int y = find(subsets, next_edge.dest);
			if (x != y) {
				result[e++] = next_edge;
				Union(subsets, x, y);
			}
		}

		//System.out.println("Following are the edges in "
		//+ "the constructed MST");
		int minimumCost = 0;
		for (i = 0; i < e; ++i) {
			//System.out.println(result[i].src + " -- "
			//		+ result[i].dest
			//		+ " == " + result[i].weight);
			minimumCost += result[i].weight;
		}
		//System.out.println("Minimum Cost Spanning Tree "
		//+ minimumCost);
	}

	private static int getRandomElement(List<Integer> l) {
		if (l.size() == 0)
			return 0;

		int index = (int) (Math.random()*(l.size() - 1));
		return l.get(index);
	}

	public static Graph generate(int n, int weight, Type t) {
		int c = 0;
		int cap = (n*(n-1))/2;
		int min = (n-1);
		if (t.equals(Type.SPARSE))
			cap = (n*(n-1))/4;
		else if (t.equals(Type.DENSE))
			min = (n*(n-1))/4;
		int num_e = (int) (Math.random()*cap + min);
		Graph res = new Graph(n, num_e);
		List<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			l.add(i);
		}

		int v = getRandomElement(l);

		while (l.size() > 0) {

			res.edge[c].src = v;
			Integer p = v;
			l.remove(p);
			v = getRandomElement(l);
			res.edge[c].dest = v;
			p = v;
			res.edge[c].weight = (int) (Math.random()*weight)+1;
			c++;
		}
		for (int i = (n-1); i < num_e; i++) {
			//System.out.println(num_e);
			res.edge[i].src = (int) (Math.random()*(n-1));
			res.edge[i].dest = (int) (Math.random()*(n-1));
			res.edge[i].weight = (int) (Math.random()*weight)+1;
		}
		return res;		
	}

	public static void main(String[] args) throws IOException
	{
		Type t = Type.SPARSE;
		int start = 3000;
		int end = 4100;
		int step = 100;

		int iter = 500;
		Graph graph;
		File data = new File("data.txt");
		long startTime;
		long endTime;

		data.createNewFile();
		FileWriter fw = new FileWriter("data.txt");
		for (int j = start; j < end; j += step) {
			fw.write(j+"\n");
			for (int i = 0; i <= iter; i++) {
				graph = generate(j, j*2, t);
				startTime = System.currentTimeMillis();

				graph.KruskalMST();

				endTime = System.currentTimeMillis();

				fw.write((endTime - startTime) + ", ");
				if (i % 50 == 0 && i != 0)
					fw.write("\n");
				System.out.println("That took " + (endTime - startTime) + " milliseconds");
			}
			fw.write("\n\n");
		}
		fw.close();
	}
}
