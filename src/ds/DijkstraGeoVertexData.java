package ds;

import edu.uci.ics.jung.graph.Graph;

/**
 * A specialized version of DijkstraVertexData, that also contains geo coordinates
 * 
 * @author michael
 * 
 */
public class DijkstraGeoVertexData extends DijkstraVertexData {
	// private final Double bst = null; // bst, but and naming variables follows slide 30, chapter 4
	// private final Double but = null;
	/**
	 * default constructor for newInstance
	 */
	public DijkstraGeoVertexData() {
		super();
	}

	/**
	 * see DijkstraVertexData
	 * 
	 * @param g
	 * @param v
	 */
	public DijkstraGeoVertexData(Graph<Vertex, Edge> g, Vertex v) {
		super(g, v);
	}

	/**
	 * see DijkstraVertexData
	 * 
	 * @param v
	 */
	public DijkstraGeoVertexData(Vertex v) {
		super(v);
	}

	@Override
	public double getLowerBoundOfDistanceTo(Vertex target) {
		// in our lovely euclideon space that we became so accustomed to,
		// the shortest distance between two points is a straight line.
		return ((GeoVertex) this.getVertex()).getDistanceTo((GeoVertex) target);
	}

}
