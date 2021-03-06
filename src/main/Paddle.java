package main;

import java.awt.*;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle extends GameObject{
	// Constants
	/**
	 * The width of the paddle.
	 */
	public static final int PADDLE_WIDTH = 100;
	/**
	 * The height of the paddle.
	 */
	public static final int PADDLE_HEIGHT = 5;
	/**
	 * The initial position (specified as a fraction of the game height) of center of the paddle.
	 */
	public static final double INITIAL_Y_LOCATION_FRAC = 0.8;
	/**
	 * The minimum position (specified as a fraction of the game height) of center of the paddle.
	 */
	public static final double MIN_Y_LOCATION_FRAC = 0.7;
	/**
	 * The maximum position (specified as a fraction of the game height) of center of the paddle.
	 */
	public static final double MAX_Y_LOCATION_FRAC = 0.9;
	
	final private Label _image;

	// Instance variables
	private Rectangle rectangle;

	/**
	 * @return the x coordinate of the center of the paddle.
	 */
	public double getX () {
		return rectangle.getLayoutX() + rectangle.getTranslateX() + PADDLE_WIDTH/2;
	}

	/**
	 * @return the y coordinate of the center of the paddle.
	 */
	public double getY () {
		return rectangle.getLayoutY() + rectangle.getTranslateY() + PADDLE_HEIGHT/2;
	}

	public Bounds getBoundingBox() {
		return getRectangle().getBoundsInParent();
	}
	/**
	 * Constructs a new Paddle whose vertical center is at INITIAL_Y_LOCATION_FRAC * GameImpl.HEIGHT.
	 */
	public Paddle () {
		final double x = PADDLE_WIDTH/2;
		final double y = INITIAL_Y_LOCATION_FRAC * GameImpl.HEIGHT;

		rectangle = new Rectangle(0, 0, PADDLE_WIDTH, PADDLE_HEIGHT);
		rectangle.setLayoutX(x-PADDLE_WIDTH/2);
		rectangle.setLayoutY(y-PADDLE_HEIGHT/2);
		rectangle.setStroke(Color.GREEN);
		rectangle.setFill(Color.GREEN);
		
		Image image = new Image(getClass().getResourceAsStream("paddle.jpg"));
		_image = new Label("", new ImageView(image));
		_image.setLayoutX(getX() - image.getWidth()/2);
		_image.setLayoutY(getY() - image.getHeight()/2);
	}

	/**
	 * @return the Rectangle object that represents the paddle on the game board.
	 */
	public Rectangle getRectangle () {
		return rectangle;
	}
	
	public Label getImage() {
		return _image;
	}

	/**
	 * Moves the paddle so that its center is at (newX, newY), subject to
	 * the horizontal constraint that the paddle must always be completely visible
	 * and the vertical constraint that its y coordiante must be between MIN_Y_LOCATION_FRAC
	 * and MAX_Y_LOCATION_FRAC times the game height.
	 * @param newX the newX position to move the center of the paddle.
	 * @param newY the newX position to move the center of the paddle.
	 */
	public void moveTo (double newX, double newY) {
		if (newX < PADDLE_WIDTH/2) {
			newX = PADDLE_WIDTH/2;
		} else if (newX > GameImpl.WIDTH - PADDLE_WIDTH/2) {
			newX = GameImpl.WIDTH - PADDLE_WIDTH/2;
		}

		if (newY < MIN_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
			newY = MIN_Y_LOCATION_FRAC * GameImpl.HEIGHT;
		} else if (newY > MAX_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
			newY = MAX_Y_LOCATION_FRAC * GameImpl.HEIGHT;
		}

		rectangle.setTranslateX(newX - (rectangle.getLayoutX() + PADDLE_WIDTH/2));
		rectangle.setTranslateY(newY - (rectangle.getLayoutY() + PADDLE_HEIGHT/2));
		_image.setLayoutX(getX() - _image.getWidth()/2);
		_image.setLayoutY(getY() - _image.getHeight()/2);
	}
	
}
