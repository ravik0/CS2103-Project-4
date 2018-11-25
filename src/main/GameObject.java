package main;

import javafx.geometry.Bounds;

public abstract class GameObject {
	protected boolean hasCollided(Bounds one, Bounds two) {
		return one.intersects(two);
	}
	public abstract Bounds getBoundingBox();
}
