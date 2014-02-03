package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import main.MSTAlgorithm;
import ds.Edge;
import ds.GeoVertex;
import ds.Vertex;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class GraphFileIO_P2 {
	/**
	 * create JUNG Graph Object from given file
	 * 
	 * @param file
	 * @return
	 */
	public DirectedSparseGraph<Vertex, Edge> readGraph(String file) {
		DirectedSparseGraph<Vertex, Edge> g = new DirectedSparseGraph<Vertex, Edge>();
		HashMap<Integer, Vertex> vertices = new HashMap<Integer, Vertex>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				if(split[0].equals("v")) {
					int id = Integer.parseInt(split[1]);
					double longitude = Double.parseDouble(split[2]);
					double latitude = Double.parseDouble(split[3]);
					Vertex vertex = new GeoVertex(id, longitude, latitude);
					g.addVertex(vertex);
					vertices.put(id, vertex);
				}
				if(split[0].equals("e")) {
					int fromID = Integer.parseInt(split[1]);
					int toID = Integer.parseInt(split[2]);
					int weight = Integer.parseInt(split[3]);
					Edge findEdge = g.findEdge(vertices.get(fromID), vertices.get(toID));
					if(findEdge == null) {
						g.addEdge(new Edge(weight), vertices.get(fromID), vertices.get(toID));
					} else {
						findEdge.weight = weight < findEdge.weight ? weight : findEdge.weight;
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return g;
	}

	public void writeGraph(UndirectedSparseGraph<Vertex, Edge> g, String file) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))); // TODO extend so that folders are created automatically
			bw.write("mst " + MSTAlgorithm.getWeight(g) + " " + g.getEdgeCount());
			bw.newLine();
			// for(Vertex v : g.getVertices()) {
			// bw.write("v " + v.id);
			// bw.newLine();
			// }
			for(Edge e : g.getEdges()) {
				Pair<Vertex> endpoints = g.getEndpoints(e);
				bw.write("e " + endpoints.getFirst().getId() + " " + endpoints.getSecond().getId() + " " + e.weight);
				bw.newLine();
			}
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
