package application;

import java.awt.Point;

import javafx.scene.paint.Color;

public class ColoredPoint extends Point {
	private Color color;
	
	public ColoredPoint(int x, int y, Color color){
		super(x, y);
		setColor(color);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
