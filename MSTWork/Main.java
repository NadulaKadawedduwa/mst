import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//The code for the Graph object and Kruskal's is contributed by Aakash Hasija
//Code for random Graph generation and experimentation 
//contributed by Nadula Kadawedduwa

enum Alg {KRUSKAL, FILTER_KRUSKAL}

public class Main {
	public static void main (String args[]) throws IOException, InterruptedException {
		experiment(200,100,10,Type.DENSE,Alg.FILTER_KRUSKAL);
		System.out.println(Runtime.getRuntime().availableProcessors());
	}

	// end: the number of vertices there should be in the final step
	// step: how many vertices you want to add at each step
	// iter: how many trees should be tested for each step
	// t: Type of tree to be made: DENSE or SPARSE
	private static void experiment(int end, int step, int iter, Type t, Alg a) 
			throws IOException, InterruptedException {

		Graph graph;
		File data = new File("data.txt");
		long startTime;
		long endTime;

		data.createNewFile();
		FileWriter fw = new FileWriter("data.txt");
		for (int j = step; j <= end; j += step) {
			fw.write(j+"\n");
			System.out.println("Currently running " + j + " step");
			for (int i = 0; i <= iter; i++) {
				// These statements are just for logging purposes
				if (i == iter/4)
					System.out.println("25%");
				else if (i == iter/2)
					System.out.println("50%");
				else if (i == 3*iter/4)
					System.out.println("75%");

				graph = Randomizer.generate(j, j*2, t);
				//System.out.println("Num of edges in graph: " + graph.E);

				if (a.equals(Alg.KRUSKAL)) {
					startTime = System.currentTimeMillis();

					graph.KruskalMST();

					endTime = System.currentTimeMillis();
				}
				else {
					ArrayList<Edge> F = new ArrayList<Edge>();

					subset S[] = new subset[j];
					for (int n = 0; n < j; n++)
						S[n] = new subset();

					// Create V subsets with single elements
					for (int v = 0; v < j; v++) {
						S[v].parent = v;
						S[v].rank = 0;
					}	

					startTime = System.currentTimeMillis();

					FilterVersion.FilterKruskalMST(graph, graph.edge, F, S);

					endTime = System.currentTimeMillis();
				}

				fw.write((endTime - startTime) + ", ");
				if (i % 50 == 0 && i != 0)
					fw.write("\n");
				System.out.println("That took " 
						+ (endTime - startTime) + " milliseconds");
			}
			fw.write("\n\n");
		}
		fw.close();
	}
}
