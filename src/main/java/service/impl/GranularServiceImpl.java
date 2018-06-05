package service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;

import service.FileService;
import service.impl.FileServiceImpl;
import persistence.data.Force;
import persistence.granular.GranularParticle;
import persistence.granular.Parameters;
import persistence.granular.Surface;
import service.GranularService;
import service.CellIndexService;
import service.impl.CellIndexServiceImpl;
import persistence.cellIndex.Particle;

public class GranularServiceImpl implements GranularService {

	private CellIndexService cellIndexService = new CellIndexServiceImpl();
	private FileService 	 fileServiceTP4   = new FileServiceImpl();
	
	@Override
	public void createRandomParticles(Parameters params, String filename) {
		
		double h = params.getHeight() - 2*params.getMaxRadius();
		double w = params.getWidth();
		double minR = params.getMinRadius();
		double maxR = params.getMaxRadius();
		
		Random rnd = new Random();
		List<Particle> gps = new ArrayList<>();
		
		for(int i = 0; i < params.getCount() - 1; i++) {
			double r = rnd.nextDouble()*(maxR - minR) + minR;
			GranularParticle gp = new GranularParticle.Builder()
					.id(i + 1).m(params.getMass()).r(r).v(params.getSpeed()).ang(0.0).acc(0.0)
					.build();
			do {
				gp.setX(rnd.nextDouble()*(w - 2*r) + r);
				gp.setY(rnd.nextDouble()*(h - 2*r) + r);
			}
			while(cellIndexService.isOverlappingAny(gp, gps));
			
			gps.add(gp);
			System.out.println(gp.toString());
		}
		GranularParticle runner = new GranularParticle.Builder()
				.id(params.getCount())
				.x(params.getWidth()/2)
				.y(params.getHeight() - params.getMaxRadius())
				.r(params.getMinRadius())
				.m(params.getMass())
				.v(0.0)
				.ang(0)
				.acc(0.0)
				.build();
		gps.add(runner);
		System.out.println("runner: " + runner.toString());
		Gson gson = new Gson();
		String json = gson.toJson(gps);
		
		File file = new File(filename);
		fileServiceTP4.writeStringToFile(file, json);
	}
	
	@Override
	public void updateParticlePeriodic(Particle p, List<Particle> list,
			Parameters params, Random rnd) {
		
		double h = params.getHeight();
		double w = params.getWidth();
		double maxr = params.getMaxRadius();
		
		if(p.getY() < (- h/10)) {	
			GranularParticle gp = (GranularParticle) p;
							
			do {
				p.setX(maxr + rnd.nextDouble()*(w - 2*maxr));
				p.setY(getSpawnHeight(list, params));
			}
			while(cellIndexService.isOverlappingAny(p, list));
			
			gp.setAx(0.0);
			gp.setAy(0.0);
			gp.setVx(0.0);
			gp.setVy(0.0);
			
		}
		
	}

	@Override
	public void updateAcceleration(GranularParticle gp, List<Particle> neighbors, 
			Surface surface, Parameters params) {
		double fx = 0.0;
		double fy = 0.0;
		
		if(gp.getY() < 0) {
			gp.setAx(0.0);
			gp.setAy(-9.80665);
			return;
		}
		
		// forces against other particles
		if(neighbors != null) {
			for(Particle n : neighbors) {
				Force f = getForceAgainstParticle(gp, n, params.getKn(), params.getGamma());
				fx += f.getFx();
				fy += f.getFy();
			}
		}
		
		// forces against the walls
		Force f = getForceAgainstSurface(gp, surface, params.getKn(), params.getGamma());
		fx += f.getFx();
		fy += f.getFy();
		
		// driving force
		Force df;
		if(gp.getId() == 40) {
			df = getDrivingForce(gp, surface, params.getRunnerDs(), params.getRunnerTau());
		}
		else {
			df = getDrivingForce(gp, surface, params.getDs(), params.getTau());
		}
		fx += df.getFx();
		fy += df.getFy();
		
		// social force
		Force sf = getSocialForce(gp, neighbors, params);
		fx += sf.getFx();
		fy += sf.getFy();
				
		gp.setAx(fx / gp.getM());
		gp.setAy(fy / gp.getM());
		
	}
	
	public Force getSocialForce(GranularParticle gp, List<Particle> neighbors, 
			Parameters params) {
		double fx = 0.0;
		double fy = 0.0;
		
		for(Particle n : neighbors) {
			double dx = gp.getX() - n.getX();
			double dy = gp.getY() - n.getY();
			double r  = Math.hypot(dx, dy);
			
			double ex = dx/r;
			double ey = dy/r;
			
			double d = cellIndexService.getDistance(n, gp);
			
			fx += params.getSocialA()*Math.exp(- d/params.getSocialB())*ex;
			fy += params.getSocialA()*Math.exp(- d/params.getSocialB())*ey;
		}
		
		return new Force(fx, fy);
	}
	
	public Force getDrivingForce(GranularParticle gp, Surface surface,
			double drivingSpeed, double tau) {
		
		double fixedPointx = surface.getWidth() / 2;
		double fixedPointy = 0.0;
		
		double dx = fixedPointx - gp.getX();
		double dy = fixedPointy - gp.getY();
		double r  = Math.hypot(dx, dy);
		double ex = dx/r;
		double ey = dy/r;
		
		double m  = gp.getM();
		double vx = gp.getVx();
		double vy = gp.getVy();
		
		double fx = (m*(drivingSpeed*ex - vx))/tau;
		double fy = (m*(drivingSpeed*ey - vy))/tau;
				
		return new Force(fx, fy);
	}
	
	@Override
	public Force getForceAgainstParticle(Particle p1, Particle p2, double kn, 
			double gamma) {
		double fx = 0.0;
		double fy = 0.0;
		
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		double r  = Math.hypot(dx, dy);
		
		double ex = dx/r;
		double ey = dy/r;
		
		// overlapping
		if(r < p1.getR() + p2.getR()) {
			double overlap = p1.getR() + p2.getR() - r;
			
			GranularParticle gp1 = (GranularParticle) p1;
			GranularParticle gp2 = (GranularParticle) p2;
			
//			if(Simulation.DEBUG && (gp1.getId() == 26 || gp2.getId() == 26)) {
//				System.out.println("Distance: " + cellIndexService.getDistance(gp1, gp2));
//				System.out.println("Force: ");
//				System.out.println(gp1.toString());
//				System.out.println(gp2.toString());
//			}
			
			double normalSpeed1 = gp1.getVx()*ex + gp1.getVy()*ey;
			double normalSpeed2 = gp2.getVx()*ex + gp2.getVy()*ey;
			
			double relativeSpeed = normalSpeed1 - normalSpeed2;
			//System.out.println("relative speed: " + relativeSpeed);
			
			double f = - kn*overlap - gamma*relativeSpeed;
			fx = f*ex;
			fy = f*ey;
		}
		
		return new Force(fx, fy);
	}
	
	@Override
	public Force getForceAgainstSurface(Particle p, Surface s, double kn,
			double gamma) {
		double fx = 0.0;
		double fy = 0.0;
		
		GranularParticle gp = (GranularParticle) p;
		
		double x  = p.getX();
		double y  = p.getY();
		double r  = p.getR();
		
		double relativeSpeedx = gp.getVx();
		double relativeSpeedy = gp.getVy();
			
		double h  = s.getHeight();
		double w  = s.getWidth();
		double os = s.getOpenstart();
		double oe = s.getOpenend();
		double th = s.getThickness();
		
		if(x < 0 || x > w) {
			throw new RuntimeException("One particle is out of bounds\n" + p.toString());
		}
		
		if(x - r < 0) {
			double overlap = - (x - r);
			double f = - kn*overlap + gamma*relativeSpeedx;
			fx = f*(-1);
		}
		if(x + r > w) {
			double overlap = (x + r) - w;
			double f = - kn*overlap - gamma*relativeSpeedx;
			fx = f*(1);
		}
		if(y + r > h) {
			double overlap = (x + r) - h;
			double f = - kn*overlap + gamma*relativeSpeedx;
			fy = f*(1);
		}
		if(x <= os - th/2 || x >= oe + th/2) {
			if(y - r < 0) {
				double overlap = - (y - r);
//				System.out.println("overlap y: ");
//				System.out.println(gp.toString());
//				System.out.println("relativeSpeedy: " + relativeSpeedy);
				double f = - kn*overlap + gamma*relativeSpeedy;
				fy = f*(-1);
			}
		}
		if(x > os - th/2 && x - r < os) {
			Particle vertex = new GranularParticle.Builder()
					.x(os - th/2).y(- th/2).r(th/2).build();
			Force force = getForceAgainstParticle(p, vertex, kn, gamma);
			fx = force.getFx();
			fy = force.getFy();
		}
		if(x < oe + th/2 && x + r > oe) {
			Particle vertex = new GranularParticle.Builder()
					.x(oe + th/2).y(- th/2).r(th/2).build();
			Force force = getForceAgainstParticle(p, vertex, kn, gamma);
			fx = force.getFx();
			fy = force.getFy();
		}
		
		return new Force(fx, fy);
	}
	
	public double getSpawnHeight(List<Particle> particles, Parameters params) {
		Double maxHeight = 0.0;
		for(Particle p : particles) {
			if(p.getY() > maxHeight) {
				maxHeight = p.getY();
			}
		}
		Double spawnHeight = maxHeight + 4*params.getMaxRadius();
		if(spawnHeight >= (params.getHeight() - params.getMaxRadius())) {
			spawnHeight = params.getHeight() - params.getMaxRadius();
		}
		
		return spawnHeight;
	}
	
}
