package com.gtx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import com.badlogic.gdx.math.Vector2;

public class MapGenerator {
	
	private static Random rand;
	
	public static Collection<Entity> placeEntities(Tile[][] map, Hero hero, int maxNumberOfHostiles) {
		int width = map[0].length;
		int height = map.length;
		
		TileType[][] tileMap = new TileType[height][width];
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				tileMap[row][col] = map[row][col].tileType;				
			}
		}
		
		boolean[][] spawns = findValidSpawns(tileMap);
		List<Vector2> positions = new ArrayList<Vector2>();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (spawns[row][col])
					positions.add( new Vector2(col, row) );
			}
		}
		

		Collections.shuffle(positions);
		Vector2 tilePos = positions.remove(positions.size()-1);
		
		hero.setPosition( new Vector2(map[(int)tilePos.y][(int)tilePos.x].position) );
		
		Collection<Entity> entities = new ArrayList<Entity>();
		entities.add(hero);
		
		int numHostiles = 0;
		while (!positions.isEmpty()) {
			Vector2 position = positions.remove(positions.size()-1);
			if (numHostiles > maxNumberOfHostiles) {
				break;
			}
			entities.add( new Enemy( new Vector2(map[(int)position.y][(int)position.x].position), new Vector2(.9f,.9f), EntityType.GOBLIN ) );
			numHostiles++;	
		}
		
		return entities;
	}
	
	public static Tile[][] generateMap(int mapWidth, int mapHeight, int seed) {
		
		int maxTries = 5;
		boolean done = false;
		Tile[][] ret = null;
		rand = new Random(seed);
		
		while (!done) {
			try {
				int tries = 1;								
				
				TileType[][] map = new TileType[mapHeight][mapWidth];
				
				
				Vector2 minRoomSize = new Vector2( 4,4 );
				Vector2 maxRoomSize = new Vector2( 8,8 );
				int numTriesToPlaceRoom = 150;
				
				int numTriesToPlaceMaze = 20;
				
				fillWithWalls(map);
				fillWithRooms(map, minRoomSize, maxRoomSize, numTriesToPlaceRoom);
				for (int attempts = 0; attempts < numTriesToPlaceMaze; attempts++)
					fillWithMaze(map);
				
				drawBorder(map, TileType.WALL);
				
				
				TileType[][] mapCopy = cloneMap(map);
				while (!areRoomsConnected(mapCopy)) {

					mapCopy = cloneMap(map);
					connectRoomsWithMaze(mapCopy);
					tries++;
					if (tries > maxTries)
						throw new Exception();
				}		
				map = mapCopy;
				drawBorder(map, TileType.WALL);
				
				removeDeadEnds(map);
				
				
				ret = new Tile[mapHeight][mapWidth];		
				
				for (int row = 0; row < mapHeight; row++) {
					for (int col = 0; col < mapWidth; col++) {
						ret[row][col] = new Tile(new Vector2((float)col, (float)row), new Vector2(1f,1f), map[row][col]);
					}
				}
				
				done = true;
				
			} catch (Exception e) {
				
			}
		}
		
		return ret;
	}
	
	private static boolean[][] findValidSpawns(TileType[][] map) {
		int width = map[0].length;
		int height = map.length;
		
		boolean[][] tiles = findTiles(map, TileType.GROUND);
		boolean[][] rtiles = new boolean[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (tiles[row][col] && numValidSurroundingPos( new Vector2(col, row) , width, height, tiles) >= 3)
					rtiles[row][col] = true;
			}
		}
		
		return rtiles;
	}
	
	private static void drawBorder(TileType[][] map, TileType type) {
		int width = map[0].length;
		int height = map.length;
		
		for (int row = 0; row < height; row++) {
			map[row][0] = type;
			map[row][width-1] = type;
		}
		
		for (int col = 0; col < width; col++) {
			map[0][col] = type;
			map[height-1][col] = type;
		}
	}
	
	private static void removeDeadEnds(TileType[][] map) {
		int width = map[0].length;
		int height = map.length;
		
		
		boolean removedDeadEnd = true;
		while (removedDeadEnd){
			removedDeadEnd = false;
			
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {
					Vector2[] surroundingPos = generateSurroundingPositions( new Vector2((float)col, (float)row) );
					int numWalls = 0;
					for (Vector2 pos : surroundingPos) {
						if (pos.x < 0 || pos.y < 0 || pos.x > width-1 || pos.y > height-1)
							continue;
						
						if (map[(int)pos.y][(int)pos.x] != TileType.WALL)
							continue;
						
						numWalls++;
					}
					if (numWalls >= 3 && map[row][col] == TileType.GROUND) {
						map[row][col] = TileType.WALL;
						removedDeadEnd = true;
					}
				}
			}
		}
	}
	
	private static TileType[][] cloneMap(TileType[][] map) {
		int width = map[0].length;
		int height = map.length;
		TileType[][] tiles = new TileType[height][width];
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				tiles[row][col] = map[row][col];
			}
		}
		
		return tiles;
	}
	
	private static boolean[][] cloneBools(boolean[][] map) {
		int width = map[0].length;
		int height = map.length;
		boolean[][] tiles = new boolean[height][width];
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				tiles[row][col] = map[row][col];
			}
		}
		
		return tiles;
	}
	
	private static boolean areRoomsConnected(TileType[][] map) {
		int width = map[0].length;
		int height = map.length;
		boolean[][] tiles = findTiles(map, TileType.GROUND);
		
		boolean[][] connectedTiles = new boolean[height][width];
		
		Vector2 startPos = findTile(map, TileType.GROUND);
		
		Stack<Vector2> stack = new Stack<Vector2>();
		
		Vector2 currentPosition;
		stack.push( startPos );
		
		while (!stack.isEmpty()) {

			currentPosition = stack.pop();
			
			if (!connectedTiles[(int)currentPosition.y][(int)currentPosition.x] 
					&& tiles[(int)currentPosition.y][(int)currentPosition.x]
					) {
				connectedTiles[ (int)currentPosition.y ][ (int)currentPosition.x ] = true;				
				
				Vector2[] surroundingPos = generateSurroundingPositions(currentPosition);
				
				for (Vector2 pos : surroundingPos) {

					if (pos.x <= 0 || pos.y <= 0 || pos.x >= width-1 || pos.y >= height-1)
						continue;
					
					if (connectedTiles[(int)pos.y][(int)pos.x])
						continue;
					
					if (!tiles[(int)pos.y][(int)pos.x])
						continue;
					
					stack.push(pos);
				}
			
			}
		}
		
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (tiles[row][col] && !connectedTiles[row][col]) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private static int countNumValid(boolean[][] bools) {
		int width = bools[0].length;
		int height = bools.length;
		
		int count = 0;
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (bools[row][col])
					count++;					
			}
		}
		
		return count;
	}
	
	private static Vector2 findTile(TileType[][] map, TileType tile) {
		int width = map[0].length;
		int height = map.length;
		boolean[][] tiles = findTiles(map, tile);
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (tiles[row][col])
					return new Vector2(col, row);
			}
		}
		
		return null;
	}
	
	private static void connectRoomsWithMaze(TileType[][] map) {

		Vector2 roomTile = findTile(map, TileType.GROUND);
		
		while(true) {
			boolean[][] sharedWalls = fillFindAdjacentWalls(map, roomTile);
			ArrayList<Vector2> potentialHoles = positionsFromBools(sharedWalls);
			
			if (potentialHoles.size() != 0) {
				Vector2 hole = potentialHoles.remove(potentialHoles.size() - 1);
				map[(int)hole.y][(int)hole.x] = TileType.GROUND;
			} else {
				break;
			}
		}
		
	}
	
	private static Vector2 findTrue(boolean[][] map) {
		int width = map[0].length;
		int height = map.length;
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (map[row][col])
					return new Vector2(col, row);
			}
		}
		
		return null;
	}
	
	private static boolean[][] fillFindAdjacentWalls(TileType[][] map, Vector2 startPos) {
		int width = map[0].length;
		int height = map.length;
		boolean[][] tiles = findTiles(map, TileType.GROUND);
		
		boolean[][] connectedTiles = new boolean[height][width];
		boolean[][] notConnectedTiles = cloneBools(tiles);
		
		Stack<Vector2> stack = new Stack<Vector2>();
		
		Vector2 currentPosition;
		stack.push( startPos );
		
		while (!stack.isEmpty()) {

			currentPosition = stack.pop();
			
			if (!connectedTiles[(int)currentPosition.y][(int)currentPosition.x] 
					&& tiles[(int)currentPosition.y][(int)currentPosition.x]
					) {
				
				connectedTiles[ (int)currentPosition.y ][ (int)currentPosition.x ] = true;				
				
				Vector2[] surroundingPos = generateSurroundingPositions(currentPosition);
				
				for (Vector2 pos : surroundingPos) {

					if (pos.x <= 0 || pos.y <= 0 || pos.x >= width-1 || pos.y >= height-1)
						continue;
					
					if (connectedTiles[(int)pos.y][(int)pos.x])
						continue;
					
					if (!tiles[(int)pos.y][(int)pos.x])
						continue;
					
					stack.push(pos);
				}
			
			}
		}
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (connectedTiles[row][col])
					notConnectedTiles[row][col] = false;
			}
		}
		
		boolean[][] ret = new boolean[height][width];
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (map[row][col] == TileType.WALL && numValidSurroundingPos( new Vector2(col, row) , width, height, connectedTiles) > 0 && numValidSurroundingPos( new Vector2(col, row) , width, height, notConnectedTiles) > 0)
					ret[row][col] = true;
			}
		}		
				
		return ret;
	}
	
	private static ArrayList<Vector2> positionsFromBools(boolean[][] bools) {
		int width = bools[0].length;
		int height = bools.length;
		
		List<Vector2> positions = new ArrayList<Vector2>();
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (bools[row][col])
					positions.add( new Vector2(col, row) );
			}
		}
		
		Collections.shuffle(positions);
		
		return (ArrayList<Vector2>) positions;
	}
	
	private static boolean[][] findAdjacentWalls(TileType[][] map, boolean[][] checkMe) {
		int width = map[0].length;
		int height = map.length;
		boolean[][] walls = new boolean[height][width];
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (numValidSurroundingPos( new Vector2(col, row) , width, height, checkMe) > 0 && map[row][col] == TileType.WALL)
					walls[row][col] = true;
			}
		}
		
		return walls;
	}
	
	private static boolean[][] findRoomTiles(TileType[][] map) {
		int width = map[0].length;
		int height = map.length;
		boolean[][] roomTiles = new boolean[height][width];
		
		boolean[][] floorTiles = findTiles(map, TileType.GROUND);
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (floorTiles[row][col] == true && numValidSurroundingPos( new Vector2(col, row) , width, height, floorTiles) == 3)
					roomTiles[row][col] = true;
			}
		}
		
		return roomTiles;
	}
	
	private static boolean[][] findTiles(TileType[][] map, TileType type) {
		int width = map[0].length;
		int height = map.length;
		boolean[][] tiles = new boolean[height][width];
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (map[row][col] == type)
					tiles[row][col] = true;
			}
		}
		
		return tiles;
	}
	
	private static void fillWithWalls(TileType[][] map) {
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[0].length; col++) {
				map[row][col] = TileType.WALL;
			}
		}
	}
	
	private static void fillWithRooms(TileType[][] map, Vector2 minRoomSize, Vector2 maxRoomSize, int numIterations) {
		int width = map[0].length;
		int height = map.length;
		
		for (int iteration = 0; iteration < numIterations; iteration++) {
		
			Vector2 botLeft = new Vector2( generateRandomInt(1,width-2), generateRandomInt(1,height-2)  );
			Vector2 topRight = new Vector2( botLeft.x + generateRandomInt((int)minRoomSize.x,(int)maxRoomSize.x), botLeft.y + generateRandomInt((int)minRoomSize.y,(int)maxRoomSize.y) );
			
			if (isValidRoom(map, botLeft, topRight)) {
				for (int row = (int)botLeft.y; row <= (int)topRight.y; row++) {
					for (int col = (int)botLeft.x; col <= (int)topRight.x; col++) {
						map[row][col] = TileType.GROUND;
					}
				}
			}
			
		}
		
	}
	
	private static boolean isValidRoom(TileType[][] map, Vector2 botLeft, Vector2 topRight) {
		
		int width = map[0].length;
		int height = map.length;
		
		if (botLeft.x <= 0 || botLeft.y <= 0 || botLeft.x >= width-1 || botLeft.y >= height-1)
			return false;
		
		if (topRight.x <= 0 || topRight.y <= 0 || topRight.x >= width-1 || topRight.y >= height-1)
			return false;
		
		for (int row = (int)botLeft.y; row <= (int)topRight.y; row++) {
			for (int col = (int)botLeft.x; col <= (int)topRight.x; col++) {
				if (map[row][col] == TileType.GROUND)
					return false;
			}
		}
		
		return true;
	}
	
	private static void fillWithMaze(TileType[][] map) {
		
		int width = map[0].length;
		int height = map.length;
		
		boolean[][] invalidTiles = findAllOpenAreas(map);
		boolean[][] validTiles = new boolean[height][width];
		
		Vector2 startingPosition = new Vector2(generateRandomInt(1, width-2),generateRandomInt(1, height-2));
		
		validTiles = depthFirstSearch(startingPosition, invalidTiles, validTiles);
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (validTiles[row][col])
					map[row][col] = TileType.GROUND;
			}
		}
		
	}
	
	public static boolean[][] findAllOpenAreas(TileType[][] map) {
		int width = map[0].length;
		int height = map.length;
		boolean[][] ret = new boolean[height][width];
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (map[row][col] == TileType.GROUND) {
					ret[row][col] = true;
				}
			}
		}
		
		return ret;
	}
	
	private static boolean[][] depthFirstSearch(Vector2 startingPosition, boolean[][] invalidTiles, boolean[][] validTiles) {
		
		int width = invalidTiles[0].length;
		int height = invalidTiles.length;
		
		Stack<Vector2> stack = new Stack<Vector2>();
		
		Vector2 currentPosition;
		stack.push( startingPosition );
		
		while (!stack.isEmpty()) {
			
			currentPosition = stack.pop();
			
			if (!validTiles[(int)currentPosition.y][(int)currentPosition.x] 
					&& !(numValidSurroundingPos(currentPosition, width, height, validTiles) > 1)
					&& !(numValidSurroundingPos(currentPosition, width, height, invalidTiles) > 0)
					) {
				validTiles[ (int)currentPosition.y ][ (int)currentPosition.x ] = true;				
				
				Vector2[] surroundingPos = generateSurroundingPositions(currentPosition);
				
				for (Vector2 pos : surroundingPos) {
					if (pos.x <= 0 || pos.y <= 0 || pos.x >= width-1 || pos.y >= height-1)
						continue;
					
					if (invalidTiles[(int)pos.y][(int)pos.x])
						continue;
					
					if (validTiles[(int)pos.y][(int)pos.x])
						continue;
					
					if (numValidSurroundingPos(pos, width, height, validTiles) > 1)
						continue;
					
					if (numValidSurroundingPos(currentPosition, width, height, invalidTiles) > 0)
						continue;
					
					stack.push(pos);
				}
			
			}
		}
		
		return validTiles;
	}
	
	private static int numValidSurroundingPos(Vector2 position, int width, int height, boolean[][] validTiles) {
		int total = 0;
		
		Vector2[] surroundingPos = generateSurroundingPositions(position);
		for (Vector2 pos : surroundingPos) {
			
			if (pos.x <= 0 || pos.y <= 0 || pos.x >= width-1 || pos.y >= height-1)
				continue;
			
			if (!validTiles[(int)pos.y][(int)pos.x])
				continue;
			
			total++;
		}
		
		return total;
	}
	
	private static int generateRandomInt(int min, int max) {
		return (int) Math.round(rand.nextDouble() * (max - min) + min);
	}
	
	private static Vector2[] generateSurroundingPositions(Vector2 position) {
		
		Vector2[] ret = new Vector2[] {
				new Vector2(position.x-1, position.y),
				new Vector2(position.x+1, position.y),
				new Vector2(position.x, position.y-1),
				new Vector2(position.x, position.y+1)
			};
		
		
		List<Vector2> l = Arrays.asList(ret);
		Collections.shuffle(l);
		
		return (Vector2[]) l.toArray();
	}
	
}
