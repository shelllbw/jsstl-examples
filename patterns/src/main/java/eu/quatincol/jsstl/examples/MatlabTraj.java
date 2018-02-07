package eu.quatincol.jsstl.examples;

import eu.quanticol.jsstl.formula.Signal;
import eu.quanticol.jsstl.space.GraphModel;

public class MatlabTraj {
		
	
	
	public static Signal signalConverter (String [] var, double [][][][] traj, GraphModel graph ){ 	
		final double[] time = new double[traj.length];
		for (int i = 0; i < traj.length; i++)
			time[i] = i;
		final int size = traj[0].length * traj[0][0].length;
		final double[][][] trajMono = new double[size][traj.length][var.length];
		for (int t = 0; t < traj.length; t++) {
			int i = 0;
			for (int y = 0; y < traj[0][0].length; y++)
				for (int x = 0; x < traj[0].length; x++)
					trajMono[i++][t] = traj[t][x][y];
		}
		double[][][] data = trajMono;
		for (int location = 0; location < graph.getNumberOfLocations(); location++) {
			final int timepoins_actual = data[location].length;
			final double[] lastValues = data[location][timepoins_actual - 1];
			final double[][] temporalSignal = new double[traj.length][];
			for (int timeIndex = 0; timeIndex < traj.length; timeIndex++) {
				if (timeIndex < timepoins_actual)
					temporalSignal[timeIndex] = data[location][timeIndex];
				else
					temporalSignal[timeIndex] = lastValues;
			}
			data[location] = temporalSignal;
		}
		Signal signal = new Signal(graph, time, data);
		return signal;
	}

}
