/**
 * 
 */
package algorithms.dijkstra;

import java.util.ArrayList;
import java.util.Collection;

import ds.DijkstraVertexData;
import ds.Edge;
import ds.QueueDial;
import ds.Vertex;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

/**
 * Uses Dial's implementation.
 * 
 * @author Marcel
 * 
 * @param <D>
 */
public class DialDijkstra<D extends DijkstraVertexData> extends AbstractDijkstra<D> {

	// Dial, uses modulo
	QueueDial<DijkstraVertexData> queue;

	/**
	 * see AbstractDijkstra
	 * 
	 * @param graph
	 * @param start
	 * @param target
	 * @param cls
	 */
	public DialDijkstra(DirectedSparseGraph<Vertex, Edge> graph, Vertex start, Vertex target, Class<D> cls) {
		super(graph, start, target, cls);
		queue = new QueueDial<DijkstraVertexData>(graph);
		insert(start);
	}

	@Override
	boolean contains(Vertex dv) {
		return queue.contains(vertexAnnotation.get(dv));
	}

	@Override
	Collection<Vertex> getFringe() {
		Collection<Vertex> result = new ArrayList<Vertex>();

		DijkstraVertexData dv = null;
		while((dv = queue.pollMin()) != null) {
			result.add(dv.getVertex());
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see algorithms.dijkstra.AbstractDijkstra#insert(ds.DijkstraVertex)
	 */
	@Override
	void insert(Vertex gv) {
		queue.insert(vertexAnnotation.get(gv));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see algorithms.dijkstra.AbstractDijkstra#pollMin()
	 */
	@Override
	Vertex pollMin() {
		DijkstraVertexData data = queue.pollMin();
		if(data == null) {
			return null;
		}
		return data.getVertex();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see algorithms.dijkstra.AbstractDijkstra#update(ds.DijkstraVertex)
	 */
	@Override
	void update(Vertex dv) {
		queue.update(vertexAnnotation.get(dv));
	}

}
