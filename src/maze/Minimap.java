package maze;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import sprite.NodeBase;

public class Minimap extends NodeBase {
	
	List<Shape> minimap;
	
	public Minimap(Pane layer, double x, double y, double width, double height, int mazeSize) {
		super(layer, x, y, width, height);
		minimap = new ArrayList<Shape>();
	}

	@Override
	public void addToLayer() {
		this.getLayer().getChildren().addAll(this.minimap);
	}

	@Override
	public void removeFromLayer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
