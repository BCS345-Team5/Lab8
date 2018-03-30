import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainDriver extends Application {

	public static double WINDOW_SIZE_X = 375;
	public static double WINDOW_SIZE_Y = 375;
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
		myCircle.showDebug();

		StringProperty sp = new SimpleStringProperty("Radius: ");
		Text radiusLabel = new Text(10, 15, "");
		radiusLabel.textProperty().bind(sp.concat(myCircle.getRadiusProperty()));

		Pane root = new Pane(myCircle.getCircle(), radiusLabel);

		scene = new Scene(root, WINDOW_SIZE_X, WINDOW_SIZE_Y);

		scene.widthProperty().addListener(this::sceneSizeListener);
		scene.heightProperty().addListener(this::sceneSizeListener);
		
		primaryStage.setScene(scene);
		primaryStage.show();

	}

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
