package sprite;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class SpriteBase extends NodeBase {

    Image image;
    ImageView imageView;

    double dx;
    double dy;
    
    double px;
    double py;
    
    boolean canMove = true;

    public SpriteBase(Pane layer, Image image, double x, double y, double dx, double dy, double width, double height, boolean animated) {
    	
    	super(layer, x, y, width, height);
    	
        this.image = image;
        this.dx = dx;
        this.dy = dy;

        this.imageView = new ImageView(image);
        this.imageView.relocate(x, y);
        
        //this.w = image.getWidth(); // imageView.getBoundsInParent().getWidth();
        //this.h = image.getHeight(); // imageView.getBoundsInParent().getHeight();

        if (!animated)
        	addToLayer();
        
    }

    public void addToLayer() {
        this.layer.getChildren().add(this.imageView);
    }

    public void removeFromLayer() {
        this.layer.getChildren().remove(this.imageView);
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void move() {
        if( !canMove)
            return;
        px = x;
        py = y;
        x += dx;
        y += dy;
    }

    public ImageView getView() {
        return imageView;
    }

    public void update() {
        imageView.relocate(x, y);
    }
    
    public void remove() {
        setRemovable(true);
        removeFromLayer();
    }
    
    public void setCanMove(boolean canMove) {
    	this.canMove = canMove;
    }

    public abstract void checkRemovability();
    public abstract void handleCollision(SpriteBase other);

}