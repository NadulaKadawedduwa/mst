import java.util.Iterator;
import java.util.List;

class FilterThread extends Thread {
		private List<Edge> EGreater;
		private subset S[];
		
		public FilterThread(List<Edge> list, subset sub[]) {
			EGreater = list;
			S = sub;
		}
		
		@Override
		public void run() {		
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
		}
	}
