package persistence.granular;

import persistence.cellIndex.Particle;

public class GranularParticle implements Particle {	
	
	private int id;
	private double x;
	private double y;
	private double r;
	private double m;
	private double vx;
	private double vy;
	private double ax;
	private double ay;
	
	public GranularParticle(Builder builder) {
		this.id = builder.id;
		this.x 	= builder.x;
		this.y 	= builder.y;
		this.r 	= builder.r;
		this.m 	= builder.m;
		this.vx = builder.v * Math.cos(builder.ang);
		this.vy = builder.v * Math.sin(builder.ang);
		this.ax = builder.acc * Math.cos(builder.ang);
		this.ay = builder.acc * Math.sin(builder.ang);
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public double getR() {
		return r;
	}

	@Override
	public void setR(double r) {
		this.r = r;
	}
	
	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}
	
	public double getV() {
		return Math.hypot(vx, vy);
	}

	public double getAng() {
		return Math.atan(vy / vx);
	}
	
	public double getE() {
		return (m * getV() * getV()) / 2;
	}
	
	public double getVx() {
		return vx;
	}
	
	public void setVx(double vx) {
		this.vx = vx;
	}
	
	public double getVy() {
		return vy;
	}
	
	public void setVy(double vy) {
		this.vy = vy;
	}

	public double getAx() {
		return ax;
	}

	public void setAx(double ax) {
		this.ax = ax;
	}

	public double getAy() {
		return ay;
	}

	public void setAy(double ay) {
		this.ay = ay;
	}

	@Override
	public String toString() {
		return "GranularParticle [id=" + id + ", x=" + x + ", y=" + y + ", r=" + r + ", m=" + m + ", vx=" + vx + ", vy=" + vy
				+ ", ax=" + ax + ", ay=" + ay + "]";
	}

	@Override
	public boolean equals(Object other){
	    if (other == null) 
	    	return false;
	    if (other == this) 
	    	return true;
	    if (!(other instanceof Particle))
	    	return false;
	    Particle otherParticle = (Particle) other;
	    return otherParticle.getId() == this.getId();
	}
	
	@Override
	public int hashCode() {
		return this.getId();
	}
	
	public static class Builder {
		
		private int id;
		private double m;
		private double x;
		private double y;
		private double r;
		private double v;
		private double ang;
		private double acc;
		
		public Builder() {}
		
		public GranularParticle build() {
			return new GranularParticle(this);
		}
		
		public Builder id(int id) {
			this.id = id;
			return this;
		}
		
		public Builder m(double m) {
			this.m = m;
			return this;
		}
		
		public Builder x(double x) {
			this.x = x;
			return this;
		}
		
		public Builder y(double y) {
			this.y = y;
			return this;
		}
		
		public Builder r(double r) {
			this.r = r;
			return this;
		}
		
		public Builder v(double v) {
			this.v = v;
			return this;
		}
		
		public Builder ang(double ang) {
			this.ang = ang;
			return this;
		}
		
		public Builder acc(double acc) {
			this.acc = acc;
			return this;
		}
		
	}

}