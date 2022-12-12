import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;



public class FilterVersion {	
	private static Random rgen = new Random();
	static int partition;
	static int num_threads = 6;

	private static void ModifiedKruskal(Edge[] E, 
			ArrayList<Edge> F, subset[] S) {
		if (E.length > 0) {

			Arrays.parallelSort(E);

			//System.out.println("Size of E: " + E.length);
			for (Edge e : E) {
				int u = Graph.find(S, e.src);
				int v = Graph.find(S, e.dest);
				/*System.out.println("Edge uv: ");
			System.out.println(e.src + " -- " + e.dest);
			System.out.println(u);
			System.out.println(v);*/
				if (u != v) {
					//System.out.println("This thng was unioned");
					F.add(e);
					Graph.Union(S, u, v);
					/*for (subset o : S) {
					System.out.println("parent: " + o.parent + " rank: " + o.rank);
				}*/
				}
			}
			/*System.out.println("subsets:");
		for (subset o : S) {
			System.out.println(o.parent);
			System.out.println(o.rank);
		}*/
		}
	}

	public static void FilterKruskalMST(Graph graph, Edge[] E,
			ArrayList<Edge> F, subset[] S) throws InterruptedException {
		ArrayList<Edge> ELesser = new ArrayList<Edge>();
		ArrayList<Edge> EGreater = new ArrayList<Edge>();

		//System.out.println(E.length);
		if (E.length <= Threshold(graph.V, E.length, F.size())) {
			ModifiedKruskal(E, F, S);
			// print the contents of result[] to display
			// the built MST

			/*System.out.println("Following are the edges in "
					+ "the constructed MST");
			for (Edge it : F)
				System.out.println(it.src + " --- " + it.dest + " === " + it.weight);*/

		}
		else {
			// Slight efficiency boost at larger edge list sizes
			// Takes an informed but still random median
			partition = randMedian(E);

			for (Edge cur_edge: E) {
				if (cur_edge.weight <= partition)
					ELesser.add(cur_edge);
				else
					EGreater.add(cur_edge);
			}
			/*System.out.println("size of graph E " + graph.E);
			System.out.println("E is " + E.length + " \n===========");
			System.out.println("size of ELesser is " + ELesser.size());
			System.out.println("size of EGreater is " + EGreater.size());
			System.out.println("partition is: " + partition);*/

			/*Edge ls[] = ELesser.toArray(new Edge[0]);
			System.out.println("size of F is: " + F.size() + "\n\n") ;
			for (Edge ed : ls) {
				System.out.println(ed.src + " -- " + ed.dest + " === " + ed.weight);
			}*/

			FilterKruskalMST(graph, ELesser.toArray(new Edge[0]), F, S);
			EGreater = Filter(EGreater, S, false);
			FilterKruskalMST(graph, EGreater.toArray(new Edge[0]), F, S);
		}
	}

	private static int Threshold(int V, int E, int F) {

		return 1000;
	}

	private static int randMedian(Edge[] E) {
		int len = (int) Math.sqrt(E.length);
		int sample[] = new int[len];
		for (int x = 0; x < len; x++) {
			sample[x] = E[rgen.nextInt(E.length)].weight;
		}
		Arrays.parallelSort(sample);
		return sample[(int) sample.length/2];	
	}

	private static ArrayList<Edge> 
	Filter(ArrayList<Edge> EGreater, subset S[], boolean isParallel) throws InterruptedException {
		if (isParallel) {
			ArrayList<List<Edge>> subEdgeList = new ArrayList<List<Edge>>();
			FilterThread threads[] = new FilterThread[num_threads];
			int step = EGreater.size()/num_threads;
			int n;

			for (n = 0; n < num_threads - 1; n++) {
				subEdgeList.add(EGreater.subList(n*step, n*step+step));
				System.out.println(subEdgeList.get(n));
			}
			subEdgeList.add(EGreater.subList(n*step, EGreater.size()));

			for (int i = 0; i < num_threads; i++) {
				threads[i] = new FilterThread(subEdgeList.get(i), S);
			}

			for (int i = 0; i < num_threads; i++) {
				threads[i].start();
			}
			for (int i = 0; i < num_threads; i++) {
				threads[i].join();
			}
			EGreater = new ArrayList<Edge>();

			for (List<Edge> sublist : subEdgeList) {
				for (Edge e : sublist) {
					EGreater.add(e);
				}
			}
			//System.out.println("Greater size now: " + EGreater.size());
			return EGreater;
		}
		else {
			int u;
			int v;
			int x;
			int y;

			//System.out.println("Greater size: " + EGreater.size());
			Iterator<Edge> iter = EGreater.iterator();
			while (iter.hasNext()) {
				Edge cur_edge = iter.next();
				u = S[cur_edge.src].parent;
				v = S[cur_edge.dest].parent;
				// Small efficiency boost in this if statement
				if (u == v)
					iter.remove();
				else {
					x = Graph.find(S, cur_edge.src);
					y = Graph.find(S, cur_edge.dest);
					if (x == y) {
						//System.out.println(cur_edge.src + " --- " + cur_edge.dest + " === " + cur_edge.weight);
						iter.remove();
					}
				}
			}
			//System.out.println("Greater size now: " + EGreater.size());
			return EGreater;
		}
	}
}
