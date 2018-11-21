package main;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Class that implements a ball with a position and velocity.
 */
public class Ball extends GameObject {
	// Constants
	/**
	 * The radius of the ball.
	 */
	public static final int BALL_RADIUS = 8;
	/**
	 * The initial velocity of the ball in the x direction.
	 */
	public static final double INITIAL_VX = 1e-7;
	/**
	 * The initial velocity of the ball in the y direction.
	 */
	public static final double INITIAL_VY = 1e-7;

	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double x, y;
	private double vx, vy;
	private Circle circle;
	final private Label _image;

	/**
	 * @return the Circle object that represents the ball on the game board.
	 */
	public Circle getCircle () {
		return circle;
	}
	
	public Label getImage() {
		return _image;
	}
	
	public Bounds getBoundingBox() {
		return new Rectangle(x-BALL_RADIUS, y-BALL_RADIUS, BALL_RADIUS*2, BALL_RADIUS*2).getBoundsInParent();
	}
	/**
	 * Constructs a new Ball object at the centroid of the game board
	 * with a default velocity that points down and right.
	 */
	public Ball () {
		x = GameImpl.WIDTH/2;
		y = GameImpl.HEIGHT/2;
		vx = INITIAL_VX;
		vy = INITIAL_VY;

		circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
		circle.setLayoutX(x - BALL_RADIUS);
		circle.setLayoutY(y - BALL_RADIUS);
		circle.setFill(Color.BLACK);
		
		Image image = new Image(getClass().getResourceAsStream("ball.jpg"));
		_image = new Label("", new ImageView(image));
		_image.setLayoutX(x - image.getWidth()/2);
		_image.setLayoutY(y - image.getHeight()/2);
	}

	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 */
	public void updatePosition (long deltaNanoTime) {
		final double dx = vx * deltaNanoTime;
		final double dy = vy * deltaNanoTime;
		x += dx;
		y += dy;
		
		circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
		circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));
		_image.setLayoutX(x - _image.getWidth()/2);
		_image.setLayoutY(y - _image.getHeight()/2);
	}
	
	public void negateX() {
		vx*=-1;
		x+=Math.signum(vx)*2;
	}
	
	public void negateY() {
		vy*=-1;
		y+=Math.signum(vy)*2;
	}
	
	public void increaseSpeed() {
		vx*=1.05;
		vy*=1.05;
	}
	
	public boolean hasCollided(Bounds bound) {
		return super.hasCollided(getBoundingBox(), bound);
	}
}


