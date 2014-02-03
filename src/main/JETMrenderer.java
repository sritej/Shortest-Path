package main;

import io.PrintTables;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import etm.core.aggregation.Aggregate;
import etm.core.monitor.EtmPoint;
import etm.core.renderer.MeasurementRenderer;

public class JETMrenderer implements MeasurementRenderer {

	@Override
	public void render(Map arg0) {
		ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();

		ArrayList<String> header = new ArrayList<String>();
		header.add("parameters");
		header.add("# PQ-Ops");
		header.add("time [ms]");
		output.add(header);

		Map<EtmPoint, Aggregate> points = arg0;

		for(Entry<EtmPoint, Aggregate> ep : points.entrySet()) {
			String entryName = "" + ep.getKey();

			if(entryName.contains("time")) {
				addColumn(output, 2, formatCall(entryName), "" + ep.getValue().getTotal());
			} else if(entryName.contains("PQops")) {
				addColumn(output, 1, formatCall(entryName), "" + ep.getValue().getMeasurements());
			}

		}

		new PrintTables().print(output);
	}

	private void addColumn(ArrayList<ArrayList<String>> output, int i, String params, String value) {
		for(ArrayList<String> line : output) {
			if(line.get(0).equals(params)) {
				line.set(i, value);
				return;
			}
		}
		ArrayList<String> line = new ArrayList<String>();
		line.add(params);
		line.add("");
		line.add("");
		line.set(i, value);
		output.add(line);
	}

	/**
	 * format the calling string / parameters in a nice way
	 * 
	 * @param s
	 * @return
	 */
	private String formatCall(String s) {
		return s.replace("BidirectionalDijkstra", "-b").replace("DialDijkstra", "-d").replace("DijkstraVertexData", "").replace("BinaryHeapDijkstra", "-h").replace("DijkstraGeoVertexData", "-z").replace(" time", "").replace(" PQops", "").trim();
	}
}
