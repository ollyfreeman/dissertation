package engine.graph.BlockAStar.LDDB.geometric;

import engine.graph.BlockAStar.Block;
import engine.map.Map;
import utility.Coordinate;
import utility.Pair;

public class Block_geometric extends Block{
	
	private int rotation;
	private Coordinate[][][] rotationMatrix = initialiseRotationMatrix();
	
	public Block_geometric(Map map, Coordinate topLeft, Coordinate goal) {
		super(map, topLeft, goal);
		Pair<Integer,Integer> p = getCodeAndRotation(map,size,topLeft);
		code = p.get0();
		rotation = p.get1();
	}
	
	protected Pair<Integer,Integer> getCodeAndRotation(Map map, int size, Coordinate topLeft) {
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
		int minRotation = 0;
		for(int rot=1; rot<=3; rot++) {
			int[][] tempMap = new int[size][size];
			for(int i=0;i<size;i++) {
				for(int j=0;j<size;j++) {
					tempMap[i][j] = m[size-1-j][i];
				}
			}
			if(getCodeFromMap(tempMap)<minCode) {
				minRotation = rot;
				minCode = getCodeFromMap(tempMap);
			}
			m=tempMap;
			
		}
		return new Pair<Integer,Integer>(minCode,minRotation);
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
	
	private static void initialiseMatrix(Coordinate[][] matrix) {
		for(int i=0;i<matrix.length; i++) {
			for(int j=0;j<matrix.length;j++) {
				matrix[i][j] = new Coordinate(i,j);
			}
		}
	}
	
	public Coordinate toRotated(Coordinate c) {
		if(rotation!=0) {
			return rotationMatrix[3-rotation][c.getX()][c.getY()];
		} else {
			return c;
		}
	}
	
	public Coordinate fromRotated(Coordinate c) {
		if(rotation!=0){
			return rotationMatrix[rotation-1][c.getX()][c.getY()];
		} else {
			return c;
		}
	}
	
	private static Coordinate[][][] initialiseRotationMatrix() {
		Coordinate[][][] rotMatrix = new Coordinate[3][size+1][size+1];
		Coordinate[][] fromRotatedTemp = new Coordinate[size+1][size+1]; 
		initialiseMatrix(fromRotatedTemp);
		for(int rot=1; rot<=3; rot++) {
			Coordinate[][] fromRotatedTemp1 = new Coordinate[size+1][size+1]; 
			for(int i=0;i<=size;i++) {
				for(int j=0;j<=size;j++) {
					fromRotatedTemp1[i][j] = fromRotatedTemp[size-j][i];
				}
			}
			fromRotatedTemp = fromRotatedTemp1;
			for(int i=0;i<size+1;i++) {
				for(int j=0;j<size+1;j++) {
					rotMatrix[rot-1][i][j] = fromRotatedTemp[i][j];
				}
			}
		}
		return rotMatrix;
	}
	
}
