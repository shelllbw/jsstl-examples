package eu.quatincol.jsstl.examples;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.jsstl.core.formula.Formula;
import org.jsstl.core.formula.Signal;
import org.jsstl.core.formula.SignalStatistics;
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


import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import matlabcontrol.MatlabProxyFactoryOptions.Builder;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;


public class ReactionDiffusionSystemMatlab {

	public static void main(String[] args) throws IOException, MatlabInvocationException, MatlabConnectionException {
		
		
		// //////////// %%%%%%%%%%%% GRAPH %%%%%%%%% ///////////////////
		// Designing the grid
		GraphModel g = GraphModel.createGrid(32, 32, 1.0);
		// Computing of the distance matrix
		g.dMcomputation();
		

		// /// %%%%%% PROPERTY %%%%%%% /////////////////////////		
		// loading the formulas files
		jSSTLScript script = new jSSTLPatternScript();
		String[] formulae = script.getFormulae(); 
		
		// Printing the list of formulas. The formulas are memorized in the alphabetic order
		System.out.println(Arrays.toString( formulae )); 
		// Choosing the formula that we want to to verify. The spot formation formula. 
		Formula phi = script.getFormula("spotformation"); 
		
		// Loading the variables. That we have defined in the formulas files.
		String [] var = script.getVariables();

		
		
		////  %%%%%%%%%  TRAJECTORIES %%%%%%  //////
		/////Connection with Matlab /////////				
		Builder optionsBuilder = new Builder();
		optionsBuilder.setHidden(true);
		MatlabProxyFactoryOptions options = optionsBuilder.build();

		MatlabProxyFactory factory = new MatlabProxyFactory(options);
		System.out.print("Connecting to MATLAB... ");
		MatlabProxy proxy = factory.getProxy();
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		System.out.println("Done!");

		proxy.eval("cd matlab;");		
		/// Generation of the trace
		proxy.eval("TuringDataGenerator");
		MatlabNumericArray array = processor.getNumericArray("traj");
		double [][][][] traj = array.getRealArray4D();
		Signal s = new Signal( g, var, traj);
		
		
		
		// /////// %%%%%%%% CHECK %%%%%%% ////////////////////////
		// /////// Boolean and Quantitative Signal ////////
		SpatialBooleanSignal b = phi.booleanCheck(null, g, s);		
		SpatialQuantitativeSignal q = phi.quantitativeCheck(null,g, s);
		// //////// Boolean and Quantitative Satisfaction /////////////
		double[] boolSat = b.boolSat();
		double[] quantSat = q.quantSat();

		// / Generation of the output data.txt
		 TxtSpatialQuantSat outDataQuant = new TxtSpatialQuantSat();
		 outDataQuant.write(quantSat, "data/dataSatQuant.txt");
		 TxtSpatialBoolSat outDataBool = new TxtSpatialBoolSat();
		 outDataBool.write(boolSat, "data/dataSatBool.txt");
		 
		 // to see the result, the matlab file produces also a solution.fig, saved in data.
		 proxy.eval("PlotGenerator");
		 
		
//		// Disconnect Matlab
		proxy.disconnect();	
	}
}
