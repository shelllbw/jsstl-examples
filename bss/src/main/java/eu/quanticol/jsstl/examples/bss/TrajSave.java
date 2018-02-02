package eu.quanticol.jsstl.examples.bss;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

import org.jsstl.core.formula.Signal;
import org.jsstl.core.space.GraphModel;
import org.jsstl.io.TraGraphModelReader;

public class TrajSave {

	public static void main(String[] args) throws Exception {

//		 SimHyAWrapper model = new SimHyAWrapper();
//		 model.loadModel("models/bike733hybrid.txt");
//		 model.setGB();
//		 int tinit = 0;
//		 int tf = 45;
//		 int steps = 100;
//		 int vartosave = 733 * 2;
//		 int runs = 1000;
//		 String string = "models/trajectories/traj";
//		 model.simulate(vartosave, tinit, tf, steps);
//		 for (int i = 0; i < runs; i++) {
//		 model.simulate(vartosave, tinit, tf, steps);
//		 model.getCollector().saveSingleTrajectoryToCSV(0, string + i + ".txt");
//		 }

		//// /// %%%%%%% GRAPH %%%%% /////////
		TraGraphModelReader graphread = new TraGraphModelReader();
		GraphModel graph = graphread.read("models/733stationsGraph.tra");
		graph.dMcomputation();
		// /// %%%%%%% DATA import %%%%%%%%%%%%/////////
		String string = "models/trajectories/traj";
		int tinit = 0;
		int tf = 45;
		int steps = 100;
		int vartosave = 733 * 2;
		int runs = 1000;
		SimHyAWrapper model = new SimHyAWrapper();
		model.loadModel("models/bike733hybrid.txt");
		for (int i = 0; i < runs; i++) {
			Signal s = model.readTraj(graph, string + i + ".txt");
		}
	}
}

 
