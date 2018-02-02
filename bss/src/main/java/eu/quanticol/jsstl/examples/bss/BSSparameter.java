package eu.quanticol.jsstl.examples.bss;
import java.util.ArrayList;

import simhya.matlab.SimHyAModel;

public class BSSparameter {

	public static void main(String[] args) {
		SimHyAModel m = new SimHyAModel();
		m.loadModel("models/733bike.txt");
		m.setGB();
		
		int tf = 50;
		int steps = 100;
		m.simulate(tf, steps);
//		
//		ArrayList<String> vars = new ArrayList<String>();
//		vars.add("B0");
//		m.plotTrajectory(vars);
//		
//		String [] param = {"da","ds"};
//		Double [] values = {1.0,2.0};

	}

}
