package engine.graph.BlockAStar.LDDB.geometric;

import engine.graph.BlockAStar.Block;
import engine.map.Map;
import utility.Coordinate;

public class Block_geometric extends Block{
	
	//private int rotation;
	private Coordinate[][] toRotated;		//OBTAIN BY CW ROTATION
	private Coordinate[][] fromRotated;		//OBTAIN BY CCW ROTATION
	
	public Block_geometric(Map map, int size, Coordinate topLeft, Coordinate goal) {
		super(map, size, topLeft, goal);
		toRotated = new Coordinate[size+1][size+1];
		fromRotated = new Coordinate[size+1][size+1];
		code = getCodeAndRotation(map,size,topLeft);
	}
	
	protected int getCodeAndRotation(Map map, int size, Coordinate topLeft) {
		int[][] m = new int[size][size];
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				try {
					if(map.getCell(i+topLeft.getX(), j+topLeft.getY()).isBlocked()) {
						m[i][j] = 0;
					} else {
						m[i][j] = 1;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					m[i][j] = 0;
				}
			}
		}
		int minCode = getCodeFromMap(m);
		//int minRotation = 0;
		Coordinate[][] fromRotatedTemp = new Coordinate[size+1][size+1]; 
		Coordinate[][] toRotatedTemp = new Coordinate[size+1][size+1];
		initialiseMatrix(fromRotatedTemp);
		initialiseMatrix(toRotatedTemp);
		System.arraycopy(fromRotatedTemp, 0, fromRotated, 0, fromRotatedTemp.length);
		System.arraycopy(toRotatedTemp, 0, toRotated, 0, toRotatedTemp.length);
		for(int rot=1; rot<=3; rot++) {
			int[][] tempMap = new int[size][size];
			Coordinate[][] fromRotatedTemp1 = new Coordinate[size+1][size+1]; 
			Coordinate[][] toRotatedTemp1 = new Coordinate[size+1][size+1];
			for(int i=0;i<=size;i++) {
				for(int j=0;j<=size;j++) {
					if(i!=size && j!=size) {
						tempMap[i][j] = m[size-1-j][i];
					}
					fromRotatedTemp1[i][j] = fromRotatedTemp[size-j][i];
					toRotatedTemp1[i][j] = toRotatedTemp[j][size-i];
					
				}
			}
			if(getCodeFromMap(tempMap)<minCode) {
				//minRotation = rot;
				minCode = getCodeFromMap(tempMap);
				System.arraycopy(fromRotatedTemp1, 0, fromRotated, 0, fromRotatedTemp.length);
				System.arraycopy(toRotatedTemp1, 0, toRotated, 0, toRotatedTemp.length);
			}
			m=tempMap;
			fromRotatedTemp = fromRotatedTemp1;
			toRotatedTemp = toRotatedTemp1;
			
		}
		return minCode;
	}
	
	protected int getCodeFromMap(int[][] m) {
		int code = 0;
		for(int j=0;j<size;j++) {
			for(int i=0;i<size;i++) {
				code = code<<1;
				try{
					if(m[i][j] > 0) {
						code++;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//skip if part of this block is out of bounds, because we call those cells blocked!
				}
			}
		}
		return code;
	}
	
	private void initialiseMatrix(Coordinate[][] matrix) {
		for(int i=0;i<matrix.length; i++) {
			for(int j=0;j<matrix.length;j++) {
				matrix[i][j] = new Coordinate(i,j);
			}
		}
	}
	
	public Coordinate toRotated(Coordinate c) {
		return toRotated[c.getX()][c.getY()];
	}
	
	public Coordinate fromRotated(Coordinate c) {
		return fromRotated[c.getX()][c.getY()];
	}
	
}
