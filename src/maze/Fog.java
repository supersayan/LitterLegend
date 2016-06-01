package maze;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sprite.NodeBase;

public class Fog extends NodeBase {

	Rectangle box;
	
	public Fog(Pane layer, double x, double y, double width, double height) {
		super(layer, x, y, width, height);
		
		box = new Rectangle();
		box.setX(x);
		box.setY(y);
		box.setWidth(width);
		box.setHeight(height);
		box.setFill(Color.BLACK);
		box.setVisible(true);
		
		addToLayer();
	}
	
	@Override
	public void addToLayer() {
        this.layer.getChildren().add(this.box);
    }
	
	@Override
    public void removeFromLayer() {
        this.layer.getChildren().remove(this.box);
    }

	@Override
	public void updateUI() {
		// TODO Auto-generated method stub
		
	}

}
