package service.impl;

import java.util.List;

import persistence.granular.GranularParticle;
import service.MetricsService;
import persistence.cellIndex.Particle;

public class MetricsServiceImpl implements MetricsService {

	@Override
	public <E extends Number> double getMean(List<E> values) {
		double sum = 0;
		for (E value : values)
			sum += value.doubleValue();
		return sum / values.size();
	}
	
	@Override
	public <E extends Number> double getVariance(List<E> values) {
		double mean = getMean(values);
        double aux = 0;
        for (Number value : values)
        	aux += (value.doubleValue() - mean) * (value.doubleValue() - mean);
        return aux / (values.size() - 1);
    }

	@Override
	public <E extends Number> double getStandardDeviation(List<E> values) {
		return Math.sqrt(getVariance(values));
	}

	@Override
	public double getKyneticEnergy(List<Particle> particles) {
		double energy = 0.0;
		for (Particle p : particles) {
			GranularParticle gp = (GranularParticle) p;
			energy += (1.0/2)*gp.getM()*gp.getV()*gp.getV();
		}
		return energy;
	}
	
	@Override
	public double getPotentialEnergy(List<Particle> particles) {
		double energy = 0.0;
		for (Particle p : particles) {
			GranularParticle gp = (GranularParticle) p;
			energy += gp.getM()*(9.80665)*gp.getY();
		}
		return energy;
	}

}