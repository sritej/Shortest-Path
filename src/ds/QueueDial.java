package ds;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import edu.uci.ics.jung.graph.Graph;

/**
 * Dial's queueueue, using modulo.
 * 
 * @author michael
 * 
 * @param <T>
 */
public class QueueDial<T extends DijkstraVertexData> {

	private final Vector<LinkedList<T>> buckets; // links to the first item in a bucket

	private final HashMap<T, Integer> dataToIndex = new HashMap<T, Integer>(); // stored in case data.estimate changes

	private int dialsPointer = 0; // points to the next bucket to be checked for items

	private int elementCounter = 0; // the number of elements which are contained in the queue

	/**
	 * Sets up the queue, ready for use
	 * 
	 * @param g
	 */
	public QueueDial(Graph<Vertex, Edge> g) {
		// for modulo value, find longest edge
		int maxEdgeLength = 0;
		for(Edge e : g.getEdges()) {
			maxEdgeLength = Math.max(maxEdgeLength, e.weight);
		}

		buckets = new Vector<LinkedList<T>>(maxEdgeLength + 1);// arbitrary number //worstEstimate + 1);
		buckets.setSize(maxEdgeLength + 1);

		// fill buckets with empty linked lists
		for(int i = 0; i < buckets.size(); i++) {
			buckets.set(i, new LinkedList<T>());
		}
	}

	/**
	 * Checks weather the data is contained in this queue
	 * 
	 * @param dv
	 *            any vertex data
	 * @return true if queue contains data
	 */
	public boolean contains(T dv) {
		return dataToIndex.containsKey(dv);
	}

	/**
	 * inserts item into the right position in the queue
	 * 
	 * @param gv
	 */
	public void insert(T gv) {
		// updates, if item is already in queue
		if(contains(gv)) {
			update(gv);
		} else {
			buckets.get(((int) gv.estimate) % buckets.size()).add(gv);
			dataToIndex.put(gv, (int) gv.estimate);
			elementCounter++;
		}
	}

	/**
	 * returns the lowest item out of this queue and removes it.
	 * 
	 * @return next item in queue
	 */
	public T pollMin() {
		// advance pointer to first non-empty bucket
		// or to the last bucket
		if(elementCounter == 0) {
			return null;
		}
		while(buckets.get(dialsPointer).isEmpty()) {
			dialsPointer = (dialsPointer + 1) % buckets.size();
		}

		// the pointer is now at the first occupied bucket
		T result = buckets.get(dialsPointer).remove();
		dataToIndex.remove(result);
		elementCounter--;
		return result;
	}

	/**
	 * in case the estimate has changed
	 * 
	 * @param dv
	 */
	public void update(T dv) {
		// remove from old bucket
		buckets.get(dataToIndex.get(dv) % buckets.size()).remove(dv);

		// put into new bucket
		buckets.get(((int) dv.estimate) % buckets.size()).add(dv);

		// store current position for
		// potential future removals
		dataToIndex.put(dv, (int) dv.estimate);
	}
}
