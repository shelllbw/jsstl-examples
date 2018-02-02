package eu.quatincol.jsstl.examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.jsstl.core.formula.AtomicFormula;
import org.jsstl.core.formula.EventuallyFormula;
import org.jsstl.core.formula.EverywhereFormula;
import org.jsstl.core.formula.GloballyFormula;
import org.jsstl.core.formula.NotFormula;
import org.jsstl.core.formula.ParametricExpression;
import org.jsstl.core.formula.ParametricInterval;
import org.jsstl.core.formula.Signal;
import org.jsstl.core.formula.SignalExpression;
import org.jsstl.core.formula.SomewhereFormula;
import org.jsstl.core.formula.SurroundFormula;
import org.jsstl.core.monitor.SpatialBooleanSignal;
import org.jsstl.core.monitor.SpatialQuantitativeSignal;
import org.jsstl.core.space.GraphModel;
import org.jsstl.io.TxtSpatialBoolSat;
import org.jsstl.io.TxtSpatialQuantSat;
import org.jsstl.util.signal.BooleanSignal;
import org.jsstl.util.signal.QuantitativeSignal;

public class ReactionDiffusionSystem_old {

	public static void main(String[] args) throws IOException {
		
		
		// /// %%%%%%%%%%%%% DATA import (traj.txt) %%%%%%%%%%%%/////////
		FileReader fr = new FileReader("data/traj.txt");
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> strings = new ArrayList<String>();
		String line = null;
		while ((line = br.readLine()) != null) {
			strings.add(line);
		}
		int locSize = strings.size();
		int timeSize = strings.get(0).split(",").length / 2;
		double[][][] data = new double[locSize][timeSize][2];
		for (int i = 0; i < locSize; i++) {
			String[] splitted = strings.get(i).split(",");
			for (int t = 0; t < timeSize; t++) {
				data[i][t][0] = Double.valueOf(splitted[t]);
				data[i][t][1] = Double.valueOf(splitted[t + timeSize]);
			}
		}
		br.close();
		fr.close();

		double[] time = new double[timeSize];
		for (int i = 0; i < timeSize; i++)
			time[i] = i;
		System.out.println("Time Size: " + timeSize);
		
		// //////////// %%%%%%%%%%%% GRAPH %%%%%%%%% ///////////////////
		// // interpretation of the grid xMax x yMax as an undirected weighted
		// graph
		int xMax = 32;
		int yMax = 32;
		GraphModel g = new GraphModel();

		for (int yCoord = 0; yCoord < yMax; yCoord++)
			for (int xCoord = 0; xCoord < xMax; xCoord++) {
				int position = yCoord * xMax + xCoord;
				// System.out.println(position);
				String label = "l" + Integer.toString(position);
				g.addLoc(label, position);
			}

		for (int yCoord = 0; yCoord < yMax - 1; yCoord++) {
			for (int xCoord = 0; xCoord < xMax - 1; xCoord++) {
				int position = yCoord * xMax + xCoord;
				g.addEdge(position, position + 1, 1);
				g.addEdge(position, position + xMax, 1);
			}
			g.addEdge(yCoord * xMax + xMax - 1,
					yCoord * xMax + xMax - 1 + xMax, 1);
		}
		for (int position = (yMax - 1) * xMax; position < (xMax * yMax) - 1; position++)
			g.addEdge(position, position + 1, 1);

		// ////// Print of the List of the adjacent locations for each node
		// for (Location lstart : g.locList) {
		// System.out.print(lstart.getLabel() + " ->  { ");
		// for (Location lend : lstart.getNeighbourd()) {
		// System.out.print(lend.getLabel() + " ");
		// }
		// System.out.println("}");
		//
		// }

		// // Computation of the distance matrix
		long startg = System.currentTimeMillis();
		g.dMcomputation();
		long elapsedTimeMillisg = System.currentTimeMillis()-startg;
		float elapsedTimeSecg = elapsedTimeMillisg/1000F;
		System.out.println("time g:" + elapsedTimeSecg);
		

		// /// %%%%%% PROPERTY %%%%%%% /////////////////////////
		// /// VAR and SIGNAL
		final int VAR_A = 0;
		// int VAR_B = 1;
		ParametricExpression expression1 = new ParametricExpression() {

			public SignalExpression eval(Map<String, Double> parameters) {
				return new SignalExpression() {
					public double eval(double... variables) {
						return 0.5 - variables[VAR_A];
					}	
				};
			}
			
			
		};
				
		// SignalExpression expression2 = new SignalExpression() {
		// @Override
		// public double eval(double... variables) {
		// return variables[VAR_B] - 7;
		// }
		// };
		// ///// ATOMIC PROP
		// AtomicFormula atom1 = new AtomicFormula(expression2, false);
		AtomicFormula atom2 = new AtomicFormula(expression1, false);

		// //// AND, OR and NOT
		// NotFormula notFormula1 = new NotFormula(atom1);
		NotFormula notFormula2 = new NotFormula(atom2);

		// /// PARAMETRIC INTERVAL for the distance constraints
		ParametricInterval spaceMetricInterval = new ParametricInterval();
		spaceMetricInterval.setLower(1);
		spaceMetricInterval.setUpper(6);

		// /// SURROUND
		SurroundFormula surround = new SurroundFormula(spaceMetricInterval,
				atom2, notFormula2);

		// /// PARAMETRIC INTERVAL for the globally
		ParametricInterval metricInterval = new ParametricInterval();
		metricInterval.setLower(0);
		metricInterval.setUpper(50);

		// /// PARAMETRIC INTERVAL for the eventually
		ParametricInterval metricInterval1 = new ParametricInterval();
		metricInterval1.setLower(38);
		metricInterval1.setUpper(40);

		// ///// GLOBALLY and EVENTUALLY
		GloballyFormula glob = new GloballyFormula(metricInterval, surround);
		EventuallyFormula eventually = new EventuallyFormula(metricInterval1,
				glob);

		// /// PARAMETRIC INTERVAL for the somewhere
		ParametricInterval spaceInt1 = new ParametricInterval();
		spaceInt1.setLower(0);
		spaceInt1.setUpper(10);

		// /// PARAMETRIC INTERVAL for the everywhere
		ParametricInterval spaceInt2 = new ParametricInterval();
		spaceInt2.setLower(0);
		spaceInt2.setUpper(45);

		// ///// SOMEWHERE and EVERYWHERE
		SomewhereFormula somewh = new SomewhereFormula(spaceInt1, eventually);
		EverywhereFormula everywhere = new EverywhereFormula(spaceInt2, somewh);

		// /////// %%%%%%%% CHECK %%%%%%% ////////////////////////

		Signal s = new Signal(g, time, data);

		// /////// Boolean and Quantitative Signals ////////	
		long start = System.currentTimeMillis();
		SpatialBooleanSignal b = eventually.booleanCheck(null, g, s);
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		float elapsedTimeSec = elapsedTimeMillis/1000F;
		System.out.println("time b:" + elapsedTimeSec);
		
		long startq = System.currentTimeMillis();
		SpatialQuantitativeSignal q = eventually.quantitativeCheck(null,g, s);	
		long elapsedTimeMillisq = System.currentTimeMillis()-startq;
		float elapsedTimeSecq = elapsedTimeMillisq/1000F;
		System.out.println("time q:" + elapsedTimeSecq);

		// //////// Satisfaction /////////////
		double[] boolSat = b.boolSat();
		double[] quantSat = q.quantSat();
		// / Generation of the output data.txt
		 TxtSpatialQuantSat outDataQuant = new TxtSpatialQuantSat();
		 outDataQuant.write(quantSat, "data/dataSatQuant.txt");
		 TxtSpatialBoolSat outDataBool = new TxtSpatialBoolSat();
		 outDataBool.write(boolSat, "data/dataSatBool.txt");

		//
		// Satisfaction at location 0 and time 0
		// (b and q are spatio-temporal signals)
		// (boolSat and quantSat are spatial signals)

			BooleanSignal bt = b.spatialBoleanSignal.get(g.getLocation(0));
			QuantitativeSignal qt = q.spatialQuantitativeSignal.get(g.getLocation(0));
//			boolean satB = bt.getValueAt(0);
//			double satQ = qt.getValueAt(0);
			double satB = boolSat[0];
			double satQ = quantSat[0];
			System.out.println("Formula satified: " + satB);
			System.out.println("Robustness value: " + satQ);
		

	}
}
