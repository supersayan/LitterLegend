package sprite;

import javafx.scene.layout.Pane;

public abstract class NodeBase {

	protected Pane layer;

    protected double x;
    protected double y;

    protected boolean removable = false;

    protected double w;
    protected double h;
	
	public NodeBase(Pane layer, double x, double y, double width, double height) {
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
		
	}

	public abstract void addToLayer();

    public abstract void removeFromLayer();

    public Pane getLayer() {
        return layer;
    }

    public void setLayer(Pane layer) {
        this.layer = layer;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getCenterX() {
        return x + w * 0.5;
    }

    public double getCenterY() {
        return y + h * 0.5;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }
    
    public abstract void update();

    public double getWidth() {
        return w;
    }

    public double getHeight() {
        return h;
    }

    // TODO: per-pixel-collision
    public boolean collidesWith( NodeBase otherSprite) {
        return ((otherSprite.getX() + otherSprite.getWidth()) > x && otherSprite.getX() < (x + w)
        			&& (otherSprite.getY() + otherSprite.getHeight()) > y && otherSprite.getY() < (y + h));
    }

    /**
     * Set flag that the sprite can be removed from the UI.
     */
    public void remove() {
        setRemovable(true);
        removeFromLayer();
    }

}
