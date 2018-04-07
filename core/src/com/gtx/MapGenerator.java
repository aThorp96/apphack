package com.gtx;

import com.badlogic.gdx.math.Vector2;

public class MapGenerator {
	public static Tile[][] generateMap(int mapWidth, int mapHeight) {
		
		Tile[][] ret = new Tile[mapHeight][mapWidth];
		
		TileType[] tt = TileType.values();
		
		for (int row = 0; row < mapHeight; row++) {
			for (int col = 0; col < mapWidth; col++) {
				ret[row][col] = new Tile(new Vector2((float)row, (float)col), new Vector2(1f,1f), tt[(row+col)%tt.length]);
			}
		}
		
		return ret;
	}
}
