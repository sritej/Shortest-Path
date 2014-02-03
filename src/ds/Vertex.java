package ds;

/**
 * Simple Vertex, only has an id. This vertex will not be changed in any way ever.
 * 
 * @author michael
 * 
 */

public class Vertex {
	protected int id;

	/**
	 * default constructor
	 * 
	 * @param id
	 */
	public Vertex(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "id=" + id;
	}

}
