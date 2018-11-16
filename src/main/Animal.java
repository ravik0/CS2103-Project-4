package main;


import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;

/**
 * Class containing the functionality for all the animal enemies in the game 
 * @author Ravi
 *
 */
public class Animal {
	final private AudioClip _deathSound;
	final private Label _image;
	final private double _x, _y; //center of image
	
	public Bounds getBoundingBox() {
		double width = _image.getWidth();
		double height = _image.getHeight();
		return new Rectangle((int)(_x-width/2),(int)(_y-height/2), (int)width, (int)height).getBoundsInLocal();
	}
	
	public Animal(Image img, double posX, double posY) {
		_deathSound = null;
		_image = new Label("", new ImageView(img));
		_x = posX;
		_y = posY; 
		_image.setLayoutX(_x - img.getWidth()/2);
		_image.setLayoutY(_y - img.getHeight()/2);
	}
	
	public Label getImage() {
		return _image;
	}
	
	public String hasCollided(Ball ball) {
		Bounds b = getBoundingBox();
		Rectangle topEdge = new Rectangle((int)b.getMinX(), (int)b.getMinY(), (int)b.getWidth(), 2);
		Rectangle bottomEdge = new Rectangle((int)b.getMinX(), (int)b.getMaxY(), (int)b.getWidth(), 2);
		Rectangle leftEdge = new Rectangle((int)b.getMinX(), (int)b.getMinY(), 2, (int)b.getHeight());
		Rectangle rightEdge = new Rectangle((int)b.getMaxX(), (int)b.getMinY(), 2, (int)b.getHeight());
		if(ball.getBoundingBox().intersects(topEdge.getBoundsInLocal())) {
			ball.negateY();
			return "top";
		}
		if(ball.getBoundingBox().intersects(bottomEdge.getBoundsInLocal())) {
			ball.negateY();
			return "bottom";
		}
		if(ball.getBoundingBox().intersects(leftEdge.getBoundsInLocal())) {
			ball.negateX();
			return "left";
		}
		if(ball.getBoundingBox().intersects(rightEdge.getBoundsInLocal())) {
			ball.negateX();
			return "right";
		}
		return "";
	}
}
