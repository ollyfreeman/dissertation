package utility;

public class MapCreationParameters {
	
	//NEED TO CHANGE THIS LATER ON TO ALLOW FOR DIFFERENT WAYS OF MAKING MAPS, AND THEREFORE DIFFERENT PARAMETER SETS
	
	private int width;
	private int height;
	private int resolution;
	
	private int coverage;
	private int clustering;
	
	public MapCreationParameters(int width, int height, int resolution, int coverage, int clustering) {
		super();
		this.width = width;
		this.height = height;
		this.resolution = resolution;
		this.coverage = coverage;
		this.clustering = clustering;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getResolution() {
		return resolution;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	public int getCoverage() {
		return coverage;
	}

	public void setCoverage(int coverage) {
		this.coverage = coverage;
	}

	public int getClustering() {
		return clustering;
	}

	public void setClustering(int clustering) {
		this.clustering = clustering;
	}

}
