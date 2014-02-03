package ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

/**
 * Merges sub-MSTs efficiently
 * 
 * @author michael
 * 
 */
public class UnionFind {

	// stores the size of each subMST. Only the value in a subMST's root is up to date!
	private final HashMap<Vertex, Integer> connectedComponentSize = new HashMap<Vertex, Integer>();

	// points vertices to a root vertex, that identifies the subMST
	private final DirectedSparseGraph<Vertex, Edge> dsg = new DirectedSparseGraph<Vertex, Edge>();

	/**
	 * 
	 * @param r
	 * @return the root
	 */
	public Vertex findRoot(Vertex r) {
		ArrayList<Vertex> path = findRootPath(r);
		return path.get(path.size() - 1);
	}

	/**
	 * To find out which MST r belongs to.
	 * 
	 * @param r
	 * @return path to root with root being the last element
	 */
	public ArrayList<Vertex> findRootPath(Vertex r) {
		Collection<Vertex> c;
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		result.add(r);
		while((c = dsg.getSuccessors(result.get(result.size() - 1))).size() > 0) {
			result.add(c.iterator().next());
		}
		return result;
	}

	/**
	 * To figure out the size of the subMST, r is in.
	 * 
	 * @param r
	 *            any Vertex
	 * @return size of subMST (minimum 1)
	 */
	public int getCCSize(Vertex r) {
		// only the value of the root is valid, so fetch that one...
		ArrayList<Vertex> path = findRootPath(r);
		Vertex root = path.get(path.size() - 1);
		return connectedComponentSize.get(root);
	}

	/**
	 * 
	 * @return the dsg
	 */
	public DirectedSparseGraph<Vertex, Edge> getResult() {
		return dsg;
	}

	/**
	 * Starts with an edge-less graph, each vertex is its own trivial MST
	 * 
	 * @param vertices
	 */
	public void init(Collection<Vertex> vertices) {
		for(Vertex vertex : vertices) { // O(n)
			dsg.addVertex(vertex);
			setCCSize(vertex, 1);
		}
	}

	/**
	 * Merges two subMST into one. TODO Does not check if r and s are already in the same MST
	 * 
	 * @param r
	 * @param s
	 * @return deprecated root
	 */
	public Vertex union(Vertex r, Vertex s) {
		// NOTE:
		// This returns the deprecated root
		// for the round robin algorithm.
		// Can be changed if absolutely
		// necessary

		ArrayList<Vertex> pathR = findRootPath(r);
		ArrayList<Vertex> pathS = findRootPath(s);
		Vertex rootR = pathR.get(pathR.size() - 1);
		Vertex rootS = pathS.get(pathS.size() - 1);
		if(getCCSize(rootR) > getCCSize(rootS)) {
			return redirectEdges(pathS, rootR);
		} else {
			return redirectEdges(pathR, rootS);
		}
	}

	private void incCCSize(Vertex r, int increment) {
		ArrayList<Vertex> path = findRootPath(r);
		Vertex root = path.get(path.size() - 1);
		connectedComponentSize.put(root, connectedComponentSize.get(root) + increment);
	}

	/**
	 * Points all vertices of one mst to the root of the other
	 * 
	 * @param path
	 * @param root
	 */
	private Vertex redirectEdges(ArrayList<Vertex> path, Vertex root) {
		for(Vertex vertex : path) {
			Collection<Edge> outEdges = dsg.getOutEdges(vertex);
			if(outEdges.size() > 0) {
				dsg.removeEdge(outEdges.iterator().next());
			}
			dsg.addEdge(new Edge(), vertex, root);
		}
		incCCSize(root, getCCSize(path.get(path.size() - 1)));

		return path.get(path.size() - 1); // obsolete root
	}

	/**
	 * Sets the Connected Component's size
	 * 
	 * @param r
	 *            any Vertex in a CC
	 * @param size
	 */
	private void setCCSize(Vertex r, int size) {
		ArrayList<Vertex> path = findRootPath(r);
		Vertex root = path.get(path.size() - 1);
		connectedComponentSize.put(root, size);
	}
}
