package eu.quanticol.jsstl.examples.bss;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsstl.core.formula.Signal;
import org.jsstl.core.space.GraphModel;

import simhya.dataprocessing.DataCollector;
import simhya.dataprocessing.HybridDataCollector;
import simhya.dataprocessing.OdeDataCollector;
import simhya.dataprocessing.StochasticDataCollector;
import simhya.dataprocessing.Trajectory;
import simhya.model.flat.FlatModel;
import simhya.model.flat.parser.FlatParser;
import simhya.simengine.HybridSimulator;
import simhya.simengine.Simulator;
import simhya.simengine.SimulatorFactory;
import simhya.simengine.ode.OdeSimulator;
import simhya.simengine.utils.InactiveProgressMonitor;

/**
 * Wrapper class for loading SimMyA models easily. (the original
 * matlab.SimHyAModel class is unpredictable for some reason)
 */
public class SimHyAWrapper {

	private FlatModel flatModel;
	private Simulator simulator;
	private DataCollector collector;

	public void loadModel(String modelFile) throws Exception {
		FlatParser parser = new FlatParser();
		flatModel = parser.parseFromFile(modelFile);
		setSSA();
	}

	public FlatModel getFlatModel() {
		return flatModel;
	}
	
	public DataCollector getCollector() {
		return collector;
	}
	
	public double []  timesTraj(Trajectory traj){
		int timeLength = traj.getPoints();
		double [][] trajectory = traj.getAllData();
		for (int i = 1; i < traj.getPoints(); i++) {
			if (trajectory[0][i] == 0) {
				timeLength = i;
			}
		}
		final double[] times = new double[timeLength];
		for (int i = 0; i < timeLength; i++) {
			times[i] = trajectory[0][i];
		}
		return times;
	}

	public Signal readTraj(GraphModel graph, String filename) throws IOException{
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> strings = new ArrayList<String>();
		String line = null;
		while ((line = br.readLine()) != null) {
			strings.add(line);
		}
		int timeSize = strings.size() - 2;
		int locSize = (strings.get(0).split("\t\t").length - 1) / 2;
		double[][][] data = new double[locSize][timeSize][2];
		double[] time = new double[strings.size()];
		for (int t = 0; t < strings.size()-1; t++) {
			String[] splitted = strings.get(t+1).split("\t\t");
			time[t] = Double.valueOf(splitted[0]);
		}
		
		int timeLength = time.length;
		for (int i = 1; i < timeLength; i++) {
			if (time[i] == 0) {
				timeLength = i;
			}
		}
		final double[] times = new double[timeLength];
		for (int t = 0; t < timeLength; t++) {
			times[t] = time[t];
		}
	
		for (int t = 0; t < timeLength; t++) {
			String[] splitted = strings.get(t+1).split("\t\t");
			for (int i = 0; i < locSize; i++) {		
				data[i][t][0] = Double.valueOf(splitted[i+1]);
				data[i][t][1] = Double.valueOf(splitted[i+1+locSize]);
			}
		}
		br.close();
		fr.close();
		Signal s = new Signal(graph, times, data);
		return s;
 }
	
	public double[][][] dataTraj(Trajectory traj, int locations, int truns, int var) {
		final double[][][] data = new double[locations][truns][var];
		for (int i = 0; i < locations; i++) {
			for (int t = 0; t < truns; t++) {
				data[i][t][0] = traj.getAllData()[i+1][t];
				data[i][t][1] = traj.getAllData()[i+1 + locations][t];
			}
		}
		return data;
	}
	
	public void changeInitVar(String var, Double value) {
		flatModel.changeInitialValueOfVariable(var, value);	
	}

	public void changeInitPar(String par, Double value) {
		flatModel.changeInitialValueOfParameter(par, value);	
	}

	
	/**
	 * @param tfinal
	 *            The simulation end time
	 * @param timepoints
	 *            The number of timepoints returned (excluding the first one)
	 * @return A 2-D array whose first row is the times for which the state is
	 *         recorded. The rest of the rows correspond to the species
	 *         involved.
	 * 
	 */

	public Trajectory simulate(int vartosave, double tinit, double tfinal, int timepoints) {
		simulator.setInitialTime(tinit);
		simulator.setFinalTime(tfinal);
		collector.clearAll();
		ArrayList<String>  vars = flatModel.getOriginalModelVariables();
		ArrayList<String>  BS = new ArrayList<String>(vars.subList(0, vartosave));
		collector.setVarsToBeSaved(BS);
		collector.storeWholeTrajectoryData(1);
		collector.setPrintConditionByTime(timepoints, tfinal);
		simulator.initialize();
		// breaks otherwise
		if (!(simulator instanceof OdeSimulator)
				&& !(simulator instanceof HybridSimulator))
			collector.newTrajectory();
		simulator.resetModel(true);
		simulator.reinitialize();
		simulator.run();
		return collector.getTrajectory(0);
	}

	public void setSSA() {
		collector = new StochasticDataCollector(flatModel);
		collector.saveAllVariables();
		simulator = SimulatorFactory.newSSAsimulator(flatModel, collector);
		simulator.setProgressMonitor(new InactiveProgressMonitor());
	}

	public void setGB() {
		collector = new StochasticDataCollector(flatModel);
		collector.saveAllVariables();
		simulator = SimulatorFactory.newGBsimulator(flatModel, collector);
		simulator.setProgressMonitor(new InactiveProgressMonitor());
	}

	public void setHybrid() {
		collector = new HybridDataCollector(flatModel);
		collector.saveAllVariables();
		simulator = SimulatorFactory.newHybridSimulator(flatModel, collector);
		simulator.setProgressMonitor(new InactiveProgressMonitor());
	}

	public void setODE() {
		collector = new OdeDataCollector(flatModel);
		collector.saveAllVariables();
		simulator = SimulatorFactory.newODEsimulator(flatModel, collector);
		simulator.setProgressMonitor(new InactiveProgressMonitor());
	}

}
