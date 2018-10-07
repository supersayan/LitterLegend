package sprite;

import maze.Corner;
import maze.Wall;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public abstract class Litter extends SpriteBase {

	public Litter(Pane layer, Image image, double x, double y, double dx,
			double dy, double width, double height) {
		super(layer, image, x, y, dx, dy, width, height, false);
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
    	}
	}
	
	public void death() {
		
	}

}
