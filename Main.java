import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//The code for the Graph object and Kruskal's is contributed by Aakash Hasija
//Code for random Graph generation and experimentation 
//contributed by Nadula Kadawedduwa

public class Main {
	public static void main (String args[]) throws IOException {
		experiment(20,10,10,Type.SPARSE);
	}
	
	// end: the number of vertices there should be in the final step
	// step: how many vertices you want to add at each step
	// iter: how many trees should be tested for each step
	// t: Type of tree to be made: DENSE or SPARSE
	private static void experiment(int end, int step, int iter, Type t) 
			throws IOException {

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

				startTime = System.currentTimeMillis();

				graph.KruskalMST();

				endTime = System.currentTimeMillis();


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
