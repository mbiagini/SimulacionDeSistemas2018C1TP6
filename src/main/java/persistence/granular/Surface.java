package persistence.granular;

public class Surface {

	private double height;
	private double width;
	private double openstart;
	private double openend;
	private double thickness;
	
	public Surface(Builder builder) {
		this.height    = builder.height;
		this.width 	   = builder.width;
		this.openstart = builder.openstart;
		this.openend   = builder.openend;
		this.thickness = builder.thickness;
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
	
	public double getOpenstart() {
		return openstart;
	}

	public void setOpenstart(double openstart) {
		this.openstart = openstart;
	}

	public double getOpenend() {
		return openend;
	}

	public void setOpenend(double openend) {
		this.openend = openend;
	}

	public double getThickness() {
		return thickness;
	}

	public void setThickness(double thickness) {
		this.thickness = thickness;
	}

	@Override
	public String toString() {
		return "Surface [height=" + height + ", width=" + width + ", openstart=" + openstart + ", openend=" + openend
				+ ", thickness=" + thickness + "]";
	}

	public static class Builder {
		
		private double height;
		private double width;
		private double openstart;
		private double openend;
		private double thickness;
		
		public Builder() {}
		
		public Surface build() {
			return new Surface(this);
		}
		
		public Builder height(double height) {
			this.height = height;
			return this;
		}
		
		public Builder width(double width) {
			this.width = width;
			return this;
		}
		
		public Builder openstart(double openstart) {
			this.openstart = openstart;
			return this;
		}
		
		public Builder openend(double openend) {
			this.openend = openend;
			return this;
		}
		
		public Builder thickness(double thickness) {
			this.thickness = thickness;
			return this;
		}
		
	}
	
}
