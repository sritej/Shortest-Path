package algorithms.dijkstra;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import ds.DijkstraVertexData;
import ds.Edge;
import ds.Vertex;
import edu.uci.ics.jung.graph.AbstractGraph;

/**
 * Dijkstra implementation that works with all kinds of vertices and all kinds of queues.
 * 
 * Queues are implemented by inheriting from this abstract class.
 * 
 * 
 * @author Marcel
 * 
 * @param <D>
 */
public abstract class AbstractDijkstra<D extends DijkstraVertexData> extends AbstractShortestPath {
	// needed for jetm so we can figure out which algorithm we are running
	public final Class<D> dijkstraClassType;

	// maps vertices to their annotation object (data).
	// vertex data needs to be separate from vertices
	// so that we can run multiple instances of dijkstra
	// on the same graph without interference (bidirectional)
	protected HashMap<Vertex, D> vertexAnnotation = new HashMap<Vertex, D>();

	// these won't change their distance anymore
	private final HashSet<D> permanent = new HashSet<D>();

	/**
	 * initialize Dijkstra algorithm, the cls parameter specifies the vertex data type (to distinguish between goal-directed and normal search
	 * 
	 * @param graph
	 * @param start
	 * @param target
	 * @param cls
	 */
	public AbstractDijkstra(AbstractGraph<Vertex, Edge> graph, Vertex start, Vertex target, Class<D> cls) {
		super(graph, start, target);
		// insert(start);
		dijkstraClassType = cls;
		// create a data annotation for each vertex
		for(Vertex v : graph.getVertices()) {
			try {
				D dva;
				dva = cls.newInstance();
				dva.setGraph(graph);
				dva.setAnnotatedVertex(v);
				vertexAnnotation.put(v, dva);
			} catch(InstantiationException e) {
				e.printStackTrace();
			} catch(IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		// to get the start node from the queue first, it needs
		// to have distance zero
		vertexAnnotation.get(start).setEstimate(0.0);

		// JETM measurement points
		pointPQops = ETM_MONITOR.createPoint(this.getClass().getSimpleName() + " " + cls.getSimpleName() + " PQops");
		pointRuntime = ETM_MONITOR.createPoint(this.getClass().getSimpleName() + " " + cls.getSimpleName() + " time");
	}

	/**
	 * overall method to run a complete dijkstra search on the given graph
	 * 
	 * @return path length
	 */
	@Override
	public double findPath() {
		// insert(start);
		// node that gets examined in a single step
		Vertex singleStep;

		// queue will return null when empty
		while((singleStep = singleStep()) != null) {
			if(singleStep.equals(target)) {
				break;
			}
		}

		// at this point, beginning from target, every
		// node has a pointer to its predecessor.
		// to calculate distance, we can add all edges
		// on the way
		double totalPath = 0;

		DijkstraVertexData targetData = vertexAnnotation.get(target);

		// we reached the start node when we reached the one node
		// that has no predecessor
		while(targetData.predecessor != null) {
			totalPath += targetData.lengthToPredecessor;
			targetData = targetData.predecessor;
		}

		// JETM: algorithm is done
		pointRuntime.collect();
		return totalPath;
	}

	@Override
	public boolean isPermanent(Vertex dv) {
		return permanent.contains(dv);
	}

	/**
	 * One iteration of the Dijkstra algorithm. This is needed for bidirectional search, so that we can run both algorithm instances from both sides in alternation.
	 * 
	 * @return Vertex which is the next permanent labeled vertex == next node in queue
	 */
	public Vertex singleStep() {
		// fetch lowest node from queue
		Vertex current = pollMin();

		// queue is empty? Done!
		if(current == null) {
			return null;
		}

		// Look at the data we associated with that node
		D d = vertexAnnotation.get(current);
		// set that node's estimate to the path that has been found to it.
		if(d.predecessor != null) {
			d.setEstimate(d.predecessor.getEstimate() + d.lengthToPredecessor);
		}

		// JETM: Count this pq operation
		pointPQops.collect();

		// Have a look at all neighbours of this node
		for(Edge edge : graph.getOutEdges(current)) {
			Vertex neighbor = graph.getOpposite(current, edge);
			double oldNeighbor = vertexAnnotation.get(neighbor).getEstimate();
			double newEstimate = vertexAnnotation.get(current).getEstimate() + edge.weight + vertexAnnotation.get(neighbor).getLowerBoundOfDistanceTo(target);

			// did we find a shorter path to this neighbour?
			if(oldNeighbor > newEstimate) {
				// redirect this neighbour to this node
				vertexAnnotation.get(neighbor).predecessor = vertexAnnotation.get(current);
				vertexAnnotation.get(neighbor).lengthToPredecessor = edge.weight;
				vertexAnnotation.get(neighbor).setEstimate(newEstimate);
				if(contains(neighbor)) {
					update(neighbor);
				} else {
					insert(neighbor);
				}
			}
		}

		return current;
	}

	/**
	 * Does the queue contain this vertex?
	 * 
	 * @param dv
	 * @return true if contained
	 */
	abstract boolean contains(Vertex dv);

	/**
	 * 
	 * @return collection of nodes which are in the queue
	 */
	abstract Collection<Vertex> getFringe();

	/**
	 * Adds gv to priority queue
	 * 
	 * @param gv
	 */
	abstract void insert(Vertex gv);

	/**
	 * fetches lowest node from pq
	 * 
	 * @return best estimate
	 */
	abstract Vertex pollMin();

	/**
	 * If the key changed, this will correct the position of dv in the priority queue, this is also called when node is new //TODO maybe we should do the check in the generic algorithm and then call
	 * appropriate insert/update method?
	 * 
	 * @param dv
	 */
	abstract void update(Vertex dv);
}
