package maze;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sprite.NodeBase;

public class Door extends NodeBase {

	Rectangle box;
	
	public Door (Pane layer, double x, double y) {
		super(layer, x, y, 100, 100);
		
		box = new Rectangle();
		box.setX(x);
		box.setY(y);
		box.setWidth(100);
		box.setHeight(100);
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
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
