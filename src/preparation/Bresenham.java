package preparation;

public class Bresenham {
	
	private static String[][]map;
	private static Coordinate from;
	private static Coordinate to;

	private static int x0;
	private static int x1;
	private static int y0;
	private static int y1;
	private static int x;
	private static int y;
	
	public static void drawLine(Coordinate fromArg, Coordinate toArg) {
		from = fromArg;
		to = toArg;

		double m = ((double)(to.getY()-from.getY()))/((double)(to.getX()-from.getX()));
		
		if(m <= 1 && m >= -1) { //octant 1,8,4 or 5
			//if octant 4 or 5, swap from and to
			if(from.getX() > to.getX()) {
				Coordinate temp = from; from = to; to = temp; //tried to do with separate method but the temp screwed it up, dunno if something to do with static vars
			}
			initialiseVariables();
			map[x][y] = "X ";
			
			double yActual = y0;
			while(x < x1) {
				x++;
				yActual += m;
				y = (int) Math.round(yActual);
				map[x][y] = "X ";
			} 
		} else {			//octant 2,3,6 or 7
			//if octant 6 or 7, swap from and to
			if(from.getY() > to.getY()) {
				Coordinate temp = from; from = to; to = temp;
			}
			initialiseVariables();
			map[x][y] = "X ";
			double xActual = x0;
			m = 1/m;
			while(y < y1) {
				y++;
				xActual += m;
				x = (int) Math.round(xActual);
				map[x][y] = "X ";
			}
		}
	}
	
	private static void initialiseVariables() {
		x0 = from.getX();
		x1 = to.getX();
		y0 = from.getY();
		y1 = to.getY();
		x = x0;
		y = y0;
	}
	
	public static void printMap() {
		for(int y=0;y < map[0].length; y++) {
			for(int x=0; x < map.length; x++) {
				System.out.print(map[x][y]);
			}
			System.out.print("\n");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		map = new String[30][30];
		for (int i=0; i<30; i++) {
			for(int j=0; j<30; j++) {
				map[i][j]= "- ";
			}
		}
		drawLine(new Coordinate(29,29), new Coordinate(0,10));
		printMap();
	}

}
