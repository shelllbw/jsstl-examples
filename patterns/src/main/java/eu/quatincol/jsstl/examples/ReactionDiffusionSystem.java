package eu.quatincol.jsstl.examples;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
 
import org.jsstl.core.formula.Formula;
import org.jsstl.core.formula.Signal;
import org.jsstl.core.formula.jSSTLScript;
import org.jsstl.core.monitor.SpatialBooleanSignal;
import org.jsstl.core.monitor.SpatialQuantitativeSignal;
import org.jsstl.core.space.GraphModel;
import org.jsstl.io.FolderSignalReader;
import org.jsstl.io.TxtSpatialBoolSat;
import org.jsstl.io.TxtSpatialQuantSat;
import org.jsstl.io.TxtSpatialQuantSignal;
import org.jsstl.util.signal.BooleanSignal;
import org.jsstl.util.signal.QuantitativeSignal;

public class ReactionDiffusionSystem {

	public static void main(String[] args) throws IOException {

		
		// %%%%%%%%%%  GRAPH  %%%%%%%%% //
		
		// Designing the grid
		GraphModel graph = GraphModel.createGrid(32, 32, 1.0);
		// Computing of the distance matrix
		graph.dMcomputation();


		// %%%%%%%%% PROPERTY %%%%%%% //
		
		// loading the formulas files
		jSSTLScript script = new jSSTLPatternScript();
		String[] formulae = script.getFormulae(); 
		
		// Printing the list of formulas. The formulas are memorized in the alphabetic order
		System.out.println(Arrays.toString( formulae )); 
		// Choosing the formula that we want to to verify. The spot formation formula. 
		Formula phi = script.getFormula("spotformation"); 
		
		// Loading the variables. That we have defined in the formulas files.
		String [] var = script.getVariables();
		
		// %%%%%%%%%%%%% DATA (trajectory) %%%%%%%%%%%%/////////
		
		// Loading the  trajectory files.
		FolderSignalReader readSignal = new FolderSignalReader(graph, var);
		File folder = new  File("data/allDataPattern");
		Signal s = readSignal.read(folder);
				

		 /////// %%%%%%%% MONITORING %%%%%%% ////////////////////////

		// /////// Boolean and Quantitative Signal ////////
		SpatialBooleanSignal boolSign = phi.booleanCheck(null, graph, s);		
		SpatialQuantitativeSignal quantSign = phi.quantitativeCheck(null,graph, s);
		// //////// Boolean and Quantitative Satisfaction /////////////
		double[] boolSat = boolSign.boolSat();
		double[] quantSat = quantSign.quantSat();
		
		 /////// %%%%%%%% Generation of the output %%%%%%% ////////////////////////

		/// Observation: boolSign and quantSign are spatio-temporal signals. They give
		/// the value of the property's satisfaction in each location at each time of
		/// the trajectory input data
		TxtSpatialQuantSignal outDataQuantSignal = new TxtSpatialQuantSignal();
	    outDataQuantSignal.write(quantSign, "data/dataQuantSignal.txt");

	    /// Observation: boolSat and quantSat are spatial signals, they give the value
	    /// of the property's satisfaction in each location 
		TxtSpatialQuantSat outDataQuant = new TxtSpatialQuantSat();
		outDataQuant.write(quantSat, "data/dataSatQuant.txt");
		TxtSpatialBoolSat outDataBool = new TxtSpatialBoolSat();
		outDataBool.write(boolSat, "data/dataSatBool.txt");

		/// Monitoring the property in a specific location
		/// e.g. Satisfaction of the property pattern at  time 0 
		/// in location 0,1 and 2 (position (1,1),(1,2) and (1,3) in the grid)
		/// This is useful to check "global" properties as the pattern property. 
		/// Indeed using the everywhere operator you can guarantee 
		/// the satisfaction in all the locations
		Formula phipattern = script.getFormula("pattern"); 	
		SpatialQuantitativeSignal q = phipattern.quantitativeCheck(null,graph, s);
		double[] Satquant = q.quantSat();

		// ////////  Satisfaction in location 0, 1, 2 /////////////
		System.out.println("Robustness value: " + Satquant[0]);
		System.out.println("Robustness value: " + Satquant[1]);
		System.out.println("Robustness value: " + Satquant[2]);
		}
}
