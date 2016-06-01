package maze;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import sprite.SpriteBase;

public class Wall extends SpriteBase {
	
	public Wall(Pane layer, Image image, double x, double y, boolean horizontal) {
		super(layer, image, x, y, 0, 0,	(horizontal?256:64), (horizontal?64:256), false);
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
