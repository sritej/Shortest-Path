/**
 * 
 */
package algorithms.dijkstra;

import ds.Edge;
import ds.Vertex;
import edu.uci.ics.jung.graph.AbstractGraph;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

/**
 * Superclass for all shortest path algorithms
 * 
 * @author Marcel
 * 
 */
public abstract class AbstractShortestPath {
	// JETM measurements
	protected static final EtmMonitor ETM_MONITOR = EtmManager.getEtmMonitor();
	protected EtmPoint pointPQops;
	protected EtmPoint pointRuntime;

	// Runs on a graph with start and target, obviously
	AbstractGraph<Vertex, Edge> graph;
	Vertex start;
	Vertex target;

	/**
	 * Initializes this algorithm with all the relevant data, but does not launch it yet. (Use findPath for that)
	 * 
	 * @param permanent
	 * @param graph
	 * @param start
	 * @param target
	 */
	public AbstractShortestPath(AbstractGraph<Vertex, Edge> graph, Vertex start, Vertex target) {
		super();
		this.graph = graph;
		this.start = start;
		this.target = target;
	}

	/**
	 * Launches the algorithm
	 * 
	 * @return length of shortest path
	 */
	public abstract double findPath();

	/**
	 * 
	 * @param dv
	 * @return isPermanent
	 */
	public abstract boolean isPermanent(Vertex dv);
}
