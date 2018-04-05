import java.text.DecimalFormat;
import java.util.Random;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class SuperCircle {
	private Random gen = new Random();
	private Circle circle;
	private int radius;
	private double centerX = MainDriver.WINDOW_CENTER_X;
	private double centerY = MainDriver.WINDOW_CENTER_Y;
	private final int minAngle = 45;
	private final int minDistance = 65;

	@SuppressWarnings("unused")
	private Point center = new Point();
	private circlePoint[] points = new circlePoint[3];
	private Line[] lines = new Line[3];
	private double[] distances = new double[3];
	private double[] angles = new double[3];
	private Group group = new Group();

	//
	// Default Constructor
	// Sets default radius of circle to 50
	// Calls initialization method
	//
	public SuperCircle() {
		radius = 50;
		init();
	}

	//
	// Constructor
	// Sets radius to given radius argument
	// Calls initialization method
	//
	public SuperCircle(int radius) {
		this.radius = radius;
		init();

	}

	//
	// Private initialization method
	// Instantiates circle object
	// Sets circle position to center of window and radius size
	// Sets circle fill and stroke properties
	//
	private void init() {
		circle = new Circle(centerX, centerY, radius);
		circle.setFill(Color.TRANSPARENT);
		circle.setStroke(Color.BLACK);
		circle.setStrokeWidth(2);
	}

	//
	// Public method to initialize all circlePoints, connecting lines, and labels
	// Generates random point positions
	// Binds lines to point positions
	// Calculates all distance and angle data
	// Ensures generated point positions are not too close together or result in too small of an angle
	//
	public void createPoints() {
		for (int i = 0; i < 3; i++)	// Instantiates points
			points[i] = genPoint();

		for (int i = 0; i < 3; i++) {
			// Adds listener to circlePoint movement
			points[i].getCircle().centerXProperty().addListener(this::pointListener);
			points[i].getCircle().centerYProperty().addListener(this::pointListener);

			lines[i] = new Line();
			lines[i].startXProperty().bind(points[i].getXProperty());
			lines[i].startYProperty().bind(points[i].getYProperty());

			lines[i].endXProperty().bind(points[(i + 1) % 3].getXProperty());
			lines[i].endYProperty().bind(points[(i + 1) % 3].getYProperty());
		}

		calculateAngles();

		for (int i = 0; i < 3; i++)
			if (angles[i] < minAngle || distances[i] < minDistance)
				createPoints();

		updateProperties();

	}
	
	//
	// Public method to return a group of all used JavaFX objects
	// Always returns group containing circle
	// If createPoints() was called, all other objects will be added to group
	//
	public Group getCircle() {
		group.getChildren().add(circle);
		
		if (points[0] != null) {
			group.getChildren().addAll(lines);
			for (int i = 0; i < 3; i++) {
				group.getChildren().add(points[i].getAngleText());
				group.getChildren().add(points[i].getCircle());
			}
		}
		return group;
	}
	
	//
	// Private listener
	// Calls updateProperties() upon any circlePoint movement
	//
	private void pointListener(ObservableValue<? extends Number> val, Number oldValue, Number newValue) {
		updateProperties();
	}

	//
	// Recalculates all distance and angle data
	// Instantiates angle text labels
	// Contains algorithm for angle label placement
	// 
	// For inner-circle label placement
	// 		Considers imaginary circle around each circlePoint of radius 30 (r)
	// 		Finds coordinates of intersection between imaginary circle and both lines relative to each circlePoint
	//		Uses both found points on imaginary circle (arc) to find the arc midpoint coordinates between the two points
	//		Arc midpoint is the position of the angle label
	//		Coordinates are then offset to account for physical text size
	//		NOTE: Formulas and Derived data assumes each ceneterPoints is at (0,0), then points are properly translated
	//
	// For outer-circle label placement
	// 		Considers imaginary ellipse slightly larger then the entire circle
	//		Computes ratio between both ellipse and circle X,Y size
	//		Multiplies X,Y ratios by point X,Y locations to get label position
	//
	private void updateProperties() {

		calculateAngles();

		double tmpRadius = radius;

		for (int i = 0; i < 3; i++) {
			Text t = points[i].getAngleText();
			t.setText(String.valueOf(angles[i]));

			boolean shortDist;

			// Test if any distances relevant to point are too small to contain text
			if (distances[(i) % 3] < minDistance || distances[(i + 2) % 3] < minDistance)
				shortDist = true;
			else
				shortDist = false;
			
			// If true, generate location for inner-circle placement
			if (angles[i] > minAngle && !shortDist) {
				double dy, dx, dr, x1, x2, y1, y2, xf, yf;
				double r = 30;
				
				// Sets values based on first line relative to point
				dx = points[(i + 1) % 3].x.get() - points[i].x.get();
				dy = points[(i + 1) % 3].y.get() - points[i].y.get();
				dr = Math.hypot(dx, dy);

				// Calculates X,Y for point of intersection between first line and imaginary circle
				x1 = ((dx) * Math.sqrt((r * r) * (dr * dr))) / (dr * dr);
				y1 = ((dy) * Math.sqrt((r * r) * (dr * dr))) / (dr * dr);

				// Calculates X,Y for intersection of second line
				dx = points[(i + 2) % 3].x.get() - points[i].x.get();
				dy = points[(i + 2) % 3].y.get() - points[i].y.get();
				dr = Math.hypot(dx, dy);

				x2 = ((dx) * Math.sqrt((r * r) * (dr * dr))) / (dr * dr);
				y2 = ((dy) * Math.sqrt((r * r) * (dr * dr))) / (dr * dr);
				
				// Calculates midpoint position between both found points
				xf = r * ((x1 + x2) / (Math.hypot((x1 + x2), (y1 + y2))));
				yf = ((y1 + y2) / (x1 + x2)) * (xf);
				
				// Sets angle text label position
				// Offsets point back to proper X,Y plane, and then translates points to window X,Y plane
				t.setX(xf + points[i].getX() + centerX - 12);
				t.setY(-1 * (yf + points[i].getY()) + centerY + 5);

			} else {
				// Set angle text label position to outer-circle position
				// Multiples circlePoint position by ratio, and then translates point to window X,Y plane
				t.setX((points[i].x.get() * ((tmpRadius + 25) / tmpRadius)) + (centerX - 14));
				t.setY(((points[i].y.get() * ((tmpRadius + 15) / tmpRadius)) * -1) + (centerY + 5));
			}
		}
	}

	private void calculateAngles() {
		DecimalFormat df = new DecimalFormat("#.0");

		for (int i = 0; i < 3; i++)
			distances[i] = Point.getDistance(points[i], points[(i + 1) % 3]);

		double a = distances[1];
		double b = distances[2];
		double c = distances[0];

		try {
			angles[0] = (Double
					.parseDouble(df.format(180 * (Math.acos(((a * a) - (b * b) - (c * c)) / (-2 * b * c))) / Math.PI)));
			angles[1] = (Double
					.parseDouble(df.format(180 * (Math.acos(((b * b) - (a * a) - (c * c)) / (-2 * a * c))) / Math.PI)));
			angles[2] = (Double
					.parseDouble(df.format(180 * (Math.acos(((c * c) - (b * b) - (a * a)) / (-2 * a * b))) / Math.PI)));
		} catch (NumberFormatException e) {
			angles[0] = (180 * (Math.acos(((a * a) - (b * b) - (c * c)) / (-2 * b * c))) / Math.PI);
			angles[1] = (180 * (Math.acos(((b * b) - (a * a) - (c * c)) / (-2 * a * c))) / Math.PI);
			angles[2] = (180 * (Math.acos(((c * c) - (b * b) - (a * a)) / (-2 * a * b))) / Math.PI);
		}
	}

	private circlePoint genPoint() {
		double tmpX = gen.nextInt(radius * 2) - radius;
		double tmpY = circlePoint.genYCoord(tmpX, radius);
		if (tmpX % 2 != 0)
			tmpY = tmpY * -1;
		return new circlePoint(tmpX, tmpY, radius);
	}

	public void updatePos(double newCenterX, double newCenterY) {

		centerX = MainDriver.WINDOW_CENTER_X;
		centerY = MainDriver.WINDOW_CENTER_Y;

		if (MainDriver.WINDOW_SIZE_X < MainDriver.WINDOW_SIZE_Y)
			radius = (int) (MainDriver.WINDOW_SIZE_X * MainDriver.CIRCLE_WINDOW_RATIO / 2);
		else
			radius = (int) (MainDriver.WINDOW_SIZE_Y * MainDriver.CIRCLE_WINDOW_RATIO / 2);

		circle.setCenterX(centerX);
		circle.setCenterY(centerY);
		circle.setRadius(radius);

		if (radius < 50)
			circlePoint.setRadius(4);
		else
			circlePoint.setRadius(6);

		for (circlePoint p : points)
			p.updatePos(radius);
	}

	public void showDebug() {
		int y = 15;
		StringProperty comma = new SimpleStringProperty(", ");
		StringProperty radius = new SimpleStringProperty("Radius: ");

		Text radiusLabel = new Text(10, y, "");
		radiusLabel.textProperty().bind(radius.concat(circle.radiusProperty()));

		group.getChildren().add(radiusLabel);

		for (int i = 0; i < 3; i++) {
			y += 15;
			StringProperty pText = new SimpleStringProperty("P" + i + ": ");
			Text p = new Text(10, y, "");
			p.textProperty().bind(pText.concat(points[i].x).concat(comma).concat(points[i].y));

			group.getChildren().add(p);
		}
		Text note = new Text(10, y += 15, "(DISABLE IN MAINDRIVER)");
		group.getChildren().add(note);

	}

}
