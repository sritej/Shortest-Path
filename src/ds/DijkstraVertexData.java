package ds;

import edu.uci.ics.jung.graph.Graph;

/**
 * Encapsulates a vertex and adds some extra information to it that can be used by any Dijkstra algorithm.
 * 
 * 
 * @author michael
 * 
 */
public class DijkstraVertexData implements Comparable<DijkstraVertexData> {

	public double lengthToPredecessor;
	public DijkstraVertexData predecessor;

	protected Vertex annotatedVertex;

	protected double estimate = Double.MAX_VALUE;

	protected Graph<Vertex, Edge> graph;

	/**
	 * default constructor for newInstance
	 */
	public DijkstraVertexData() {
		super();
	}

	/**
	 * 
	 * @param g
	 * @param v
	 */
	public DijkstraVertexData(Graph<Vertex, Edge> g, Vertex v) {
		graph = g;
		annotatedVertex = v;
	}

	/**
	 * 
	 * @param v
	 */
	public DijkstraVertexData(Vertex v) {
		annotatedVertex = v;
	}

	@Override
	public int compareTo(DijkstraVertexData vad) {
		if(estimate < vad.estimate) {
			return -1;
		}
		if(estimate > vad.estimate) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @return the annotatedVertex
	 */
	public Vertex getAnnotatedVertex() {
		return annotatedVertex;
	}

	/**
	 * for unseen nodes returns MAX DOUBLE, else the current best estimate
	 * 
	 * @return estimate
	 */
	public double getEstimate() {
		return estimate;
	}

	/**
	 * @return the graph
	 */
	public Graph<Vertex, Edge> getGraph() {
		return graph;
	}

	/**
	 * Since this data has no geo data, the lowest estimate must be 0.0
	 * 
	 * @param target
	 * @return always 0.0
	 */
	public double getLowerBoundOfDistanceTo(Vertex target) {
		return 0.0;
	}

	/**
	 * calculate exact length from this node to start, recursively iterating predecessors
	 * 
	 * @return length
	 */
	public double getPathLengthToStart() {
		double totalPath = 0;

		DijkstraVertexData targetData = this;

		while(targetData.predecessor != null) {
			totalPath += targetData.lengthToPredecessor;
			targetData = targetData.predecessor;
		}

		return totalPath;
	}

	/**
	 * returns the annotated vertex
	 * 
	 * @return vertex
	 */
	public Vertex getVertex() {
		return annotatedVertex;
	}

	/**
	 * @param annotatedVertex
	 *            the annotatedVertex to set
	 */
	public void setAnnotatedVertex(Vertex annotatedVertex) {
		this.annotatedVertex = annotatedVertex;
	}

	/**
	 * 
	 * @param newEstimate
	 */
	public void setEstimate(double newEstimate) {
		estimate = newEstimate;
	}

	/**
	 * @param graph
	 *            the graph to set
	 */
	public void setGraph(Graph<Vertex, Edge> graph) {
		this.graph = graph;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return annotatedVertex.toString();
	}
}
