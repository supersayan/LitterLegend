package maze;

//import java.util.Arrays;
import java.util.Random;

//import

public class OldMaze {
	//holds an int array
	
	byte[][] maze;
	int size;
	Random rand = new Random();
	
	public OldMaze(int size) {
		create(size);
		this.size = size;
		divide(0, 0, size, size, chooseOrientation(size, size));
		//System.out.print(this);
	}
	
	public OldMaze(int size, long seed) {
		create(size);
		this.size = size;
		divide(0, 0, size, size, chooseOrientation(size, size));
		//System.out.print(this);
		rand.setSeed(seed);
	}
	
	void create(int size) {
		maze = new byte[size][size];
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				maze[i][j] = 0;
			}
		}
		
	}
	
	public String toString() {
		/*System.out.print("[");
		for (int i=0; i<size; i++) {
			System.out.print(Arrays.toString(maze[i]) + ", \n");
		}
		System.out.print("]");*/
		String s = "";
		//s += "\e[H"; // move to upper-left
		//s += " ";
		boolean bottom, south, south2, east;
		for (int i = 0; i<(size * 2 - 1); i++) {
			s += "_";
		}
		//s += "\n";
		//grid.each_with_index do |row, y|
		for (int i=0; i<size; i++){
		    s += "\n";
			s += "|";
		    for (int j=0; j<size; j++) {
		    	bottom = (i+1 >= maze.length); //is cell at bottom of maze
		    	south  = ((maze[i][j] & 1)/*S*/ != 0 || bottom); //does cell have 1 bit or cell is at bottom
		    	south2 = (j+1 < maze[i].length && (maze[i][j+1] & 1)/*S*/ != 0 || bottom); //is (there a cell to the right and 
		    			// does cell to the right have 1 bit) or is cell at bottom
		    	east   = ((maze[i][j] & 2)/*E*/ != 0 || j+1 >= maze[i].length); //does cell have 2 bit or is the cell at the right

		    	s += (south ? "_" : " "); //if was moving left/right across cell, put wall on bottom
		    	s += (east ? "|" : ((south && south2) ? "_" : " ")); //if was moving up.down across cell, put wall on right
			}
		}
		/*for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				s += maze[i][j];
			}
			s += "\n";
		}*/
		return s;
	}
	
	//for maze creation
	/**
	 * @param width		the width of the section given
	 * @param height	the height of the section given
	 * @return			true if choose to divide the section horizontally
	 * 					false if vertically
	 */
	boolean chooseOrientation(int width, int height) {
		if (width < height) {
			return true;
		} else if (height < width) {
			return false;
		} else {
			return (int)(rand.nextInt(2)) == 1 ? true : false;
		}
	}
	
	void divide(int x, int y, int width, int height, boolean horizontal) {
		if (width < 2 || height < 2) {
			return;
		}
		//System.out.print(this);
		/*System.out.println(width);
		System.out.println(height + "\n");*/
		
		//int wx, wy;
		//int px, py;
		//int dx, dy;
		//int length;
		//char dir;
	
		//where will the wall be drawn from?
		int wx = x + (horizontal ? 0 : (int)
				((width>2)?(rand.nextInt(width-2)):(0)));
		int wy = y + (horizontal ? (int)
				((height>2)?(rand.nextInt(height-2)):(0))
				: 0);
	
		//where will the passage through the wall exist?
		int px = wx + (horizontal ? rand.nextInt(width) : 0);
		int py = wy + (horizontal ? 0 : rand.nextInt(height));

		/*//what direction will the wall be drawn?
		dx = horizontal ? 1 : 0;
		dy = horizontal ? 0 : 1;*/

		//how long will the wall be?
		int length = horizontal ? width : height;

		//what direction is perpendicular to the wall?
		byte dir = (byte) (horizontal ? 1 : 2); //S : E
		
		for (int i=0; i<length; i++) {
			if (wx != px || wy != py)
				maze[wy][wx] |= dir;
				/*
				 * dir is 1 if moving south
				 * 		  2 if moving east
				 * each cell contains two bits: (bitwise or)
				 * 0			0
				 * ^moving east	^moving south
				 */
			if (horizontal)
				 wx += 1;
			else
				 wy += 1;
			//wx += dx
			//wy += dy
		}

		int nx = x;
		int ny = y;
		int w = horizontal ? width : wx-x+1;
		int h = horizontal ? wy-y+1 : height;
		divide(nx, ny, w, h, chooseOrientation(w, h));

		nx = (horizontal) ? x : wx+1;
		ny = (horizontal) ? wy+1 : y;
		w = (horizontal) ? width : x+width-wx-1;
		h = (horizontal) ? y+height-wy-1 : height;
		divide(nx, ny, w, h, chooseOrientation(w, h));
	}
	
	
}
