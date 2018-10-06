package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import maze.Corner;
import maze.Door;
import maze.Fog;
import maze.Maze;
import maze.Wall;
import sprite.Bat;
import sprite.Nomster;
import sprite.Player;

/**
 * @author Sayan Dutta
 *
 */
public class Main extends Application {
	
	//private static final Logger LOG = Logger.getLogger(Main.class.getName());
	
	static int levelnum = 1;
	Level level;

	Pane menuLayer;
	Pane playLayer;
	Pane uiLayer;
	Pane debugLayer;
	ScrollPane camera;
	
    Image playerImage;
	Image cornerImage, wallVImage, wallHImage;
	Image batImage;
	static Player player;
	
	static Random rand = new Random();
	
	double cameraX, cameraY;
	
	private AnimationTimer gameLoop;
	//private AnimationTimer menuLoop;
	
	double offset = (Settings.SCENE_WIDTH-64-Settings.PLAYER_WIDTH/2);
	
	//private boolean gamePaused = false;
	
	ArrayList<String> input = new ArrayList<String>();
	
	List<Nomster> nomsters = new ArrayList<>();
	List<Wall> walls = new ArrayList<>();
	List<Corner> corners = new ArrayList<>();
	List<Fog> fog = new ArrayList<>();
	Door door;
	
	Maze maze;
	
	Scene scene;
	Group root;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
		
	}
	
	public void start(Stage stage) {
		root = new Group();
	    menuLayer = new Pane();
		playLayer = new Pane();
	    uiLayer = new Pane();
	    
	    
	    camera = new ScrollPane(playLayer);
	    camera.setHmin(0);
	    //camera.setHmax(64+1600*level);
	    camera.setVmin(0);
	    //camera.setVmax(64+1600*level-camera.getHeight());
	    camera.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	    camera.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	    camera.setPannable(false);
	    camera.setPrefSize(Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
	    
	    scene = new Scene(root);
	    
	    stage.initStyle(StageStyle.DECORATED);
	    stage.setWidth(Settings.SCENE_WIDTH);
	    stage.setHeight(Settings.SCENE_HEIGHT);
	    stage.setScene(scene);
		stage.setResizable(false);
        stage.show();
	    
        createGameLoop();
        root.getChildren().add(camera);
        //createMenuLoop();
        //root.getChildren().add(menuLayer);
        //menuLoop.start();
        
	    loadGame();
		
		gameLoop.start();

	}
	
	private void createGameLoop() {
		
		gameLoop = new AnimationTimer() {  
		
			@Override  
			public void handle(long l) {  
				
				//System.out.println("x: " + player.getX() + ", y: " + player.getY());

				handleKeys();
	 
	            moveSprites();
	            
	            checkCollisions();
	            
	            updateUI();
	            
	            updateCam();
	            
	            checkRemovables();
	            
		 	}
		 };
	}
	
	/*private void createMenuLoop() {
		menuLoop = new AnimationTimer() {  
			
			@Override  
			public void handle(long l) {
	            
		 	}
		 };
	}
	
	private void loadMenu() {
		
	}*/
	
	private void loadGame() {
        playerImage = new Image( getClass().getResource("/assets/link.png").toExternalForm());
        cornerImage = new Image( getClass().getResource("/assets/environment/corner.png").toExternalForm());
        wallVImage = new Image( getClass().getResource("/assets/environment/vertical.png").toExternalForm());
        wallHImage = new Image( getClass().getResource("/assets/environment/horizontal.png").toExternalForm());
        batImage = new Image(getClass().getResource("/assets/nomsters/bat.png").toExternalForm());
        
        playLayer.setStyle("-fx-background-image: url('assets/environment/background.png');");
		
        loadLevel();
    }
	
	private void loadLevel() {
		
        int startIndex = rand.nextInt(5*levelnum);
        int endIndex = rand.nextInt(5*levelnum);
        
        player = new Player(playLayer, playerImage, (192)+offset, (192+startIndex*320)+offset, Settings.PLAYER_HEALTH, 0, Settings.PLAYER_SPEED);
		maze = new Maze(levelnum*5);
		
        playLayer.setMinSize(64+1600*levelnum+2*offset, 64+1600*levelnum+2*offset);
        playLayer.setMaxSize(64+1600*levelnum+2*offset, 64+1600*levelnum+2*offset);

	    camera.setHmax(64+1600*levelnum+2*offset-Settings.SCENE_WIDTH);
	    camera.setVmax(64+1600*levelnum+2*offset-Settings.SCENE_HEIGHT);

        door = new Door(playLayer, 320*(levelnum*5-1)+142+offset, 320*(endIndex)+142+offset);
        
        for (int i=0; i<5*levelnum+1; i++) {
        	for (int j=0; j<5*levelnum+1; j++) { //add corners
        		corners.add(new Corner(playLayer, cornerImage, 320*i+offset, 320*j+offset));
        	}
        }
        for (int i=0; i<5*levelnum; i++) { //add top row
        	walls.add(new Wall(playLayer, wallHImage, 64+320*i+offset, 0+offset, true));
        }
        for (int i=0; i<(5*levelnum); i++) { //add other walls
        	walls.add(new Wall(playLayer, wallVImage, 0+offset, 64+320*i+offset, false));
        	for (int j=0; j<5*levelnum; j++) {
        		if (!(maze.get(i, j) == 0 && i+1 < 5*levelnum && maze.get(i+1, j) == 0) && ((maze.get(i, j) & 2) == 0))
        			walls.add(new Wall(playLayer, wallHImage, 64+320*j+offset, 320*(i+1)+offset, true));
        		
				if ((maze.get(i, j) == 0 && j+1 < (5*levelnum) && maze.get(i, j+1) == 0)) {
					if ((i+1 < 5*levelnum) && (maze.get(i+1, j) == 0 || maze.get(i+1, j+1) == 0))
						walls.add(new Wall(playLayer, wallHImage, 64+320*j+offset, 320*(i+1)+offset, true));
				} else if ((maze.get(i, j) & 4) != 0) {
					if (((maze.get(i, j) | maze.get(i, j+1)) & 2) == 0)
						walls.add(new Wall(playLayer, wallHImage, 64+320*j+offset, 320*(i+1)+offset, true));
				} else
					walls.add(new Wall(playLayer, wallVImage, 320*(j+1)+offset, 64+320*i+offset, false));
            }
        }
        for (int i=0; i<levelnum*5; i++) {
        	for (int j=0; j<levelnum*5; j++) {
        		if (rand.nextDouble()<Settings.NOM1_CHANCE)
        			nomsters.add(new Bat(playLayer, batImage, (320*i)+192-(45/2)+offset, (320*j)+192-(48/2)+offset, (rand.nextDouble()>0.5)?4:-4, (rand.nextDouble()>0.5)?4:-4, 1, 45, 48));
        	}
        }
        
        //nomsters.add(new Bat(playLayer, batImage, 1600*level-192, (192+endIndex*320), (rand.nextDouble()>0.5)?6:-6, (rand.nextDouble()>0.5)?6:-6, 1, 45, 48));
        //nomsters.add(new Bat(playLayer, batImage, 192, (192+startIndex*320), (rand.nextDouble()>0.5)?6:-6, (rand.nextDouble()>0.5)?6:-6, 1, 45, 48));
        
        for (int i=0; i<(5*levelnum); i++) { //add fog
        	for (int j=0; j<(5*levelnum); j++) {
        		fog.add(new Fog(playLayer, 320*(j)+64+offset, 320*(i)+64+offset, 256, 256));
        	}
        }
        for (int i=0; i<5*levelnum+1; i++) { //corner fog
        	for (int j=0; j<5*levelnum+1; j++) {
        		fog.add(new Fog(playLayer, 320*j+offset, 320*i+offset, 64, 64));
        	}
        }
        for (int i=0; i<5*levelnum+1; i++) { //column fog
        	for (int j=0; j<5*levelnum+2; j++) {
        		fog.add(new Fog(playLayer, 320*j+offset, 64+320*i+offset, 64, 256));
        	}
        }
        for (int i=0; i<(5*levelnum)+1; i++) { //row fog
        	for (int j=0; j<(5*levelnum)+2; j++) {
        		fog.add(new Fog(playLayer, 320*(j)+64+offset, 320*i+offset, 256, 64));
        	}
        }

        fog.add(new Fog(playLayer, 0, 0, 2*offset+64+1600*levelnum, offset));
        fog.add(new Fog(playLayer, 0, offset, offset, 64+1600*levelnum));
        fog.add(new Fog(playLayer, offset+64+1600*levelnum, offset, offset, 64+1600*levelnum));
        fog.add(new Fog(playLayer, 0, offset+64+1600*levelnum, 2*offset+64+1600*levelnum, offset));
        
	}

	private void clearLevel() {
		playLayer.getChildren().clear();
		for (Nomster nom : nomsters) {
			nom.remove();
			//nomsters.remove(nom);
			//nom = null;
		}nomsters.clear();
		for (Wall w : walls) {
			w.remove();
			//walls.remove(w);
			//w = null;
		}walls.clear();
		for (Corner c : corners) {
			c.remove();
			//corners.remove(c); JAR -CFMV NAME-OF-JAR.JAR MANIFEST.TXT *.CLASS
			//c = null;
		}corners.clear();
		for (Fog f : fog) {
			f.remove();
			//fog.remove(f);
			//f = null;
		}fog.clear();
		//door = null;
	}
	
	private void moveSprites() {
		player.move();
		for(Nomster nom : nomsters) {
			nom.move();
		}
	}
	
	private void handleKeys() {
		scene.setOnKeyPressed(
	    	new EventHandler<KeyEvent>() {
	        	public void handle(KeyEvent e) {
	            	String code = e.getCode().toString();
	            	// only add once... prevent duplicates
	                if ( !input.contains(code) )
	                	input.add( code );
	            }
	        }
	    );
	 
	    scene.setOnKeyReleased(
	    	new EventHandler<KeyEvent>() {
	        	public void handle(KeyEvent e) {
	            	String code = e.getCode().toString();
	            	input.remove( code );
	        	}
	    	}
	    );
	    
	    player.handleInput(input);
	    
        
	}
	
	private void updateUI() {
		player.updateUI();
		for (Wall w : walls)
			w.updateUI();
		for (Corner c : corners)
			c.updateUI();
        for (Nomster n : nomsters)
        	n.updateUI();
	}
	
	private void checkRemovables() {
		for (Wall w : walls)
			if (w.isRemovable()) {
				walls.remove(w);
				w = null;
			}
		for (Corner c : corners)
			if (c.isRemovable()) {
				corners.remove(c);
				c = null;
			}
		for (Fog f : fog)
			if (f.isRemovable()) {
				fog.remove(f);
				f = null;
			}
		for (Nomster nom : nomsters)
			if (nom.isRemovable()) {
				nom.remove();
				nom = null;
			}
	}
	
	private void updateCam() {
	    
		cameraX = player.getX()-Settings.SCENE_WIDTH/2+Settings.PLAYER_WIDTH/2;
		cameraY = player.getY()-Settings.SCENE_HEIGHT/2+Settings.PLAYER_HEIGHT/2;
	    //cameraX = ((player.getX() )-64)*(camera.getHmax()/(camera.getHmax()-128-Settings.PLAYER_WIDTH-16));
		//cameraY = ((player.getY() )-64)*(camera.getVmax()/(camera.getVmax()-128-Settings.PLAYER_HEIGHT-16));
		//System.out.println("player: " + player.getX() + " " + player.getY() + " " + playLayer.getWidth() + " " + playLayer.getHeight());
		//System.out.println("camera: " + cameraX + " " + cameraY + " " + camera.getHmax() + " " + camera.getVmax());
		
		
		if (cameraX < 0)
			cameraX = 0;
		if (cameraX > (camera.getHmax()))
			cameraX = (camera.getHmax());
		
		if (cameraY < 0)
			cameraY = 0;
		if (cameraY > (camera.getVmax()))
			cameraY = (camera.getVmax());
		
		//System.out.println(cameraX + " " + cameraY);
		
		camera.setHvalue(cameraX);
		camera.setVvalue(cameraY);
		
	}
	
	private void checkCollisions() {
        
        for( Wall w: walls) {  
        	if( player.collidesWith(w)) {
        		player.handleCollision(w);
            }
        	for (Nomster nom: nomsters) {
        		if( nom.collidesWith(w)) {
            		nom.handleCollision(w);
                }
        	}
        }
        
        for( Corner c: corners) {  
        	if( player.collidesWith(c)) {
        		player.handleCollision(c);
            }
        	for (Nomster nom: nomsters) {
        		if( nom.collidesWith(c)) {
            		nom.handleCollision(c);
                }
        	}
        }
        
        for( Nomster nom: nomsters) {  
        	if( player.collidesWith(nom)) {  
        		player.handleCollision(nom);
        		nom.handleCollision(player);
            }
        	for (Nomster nom2: nomsters) {
        		if( nom2.collidesWith(nom)) {
            		nom2.handleCollision(nom);
            		nom.handleCollision(nom2);
                }
        	}
        }
        
        if (player.collidesWith(door)) {
        	try {
				levelUp();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
    }
	
	private void levelUp() throws InterruptedException {
		levelnum++;
		player.setCanMove(false);
    	//gameLoop.wait(5000);
    	clearLevel();
    	//gameLoop.start();
    	//loadLevel();
    	player.setCanMove(true);
	}
	
	/*private void pauseGame() {
		  gamePaused = true;  
		  gameLoop.stop(); 
	}
	
	private void resumeGame() {  
		gamePaused = false;  
		gameLoop.start();  
	}
	
	private void stopGame() throws Exception {
		stop();
	}*/
	
}
