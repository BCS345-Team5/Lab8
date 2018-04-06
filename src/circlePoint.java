import javafx.beans.property.DoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class circlePoint extends Point {

	private Circle circle;
	private double centerX = MainDriver.WINDOW_CENTER_X;
	private double centerY = MainDriver.WINDOW_CENTER_Y;
	private int scRadius;
	private static int radius = 6;
	private Text angleText = new Text();

	public static double genYCoord(double xCoord, int scRadius) {
		return (Math.sqrt((Math.pow(scRadius, 2) - Math.pow((xCoord), 2))));
	}

	//
	//Constructor
	//sets the x and y coordinates of a point on the circle
	//
	public circlePoint(double x, double y, int scRadius) {
		super(x, y);
		this.scRadius = scRadius;
		init();
	}

	//
	//Private method to initialize the Circle
	//
	private void init() {
		circle = new Circle(x.get() + centerX, (-1 * y.get()) + centerY, radius);
		circle.setFill(Color.RED);
		circle.setStroke(Color.BLACK);
		circle.setOnMouseDragged(this::mouseDragHandler);
	}

	public Circle getCircle() {
		return circle;
	}
	
	public Text getAngleText() {
		return angleText;
	}

	public static void setRadius(int radius) {
		circlePoint.radius = radius;
	}

	public DoubleProperty getXProperty() {
		return circle.centerXProperty();
	}

	public DoubleProperty getYProperty() {
		return circle.centerYProperty();
	}

	//
	//Private method to handle mouse drag events
	//Gets the current mouse x position to move the point
	//
	private void mouseDragHandler(MouseEvent event) {
		double max = centerX + scRadius;
		double min = centerX - scRadius;
		double tmpX = event.getX();

		if (tmpX > max)
			tmpX = max;
		else if (tmpX < min)
			tmpX = min;
		x.set(tmpX - centerX);

		circle.setCenterX(tmpX);

		double tmpY = genYCoord(x.get(), scRadius);

		if (event.getY() > centerY)
			tmpY = tmpY * -1;
		y.set(tmpY);

		circle.setCenterY((-1 * y.get()) + centerY);
	}

	//
	//Public method used to update the position of the circle to
	//the center if the window size is changed
	//
	public void updatePos(double newSCRadius) {
		circle.setRadius(radius);
		centerX = MainDriver.WINDOW_CENTER_X;
		centerY = MainDriver.WINDOW_CENTER_Y;
		if (newSCRadius == 0)
			newSCRadius = 1;

		double radiusRatio = newSCRadius / scRadius;
		scRadius = (int) newSCRadius;

		x.set(x.get() * radiusRatio);
		y.set(y.get() * radiusRatio);

		circle.setCenterX(x.get() + centerX);
		circle.setCenterY((-1 * y.get()) + centerY);

	}

}