package eu.quatincol.jsstl.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsstl.core.formula.AtomicFormula;
import org.jsstl.core.formula.EventuallyFormula;
import org.jsstl.core.formula.EverywhereFormula;
import org.jsstl.core.formula.Formula;
import org.jsstl.core.formula.GloballyFormula;
import org.jsstl.core.formula.NotFormula;
import org.jsstl.core.formula.ParametricExpression;
import org.jsstl.core.formula.ParametricInterval;
import org.jsstl.core.formula.Signal;
import org.jsstl.core.formula.SignalExpression;
import org.jsstl.core.formula.SomewhereFormula;
import org.jsstl.core.formula.SurroundFormula;
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

public class ReactionDiffusionSystem_Laura {

	public static void main(String[] args) throws MatlabConnectionException,
	MatlabInvocationException, IOException {
		
		
		// %%%%%%%%%%  GRAPH  %%%%%%%%% //
		
		// Designing the grid
		GraphModel graph = GraphModel.createGrid(32, 32, 1.0);
		// Computing of the distance matrix
		graph.dMcomputation();
		
		// %%%%%%%%% PROPERTY %%%%%%% //
		
		// loading the formulas files
		jSSTLScript script = new jSSTLPatternScript();

		
		// Loading the variables. That we have defined in the formulas files.
		String [] var = script.getVariables();
		
		// %%%%%%%%%%%%% Connection with Matlab %%%%%%%%%%%%/////////
		
		
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
			proxy.eval("clear all;");
			proxy.eval("TuringDataGenerator");
			//double a = proxy.returningEval("A(1,1)",);
			/// Loading the  trajectory files.
			FolderSignalReader readSignal = new FolderSignalReader(graph, var);
			File folder = new  File("data/allDataPattern");
			Signal signal = readSignal.read(folder);
			double[] satB = script.booleanSat("spotformation", new HashMap<String,Double>(), graph, signal);
			double[] satQ = script.quantitativeSat("spotformation", new HashMap<String,Double>(), graph, signal);
			
			/// Monitoring
			//int nprop = 1;
//			String [] formulas ={"spot"};
//			//for ( int i=0 ; i< nprop; i++) {
//			Formula phi = script.getFormula(formulas[0]); 
//			SpatialBooleanSignal boolSign = phi.booleanCheck(null, graph, signal);
//			BooleanSignal b = boolSign.spatialBoleanSignal.get(graph.getLocation(0));
//			//double satQ = qt.getValueAt(0);
//			//outputmatrix[i][j]= satQ;
//			System.out.println("all bool value: " + b.toString());
			//}
				
			
			System.out.println("Satisfaction value: " + Arrays.toString(satB));
			System.out.println("Robustness value: " + Arrays.toString(satQ));
		
		
		// Disconnect Matlab
		proxy.disconnect();
		
	}
}
