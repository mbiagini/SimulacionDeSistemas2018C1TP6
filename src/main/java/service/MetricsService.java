package service;

import java.util.List;

import persistence.cellIndex.Particle;

public interface MetricsService {

	public <E extends Number> double getMean(List<E> values);

	public <E extends Number> double getVariance(List<E> values);

	public <E extends Number> double getStandardDeviation(List<E> values);
	
	public double getKyneticEnergy(List<Particle> particles);
	
	public double getPotentialEnergy(List<Particle> particles);

}
