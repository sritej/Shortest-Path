package ds;

/**
 * Only type of edge there is.
 * 
 * weight can be an abstraction or distance in dm
 * 
 * @author michael
 * 
 */
public class Edge implements Comparable<Edge> {
	public Color color;
	public int weight;

	/**
	 * default constructor
	 */
	public Edge() {
	}

	/**
	 * init weight
	 * 
	 * @param weight
	 */
	public Edge(int weight) {
		super();
		this.weight = weight;
	}

	@Override
	public int compareTo(Edge o) {
		return this.weight - o.weight;
	}
}
