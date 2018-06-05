package persistence.granular;

public class Parameters {
	
	// surface
	private double height;
	private double width;
	private double openWidth;
	private double thickness;
	
	// particles
	private double minRadius;
	private double maxRadius;
	private double mass;
	private double speed;
	
	// simulation
	private double rc;
	private double dt;
	private double kn;
	private double gamma;
	private int size;
	private int count;
	
	// forces
	private double ds;
	private double tau;
	private double runnerDs;
	private double runnerTau;
	private double socialA;
	private double socialB;
	
	public Parameters(double height, double width, double openWidth, 
			double thickness, double minRadius, double maxRadius, double mass, 
			double speed, double rc, double dt, double kn, double gamma,
			int size, int count, double ds, double tau, double runnerDs, 
			double runnerTau, double socialA, double socialB) {
		
		this.height    = height;
		this.width 	   = width;
		this.openWidth = openWidth;
		this.thickness = thickness;
		this.minRadius = minRadius;
		this.maxRadius = maxRadius;
		this.mass 	   = mass;
		this.speed 	   = speed;
		this.rc 	   = rc;
		this.dt 	   = dt;
		this.kn 	   = kn;
		this.gamma 	   = gamma;
		this.size 	   = size;
		this.count 	   = count;
		this.ds 	   = ds;
		this.tau 	   = tau;
		this.runnerDs  = runnerDs;
		this.runnerTau = runnerTau;
		this.socialA   = socialA;
		this.socialB   = socialB;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getOpenWidth() {
		return openWidth;
	}

	public void setOpenWidth(double openWidth) {
		this.openWidth = openWidth;
	}

	public double getThickness() {
		return thickness;
	}

	public void setThickness(double thickness) {
		this.thickness = thickness;
	}

	public double getMinRadius() {
		return minRadius;
	}

	public void setMinRadius(double minRadius) {
		this.minRadius = minRadius;
	}

	public double getMaxRadius() {
		return maxRadius;
	}

	public void setMaxRadius(double maxRadius) {
		this.maxRadius = maxRadius;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getRc() {
		return rc;
	}

	public void setRc(double rc) {
		this.rc = rc;
	}

	public double getDt() {
		return dt;
	}

	public void setDt(double dt) {
		this.dt = dt;
	}

	public double getKn() {
		return kn;
	}

	public void setKn(double kn) {
		this.kn = kn;
	}

	public double getGamma() {
		return gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getDs() {
		return ds;
	}

	public void setDs(double ds) {
		this.ds = ds;
	}

	public double getTau() {
		return tau;
	}

	public void setTau(double tau) {
		this.tau = tau;
	}

	public double getRunnerDs() {
		return runnerDs;
	}

	public void setRunnerDs(double runnerDs) {
		this.runnerDs = runnerDs;
	}

	public double getRunnerTau() {
		return runnerTau;
	}

	public void setRunnerTau(double runnerTau) {
		this.runnerTau = runnerTau;
	}

	public double getSocialA() {
		return socialA;
	}

	public void setSocialA(double socialA) {
		this.socialA = socialA;
	}

	public double getSocialB() {
		return socialB;
	}

	public void setSocialB(double socialB) {
		this.socialB = socialB;
	}

	@Override
	public String toString() {
		return "Parameters [height=" + height + ", width=" + width + ", openWidth=" + openWidth + ", thickness="
				+ thickness + ", minRadius=" + minRadius + ", maxRadius=" + maxRadius + ", mass=" + mass + ", speed="
				+ speed + ", rc=" + rc + ", dt=" + dt + ", kn=" + kn + ", gamma=" + gamma + ", size=" + size
				+ ", count=" + count + ", ds=" + ds + ", tau=" + tau + ", runnerDs=" + runnerDs + ", runnerTau="
				+ runnerTau + ", socialA=" + socialA + ", socialB=" + socialB + "]";
	}

}
