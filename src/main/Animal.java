package main;


import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;

/**
 * Class containing the functionality for all the animal enemies in the game 
 * @author Ravi
 *
 */
public class Animal {
	final private AudioClip _deathSound;
	final private Image _image;
	final private double _x, _y; //center of image
	
	public Bounds getBoundingBox() {
		double width = _image.getWidth();
		double height = _image.getHeight();
		return new Rectangle((int)(_x-width/2),(int)(_y-height/2), (int)width, (int)height).getBoundsInLocal();
	}
	
	public Animal(AudioClip sound, Image img, double posX, double posY) {
		_deathSound = sound;
		_image = img;
		_x = posX;
		_y = posY; 
	}
}
