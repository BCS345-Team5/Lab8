import javafx.beans.property.DoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class circlePoint extends Point{
	
	private Circle circle;
	private double centerX = MainDriver.WINDOW_CENTER_X;
	private double centerY = MainDriver.WINDOW_CENTER_Y;
	private int scRadius;
	private int radius = 6;
	
	public static double genYCoord(double xCoord, int scRadius) {
		return (Math.sqrt((Math.pow(scRadius,2) - Math.pow((xCoord), 2))));
	}
	
	public circlePoint(double x, double y, int scRadius) {
		super(x,y);
		this.scRadius = scRadius;
		init();
	}
	
	private void init() {
		circle = new Circle(x + centerX, y + centerY, radius);
		circle.setFill(Color.RED);
		circle.setStroke(Color.BLACK);
		circle.setOnMouseDragged(this::mouseDragHandler);
	}
	
	public Circle getCircle() {
		return circle;
	}
	
	public DoubleProperty getXProperty() {
		return circle.centerXProperty();
	}
	
	public DoubleProperty getYProperty() {
		return circle.centerYProperty();
	}
	
	private void mouseDragHandler(MouseEvent event) {
		double max = centerX + scRadius;
		double min = centerX - scRadius;
		double tmpX = event.getX();
		
		if(tmpX > max)
			tmpX = max;
		else if(tmpX < min)
			tmpX = min;
		x = tmpX - centerX;
		
		circle.setCenterX(tmpX);
		
		double tmpY = genYCoord(x, scRadius);
		
		if(event.getY() < centerY)
			tmpY = tmpY * -1;
		y = tmpY;
			
		circle.setCenterY(y + centerY);
	}
	
	public void updatePos(double newSCRadius) {
		
		centerX = MainDriver.WINDOW_CENTER_X;
		centerY = MainDriver.WINDOW_CENTER_Y;
		
		double radiusRatio = newSCRadius/scRadius;
		scRadius = (int) newSCRadius;
		
		x = x * radiusRatio;
		y = y * radiusRatio;
		
		circle.setCenterX(x + centerX);
		circle.setCenterY(y + centerY);
		
	}	
	
}