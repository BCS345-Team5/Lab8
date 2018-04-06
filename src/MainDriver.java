import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainDriver extends Application {

	public static double WINDOW_SIZE_X = 450;
	public static double WINDOW_SIZE_Y = 450;
	public static double WINDOW_CENTER_X = WINDOW_SIZE_X / 2;
	public static double WINDOW_CENTER_Y = WINDOW_SIZE_Y / 2;
	public final static double CIRCLE_WINDOW_RATIO = 2.0 / 3.0;

	Scene scene;
	SuperCircle myCircle;

	@Override
	public void start(Stage primaryStage) throws Exception {

		double calculatedRadius = WINDOW_SIZE_X * CIRCLE_WINDOW_RATIO / 2;

		myCircle = new SuperCircle((int) (calculatedRadius));
		myCircle.createPoints();
		
		//myCircle.showDebug();

		Pane root = new Pane(myCircle.getCircle());

		scene = new Scene(root, WINDOW_SIZE_X, WINDOW_SIZE_Y);
		scene.widthProperty().addListener(this::sceneSizeListener);
		scene.heightProperty().addListener(this::sceneSizeListener);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Lab8-Assignment");
		primaryStage.show();

	}

	//
	//Private method to update the position of the circle when
	//the window size has been changed
	//
	private void sceneSizeListener(ObservableValue<? extends Number> val, Number oldValue, Number newValue) {
		WINDOW_SIZE_X = scene.getWidth();
		WINDOW_SIZE_Y = scene.getHeight();
		WINDOW_CENTER_X = WINDOW_SIZE_X / 2;
		WINDOW_CENTER_Y = WINDOW_SIZE_Y / 2;

		myCircle.updatePos(WINDOW_CENTER_X, WINDOW_CENTER_Y);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
