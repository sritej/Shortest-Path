/**
 * 
 */
package algorithms.dijkstra;

import java.util.HashSet;

import ds.DijkstraVertexData;
import ds.Edge;
import ds.Vertex;
import edu.uci.ics.jung.graph.AbstractGraph;

/**
 * Runs two Dijkstra instances, alternating between the two, until their fringes meet. When that happens, check for shorter path once -> done.
 * 
 * @author michael
 * 
 * @param <D>
 */
public class BidirectionalDijkstra<D extends DijkstraVertexData> extends AbstractShortestPath {

	// the two Dijkstra instances. Can be any Dijkstra
	private AbstractDijkstra<D> dijkstra1;
	private AbstractDijkstra<D> dijkstra2;

	// Needed to notice when the two fringes overlap
	private final HashSet<Vertex> permanent1 = new HashSet<Vertex>();
	private final HashSet<Vertex> permanent2 = new HashSet<Vertex>();

	/**
	 * Run
	 * 
	 * @param graph
	 * @param start
	 * @param target
	 */
	public BidirectionalDijkstra(AbstractGraph<Vertex, Edge> graph, Vertex start, Vertex target) {
		super(graph, start, target);
	}

	/**
	 * Run bidirectional search, using the two provided Dijkstra instances.
	 * 
	 * @param graph
	 * @param start
	 * @param target
	 * @param dijkstra1
	 * @param dijkstra2
	 */
	public BidirectionalDijkstra(AbstractGraph<Vertex, Edge> graph, Vertex start, Vertex target, AbstractDijkstra<D> dijkstra1, AbstractDijkstra<D> dijkstra2) {
		super(graph, start, target);
		this.dijkstra1 = dijkstra1;
		this.dijkstra2 = dijkstra2;
		pointPQops = ETM_MONITOR.createPoint(this.getClass().getSimpleName() + " " + dijkstra1.getClass().getSimpleName() + " " + dijkstra1.dijkstraClassType.getSimpleName() + " PQops");
		pointRuntime = ETM_MONITOR.createPoint(this.getClass().getSimpleName() + " " + dijkstra1.getClass().getSimpleName() + " " + dijkstra1.dijkstraClassType.getSimpleName() + " time");
	}

	@Override
	public double findPath() {
		Vertex intersection = findIntersection();
		// fringes intersect at one node
		double totalPath = dijkstra1.vertexAnnotation.get(intersection).getPathLengthToStart() + dijkstra2.vertexAnnotation.get(intersection).getPathLengthToStart();

		// check if there is a shorter path
		for(Vertex v1 : permanent1) {
			for(Edge e : graph.getOutEdges(v1)) {
				Vertex v2 = graph.getOpposite(v1, e);
				if(permanent2.contains(v2)) {
					double possiblePathLength = dijkstra1.vertexAnnotation.get(v1).getPathLengthToStart() + e.weight + dijkstra2.vertexAnnotation.get(v2).getPathLengthToStart();
					if(possiblePathLength < totalPath) {
						totalPath = possiblePathLength;
					}
				}
			}
		}
		pointRuntime.collect();
		return totalPath;
	}

	@Override
	public boolean isPermanent(Vertex dv) {
		return false;
	}

	/**
	 * Run both Dijkstras from both sides until they overlap in their permanent labeled nodes and returns intersection
	 * 
	 * @return
	 */
	private Vertex findIntersection() {
		// expand from both sides

		while(true) {
			// advance dijkstra1 by one.
			Vertex currentVertexOfDijkstra1 = dijkstra1.singleStep();
			pointPQops.collect();
			// check if it touches dijkstra2 yet
			if(permanent2.contains(currentVertexOfDijkstra1)) {
				return currentVertexOfDijkstra1;
			}
			permanent1.add(currentVertexOfDijkstra1);
			Vertex currentVertexOfDijkstra2 = dijkstra2.singleStep();
			pointPQops.collect();
			if(permanent1.contains(currentVertexOfDijkstra2)) {
				return currentVertexOfDijkstra2;
			}
			permanent2.add(currentVertexOfDijkstra2);
			// not connected
			if(currentVertexOfDijkstra1 == null && currentVertexOfDijkstra2 == null) {
				return null;
			}
		}
	}

}
