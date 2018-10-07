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
import maze.Fog;
import maze.Maze;
import maze.Wall;
import sprite.TrashLitter;
import sprite.Bin;
import sprite.Litter;
import sprite.Player;
import sprite.TrashBin;

/**
 * @author Sayan Dutta
 *
 */
public class Main extends Application {
	
	//private static final Logger LOG = Logger.getLogger(Main.class.getName());
	
	static int levelnum;
	static int playerlives;
	//Level level;

	Pane menuLayer;
	Pane playLayer;
	Pane uiLayer;
	Pane debugLayer;
	ScrollPane camera;
	
    Image playerImage;
	Image cornerImage, wallVImage, wallHImage;
	Image trashBinImage, recycleBinImage;
	Image trashLitterImage;
	static Player player;
	
	static Random rand = new Random();
	
	double cameraX, cameraY;
	
	private AnimationTimer gameLoop;
	//private AnimationTimer menuLoop;
	
	//offset variable for location of player relative to screen
	double offset = (Settings.SCENE_WIDTH-64-Settings.PLAYER_WIDTH/2);
	
	//private boolean gamePaused = false;
	
	ArrayList<String> input = new ArrayList<String>();
	
	List<Litter> litters = new ArrayList<>();
	List<Wall> walls = new ArrayList<>();
	List<Corner> corners = new ArrayList<>();
	List<Fog> fog = new ArrayList<>();
	List<Bin> bin = new ArrayList<>();
	
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
		root = new Group(); //group containing main game layers (camera, which is a scrollPane of playLayer, and UI)
	    menuLayer = new Pane(); //unimplemented, nothing actually here
		playLayer = new Pane(); // pane containing sprites, environment
	    uiLayer = new Pane(); //unimplemented, nothing actually here
	    
	    
	    camera = new ScrollPane(playLayer); //pane that you view from, contains all of playLayer and views a part of it
	    camera.setHmin(0);
	    //camera.setHmax(64+1600*level);
	    camera.setVmin(0);
	    //camera.setVmax(64+1600*level-camera.getHeight());
	    camera.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	    camera.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // no scrollbars
	    camera.setPannable(false);
	    camera.setPrefSize(Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT); //set size of view pane
	    
	    scene = new Scene(root); //add root to scene
	    
	    stage.initStyle(StageStyle.DECORATED);
	    stage.setWidth(Settings.SCENE_WIDTH);
	    stage.setHeight(Settings.SCENE_HEIGHT); //set size of app
	    stage.setScene(scene); //add scene to stage
		stage.setResizable(false);
        stage.show(); // show everything
	    
        createGameLoop(); //create AnimationTimer class "gameLoop" which constantly runs while you're playing
        root.getChildren().addAll(camera, uiLayer); //add camera and UI to root
        //createMenuLoop();
        //root.getChildren().add(menuLayer);
        //menuLoop.start();
        
	    loadGame(); //load game images and maze
		
		gameLoop.start();

	}
	
	private void createGameLoop() {
		
		gameLoop = new AnimationTimer() { 
		
			@Override  
			public void handle(long l) {  
				
				//System.out.println("x: " + player.getX() + ", y: " + player.getY());

				handleKeys(); //handle input keys
	 
	            moveSprites(); //
	            
	            checkCollisions(); //check for collisions between sprites or nodes
	            
	            updateObjects(); //
	            
	            updateCam(); //
	            
	            checkRemovables(); //removes all objects that are set to removable
	            
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
		//put images in variables
        playerImage = new Image( getClass().getResource("/assets/link.png").toExternalForm());
        cornerImage = new Image( getClass().getResource("/assets/environment/corner.png").toExternalForm());
        wallVImage = new Image( getClass().getResource("/assets/environment/vertical.png").toExternalForm());
        wallHImage = new Image( getClass().getResource("/assets/environment/horizontal.png").toExternalForm());
        trashBinImage = new Image(getClass().getResource("/assets/nomsters/bat.png").toExternalForm());
        //recycleBinImage = new Image(getClass().getResource("/assets/").toExternalForm());
        trashLitterImage = new Image(getClass().getResource("/assets/nomsters/bat.png").toExternalForm());
        //recycleLitterImage = new Image(getClass().getResource("/assets/").toExternalForm());
        
        playLayer.setStyle("-fx-background-image: url('assets/environment/background.png');");
		
        levelnum = 1;
        playerlives = 3;
        
        loadLevel();
    }
	
	private void loadLevel() {
		int mazeSize = Settings.LEVEL_SIZE_START + (levelnum * Settings.LEVEL_SIZE_ADD); //calculate size of level
		
        int startIndex = rand.nextInt(mazeSize);
        int endIndex = rand.nextInt(mazeSize);
        
        player = new Player(playLayer, playerImage, (192)+offset, (192+startIndex*320)+offset, Settings.PLAYER_HEALTH, 0, Settings.PLAYER_SPEED);
		maze = new Maze(mazeSize);
		
        playLayer.setMinSize(64+320*(mazeSize)+2*offset, 64+320*mazeSize+2*offset);
        playLayer.setMaxSize(64+320*(mazeSize)+2*offset, 64+320*mazeSize+2*offset);

	    camera.setHmax(64+320*(mazeSize)+2*offset-Settings.SCENE_WIDTH);
	    camera.setVmax(64+320*(mazeSize)+2*offset-Settings.SCENE_HEIGHT);

        bin.add(new TrashBin(playLayer, trashBinImage, 320*(mazeSize-1)+142+offset, 320*(endIndex)+142+offset, 0, 0, Settings.BIN_WIDTH, Settings.BIN_HEIGHT));
        
        for (int i=0; i<mazeSize+1; i++) {
        	for (int j=0; j<mazeSize+1; j++) { //add corners
        		corners.add(new Corner(playLayer, cornerImage, 320*i+offset, 320*j+offset));
        	}
        }
        for (int i=0; i<mazeSize; i++) { //add top row
        	walls.add(new Wall(playLayer, wallHImage, 64+320*i+offset, 0+offset, true));
        }
        for (int i=0; i<(mazeSize); i++) { //add other walls
        	walls.add(new Wall(playLayer, wallVImage, 0+offset, 64+320*i+offset, false));
        	for (int j=0; j<mazeSize; j++) {
        		if (!(maze.get(i, j) == 0 && i+1 < 5*levelnum && maze.get(i+1, j) == 0) && ((maze.get(i, j) & 2) == 0))
        			walls.add(new Wall(playLayer, wallHImage, 64+320*j+offset, 320*(i+1)+offset, true));
        		
				if ((maze.get(i, j) == 0 && j+1 < (5*levelnum) && maze.get(i, j+1) == 0)) {
					if ((i+1 < mazeSize) && (maze.get(i+1, j) == 0 || maze.get(i+1, j+1) == 0))
						walls.add(new Wall(playLayer, wallHImage, 64+320*j+offset, 320*(i+1)+offset, true));
				} else if ((maze.get(i, j) & 4) != 0) {
					if (((maze.get(i, j) | maze.get(i, j+1)) & 2) == 0)
						walls.add(new Wall(playLayer, wallHImage, 64+320*j+offset, 320*(i+1)+offset, true));
				} else
					walls.add(new Wall(playLayer, wallVImage, 320*(j+1)+offset, 64+320*i+offset, false));
            }
        }
        for (int i=0; i<mazeSize; i++) {
        	for (int j=0; j<mazeSize; j++) {
        		if (rand.nextDouble()<Settings.NOM1_CHANCE)
        			litters.add(new TrashLitter(playLayer, trashLitterImage, (320*i)+192-(45/2)+offset, (320*j)+192-(48/2)+offset, 0, 0, 1, 45, 48));
        	}
        }
        
        //nomsters.add(new Bat(playLayer, batImage, 1600*level-192, (192+endIndex*320), (rand.nextDouble()>0.5)?6:-6, (rand.nextDouble()>0.5)?6:-6, 1, 45, 48));
        //nomsters.add(new Bat(playLayer, batImage, 192, (192+startIndex*320), (rand.nextDouble()>0.5)?6:-6, (rand.nextDouble()>0.5)?6:-6, 1, 45, 48));
        
        /*for (int i=0; i<(5*levelnum); i++) { //add fog (low opacity black rectangles)
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
        }*/

        //add fog outside play area:
        fog.add(new Fog(playLayer, 0, 0, 2*offset+64+320*(mazeSize), offset));
        fog.add(new Fog(playLayer, 0, offset, offset, 64+320*(mazeSize)));
        fog.add(new Fog(playLayer, offset+64+320*(mazeSize), offset, offset, 64+320*(mazeSize)));
        fog.add(new Fog(playLayer, 0, offset+64+320*(mazeSize), 2*offset+64+320*(mazeSize), offset));
        
	}

	private void clearLevel() { //clear map
		playLayer.getChildren().clear();
		for (Litter nom : litters) {
			nom.remove();
			//nomsters.remove(nom);
			//nom = null;
		}litters.clear();
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
	
	private void moveSprites() { //move player and nomsters
		player.move();
		for(Litter nom : litters) {
			nom.move();
		}
	}
	
	private void handleKeys() { //
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
	
	private void updateObjects() { 
		player.update();
		for (Wall w : walls)
			w.update();
		for (Corner c : corners)
			c.update();
        for (Litter n : litters)
        	n.update();
	}
	
	private void checkRemovables() { //removes objects that are set to removable
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
		for (Litter nom : litters)
			if (nom.isRemovable()) {
				nom.remove();
				nom = null;
			}
	}
	
	private void updateCam() {
	    // set camera coordinates based on player position
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
        // if sprites collide delegate to their respective objects
        for( Wall w: walls) {  
        	if( player.collidesWith(w)) {
        		player.handleCollision(w);
            }
        	for (Litter nom: litters) {
        		if( nom.collidesWith(w)) {
            		nom.handleCollision(w);
                }
        	}
        }
        
        for( Corner c: corners) {  
        	if( player.collidesWith(c)) {
        		player.handleCollision(c);
            }
        	for (Litter nom: litters) {
        		if( nom.collidesWith(c)) {
            		nom.handleCollision(c);
                }
        	}
        }
        
        for( Litter nom: litters) {  
        	if( player.collidesWith(nom)) {  
        		player.handleCollision(nom);
        		nom.handleCollision(player);
            }
        	for (Litter nom2: litters) {
        		if( nom2.collidesWith(nom)) {
            		nom2.handleCollision(nom);
            		nom.handleCollision(nom2);
                }
        	}
        }
        
        for( Fog f : fog) {  
        	if( player.collidesWith(f)) {  
        		f.remove();
            }
        }
        
        for (Bin b : bin) {
        	if (player.collidesWith(b)) {
            	/*try {
    				levelUp();
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}*/
            }
        }
        
        
    }
	
	private void levelUp() throws InterruptedException {
		//increase level number, clear current level, and move to next
		levelnum++;
		player.setCanMove(false);
    	//gameLoop.wait(5000);
    	clearLevel();
    	gameLoop.start();
    	loadLevel();
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
