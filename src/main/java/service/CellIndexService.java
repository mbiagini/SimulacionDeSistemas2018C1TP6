package service;

import java.util.List;
import java.util.Map;

import persistence.cellIndex.Grid;
import persistence.cellIndex.Particle;

public interface CellIndexService {
	
	/**
	 * Creates a grid containing a matrix of cells. The dimension of the matrix
	 * is size x size and each cell has dimensions side/size x side/size.
	 * In addition, the cells have their list of neighbors included.
	 */
	public Grid getAllocatedGrid(List<Particle> particles, int size, double side,
			double xStart, double yStart, boolean isPeriodic);
	
	/**
	 * Calculates the list of neighbor particles for each particle in the given
	 * list, this is, those that are at a distance less than rc, using the
	 * cellIndexMethod (searches only in the neighbor cells).
	 */
	public Map<Particle, List<Particle>> cellIndexMethod(Grid grid, 
			List<Particle> particles, double rc);
	
	/**
	 * Calculates the list of neighbor particles for each particle in the given
	 * list, this is, those that are at a distance less than rc, using the
	 * brute force method (checks each particle against each other).
	 */
	public Map<Particle, List<Particle>> bruteForce(List<Particle> particles, 
			double side, boolean isPeriodic, double rc);
	
	/**
	 * Calculates the distance between the border of the two particles.
	 */
	public double getDistance(Particle p1, Particle p2);
	
	/**
	 * Calculates the distance between the border of the two particles,
	 * but taking in consideration that they are in a square surface with the
	 * given side, and that periodic conditions are permitted.
	 */
	public double getPeriodicDistance(Particle p1, Particle p2, double side);
	
	/**
	 * Computes the list of cells that each cell of the grid has as 
	 * neighbors.
	 */
	public void computeNeighborCells(Grid grid);
	
	/**
	 * Checks whether a particle is overlapping any other from a given list.
	 */
	public boolean isOverlappingAny(Particle particle, List<Particle> list);
	
	/**
	 * Checks whether two particles are overlapping.
	 */
	public boolean areOverlapping(Particle p1, Particle p2);
	
	/**
	 * Computes the distance between two particles from their centers.
	 */
	public double getCenterDistance(Particle p1, Particle p2);

}
