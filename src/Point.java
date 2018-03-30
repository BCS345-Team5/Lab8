import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Point {
	
	protected DoubleProperty x = new SimpleDoubleProperty();
	protected DoubleProperty y = new SimpleDoubleProperty();

	public Point() {
		x.set(0);
		y.set(0);
	}
	
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
	
	public double distanceFrom(Point p) {
		return getDistance(this, p);
	}
	
	public static double getDistance(Point p1, Point p2) {
			return Math.abs(Math.hypot(p2.getX() - p1.getX(), p2.getY() - p1.getY()));
	}

	public String toString() {
		return x.get() + " " + y.get();
	}

}