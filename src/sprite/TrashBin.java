package sprite;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class TrashBin extends Bin {
	

    static int[] frameCount = {2, 2, 3};
    static int[] frameDuration = {500, 500, 1000};
	
	public TrashBin(Pane layer, Image image, double x, double y, double dx,
			double dy, int level, double width, double height) {
		super(layer, image, x, y, dx, dy, 40*level, 5*level, frameDuration, frameCount,
				width, height);
		
	}

	/*public void handleCollision(SpriteBase other) {
		// TODO Auto-generated method stub
		if (other instanceof Player) {
			state = 1;
		}
	}*/

}
