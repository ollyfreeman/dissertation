package data;

public class MapResolutions {
	
	private static final String[] mapResolutionArray = {"50x50", "100x100", "200x200"};// , "200x400", "400x400", "400x800", "800x800"};
	
	private static final String[] brushResolutionArray = {"64","32","16","8","4","2","1"};
	
	public static String[] getMapResolutionArray() {
		return mapResolutionArray;
	}
	
	public static String[] getBrushResolutionArray() {
		return brushResolutionArray;
	}
	
	public static int getResolutionFromMapHeight(int height) {
		switch (height) {
		//case 800: 	return 1;
		//case 400:	return 2;
		case 200:	return 4;
		case 100:	return 8;
		case 50:	return 16;
		case 25:	return 32;
		default:	return 0;
		}
	}

}
