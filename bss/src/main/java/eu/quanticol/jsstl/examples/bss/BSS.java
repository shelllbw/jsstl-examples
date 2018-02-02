package eu.quanticol.jsstl.examples.bss;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import org.jsstl.core.formula.Signal;
import org.jsstl.core.formula.SignalStatistics;
import org.jsstl.core.formula.jSSTLScript;
import org.jsstl.core.space.GraphModel;
import org.jsstl.io.TraGraphModelReader;

public class BSS {

	public static void main(String[] args) throws Exception {
		long tStart = System.currentTimeMillis();
		//// /// %%%%%%% GRAPH %%%%% /////////
		TraGraphModelReader graphread = new TraGraphModelReader();
		GraphModel graph = graphread.read("models/733stationsGraph.tra");
		graph.dMcomputation();
		int locations = graph.getNumberOfLocations();
		//// /// %%%%%%% PROPERTY %%%%% //////////
//		ScriptLoader loader = new ScriptLoader();
//		jSSTLScript script = loader.load("models/BSS.sstl");
		jSSTLScript script = new BSSScript();
		// /// %%%%%%% DATA import %%%%%%%%%%%%/////////
		String string = "models/trajectories/traj";
		SimHyAWrapper model = new SimHyAWrapper();
		model.loadModel("models/bike733hybrid.txt");
		model.setGB();
		int runs = 1;
		HashMap<String, Double> parameters = new HashMap<String, Double>();
		int runparam = 1;
		// Trajectory traj = model.simulate(vartosave, tinit, tf, steps);

		/////// Data formulas
		ArrayList<SignalStatistics> statphi1 = new ArrayList<SignalStatistics>();
		ArrayList<SignalStatistics> statphi2 = new ArrayList<>();
		double[][] mean1 = new double[runparam][locations];
		double[][] mean2 = new double[runparam][locations];
		double[][] stdev1 = new double[runparam][locations];
		double[][] stdev2 = new double[runparam][locations];
		double[][] outputmatrix3 = new double[runparam][runs];
		double[][] outputmatrix4 = new double[runparam][runs];
		String textphi1mean = "";
		String textphi2mean = "";
		String textphi1stdev = "";
		String textphi2stdev = "";
		String textphi3 = "";
		String textphi4 = "";
		for (int i = 0; i < runparam; i++) {
			SignalStatistics statistic1 = new SignalStatistics(locations);
			SignalStatistics statistic2 = new SignalStatistics(locations);
			statphi1.add(i, statistic1);
			statphi2.add(i, statistic2);
		}

		for (int i = 0; i < runs; i++) {
			System.out.println("i :" + i);
			//Trajectory traj = model.simulate( vartosave, tinit, tf, steps);
			//double[] times = model.timesTraj(traj);
			//double[][][] data = model.dataTraj(traj, locations, times.length, var);
			//Signal s = new Signal(graph, times, data);
			Signal s = model.readTraj(graph, string + i + ".txt");
			double d = 0;
			double t = 0;
			for (int k = 0; k < runparam; k++) {
				// System.out.println("k :" + k);
				parameters.put("d", d);
				parameters.put("t", t);
				// System.out.println(Arrays.toString(traj.getAllData()));
				// /////// %%%%%%%% CHECK %%%%%%% ////////////////////////
				long tStartB = System.currentTimeMillis();
				statphi1.get(k).add(script.booleanSat("phi1", parameters, graph, s));
				long tEndB = System.currentTimeMillis();
				double elapsedSecondsB = (tEndB - tStartB) / 1000.0;
				System.out.println(elapsedSecondsB);
//				statphi2.get(k).add(script.booleanSat("phi2", parameters, graph, s));
				long tStartQ = System.currentTimeMillis();
				outputmatrix3[k][i] = script.quantitativeSat("phi1", parameters, graph, s)[0];
				long tEndQ = System.currentTimeMillis();
				double elapsedSecondsQ = (tEndQ - tStartQ) / 1000.0;
				System.out.println(elapsedSecondsQ);
//				outputmatrix4[k][i] = script.quantitativeSat("phi4", parameters, graph, s)[0];
//				textphi3 += String.format(Locale.US, " %20.10f", outputmatrix3[k][i]);
//				textphi4 += String.format(Locale.US, " %20.10f", outputmatrix4[k][i]);
				d = d + 0.1;
				t = t + 1;
			}
			textphi3 += "\n";
			textphi4 += "\n";
		}

//		for (int k = 0; k < runparam; k++) {
//			for (int l = 0; l < locations; l++) {
//				// double [] min1 = statphi1.get(k).getMin();
//				// double [] max1 = statphi1.get(k).getMax();
//				mean1[k][l] = statphi1.get(k).getAverage()[l];
//				mean2[k][l] = statphi2.get(k).getAverage()[l];
//				textphi1mean += String.format(Locale.US, " %20.10f", mean1[k][l]);
//				textphi2mean += String.format(Locale.US, " %20.10f", mean2[k][l]);
//				// double [] min2 = statphi2.get(k).getMin();
//				// double [] max2 = statphi2.get(k).getMax();
//				stdev1[k][l] = statphi1.get(k).getStandardDeviation()[l];
//				stdev2[k][l] = statphi2.get(k).getStandardDeviation()[l];
//				textphi1stdev += String.format(Locale.US, " %20.10f", stdev1[k][l]);
//				textphi2stdev += String.format(Locale.US, " %20.10f", stdev2[k][l]);
//			}
//			textphi1mean += "\n";
//			textphi2mean += "\n";
//			textphi1stdev += "\n";
//			textphi2stdev += "\n";
//		}
//		PrintWriter printerphi1mean = new PrintWriter("data/phi1mean10.txt");
//		printerphi1mean.print(textphi1mean);
//		printerphi1mean.close();
//		PrintWriter printerphi2mean = new PrintWriter("data/phi2mean10.txt");
//		printerphi2mean.print(textphi2mean);
//		printerphi2mean.close();
//		PrintWriter printerphi1stdev = new PrintWriter("data/phi1std10.txt");
//		printerphi1stdev.print(textphi1stdev);
//		printerphi1stdev.close();
//		PrintWriter printerphi2stdev = new PrintWriter("data/phi2std10.txt");
//		printerphi2stdev.print(textphi2stdev);
//		printerphi2stdev.close();
//		PrintWriter printerphi3 = new PrintWriter("data/phi310.txt");
//		printerphi3.print(textphi3);
//		printerphi3.close();
//		PrintWriter printerphi4 = new PrintWriter("data/phi410.txt");
//		printerphi4.print(textphi4);
//		printerphi4.close();
//
//		long tEnd = System.currentTimeMillis();
//		double elapsedSeconds = (tEnd - tStart) / 1000.0;
//		System.out.println(elapsedSeconds);
//		/////// %%%%%%% OUTPUT %%%%% /////////
//		// System.out.println(Arrays.toString(boolSat));
//		// System.out.println(Arrays.toString(quantSat));
//		// TxtSpatialQuantSat outDataQuant = new TxtSpatialQuantSat();
//		// outDataQuant.write(quantSat, "data/dataSatQuant.txt");
//		// TxtSpatialBoolSat outDataBool = new TxtSpatialBoolSat();
//		// outDataBool.write(boolSat, "data/dataSatBool.txt");

	}

}
