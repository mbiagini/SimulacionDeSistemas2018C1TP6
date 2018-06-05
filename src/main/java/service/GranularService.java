package service;

import java.util.List;
import java.util.Random;

import persistence.data.Force;
import persistence.granular.GranularParticle;
import persistence.granular.Parameters;
import persistence.granular.Surface;
import persistence.cellIndex.Particle;

public interface GranularService {
	
	public void createRandomParticles(Parameters params, String fileName);
		
	public void updateParticlePeriodic(Particle p, List<Particle> list,
			Parameters params, Random rnd);
	
	public void updateAcceleration(GranularParticle gp, List<Particle> neighbors,
			Surface surface, Parameters params);
	
	public Force getForceAgainstParticle(Particle p1, Particle p2, double kn, 
			double gamma);
	
	public Force getForceAgainstSurface(Particle p, Surface s, double kn,
			double gamma);
	
	public double getSpawnHeight(List<Particle> particles, Parameters params);

}
