package eu.quanticol.jsstl.examples.bss;


import org.jsstl.core.formula.Signal;
import org.jsstl.core.space.GraphModel;

import simhya.dataprocessing.Trajectory;

public class SpatioTemporalTransform {

	private SimHyAWrapper model = new SimHyAWrapper();
	private GraphModel graph;
	private int locations;

	public void loadModel(String modelFile) throws Exception {
		model.loadModel(modelFile);
		model.setGB();
		locations = model.getFlatModel().getVariablesValues().length;
		this.graph = createGraphForGrid(locations, 1);
	}

	public GraphModel getGraphModel() {
		return graph;
	}

	public Signal simulate(int runs, int vartosave, double tinit, double tfinal, int timepoints) {
		Trajectory traj = model.simulate(vartosave, tinit,tfinal, timepoints);
		
		int timeLength = traj.getAllData()[0].length;

		for (int i = 1; i < traj.getAllData()[0].length; i++) {
			if (traj.getAllData()[0][i] == 0) {
				timeLength = i;
			}

		}
		final double[] times = new double[timeLength];
		for (int i = 0; i < timeLength; i++) {
			times[i] = traj.getAllData()[0][i];
		}
		
		//final double[] times = traj[0];
		final double[][][] data = new double[locations][times.length][1];
		for (int i = 0; i < locations; i++)
			for (int t = 0; t < times.length; t++)
				data[i][t][0] = traj.getAllData()[i + 1][t];
		return new Signal(graph, times, data);
	}

	static protected GraphModel createGraphForGrid(int xMax, int yMax) {
		GraphModel graph = new GraphModel();
		for (int yCoord = 0; yCoord < yMax; yCoord++)
			for (int xCoord = 0; xCoord < xMax; xCoord++) {
				int position = yCoord * xMax + xCoord;
				// System.out.println(position);
				String label = "l" + Integer.toString(position);
				graph.addLoc(label, position);
			}
		for (int yCoord = 0; yCoord < yMax - 1; yCoord++) {
			for (int xCoord = 0; xCoord < xMax - 1; xCoord++) {
				int position = yCoord * xMax + xCoord;
				graph.addEdge(position, position + 1, 1);
				graph.addEdge(position, position + xMax, 1);
			}
			graph.addEdge(yCoord * xMax + xMax - 1, yCoord * xMax + xMax - 1
					+ xMax, 1);
		}
		for (int position = (yMax - 1) * xMax; position < (xMax * yMax) - 1; position++)
			graph.addEdge(position, position + 1, 1);
		// // Computation of the distance matrix
		graph.dMcomputation();
		return graph;
	}

}
