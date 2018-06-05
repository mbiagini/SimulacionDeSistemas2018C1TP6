package simulation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import service.FileService;
import service.impl.FileServiceImpl;
import persistence.granular.GranularParticle;
import persistence.granular.Parameters;
import persistence.granular.Surface;
import persistence.data.GranularData;
import service.GranularService;
import service.MetricsService;
import service.impl.GranularServiceImpl;
import service.impl.MetricsServiceImpl;
import service.CellIndexService;
import service.impl.CellIndexServiceImpl;
import persistence.cellIndex.Grid;
import persistence.cellIndex.Particle;

public class Simulation {
	
	private static CellIndexService cellIndexService = new CellIndexServiceImpl();
	private static FileService 		fileServiceTP4 	 = new FileServiceImpl();
	private static GranularService  granularService  = new GranularServiceImpl();
	private static MetricsService   metricsService   = new MetricsServiceImpl();
	
	private static String filesPath = "src/main/resources/data/";
	private static String particlesFilename = filesPath + "particles.json";
	
	public static Boolean DEBUG = false;
	
	public static Surface initializeOpenSurface(Parameters params) {
		
		double l = params.getHeight();
		double w = params.getWidth();
		double d = params.getOpenWidth();
		double t = params.getThickness();
		
		return new Surface.Builder()
				.height(l).width(w).openstart(w/2.0 - d/2.0).openend(w/2.0 + d/2.0).thickness(t).build();
	}
	
	public static Surface initializeClosedSurface(Parameters params) {
		
		double l = params.getHeight();
		double w = params.getWidth();
		
		return new Surface.Builder()
				.height(l).width(w).openstart(-10.0).openend(-10.0).thickness(0.3).build();
	}
	
	public static void simulate(Parameters params, Surface surface) throws IOException {
				
		File particlesFile = new File(particlesFilename);
		String json = fileServiceTP4.readFileToString(particlesFile);
		Type listType = new TypeToken<ArrayList<GranularParticle>>(){}.getType();
		List<Particle> gps = fileServiceTP4.getFromValidatedJson(json, listType);
		
		// for Beeman
		Map<Particle, Double> axtminusdtMap = new HashMap<>();
		Map<Particle, Double> aytminusdtMap = new HashMap<>();
		Map<Particle, Double> axtMap		= new HashMap<>();
		Map<Particle, Double> aytMap		= new HashMap<>();
		for(Particle p : gps) {
			axtminusdtMap.put(p, new Double(0.0));
			aytminusdtMap.put(p, new Double(0.0));
		}
		
		// for analysis
		List<Double> kEs = new ArrayList<>();
		List<GranularData> dataList = new ArrayList<>();
		
		// for flow
		Map<Particle, Double> prevYMap = new HashMap<>();
		for(Particle p : gps) {
			prevYMap.put(p, p.getY());
		}
		int escapeCount = 0;
		
		Random rnd = new Random(0);
		
		// condition checker
		boolean stop = false;
		
		// simulate
		int i = 0;
		//for(i = 0; !stop ; i++) {
		for(i = 0; i<100000 ; i++) {
						
			// initialize data object
			GranularData data = new GranularData(i);
			
//			for(Particle p : gps) {
//				System.out.println(p.toString());
//			}
			
			if(i % 100 == 0) {
				
				// we save the state of the system
				//saveParticles(gps, i);
				
				double kE = metricsService.getKyneticEnergy(gps);
				
				// for the closed box
				if(surface.getOpenstart() < 0) {

					System.out.println("instant " + i + ": KE: " + kE);
					System.out.println("runner: " + gps.get(params.getCount() - 1).toString());

//					for(Particle p : gps) {
//						System.out.println(p.toString());
//					}
//					System.out.println("");

				}
				
			}
			
			// check for escaped particles and periodic condition
			for(Particle p : gps) {
				if(p.getY() < 0 && prevYMap.get(p) >= 0) {
					escapeCount++;
				}
				prevYMap.put(p, p.getY());
				
				// periodic condition
				granularService.updateParticlePeriodic(p, gps, params, rnd);
			}
				
//			check for elevated speeds
			for(Particle p : gps) {
				GranularParticle gp = (GranularParticle) p;
				if(Math.abs(gp.getVx()) > 10 || Math.abs(gp.getVy()) > 10) {
					System.out.println("ELEVATED SPEED FOUND");
					System.out.println("instant: " + i + ", " + gp.toString());
					stop = true;
				}
			}
			
			if(stop) {
				break;
			}
			
			// allocate particles in grid for cell index method
			Grid grid = cellIndexService.getAllocatedGrid(
					gps, 
					params.getSize(), 
					params.getHeight(), 
					0.0,
					0.0,
					false);
			
			Map<Particle, List<Particle>> neighborMap = cellIndexService
					.cellIndexMethod(grid, gps, params.getRc());
			
			// calculate all the predictions
			for(Particle p : gps) {
				GranularParticle gp = (GranularParticle) p;
				
				double dt = params.getDt();
				
				double xt  = gp.getX();
				double yt  = gp.getY();
				double vxt = gp.getVx();
				double vyt = gp.getVy();
				double axt = gp.getAx();
				double ayt = gp.getAy();
				
				double axtminusdt = axtminusdtMap.get(p);
				double aytminusdt = aytminusdtMap.get(p);
				
				axtMap.put(p, axt);
				aytMap.put(p, ayt);
				
				double xtplusdt = xt + vxt*dt + (2.0/3)*axt*dt*dt - (1.0/6)*axtminusdt*dt*dt;
				double ytplusdt = yt + vyt*dt + (2.0/3)*ayt*dt*dt - (1.0/6)*aytminusdt*dt*dt;
				
				double vxp = vxt + (3.0/2)*axt*dt - (1.0/2)*axtminusdt*dt;
				double vyp = vyt + (3.0/2)*ayt*dt - (1.0/2)*aytminusdt*dt;
				
				gp.setX(xtplusdt);
				gp.setY(ytplusdt);
				if(Math.abs(vxp) < 1) {
					gp.setVx(vxp);
				}
				if(Math.abs(vyp) < 1) {
					gp.setVy(vyp);
				}
			}
			
			// update all the accelerations
			for(Particle p : gps) {
				GranularParticle gp = (GranularParticle) p;
				granularService.updateAcceleration(gp, neighborMap.get(p), surface, params);
			}
			
			// fix predictions
			for(Particle p : gps) {
				
				double axt = axtMap.get(p);
				double ayt = aytMap.get(p);
				
				axtminusdtMap.put(p, axt);
				aytminusdtMap.put(p, ayt);
			}
			
		}
		
		System.out.println("SIMULATION STOPPED AT INSTANT " + i);
		
	}
	
	private static void saveParticles(List<Particle> particles, int instant) {
		Gson gson = new Gson();
		String json = gson.toJson(particles);
		String filename = String.format(
				"src/main/resources/data/simulations/count40_proportion17_5/particles%07d.json",
				instant);
		File file = new File(filename);
		fileServiceTP4.writeStringToFile(file, json);
	}
	
	public static void clearData(int min, int max) {
		for(int i = min; i <= max; i++) {
			File file = new File(String.format(
					"src/main/resources/data/simulations/particles%07d.json",
					i));
			file.delete();
		}
	}

}
