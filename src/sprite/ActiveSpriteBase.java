package sprite;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import main.Settings;

public abstract class ActiveSpriteBase extends SpriteBase {

    double health;
    double damage;
    
    int state;
    Animation[] animation;

    public ActiveSpriteBase(Pane layer, Image image, double x, double y, double dx, double dy, double health, double damage,
    					int[] duration, int[] count, double width, double height) {

        super(layer, image, x, y, dx, dy, width, height, true);
        this.health = health;
        this.damage = damage;

        //this.imageView = new ImageView(image);
        //this.imageView.relocate(x, y);

        //this.w = image.getWidth(); // imageView.getBoundsInParent().getWidth();
        //this.h = image.getHeight(); // imageView.getBoundsInParent().getHeight();

        addToLayer();
        
        animation = new SpriteAnimation[count.length];
        
        this.state = 0;
        for (int i=0; i<count.length; i++) {
        	animation[i] = new SpriteAnimation(
               	imageView,
               	Duration.millis(duration[i]),
               	count[i], i,
               	width, height
        	);
        	//System.out.println("i: " + i + " count: " + count[i]);
        }
        
    }
    
    public double getHealth() {
        return health;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public boolean isAlive() {
        return Double.compare(health, 0) > 0;
    }
    
    @Override
    public void update() {
    	super.update();
        if (animation[state].getStatus() != Animation.Status.RUNNING)
        	animation[state].play();
    }

    // TODO: per-pixel-collision
    public boolean collidesWith( ActiveSpriteBase otherSprite) {

        return ( otherSprite.x + otherSprite.w >= x && otherSprite.y + otherSprite.h >= y && otherSprite.x <= x + w && otherSprite.y <= y + h);

    }

    /**
     * Reduce health by the amount of damage that the given sprite can inflict
     * @param sprite
     */
    public void getDamagedBy( ActiveSpriteBase sprite) {
        health -= sprite.getDamage();
    }

    public abstract void death();

    /**
     * Set flag that the sprite can't move anymore.
     */
    public void stopMovement() {
        this.canMove = false;
    }

    public abstract void checkRemovability();
    
    public class SpriteAnimation extends Transition {
    	private final ImageView imageView;
        private final int count;
        private final double width;
        private final double height;
        private final int row;

        private int lastIndex = -1;

        public SpriteAnimation(
                ImageView imageView, 
                Duration duration, 
                int count, int row,
                double width,   double height) {
            this.imageView = imageView;
            this.count     = count;
            this.width     = width;
            this.height    = height;
            this.row	   = row;
            setCycleDuration(duration);
            setInterpolator(Interpolator.LINEAR);
        }
        
        public void play() {
        	lastIndex = -1;
        	super.play();
        }
        
        protected void interpolate(double k) {
        	
            final int index = Math.min((int) Math.floor(k * count), count - 1);
            if (index != lastIndex) {
                final double m = (index) * width;
                final double n = row * height;
                imageView.setViewport(new Rectangle2D(m, n, width, height));
                lastIndex = index;
            }
        }
    }
    
    public int getState() {
    	return state;
    }
    
    public void setState(int state) {
    	if (this.state != state) {
    		//System.out.println(this.state + " " + state);
    		animation[this.state].stop();
    		this.state = state;
    		animation[this.state].play();
    	}
    }
    
    Blend hurt = new Blend(
    	BlendMode.MULTIPLY,
    	new ColorAdjust(),
    	new ColorInput(
    			0,
                0,
                Settings.PLAYER_WIDTH,
                Settings.PLAYER_HEIGHT,
                Color.RED
            )
    );
    
    public abstract void handleCollision(SpriteBase other);

}