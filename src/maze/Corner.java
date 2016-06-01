package maze;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import sprite.SpriteBase;

public class Corner extends SpriteBase {
	
	static int[] c = {1};
	static int[] d = {1};
	
	public Corner(Pane layer, Image image, double x, double y) {
		super(layer, image, x, y, 0, 0, 64, 64, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void checkRemovability() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCollision(SpriteBase other) {
		// TODO Auto-generated method stub
		
	}

}
