public class Point {
	
	protected double x;
	protected double y;

	public Point() {
		x = 0;
		y = 0;
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public double distanceFrom(Point p) {
		return getDistance(this, p);
	}
	
	public static double getDistance(Point p1, Point p2) {
			return Math.abs(Math.hypot(p2.getX() - p1.getX(), p2.getY() - p1.getY()));
	}

	public String toString() {
		return x + " " + y;
	}

}