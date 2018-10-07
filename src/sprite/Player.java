package sprite;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import main.Settings;
import maze.Corner;
import maze.Wall;

public class Player extends ActiveSpriteBase {

    double speed;
    static int[] frameCount = {3, 3, 1, 3, 10, 10, 10, 10};
    static int[] frameDuration = {3000, 3000, 100, 3000, 1000, 1000, 1000, 1000};
    
    boolean onLitter;
    boolean hasLitter;
    
    Timer timer = new Timer();

    public Player(Pane layer, Image image, double x, double y, double speed) {

        super(layer, image, x-(Settings.PLAYER_WIDTH/2), y-(Settings.PLAYER_HEIGHT/2), 0, 0, frameDuration, frameCount, Settings.PLAYER_WIDTH, Settings.PLAYER_HEIGHT);

        this.speed = speed;
    }
    
    public void handleInput(List<String> input) {
    	if (this.canMove == false) {
    		return;
    	}
    	if (input.contains(Settings.LEFT)) {
        	if (input.contains(Settings.RIGHT)) {
        		moveX(0);
        	} else {
	            moveX(-1);
	            if (!(input.contains(Settings.UP) || input.contains(Settings.DOWN)))
	            	setState(5);
        	}
	    } else {
        	if (input.contains(Settings.RIGHT)) {
        		moveX(1);
        		if (!(input.contains(Settings.UP) || input.contains(Settings.DOWN)))
        			setState(7);
        	} else {
 	            moveX(0);
        	}
        }
        
        if (input.contains(Settings.UP)) {
        	if (input.contains(Settings.DOWN)) {
        		moveY(0);
        	} else {
        		moveY(-1);
        		setState(6);
        	}
        } else {
        	if (input.contains(Settings.DOWN)) {
        		moveY(1);
    			setState(4);
        	} else {
 	            moveY(0);
        	}
        }
        if(state > 3 && dx == 0 && dy == 0) {
        	setState(state - 4);
        }
        
        if (input.contains(Settings.PICKUP)) {
        	
        }
    }
    
    public void moveY(int scale) {
    	dy = scale * speed;
    }
    
    public void moveX(int scale) {
    	dx = scale * speed;
    }
    
    @Override
    public void update() {
        super.update();
        if (animation[state].getStatus() != Animation.Status.RUNNING)
        	animation[state].play();
    }
    
    @Override
    public void handleCollision(SpriteBase otherSprite) {
    	if (otherSprite instanceof Wall || otherSprite instanceof Corner) {
    		if (!((otherSprite.getY() + otherSprite.getHeight()) > py && otherSprite.getY() < (py + h))) {
    			//if player hits a wall from top or bottom, stop moving
    			y = py;
    		}
    		else if (!((otherSprite.getX() + otherSprite.getWidth()) > px && otherSprite.getX() < (px + w))) {
    			//if player hits a wall from left or right
    			x = px;
    		} else {
    			//both
    			x = px;
    			y = py;
    		}
    	}
    }
    
    public void death() {
    	//TODO
    }

    @Override
    public void checkRemovability() {
    	
    }

}