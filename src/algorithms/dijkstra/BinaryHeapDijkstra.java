/**
 * 
 */
package algorithms.dijkstra;

import java.util.ArrayList;
import java.util.Collection;

import ds.DijkstraVertexData;
import ds.Edge;
import ds.Vertex;
import edu.uci.ics.jung.algorithms.util.MapBinaryHeap;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

/**
 * Runs Dijkstra using a binary heap as queue
 * 
 * @author michael
 * 
 * @param <D>
 */
public class BinaryHeapDijkstra<D extends DijkstraVertexData> extends AbstractDijkstra<D> {

	MapBinaryHeap<DijkstraVertexData> heap = new MapBinaryHeap<DijkstraVertexData>();

	/**
	 * see AbstractDijkstra
	 * 
	 * @param graph
	 * @param start
	 * @param target
	 * @param cls
	 *            specifies the Dijkstra Algorithm to use
	 */
	public BinaryHeapDijkstra(DirectedSparseGraph<Vertex, Edge> graph, Vertex start, Vertex target, Class<D> cls) {
		super(graph, start, target, cls);
		insert(start);
	}

	@Override
	boolean contains(Vertex dv) {
		return heap.contains(vertexAnnotation.get(dv));
	}

	@Override
	Collection<Vertex> getFringe() {
		Collection<Vertex> result = new ArrayList<Vertex>();

		for(DijkstraVertexData vad : heap) {
			result.add(vad.getVertex());
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
		heap.add(vertexAnnotation.get(gv));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see algorithms.dijkstra.AbstractDijkstra#pollMin()
	 */
	@Override
	Vertex pollMin() {
		return heap.poll().getVertex();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see algorithms.dijkstra.AbstractDijkstra#update(ds.DijkstraVertex)
	 */
	@Override
	void update(Vertex dv) {
		heap.update(vertexAnnotation.get(dv));
	}

}
