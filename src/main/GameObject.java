package main;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;

/**
 * Class that implements functionality that all physical GameObjects should have
 * @author Ravi
 *
 */
public abstract class GameObject {
	/**
	 * Checks to see if two bounds are intersecting with each other
	 * @param one the first bound
	 * @param two the second bound
	 * @return true if the bounds intersect, false otherwise
	 */
	protected boolean hasCollided(Bounds one, Bounds two) {
		return one.intersects(two);
	}
	
	/**
	 * Returns the bounds around the game object
	 * @return Bounds around the objects
	 */
	public abstract Bounds getBoundingBox();
	
	/**
	 * Returns the Label to display the image
	 * @return The label of the object
	 */
	public abstract Label getImage();
}
