package nufeb.examples.ibm.shear;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;




import nufeb.examples.ibm.IBMScript;
//import eu.quanticol.jsstl.core.dsl.ScriptLoader;
import eu.quanticol.jsstl.core.formula.Signal;
import eu.quanticol.jsstl.core.formula.jSSTLScript;
import eu.quanticol.jsstl.core.signal.BooleanSignal;
import eu.quanticol.jsstl.core.space.GraphModel;
import eu.quanticol.jsstl.core.io.TraGraphModelReader;
import eu.quanticol.jsstl.core.monitor.SpatialBooleanSignal;

public class IBMShear {

	public static void main(String[] args) throws Exception {
		//// /// %%%%%%% GRAPH %%%%% /////////
		TraGraphModelReader graphread = new TraGraphModelReader();
		GraphModel graph = graphread.read("models/sstl_shear_model.txt");
		long start = System.currentTimeMillis();

		System.out.println("Compute shortest path...");
		graph.dMSurfacecomputation();
		double dmtime = (System.currentTimeMillis()-start)/1000.0;
		System.out.println("DM Computation: "+dmtime);

		//// /// %%%%%%% PROPERTY %%%%% //////////
		jSSTLScript script = new IBMShearScript();
		// /// %%%%%%% DATA import %%%%%%%%%%%%/////////

		check(script, graph, getShearParams());

		System.out.println("Done");

	}

	private static ArrayList<HashMap<String, Double>> getShearParams() {
		ArrayList<HashMap<String, Double>> toReturn = new ArrayList<>();

		for (double i = 176400; i < 540000; i = i + 172800) {
			// short distance
			HashMap<String,Double> map1 = new HashMap<>();
			map1.put("maxT", i);
			map1.put("fromX", 1e-4);
			map1.put("toX", 1.05e-4);
			toReturn.add(map1);
			// mid distance
			HashMap<String,Double> map2 = new HashMap<>();
			map2.put("maxT", i);
			map2.put("fromX", 1.5e-4);
			map2.put("toX", 1.55e-4);
			toReturn.add(map2);
			// long distance
			HashMap<String,Double> map3 = new HashMap<>();
			map3.put("maxT", i);
			map3.put("fromX", 1.95e-4);
			map3.put("toX", 2e-4);
			toReturn.add(map3);
		}
		
		return toReturn;
	}

	
	public static Signal readMeshTraj(GraphModel graph, String filename) throws IOException{
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> strings = new ArrayList<String>();
		String line = null;
		int timeSize = 0;
		while ((line = br.readLine()) != null) {
			strings.add(line);
			String[] key = line.split(",");

			if (key[0].equals("Time"))
				timeSize ++;
		}
		System.out.println("timeSize: "+timeSize);
		int locSize = graph.getNumberOfLocations();
		System.out.println("locSize: "+locSize);
		double[][][] data = new double[locSize][timeSize][2];
		double[] time = new double[timeSize];
		
		for (int t = 0; t < timeSize; t++) {
			String[] splitted = strings.get(t*locSize+t).split(",");
			time[t] = Double.valueOf(splitted[1]);
		}

		for (int t = 0; t < timeSize; t++) {
			for (int i = 0; i < locSize; i++) {	
				String[] splitted = strings.get(t*locSize+i+t+1).split(",");
				int loc = Integer.valueOf(splitted[0])-1;
				data[loc][t][0] = Double.valueOf(splitted[1]);
				//System.out.println("loc: "+loc + " value: "+  data[loc][t][0]);
			}
		}

		br.close();
		fr.close();
		Signal s = new Signal(graph, time, data);
		return s;
	}

	public static void check(jSSTLScript script, GraphModel graph, ArrayList<HashMap<String,Double>> params) throws IOException {

		Signal s = readMeshTraj(graph, "models/trajectories/shear/volf_all.txt");
		Signal s1 = readMeshTraj(graph, "models/trajectories/shear/gridx.txt");
		
		for (int i = 0; i < params.size(); i++) {
			
			HashMap<String,Double> parValues = params.get(i);
			
			PrintWriter stprinter = new PrintWriter("data/"+"t" +parValues.get("maxT") + "-x" + + parValues.get("fromX") +".txt");
			//PrintWriter stprinter = new PrintWriter("data/test.txt");
			
			//Signal s = readMeshTraj(graph, "models/trajectories/test/test.txt");

			double[] boolSat = script.booleanSat("streamer-formation", parValues, graph, s);
			double[] boolSat1 = script.booleanSat("streamer-bound", parValues, graph, s1);
			
			System.out.println("checking : " + parValues.get("maxT") + "-x" + + parValues.get("fromX"));
			
			for (int j = 0; j < graph.getNumberOfLocations(); j++) {			
				if(boolSat[j] > 0 && boolSat1[j] > 0)
					stprinter.println(graph.getLocation(j)+ "," + 1);
				else if (boolSat[j] == 0 && boolSat1[j] > 0)
					stprinter.println(graph.getLocation(j)+ "," + 0);
			}
			
			stprinter.close();
		}
	}
	
}
