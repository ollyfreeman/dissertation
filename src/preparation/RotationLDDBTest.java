package preparation;

import utility.Pair;

public class RotationLDDBTest {
	
	private static int blockSize = 4;

	protected static Pair<Integer,Integer> getCodeAndRotation(int[][] map) {
		int minRotation = 0;
		int minCode = getCodeFromMap(map);
		for(int rot=1; rot<=3; rot++) {
			int[][] tempMap = new int[blockSize][blockSize];
			for(int i=0;i<blockSize;i++) {
				for(int j=0;j<blockSize;j++) {
					tempMap[i][j] = map[blockSize-1-j][i];
				}
			}
			System.out.println("Code: " + getCodeFromMap(tempMap) + " at rotation " + rot);
			if(getCodeFromMap(tempMap)<minCode) {
				minCode = getCodeFromMap(tempMap);
				minRotation = rot;
			}
			map=tempMap;
		}
		return new Pair<Integer,Integer>(minCode,minRotation);
	}
	
	protected static int getCodeFromMap(int[][] map) {
		int code = 0;
		for(int j=0;j<blockSize;j++) {
			for(int i=0;i<blockSize;i++) {
				code = code<<1;
				try{
					if(map[i][j] > 0) {
						code++;
						//System.out.println(i + "," + j + " is free");
					} else {
						//System.out.println(i + "," + j + " is blocked");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//skip if part of this block is out of bounds, because we call those cells blocked!
				}
			}
		}
		return code;
	}
	
	public static void main(String[] args) {
		int[][] map = new int[4][4];
		for (int i=0;i<blockSize;i++) {
			for(int j=0;j<blockSize;j++) {
				map[i][j] = 1;
			}
		}
		map[0][2] = 0;
		map[1][3] = 0;
		map[2][2] = 0;
		Pair<Integer,Integer> p = getCodeAndRotation(map);
		System.out.println("Code " + p.get0() + " with rotation " + p.get1());
	}
	
}
