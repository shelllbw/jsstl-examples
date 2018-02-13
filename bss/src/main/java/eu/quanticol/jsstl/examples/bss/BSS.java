package eu.quanticol.jsstl.examples.bss;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import eu.quanticol.jsstl.dsl.ScriptLoader;
import eu.quanticol.jsstl.formula.Signal;
import eu.quanticol.jsstl.formula.SignalStatistics;
import eu.quanticol.jsstl.formula.jSSTLScript;
import eu.quanticol.jsstl.space.GraphModel;
import eu.quanticol.jsstl.io.TraGraphModelReader;

public class BSS {

	public static void main(String[] args) throws Exception {
		long tStart = System.currentTimeMillis();
		//// /// %%%%%%% GRAPH %%%%% /////////
		TraGraphModelReader graphread = new TraGraphModelReader();
		GraphModel graph = graphread.read("models/bssSpatialModel.tra");
		long start = System.currentTimeMillis();
		graph.dMcomputation();
		double dmtime = (System.currentTimeMillis()-start)/1000.0;
		String trajPref = "models/trajectories/traj";
		int runs = 10;
		int locations = graph.getNumberOfLocations();
		//// /// %%%%%%% PROPERTY %%%%% //////////
//		ScriptLoader loader = new ScriptLoader();
//		jSSTLScript script = loader.load("models/BSS.jsstl");
		jSSTLScript script = new BSSScript();
		// /// %%%%%%% DATA import %%%%%%%%%%%%/////////

		double t1 = check(script, "phi1", graph, locations, getPhi_1_and_2_Params(11), runs, trajPref);
		double t2 = check(script, "phi2", graph, locations, getPhi_1_and_2_Params(11), runs, trajPref);
		double t3 = check(script, "phi1bis", graph, locations, getPhi1BisParams(11), runs, trajPref);
		double t4 = check(script, "psi1", graph, locations, getPsiParams(5,0.0,0.5,2.5,5), runs, trajPref);
		double t5 = check(script, "psi2", graph, locations, getPsiParams(5,0.0,0.5,2.5,5), runs, trajPref);
		check(script, "test", graph, locations, getPsiParams(1,2,0.5,20,2), runs, trajPref);
		System.out.println("phi1: "+t1);
		System.out.println("phi2: "+t2);
		System.out.println("phibis: "+t3);
		System.out.println("psi1: "+t4);
		System.out.println("psi2: "+t5);
		System.out.println("DM Computation: "+dmtime);

	}
	
	private static ArrayList<HashMap<String, Double>> getPsiParams(int size, double dw_start, double dw_step, double tw_start, double tw_step ) {
		ArrayList<HashMap<String, Double>> toReturn = new ArrayList<>();
		double dw = dw_start;
		double tw = tw_start;
		for( int i=0 ; i<size; i++) {
			HashMap<String,Double> map = new HashMap<>();
			map.put("dw", dw);
			map.put("tw", tw);
			toReturn.add(map);
			dw = dw + dw_step;
			tw = tw + tw_step;
		}
		return toReturn;
	}

	private static ArrayList<HashMap<String, Double>> getPhi_1_and_2_Params(int size) {
		ArrayList<HashMap<String, Double>> toReturn = new ArrayList<>();
		double d = 0;
		double t = 0;
		for( int i=0 ; i<size; i++) {
			HashMap<String,Double> map = new HashMap<>();
			map.put("d", d);
			map.put("t", t);
			toReturn.add(map);
			d = d + 0.1;
			t = t + 1;
		}
		return toReturn;
	}

	private static ArrayList<HashMap<String, Double>> getPhi1BisParams(int size) {
		ArrayList<HashMap<String, Double>> toReturn = new ArrayList<>();
		double d = 0;
		double t = 0;
		double tw = 2.5;
		for( int i=0 ; i<size; i++) {
			HashMap<String,Double> map = new HashMap<>();
			map.put("d", d);
			map.put("t", t);
			map.put("tw", t);
			toReturn.add(map);
			d = d + 0.1;
			t = t + 1;
			tw = tw + 1;
		}
		return toReturn;
	}

	public static Signal readTraj(GraphModel graph, String filename) throws IOException{
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

	public static double check( jSSTLScript script, String formula, GraphModel graph, int locations, ArrayList<HashMap<String,Double>> params , int runs, String trajPref) throws IOException {
		ArrayList<SignalStatistics> statphi = new ArrayList<SignalStatistics>();
		double[][] mean = new double[params.size()][locations];
		double[][] stdev = new double[params.size()][locations];
		double time = 0.0;
		String textMean = "";
		String textStdev = "";
//		String textphi3 = "";
//		String textphi4 = "";
		for (int i = 0; i < params.size(); i++) {
			statphi.add(i, new SignalStatistics(locations));
		}
		
		for (int i = 0; i < runs; i++) {
			System.out.println("("+formula+") Trajectory:" + i);
			//Trajectory traj = model.simulate( vartosave, tinit, tf, steps);
			//double[] times = model.timesTraj(traj);
			//double[][][] data = model.dataTraj(traj, locations, times.length, var);
			//Signal s = new Signal(graph, times, data);
			Signal s = readTraj(graph, trajPref + i + ".txt");
			for (int k = 0; k < params.size(); k++) {
				// System.out.println("k :" + k);
				HashMap<String,Double> parValues = params.get(k);
				//System.out.println(parValues);
				// System.out.println(Arrays.toString(traj.getAllData()));
				// /////// %%%%%%%% CHECK %%%%%%% ////////////////////////
				long tStartB = System.currentTimeMillis();
				statphi.get(k).add(script.booleanSat(formula, parValues, graph, s));
				long tEndB = System.currentTimeMillis();
				double elapsedSecondsB = (tEndB - tStartB) / 1000.0;
				time += elapsedSecondsB;
			}
		}

		for (int k = 0; k < params.size(); k++) {
			for (int l = 0; l < locations; l++) {
				// double [] min1 = statphi1.get(k).getMin();
				// double [] max1 = statphi1.get(k).getMax();
				mean[k][l] = statphi.get(k).getAverage()[l];
				textMean += String.format(Locale.US, " %20.10f", mean[k][l]);
				stdev[k][l] = statphi.get(k).getStandardDeviation()[l];
				textStdev += String.format(Locale.US, " %20.10f", stdev[k][l]);
			}
			textMean += "\n";
			textStdev += "\n";
		}
		PrintWriter meanprinter = new PrintWriter("data/"+formula+"_mean.txt");
		meanprinter.print(textMean);
		meanprinter.close();
		PrintWriter stprinter = new PrintWriter("data/"+formula+"_sdev.txt");
		stprinter.print(textStdev);
		stprinter.close();
		return time/runs;
	}
	
}
