package preparation;

public class MapGenerationAlgorithm {
	
	private static final int size = 30;
	private static final int coverage = 200;
	private static final int clusterWeight = 00;
	private static int potentialCount = size*size;
	private static int[] map = new int[size*size];
	
	public static void main (String[] args) {

		for(int i=0;i<(size*size); i++) {
				map[i]=1;
		}
		printMap();
		
		int j=0;
		int rawLocation;
		
		while (j<coverage) {
			System.out.println();
			System.out.println("PotCount "+ potentialCount);
			rawLocation = (int) Math.floor(Math.random()*potentialCount);
			System.out.println("RawLoc "+ rawLocation);
			int location = 0; int k=map[location];
			while(k<rawLocation) {
				k+=map[++location];
			}
			if(map[location] !=0) {
				potentialCount -= map[location]; //the amount you took off to zero that location (needs to be removed from potential count
				map[location] = 0;
				
				if(location<size) {
					if(location%size == 0) {
						addIfNotZero(location+1);
						addIfNotZero(location+size);
						addIfNotZero(location+size+1);
						System.out.println("Top left");
					} else if (location%size == size-1) {
						addIfNotZero(location-1);
						addIfNotZero(location+size-1);
						addIfNotZero(location+size);
						System.out.println("Top right");
					} else {
						addIfNotZero(location-1);
						addIfNotZero(location+size-1);
						addIfNotZero(location+size);
						addIfNotZero(location+size+1);
						addIfNotZero(location+1);
						System.out.println("Top middle");
					}	
				} else if (location+size >= (size*size)) {
					if(location%size == 0) {
						addIfNotZero(location-size);
						addIfNotZero(location-size+1);
						addIfNotZero(location+1);
						System.out.println("Bottom left");
					} else if (location%size == size-1) {
						addIfNotZero(location-1);
						addIfNotZero(location-size-1);
						addIfNotZero(location-size);
						System.out.println("Bottom right");
					} else {
						addIfNotZero(location-1);
						addIfNotZero(location-size-1);
						addIfNotZero(location-size);
						addIfNotZero(location-size+1);
						addIfNotZero(location+1);
						System.out.println("Bottom middle");
					}	
				} else if(location%size == 0) {
					addIfNotZero(location-size);
					addIfNotZero(location-size+1);
					addIfNotZero(location+1);
					addIfNotZero(location+size+1);
					addIfNotZero(location+size);
					System.out.println("Mid left");
				} else if (location%size == size-1) {
					addIfNotZero(location-size);
					addIfNotZero(location-size-1);
					addIfNotZero(location-1);
					addIfNotZero(location-size-1);
					addIfNotZero(location+size);
					System.out.println("Mid right");
				} else {
					addIfNotZero(location-size-1);
					addIfNotZero(location-size);
					addIfNotZero(location-size+1);
					addIfNotZero(location-1);
					addIfNotZero(location+1);
					addIfNotZero(location+size-1);
					addIfNotZero(location+size);
					addIfNotZero(location+size+1);
					System.out.println("Other");
				}
				j++;
				System.out.println("inserted " + location);
			} else {
				System.out.println("skipped " + location);
			}
		}
		printMap();
	}
	
	public static void addIfNotZero(int location) {
		if(map[location] !=0) {
			map[location] += clusterWeight;
			potentialCount += clusterWeight;
		}
	}
	
	public static void printMap() {
		for(int i=0;i<size; i++) {
			for(int j=0; j<size; j++) {
				if(map[(i*size)+j] == 0) {
					System.out.print("X ");
				} else {
					System.out.print("- ");
				}
				//System.out.print(map[(i*size)+j] + " ");
			}
			System.out.print("\n");
		}
	}

}
