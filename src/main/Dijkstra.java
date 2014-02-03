package main;

import io.GraphFileIO_P2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import algorithms.dijkstra.BidirectionalDijkstra;
import algorithms.dijkstra.BinaryHeapDijkstra;
import algorithms.dijkstra.DialDijkstra;
import ds.DijkstraGeoVertexData;
import ds.DijkstraVertexData;
import ds.Edge;
import ds.Vertex;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;

/**
 * general class for calling Dijkstra via command line
 * 
 * @author Marcel
 * 
 */
public final class Dijkstra {
	private static EtmMonitor monitor;

	private Dijkstra() {
	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws Exception {
		setup();

		String inputFile = args[0];
		List<String> asList = Arrays.asList(args);
		String[] args2 = asList.subList(1, asList.size()).toArray(new String[0]);
		CommandLine commands = configureCommandLineOptions(args2);

		GraphFileIO_P2 graphFileIO = new GraphFileIO_P2();
		DirectedSparseGraph<Vertex, Edge> graph = graphFileIO.readGraph(inputFile);
		DirectedSparseGraph<Vertex, Edge> invertedGraph = invertGraph(graph);
		ArrayList<Vertex> startTarget = determineStartAndTarget(commands, graph);
		Vertex start = startTarget.get(0);
		Vertex target = startTarget.get(1);
		double result = 0.0;
		result = executeCommandLineOptions(commands, graph, invertedGraph, start, target);
		System.out.println("call: " + Arrays.asList(args2) + " result: " + result);

		// visualize results
		monitor.render(new JETMrenderer());
		// monitor.render(new SimpleTextRenderer());
		tearDown();
	}

	private static CommandLine configureCommandLineOptions(String[] args2) throws ParseException {
		Options options = new Options();
		options.addOption("s", true, "start");
		options.addOption("t", true, "target");
		options.addOption("h", false, "binary heap");
		options.addOption("d", false, "dial");
		options.addOption("b", false, "bidirectional");
		options.addOption("z", false, "zielgerichtet");
		options.addOption("a", false, "Vergleichstest");
		PosixParser posixParser = new PosixParser();
		CommandLine commands = posixParser.parse(options, args2);
		return commands;
	}

	private static double d(DirectedSparseGraph<Vertex, Edge> graph, Vertex start, Vertex target) {
		double result;
		result = (new DialDijkstra<DijkstraVertexData>(graph, start, target, DijkstraVertexData.class)).findPath();
		return result;
	}

	private static double db(DirectedSparseGraph<Vertex, Edge> graph, DirectedSparseGraph<Vertex, Edge> invertedGraph, Vertex start, Vertex target) {
		double result;
		DialDijkstra<DijkstraVertexData> binaryHeapDijkstra1 = new DialDijkstra<DijkstraVertexData>(graph, start, target, DijkstraVertexData.class);
		DialDijkstra<DijkstraVertexData> binaryHeapDijkstra2 = new DialDijkstra<DijkstraVertexData>(invertedGraph, target, start, DijkstraVertexData.class);
		result = (new BidirectionalDijkstra<DijkstraVertexData>(graph, start, target, binaryHeapDijkstra1, binaryHeapDijkstra2)).findPath();
		return result;
	}

	private static ArrayList<Vertex> determineStartAndTarget(CommandLine commands, DirectedSparseGraph<Vertex, Edge> graph) throws Exception {
		Vertex start = null;
		Vertex target = null;
		int startID = Integer.valueOf(commands.getOptionValue("s"));
		int targetID = Integer.valueOf(commands.getOptionValue("t"));
		for(Vertex dv : graph.getVertices()) {
			if(dv.getId() == startID) {
				start = dv;
			} else if(dv.getId() == targetID) {
				target = dv;
			}
		}
		if(start == null || target == null) {
			throw new Exception("start or target not found in graph!");
		}
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		result.add(start);
		result.add(target);
		return result;
	}

	private static double dz(DirectedSparseGraph<Vertex, Edge> graph, Vertex start, Vertex target) {
		double result;
		result = (new DialDijkstra<DijkstraGeoVertexData>(graph, start, target, DijkstraGeoVertexData.class)).findPath();
		return result;
	}

	private static double dzb(DirectedSparseGraph<Vertex, Edge> graph, DirectedSparseGraph<Vertex, Edge> invertedGraph, Vertex start, Vertex target) {
		double result;
		DialDijkstra<DijkstraGeoVertexData> binaryHeapDijkstra1 = new DialDijkstra<DijkstraGeoVertexData>(graph, start, target, DijkstraGeoVertexData.class);
		DialDijkstra<DijkstraGeoVertexData> binaryHeapDijkstra2 = new DialDijkstra<DijkstraGeoVertexData>(invertedGraph, target, start, DijkstraGeoVertexData.class);
		result = (new BidirectionalDijkstra<DijkstraGeoVertexData>(graph, start, target, binaryHeapDijkstra1, binaryHeapDijkstra2)).findPath();
		return result;
	}

	private static double executeCommandLineOptions(CommandLine commands, DirectedSparseGraph<Vertex, Edge> graph, DirectedSparseGraph<Vertex, Edge> invertedGraph, Vertex start, Vertex target) {
		double result = 0.0;
		if(commands.hasOption("a")) {
			result = h(graph, start, target);
			System.out.println("-h path length: " + result);
			System.out.println("-hb path length: " + hb(graph, invertedGraph, start, target));
			System.out.println("-hz path length: " + hz(graph, start, target));
			// hzb(graph, invertedGraph, start, target);
			System.out.println("-d path length: " + d(graph, start, target));
			System.out.println("-db path length: " + db(graph, invertedGraph, start, target));
			System.out.println("-dz path length: " + dz(graph, start, target));
			// dzb(graph, invertedGraph, start, target);
		} else if(commands.hasOption("h")) {
			if(commands.hasOption("z")) {
				if(commands.hasOption("b")) {
					result = hzb(graph, invertedGraph, start, target);
				} else {
					result = hz(graph, start, target);
				}
			} else if(commands.hasOption("b")) {
				result = hb(graph, invertedGraph, start, target);
			} else {
				result = h(graph, start, target);
			}
		} else if(commands.hasOption("d")) {
			if(commands.hasOption("z")) {
				if(commands.hasOption("b")) {
					result = dzb(graph, invertedGraph, start, target);
				} else {
					result = dz(graph, start, target);
				}
			} else if(commands.hasOption("b")) {
				result = db(graph, invertedGraph, start, target);
			} else {
				result = d(graph, start, target);
			}
		}
		return result;
	}

	private static double h(DirectedSparseGraph<Vertex, Edge> graph, Vertex start, Vertex target) {
		double result;
		result = (new BinaryHeapDijkstra<DijkstraVertexData>(graph, start, target, DijkstraVertexData.class)).findPath();
		return result;
	}

	private static double hb(DirectedSparseGraph<Vertex, Edge> graph, DirectedSparseGraph<Vertex, Edge> invertedGraph, Vertex start, Vertex target) {
		double result;
		BinaryHeapDijkstra<DijkstraVertexData> binaryHeapDijkstra1 = new BinaryHeapDijkstra<DijkstraVertexData>(graph, start, target, DijkstraVertexData.class);
		BinaryHeapDijkstra<DijkstraVertexData> binaryHeapDijkstra2 = new BinaryHeapDijkstra<DijkstraVertexData>(invertedGraph, target, start, DijkstraVertexData.class);
		result = (new BidirectionalDijkstra<DijkstraVertexData>(graph, start, target, binaryHeapDijkstra1, binaryHeapDijkstra2)).findPath();
		return result;
	}

	private static double hz(DirectedSparseGraph<Vertex, Edge> graph, Vertex start, Vertex target) {
		double result;
		result = (new BinaryHeapDijkstra<DijkstraGeoVertexData>(graph, start, target, DijkstraGeoVertexData.class)).findPath();
		return result;
	}

	private static double hzb(DirectedSparseGraph<Vertex, Edge> graph, DirectedSparseGraph<Vertex, Edge> invertedGraph, Vertex start, Vertex target) {
		double result;
		BinaryHeapDijkstra<DijkstraGeoVertexData> binaryHeapDijkstra1 = new BinaryHeapDijkstra<DijkstraGeoVertexData>(graph, start, target, DijkstraGeoVertexData.class);
		BinaryHeapDijkstra<DijkstraGeoVertexData> binaryHeapDijkstra2 = new BinaryHeapDijkstra<DijkstraGeoVertexData>(invertedGraph, target, start, DijkstraGeoVertexData.class);
		result = (new BidirectionalDijkstra<DijkstraGeoVertexData>(graph, start, target, binaryHeapDijkstra1, binaryHeapDijkstra2)).findPath();
		return result;
	}

	private static DirectedSparseGraph<Vertex, Edge> invertGraph(DirectedSparseGraph<Vertex, Edge> graph) {
		DirectedSparseGraph<Vertex, Edge> invertedGraph = new DirectedSparseGraph<Vertex, Edge>();
		for(Vertex v : graph.getVertices()) {
			invertedGraph.addVertex(v);
		}
		for(Edge e : graph.getEdges()) {
			invertedGraph.addEdge(e, graph.getDest(e), graph.getSource(e));
		}
		return invertedGraph;
	}

	private static void setup() {
		BasicEtmConfigurator.configure();
		monitor = EtmManager.getEtmMonitor();
		monitor.start();
	}

	private static void tearDown() {
		monitor.stop();
		monitor.reset();
	}

}
