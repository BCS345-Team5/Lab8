import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Point {

	protected DoubleProperty x = new SimpleDoubleProperty();
	protected DoubleProperty y = new SimpleDoubleProperty();

	//
	//Default Constructor
	//Sets default point to (0,0)
	//
	public Point() {
		x.set(0);
		y.set(0);
	}

	//
	//Constructor
	//Sets point to the x and y values that are given
	//
	public Point(double x, double y) {
		this.x.set(x);
		this.y.set(y);
	}

	public void setX(double x) {
		this.x.set(x);
	}

	public void setY(double y) {
		this.y.set(y);
	}

	public double getX() {
		return x.get();
	}

	public double getY() {
		return y.get();
	}

	//
	//Public method to return the distance between this point
	//and another that is included as an argument
	//
	public double distanceFrom(Point p) {
		return getDistance(this, p);
	}

	//
	//Public method to return the distance between two points
	//
	public static double getDistance(Point p1, Point p2) {
		return Math.abs(Math.hypot(p2.getX() - p1.getX(), p2.getY() - p1.getY()));
	}

	//
	//Public method to return String form of the object
	//
	public String toString() {
		return x.get() + " " + y.get();
	}

}