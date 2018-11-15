package main;

import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
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
		restartGame(GameState.NEW);
	}

	public String getName () {
		return "Zutopia";
	}

	public Pane getPane () {
		return this;
	}

	private void restartGame (GameState state) {
		getChildren().clear();  // remove all components from the game

		// Create and add ball
		_ball = new Ball();
		getChildren().add(_ball.getCircle());  // Add the ball to the game board

		// Create and add animals ...

		// Create and add paddle
		_paddle = new Paddle();
		getChildren().add(_paddle.getRectangle());  // Add the paddle to the game board
		
		_enemies = new ArrayList<Animal>();
		
		_numLives = 5;
		
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
		if(_ball.getX()-Ball.BALL_RADIUS < 0) {
			_ball.negateX();
		}
		else if (_ball.getX()+Ball.BALL_RADIUS > WIDTH) _ball.negateX();
		else if (_ball.getY()-Ball.BALL_RADIUS < 0) _ball.negateY();
		else if (_ball.getY()+Ball.BALL_RADIUS > HEIGHT) {
			_ball.negateY();
			_numLives--;
		}
		else if (_ball.getBoundingBox().intersects(_paddle.getRectangle().getBoundsInParent())) {
			_ball.negateY();
		}
		_ball.updatePosition(deltaNanoTime);
		if(_numLives <= 0) {
			restartGame(GameState.LOST);
			return GameState.LOST;
		}
		return GameState.ACTIVE;
	}
}
