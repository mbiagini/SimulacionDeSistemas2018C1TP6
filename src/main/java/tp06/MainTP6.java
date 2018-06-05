package tp06;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import persistence.data.Force;
import persistence.granular.GranularParticle;
import persistence.granular.Parameters;
import persistence.granular.Surface;
import service.GranularService;
import service.TestService;
import service.impl.FileServiceImpl;
import service.impl.GranularServiceImpl;
import service.impl.TestServiceImpl;
import simulation.Simulation;
import persistence.cellIndex.Particle;

public class MainTP6 {
	
	// surface
	private static double L = 20.0;
	private static double W = 20.0;
	private static double D = 0.5;
	private static double T = 0.06;
	
	// particles
	private static double MIN_R = 0.5;
	private static double MAX_R = 0.58;
	private static double M 	= 0.1;
	private static double V 	= 0.0;
	
	// simulation
	private static double RC 	= 1.0;
	private static double DT 	= 0.0001;
	private static double KN 	= 1.2*Math.pow(10, 5);
	private static double GAMMA = 20;
	private static int 	  SIZE 	= 10;
	private static int 	  C 	= 50;
	
	// forces
	private static double DS    	 = 1.0;
	private static double TAU   	 = 0.5;
	private static double RUNNER_DS  = 10*DS;
	private static double RUNNER_TAU = TAU;
	private static double SOCIAL_A   = 2000.0;
	private static double SOCIAL_B   = 0.08;
	
	private static GranularService granularService = new GranularServiceImpl();
	private static TestService 	   testService 	   = new TestServiceImpl();
	private static FileServiceImpl fileServiceImpl = new FileServiceImpl();
	
	public static void main( String[] args ) throws IOException {
		
		Parameters params = new Parameters(
				L, W, D, T, MIN_R, MAX_R, M, V, RC, DT, KN, GAMMA, SIZE, C,
				DS, TAU, RUNNER_DS, RUNNER_TAU, SOCIAL_A, SOCIAL_B);
		System.out.println(params.toString());
				
		//simulateSile(params);
		simulateBox(params);
		//fileServiceImpl.changeJsonToOvitoForBox();
		//fileServiceImpl.changeJsonToOvitoForSile();
	}
	
	private static void simulateSile(Parameters params) throws IOException {
		Surface surface = Simulation.initializeOpenSurface(params);
		
//		granularService.createRandomParticles(params, "src/main/resources/data/particles.json");
		//testService.generateParticles(params, "src/main/resources/data/particles.json");
		
		Simulation.simulate(params, surface);
	}
	
	private static void simulateBox(Parameters params) throws IOException {
		Surface surface = Simulation.initializeClosedSurface(params);
		
		granularService.createRandomParticles(params, "src/main/resources/data/particles.json");
		//testService.generateParticles(params, "src/main/resources/data/particles.json");
		
		File file = new File("src/main/resources/data/system_out.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		
		Simulation.simulate(params, surface);
	}

}
