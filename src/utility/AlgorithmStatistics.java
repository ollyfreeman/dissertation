package utility;

import java.util.List;

import engine.AlgorithmData;

/*
 * object to wrap up algorithm statistics when passing between the front end and back end
 */
public class AlgorithmStatistics {
	
	private final double distance;
	private final double angle;
	private final double time;
	private final List<Coordinate> path;
	
	public AlgorithmStatistics(AlgorithmData algorithmData) {
		this.distance = algorithmData.getDistance();
		this.angle = algorithmData.getAngle();
		this.time = algorithmData.getAlgorithmTime();
		this.path = algorithmData.getPath();
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
	
	public List<Coordinate> getPath() {
		return path;
	}
	
	

}
