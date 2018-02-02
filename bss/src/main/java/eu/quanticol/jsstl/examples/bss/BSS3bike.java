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
import org.jsstl.xtext.formulas.*;
import simhya.dataprocessing.Trajectory;


public class BSS3bike {
	
	

	public static void main(String[] args) throws Exception {
		long tStart = System.currentTimeMillis();	
////		/// %%%%%%%  GRAPH    %%%%%   /////////
		TraGraphModelReader graphread = new TraGraphModelReader();
		GraphModel graph = graphread.read("models/stationsGraph3station.tra");
		graph.dMcomputation();
		int locations = graph.getNumberOfLocations();		
////		/// %%%%%%%  PROPERTY %%%%%   //////////
//		ScriptLoader loader = new ScriptLoader();
//		jSSTLScript script = loader.load("models/BSS.sstl");
		jSSTLScript script = new BSSScript();
//	/// %%%%%%%  DATA import %%%%%%%%%%%%/////////
		SimHyAWrapper model = new SimHyAWrapper();
		model.loadModel("models/bike3.txt");
		model.setGB();
		int var = 2;
		int tinit = 0;
		int tf = 45;
		int steps = 100;
		int vartosave  = 3*2;
		int runs = 1;
		HashMap<String,Double> parameters = new HashMap<String,Double>();
		int runparam = 3;

		///////    Data formulas
		ArrayList<SignalStatistics>  statphi1 = new ArrayList<>();
		ArrayList<SignalStatistics>  statphi2 = new ArrayList<>();
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
		for (int i=0; i<runparam;i++){
			SignalStatistics statistic1 = new SignalStatistics(locations);	
			SignalStatistics statistic2 = new SignalStatistics(locations);	
			statphi1.add(i, statistic1);
			statphi2.add(i, statistic2);
		}
	
		for (int i=0; i<runs; i++){
			System.out.println("i :" + i);	
			Trajectory traj = model.simulate(vartosave, tinit, tf, steps);
			String texttraj = "";
			String texttrajB = "";
			String texttrajS = "";
			double [] times = model.timesTraj(traj);			
			double [][][] data = model.dataTraj(traj, locations, times.length, var);	
			for (int t = 0; t < times.length; t++){
				texttraj += String.format(Locale.US, " %20.10f", traj.getAllData()[4][t]);
				texttrajB += String.format(Locale.US, " %20.10f", data[0][t][0]);
				texttrajS += String.format(Locale.US, " %20.10f", data[0][t][1]);
			}
			System.out.println("Traj "+texttraj);
			System.out.println("B "+texttrajB);
			System.out.println("S "+texttrajS);

			Signal s = new Signal(graph , times , data );
			double d = 0;
			double t = 0;
			for (int k=0; k< runparam; k++){	
				//System.out.println("k :" + k);
				parameters.put("d", d);
				parameters.put("t", t);
				//System.out.println(Arrays.toString(traj.getAllData()));
				// /////// %%%%%%%% CHECK %%%%%%% ////////////////////////	
				statphi1.get(k).add(script.quantitativeSat("phiB", parameters, graph, s));
				statphi2.get(k).add(script.quantitativeSat("phiS", parameters, graph, s));
				outputmatrix3 [k][i] = script.booleanSat("phiTest", parameters, graph, s)[0];
				System.out.println(" somewhere 0: " + outputmatrix3 [k][i]);
				outputmatrix4 [k][i] = script.quantitativeSat("phi2", parameters, graph, s)[0];
				textphi3 += String.format(Locale.US, " %20.10f", outputmatrix3[k][i]);
				textphi4 += String.format(Locale.US, " %20.10f", outputmatrix4[k][i]);
				d = d + 0.1;
				t = t + 1;
			}
			textphi3 += "\n";
			textphi4 += "\n";
		}

		
		for (int k=0; k< runparam; k++){
			for(int l=0; l<locations;l++){
	//			double [] min1 = statphi1.get(k).getMin();
	//			double [] max1 = statphi1.get(k).getMax();
				mean1[k][l]=statphi1.get(k).getAverage()[l];
				mean2[k][l]=statphi2.get(k).getAverage()[l];
				textphi1mean += String.format(Locale.US, " %20.10f", mean1[k][l]);
				textphi2mean += String.format(Locale.US, " %20.10f", mean2[k][l]);
	//			double [] min2 = statphi2.get(k).getMin();
	//			double [] max2 = statphi2.get(k).getMax();
				stdev1[k][l] = statphi1.get(k).getStandardDeviation()[l];
				stdev2[k][l] = statphi2.get(k).getStandardDeviation()[l];
				textphi1stdev += String.format(Locale.US, " %20.10f", stdev1[k][l]);
				textphi2stdev += String.format(Locale.US, " %20.10f", stdev2[k][l]);
			}
			textphi1mean += "\n";
			textphi2mean += "\n";
			textphi1stdev += "\n";
			textphi2stdev += "\n";
		}
		PrintWriter printerphi1mean = new PrintWriter("data/phi1Bool1.txt");
		printerphi1mean.print(textphi1mean);
		printerphi1mean.close();
		PrintWriter printerphi2mean = new PrintWriter("data/phi2bool1.txt");
		printerphi2mean.print(textphi2mean);
		printerphi2mean.close();
		PrintWriter printerphi1stdev = new PrintWriter("data/phi1stdbool1.txt");
		printerphi1stdev.print(textphi1stdev);
		printerphi1stdev.close();
		PrintWriter printerphi2stdev = new PrintWriter("data/phi2stdbool1.txt");
		printerphi2stdev.print(textphi2stdev);
		printerphi2stdev.close();
//		PrintWriter printerphi3 = new PrintWriter("data/phi3Bool1.txt");
//		printerphi3.print(textphi3);
//		printerphi3.close();
//		PrintWriter printerphi4 = new PrintWriter("data/phi4bool1.txt");
//		printerphi4.print(textphi4);
//		printerphi4.close();

		long tEnd = System.currentTimeMillis();
		double elapsedSeconds = (tEnd - tStart) / 1000.0; 
		System.out.println(elapsedSeconds);
		/////// %%%%%%%  OUTPUT    %%%%%   /////////
//System.out.println(Arrays.toString(boolSat));
//System.out.println(Arrays.toString(quantSat));
//	TxtSpatialQuantSat outDataQuant = new TxtSpatialQuantSat();
//	outDataQuant.write(quantSat, "data/dataSatQuant.txt");
//	TxtSpatialBoolSat outDataBool = new TxtSpatialBoolSat();
//	outDataBool.write(boolSat, "data/dataSatBool.txt");

	}
	 	
	
}


