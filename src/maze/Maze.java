package maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import java.util.Arrays;
import java.util.Random;

//import

public class Maze {
	//holds an int array
	
	/*
	 * N = 1
	 * S = 2
	 * E = 4
	 * W = 8
	 */
	
	byte[][] maze;
	int size;
	static Random rand = new Random();
	
	List<Integer> litCellsX = new ArrayList<>();
	List<Integer> litCellsY = new ArrayList<>();
	
	//Starting Place for Algorithm: x, y
	static int x;
	static int y;
	
	public Maze(int size) {
		this.size = size;
		create(size);
		
		//System.out.print(this);
	}
	
	public Maze(int size, long seed) {
		this.size = size;
		rand.setSeed(seed);
		create(size);
		//System.out.print(this);
		
	}
	
	void create(int size) {
		maze = new byte[size][size];
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				maze[i][j] = 0;
			}
		}
		x = rand.nextInt(size);
		y = rand.nextInt(size);
		growingTree();
		addNoms();
	}
	
	public String toString() {
		String s = "[";
		for (int i=0; i<size; i++) {
			s += Arrays.toString(maze[i]);
			s += ", \n";
		}
		s += "\n ";
		for (int i=0; i<(size * 2 - 1); i++) {
			s += "_";
		}
		for (int i=0; i<size; i++) {
			s += "\n";
			s += "|";
			for (int j=0; j<size; j++) {
				if (maze[i][j] == 0 && i+1 < size && maze[i+1][j] == 0) // if the cell's value and the cell below it's value are 0
					s += " ";
				else
					s += (((maze[i][j] & 2) != 0) ? " " : "_"); //if cell has 2(south) bit put space, otherwise put wall below
				
				if (maze[i][j] == 0 && j+1 < size && maze[i][j+1] == 0) // if the current cell and the cell to the right have value 0
					s += ((i+1 < size && (maze[i+1][j] == 0 || maze[i+1][j+1] == 0)) ? " " : "_"); //if 
				else if ((maze[i][j] & 4) != 0) //if cell has 4 bit(east)
					s += ((((maze[i][j] | maze[i][j+1]) & 2) != 0) ? " " : "_");
				else
					s += "|";
			}
		}
		return s;
	}
	
	static int nextIndex(int ceil) {
        if (ceil == 0)
        	return 0;
        
		if (rand.nextDouble() > 0.5) {
        	return rand.nextInt(ceil); //random
        } else {
        	return ceil-1; //newest
        }
        //return ceil/2; //middle
        //return 0; //oldest
	}
	
	public boolean cellIsLit(int x, int y) {
		for (int i=0; i<litCellsX.size(); i++) {
			if (litCellsX.get(i)==x && litCellsY.get(i)==y)
				return true;
		}
		return false;
	}
	
	static byte[] shuffle(byte[] a) {
		for (int i = a.length - 1; i > 0; i--)
	    {
	      byte x = (byte)rand.nextInt(i + 1);
	      // Simple swap
	      byte y = a[x];
	      a[x] = a[i];
	      a[i] = y;
	    }
		return a;
	}
	
	void growingTree() {
		ArrayList<Integer> cellsx = new ArrayList<Integer>();
		ArrayList<Integer> cellsy = new ArrayList<Integer>();
		
		cellsx.add(x);
		cellsy.add(y);
		
		int index;
		int nx=0, ny=0;
		byte[] dir = {1, 2, 4, 8};
		boolean fill = false;
		
		index = nextIndex(cellsx.size());
		while (!(cellsx.isEmpty() && cellsy.isEmpty())) {
			x = cellsx.get(index);
			y = cellsy.get(index);
			
			dir = shuffle(dir);
			for (int i=0; i<4; i++) {
				nx = x; ny = y;
				if (dir[i] == 1)
					ny = y - 1;
				else if (dir[i] == 2)
					ny = y + 1;
				else if (dir[i] == 4)
					nx = x + 1;
				else if (dir[i] == 8)
					nx = x - 1;
				
				if (nx >= 0 && ny >= 0 && nx < size && ny < size && maze[ny][nx] == 0) {
					maze[y][x] |= dir[i];
					
					//System.out.println(dir[i] + ", " + nx + ", " + ny + ", " + cellsx + ", " + cellsy);
					if (dir[i] == 1)
						maze[ny][nx] |= 2;
					else if (dir[i] == 2)
						maze[ny][nx] |= 1;
					else if (dir[i] == 4)
						maze[ny][nx] |= 8;
					else
						maze[ny][nx] |= 4;
					
					cellsx.add(nx);
					cellsy.add(ny);
					
					fill = true;
					
					break;
				}
			}
			
			if (fill == false) {
				cellsx.remove(index);
				cellsy.remove(index);
				index = nextIndex(index);
			} else {
				index = cellsx.size()-1;
				fill = false;
			}
			
			
		}
	}
	
	void addNoms() {
		
	}
	
	public byte[][] getMaze() {
		return maze;
	}
	
	public byte get(int i, int j) {
		return maze[i][j];
	}
	
}
