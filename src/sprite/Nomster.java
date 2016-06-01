package sprite;

import maze.Corner;
import maze.Wall;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public abstract class Nomster extends ActiveSpriteBase {

	public Nomster(Pane layer, Image image, double x, double y, double dx,
			double dy, double health, double damage, int[] duration, int[] count,
			double width, double height) {
		super(layer, image, x, y, dx, dy, health, damage, duration, count,
				width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void checkRemovability() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void handleCollision(SpriteBase otherSprite) {
		if (otherSprite instanceof Wall || otherSprite instanceof Corner) {
    		if (!((otherSprite.getY() + otherSprite.getHeight()) > py && otherSprite.getY() < (py + h))) {
        		y = py;
        		dy = -dy;
        	}
        	else if (!((otherSprite.getX() + otherSprite.getWidth()) > px && otherSprite.getX() < (px + w))) {
        		x = px;
        		dx = -dx;
        	} else {
        		x = px;
        		dx = -dx;
        		y = py;
        		dy = -dy;
        	}
    	} else if (otherSprite instanceof Player) {
    		setState(1);
    	}
	}
	
	public void death() {
		
	}

}
