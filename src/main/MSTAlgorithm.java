package main;

import io.GraphFileIO_P1;

import java.io.File;
import java.util.HashSet;

import ds.Color;
import ds.Edge;
import ds.Vertex;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public abstract class MSTAlgorithm {
	protected UndirectedSparseGraph<Vertex, Edge> g;
	private String file;

	private int testedEdges = 0;
	private int mergeOperations = 0;
	
	public MSTAlgorithm(String file) {
		this.file = file;
	}

	public void execute() {
		GraphFileIO_P1 graphFileIO = new GraphFileIO_P1();
		g = graphFileIO.readGraph(file);
		if(g.getVertexCount() > 0) { //special case, a mst of an empty graph is an empty graph
			compute();
		}
		graphFileIO.writeGraph(g, "target/"+this.getClass().getSimpleName()+"/"+(new File(file)).getName()+".mst");
	}

	abstract protected void compute();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		throw new RuntimeException("main needs to be implemented");
	}

	public static int getWeight(UndirectedSparseGraph<Vertex, Edge> g2) {
		int result = 0;
		for(Edge e : g2.getEdges()) {
			if(e.color==Color.BLUE) {
				result += e.weight;
			}
		}
		return result;
	}

	public int getTestedEdges() {
		return testedEdges;
	}
	public int getMergeOperations() {
		return mergeOperations;
	}
	
	
	protected void logTestedEdges() {
		testedEdges++;
	}
	protected void logMerge() {
		mergeOperations++;
	}
	
}
