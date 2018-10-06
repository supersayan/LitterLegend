package main;

import java.util.Random;

import maze.Corner;
import maze.Door;
import maze.Fog;
import maze.Maze;
import maze.Wall;
import sprite.Bat;
import sprite.Player;

public class Level {
	
	int level;
	
	int startIndex;
	int endIndex;
	
	public Level(int level) {
		this.level = level;
	}
	
	private void loadLevel() {
		
		Random rand = new Random();
		
        startIndex = rand.nextInt(5*level);
        endIndex = rand.nextInt(5*level);
        
        player = new Player(playLayer, playerImage, (192)+offset/*x*/, (192+startIndex*320)+offset/*y*/, Settings.PLAYER_HEALTH, 0, Settings.PLAYER_SPEED);
		maze = new Maze(level*5);
		
        playLayer.setMinSize(64+1600*level+2*offset, 64+1600*level+2*offset);
        playLayer.setMaxSize(64+1600*level+2*offset, 64+1600*level+2*offset);

	    camera.setHmax(64+1600*level+2*offset-Settings.SCENE_WIDTH);
	    camera.setVmax(64+1600*level+2*offset-Settings.SCENE_HEIGHT);

        door = new Door(playLayer, 320*(level*5-1)+142+offset, 320*(endIndex)+142+offset);
        
        for (int i=0; i<5*level+1; i++) {
        	for (int j=0; j<5*level+1; j++) { //add corners
        		corners.add(new Corner(playLayer, cornerImage, 320*i+offset, 320*j+offset));
        	}
        }
        for (int i=0; i<5*level; i++) { //add top row
        	walls.add(new Wall(playLayer, wallHImage, 64+320*i+offset, 0+offset, true));
        }
        for (int i=0; i<(5*level); i++) { //add other walls
        	walls.add(new Wall(playLayer, wallVImage, 0+offset, 64+320*i+offset, false));
        	for (int j=0; j<5*level; j++) {
        		if (!(maze.get(i, j) == 0 && i+1 < 5*level && maze.get(i+1, j) == 0) && ((maze.get(i, j) & 2) == 0))
        			walls.add(new Wall(playLayer, wallHImage, 64+320*j+offset, 320*(i+1)+offset, true));
        		
				if ((maze.get(i, j) == 0 && j+1 < (5*level) && maze.get(i, j+1) == 0)) {
					if ((i+1 < 5*level) && (maze.get(i+1, j) == 0 || maze.get(i+1, j+1) == 0))
						walls.add(new Wall(playLayer, wallHImage, 64+320*j+offset, 320*(i+1)+offset, true));
				} else if ((maze.get(i, j) & 4) != 0) {
					if (((maze.get(i, j) | maze.get(i, j+1)) & 2) == 0)
						walls.add(new Wall(playLayer, wallHImage, 64+320*j+offset, 320*(i+1)+offset, true));
				} else
					walls.add(new Wall(playLayer, wallVImage, 320*(j+1)+offset, 64+320*i+offset, false));
            }
        }
        for (int i=0; i<level*5; i++) {
        	for (int j=0; j<level*5; j++) {
        		if (rand.nextDouble()<Settings.NOM1_CHANCE)
        			nomsters.add(new Bat(playLayer, batImage, (320*i)+192-(45/2)+offset, (320*j)+192-(48/2)+offset, (rand.nextDouble()>0.5)?4:-4, (rand.nextDouble()>0.5)?4:-4, 1, 45, 48));
        	}
        }
        
        //nomsters.add(new Bat(playLayer, batImage, 1600*level-192, (192+endIndex*320), (rand.nextDouble()>0.5)?6:-6, (rand.nextDouble()>0.5)?6:-6, 1, 45, 48));
        //nomsters.add(new Bat(playLayer, batImage, 192, (192+startIndex*320), (rand.nextDouble()>0.5)?6:-6, (rand.nextDouble()>0.5)?6:-6, 1, 45, 48));
        
        /*for (int i=0; i<(5*level); i++) { //add fog
        	for (int j=0; j<(5*level); j++) {
        		fog.add(new Fog(playLayer, 320*(j)+64+offset, 320*(i)+64+offset, 256, 256));
        	}
        }
        for (int i=0; i<5*level+1; i++) { //corner fog
        	for (int j=0; j<5*level+1; j++) {
        		fog.add(new Fog(playLayer, 320*j+offset, 320*i+offset, 64, 64));
        	}
        }
        for (int i=0; i<5*level+1; i++) { //column fog
        	for (int j=0; j<5*level+2; j++) {
        		fog.add(new Fog(playLayer, 320*j+offset, 64+320*i+offset, 64, 256));
        	}
        }
        for (int i=0; i<(5*level)+1; i++) { //row fog
        	for (int j=0; j<(5*level)+2; j++) {
        		fog.add(new Fog(playLayer, 320*(j)+64+offset, 320*i+offset, 256, 64));
        	}
        }*/

        fog.add(new Fog(playLayer, 0, 0, 2*offset+64+1600*level, offset));
        fog.add(new Fog(playLayer, 0, offset, offset, 64+1600*level));
        fog.add(new Fog(playLayer, offset+64+1600*level, offset, offset, 64+1600*level));
        fog.add(new Fog(playLayer, 0, offset+64+1600*level, 2*offset+64+1600*level, offset));
        
	}
	
}
