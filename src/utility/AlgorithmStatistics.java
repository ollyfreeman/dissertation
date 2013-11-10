package utility;

import engine.AlgorithmData;

public class AlgorithmStatistics {
	
	private final double distance;
	private final double angle;
	private final double time;
	
	public AlgorithmStatistics(AlgorithmData algorithmData) {
		this.distance = algorithmData.getDistance();
		this.angle = algorithmData.getAngle();
		this.time = algorithmData.getTime();
	}

	public double getDistance() {
		return distance;
	}

	public double getAngle() {
		return angle;
	}

	public double getTime() {
		return time;
	}
	
	

}
