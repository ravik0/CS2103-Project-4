package main;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.*;
import javafx.scene.shape.Rectangle;

/**
 * Class containing the functionality for all the animal enemies in the game 
 * @author Ravi
 *
 */
public class Animal extends GameObject {
	final private AudioClip _deathSound;
	final private Label _image;
	final private double _x, _y; //center of image
	
	public Bounds getBoundingBox() {
		double width = _image.getWidth();
		double height = _image.getHeight();
		return new Rectangle((int)(_x-width/2),(int)(_y-height/2), (int)width, (int)height).getBoundsInLocal();
	}
	
	public Animal(String audioClip, String img, double posX, double posY) {
		//_deathSound = new AudioClip(getClass().getClassLoader().getResource(audioClip).toString());
		_deathSound = null;
		Image image = new Image(getClass().getResourceAsStream(img));
		_image = new Label("", new ImageView(image));
		_x = posX;
		_y = posY; 
		_image.setLayoutX(_x - image.getWidth()/2);
		_image.setLayoutY(_y - image.getHeight()/2);
	}
	
	public Label getImage() {
		return _image;
	}
	
	public boolean hasCollided(Ball ball) {
		Bounds b = getBoundingBox();
		Bounds topEdge = new Rectangle((int)b.getMinX(), (int)b.getMinY(), (int)b.getWidth(), 2).getBoundsInLocal();
		Bounds bottomEdge = new Rectangle((int)b.getMinX(), (int)b.getMaxY(), (int)b.getWidth(), 2).getBoundsInLocal();
		Bounds leftEdge = new Rectangle((int)b.getMinX(), (int)b.getMinY(), 2, (int)b.getHeight()).getBoundsInLocal();
		Bounds rightEdge = new Rectangle((int)b.getMaxX(), (int)b.getMinY(), 2, (int)b.getHeight()).getBoundsInLocal();
		Bounds theBall = ball.getBoundingBox();
		if(super.hasCollided(theBall, topEdge)) {
			ball.negateY();
			//_deathSound.play();
			return true;
		}
		else if(super.hasCollided(theBall, bottomEdge)) {
			ball.negateY();
			//_deathSound.play();
			return true;
		}
		else if(super.hasCollided(theBall, leftEdge)) {
			ball.negateX();
			//_deathSound.play();
			return true;
		}
		else if(super.hasCollided(theBall, rightEdge)) {
			ball.negateX();
			//_deathSound.play();
			return true;
		}
		return false;
	}
}
