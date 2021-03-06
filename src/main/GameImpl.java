package main;

import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import javafx.geometry.Bounds;

import java.util.*;

public class GameImpl extends Pane implements Game {
	/**
	 * Defines different states of the game.
	 */
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}

	// Constants
	/**
	 * The width of the game board.
	 */
	public static final int WIDTH = 400;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 600;
	
	private final int ENEMY_START_X = 50;
	private final int ENEMY_END_X = 350;
	private final int ENEMY_X_INCREASE = 100;
	private final int ENEMY_Y_INCREASE = 65;
	private final int ENEMY_START_Y = 50;
	private final int ENEMY_END_Y = 245;
	
	private final Bounds _topWall;
	private final Bounds _bottomWall;
	private final Bounds _leftWall;
	private final Bounds _rightWall;
	private final double BOUND_WIDTH = 0.5;

	// Instance variables
	private Ball _ball;
	private Paddle _paddle;
	private boolean _canMove;
	private List<Animal> _enemies;
	private int _numLives;

	/**
	 * Constructs a new GameImpl.
	 */
	public GameImpl () {
		setStyle("-fx-background-color: white;");
		_topWall = new Rectangle(0 ,-BOUND_WIDTH, WIDTH, 0).getBoundsInLocal();
		_bottomWall = new Rectangle(0, HEIGHT, WIDTH, HEIGHT+BOUND_WIDTH).getBoundsInLocal();
		_leftWall = new Rectangle(-BOUND_WIDTH, 0, 0, HEIGHT).getBoundsInLocal();
		_rightWall = new Rectangle(WIDTH, 0, WIDTH+BOUND_WIDTH, HEIGHT).getBoundsInLocal();
		restartGame(GameState.NEW);
	}

	public String getName () {
		return "Racketopia";
	}

	public Pane getPane () {
		return this;
	}

	private void restartGame (GameState state) {
		getChildren().clear();  // remove all components from the game

		// Create and add ball
		_ball = new Ball();
		getChildren().add(_ball.getImage());  // Add the ball to the game board

		// Create and add paddle
		_paddle = new Paddle();
		getChildren().add(_paddle.getImage());  // Add the paddle to the game board
		
		//Create and add animals
		_enemies = new ArrayList<Animal>();
		spawnAnimals();
		
		//set max number of lives
		_numLives = 5;
		
		//boolean that allows paddle to move, set to true when mouse is clicked.
		_canMove = false;

		// Add start message
		final String message;
		if (state == GameState.LOST) {
			message = "Game Over\n";
		} else if (state == GameState.WON) {
			message = "You won!\n";
		} else {
			message = "";
		}
		final Label startLabel = new Label(message + "Click mouse to start");
		startLabel.setLayoutX(WIDTH / 2 - 50);
		startLabel.setLayoutY(HEIGHT / 2 + 100);
		getChildren().add(startLabel);

		// Add event handler to start the game
		setOnMouseClicked(new EventHandler<MouseEvent> () {
			@Override
			public void handle (MouseEvent e) {
				GameImpl.this.setOnMouseClicked(null);
				_canMove = true;
				// As soon as the mouse is clicked, remove the startLabel from the game board
				getChildren().remove(startLabel);
				run();
			}
		});
		setOnMouseMoved(new EventHandler<MouseEvent> () {
			public void handle(MouseEvent e) {
				//if _canMove is true, move paddle.
				if(_canMove) _paddle.moveTo(e.getSceneX(), e.getSceneY());
			}	
		});
	}

	/**
	 * Begins the game-play by creating and starting an AnimationTimer.
	 */
	public void run () {
		// Instantiate and start an AnimationTimer to update the component of the game.
		new AnimationTimer () {
			private long lastNanoTime = -1;
			public void handle (long currentNanoTime) {
				if (lastNanoTime >= 0) {  // Necessary for first clock-tick.
					GameState state;
					if ((state = runOneTimestep(currentNanoTime - lastNanoTime)) != GameState.ACTIVE) {
						// Once the game is no longer ACTIVE, stop the AnimationTimer.
						stop();
						// Restart the game, with a message that depends on whether
						// the user won or lost the game.
						restartGame(state);
					}
				}
				// Keep track of how much time actually transpired since the last clock-tick.
				lastNanoTime = currentNanoTime;
			}
		}.start();
	}

	/**
	 * Updates the state of the game at each timestep. In particular, this method should
	 * move the ball, check if the ball collided with any of the animals, walls, or the paddle, etc.
	 * @param deltaNanoTime how much time (in nanoseconds) has transpired since the last update
	 * @return the current game state
	 */
	public GameState runOneTimestep (long deltaNanoTime) {
		if(_ball.hasCollided(_leftWall)) {
			_ball.negateX();
		}
		else if (_ball.hasCollided(_rightWall)) _ball.negateX();
		else if (_ball.hasCollided(_topWall)) _ball.negateY();
		else if (_ball.hasCollided(_bottomWall)) {
			_ball.negateY();
			_numLives--; //special collision case, if ball collides w/ bottom wall then remove a life.
		}
		else if (_ball.hasCollided(_paddle.getBoundingBox())) {
			_ball.negateY();
		}
		if(_enemies.size() == 0) return GameState.WON; //if no enemies left, has won
		for(int i = 0; i < _enemies.size(); i++) {
			if(_enemies.get(i).hasCollided(_ball)) {
				_ball.increaseSpeed();
				getChildren().remove(_enemies.get(i).getImage());
				_enemies.remove(i);
				i--;
			}
		}
		_ball.updatePosition(deltaNanoTime);
		if(_numLives <= 0) {
			return GameState.LOST;
		}
		return GameState.ACTIVE;
	}
	
	/**
	 * Function to spawn all of the animals onto the game board.
	 */
	private void spawnAnimals() {
		List<String> imageFilePaths = new ArrayList<String>(); //file paths for the images
		imageFilePaths.add("car.jpg");
		imageFilePaths.add("gregor.jpg");
		imageFilePaths.add("cow.jpg");
		imageFilePaths.add("thunk.jpg");
		
		List<String> audioFilePaths = new ArrayList<String>(); //file paths for the audio, same size as image list
		audioFilePaths.add("C:/Users/Ravi/Documents/WPICS/CS2103-Project-4/src/main/bleat.wav");
		audioFilePaths.add("C:/Users/Ravi/Documents/WPICS/CS2103-Project-4/src/main/bleat.wav");
		audioFilePaths.add("C:/Users/Ravi/Documents/WPICS/CS2103-Project-4/src/main/bleat.wav");
		audioFilePaths.add("C:/Users/Ravi/Documents/WPICS/CS2103-Project-4/src/main/bleat.wav"); 
		//could not get audio to work, it is nonfunctional currently
		
		for(int y = ENEMY_START_Y; y <= ENEMY_END_Y; y+=ENEMY_Y_INCREASE) {
			for(int x = ENEMY_START_X; x <= ENEMY_END_X; x+=ENEMY_X_INCREASE) {
				int index = (int) (Math.random()*imageFilePaths.size()); //chooses random image to put on the screen
				_enemies.add(new Animal(audioFilePaths.get(index), imageFilePaths.get(index), x,y));
			}
		}
		for(int i = 0; i < _enemies.size(); i++) {
			getChildren().add(_enemies.get(i).getImage()); //adds everything to the game panel.
		}
	}
}
