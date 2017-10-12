package application;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DrawingApplication extends Application {
	private static Color fillColor;
	private static int penSize;
	private static GridPane mainPane;
	private static Canvas canvas;
	private static HBox controlPanel;
	private static Slider penSizeSlider;
	private static ArrayList<ColoredPoint> points;
	private static boolean isDrawing = false;
	private final static String[] DEFAULT_BUTTON_STYLES = {"-fx-base: grey;-fx-selected-color: black", "-fx-selected-color: black;-fx-base: yellow;-fx-text-fill: black;",
			"-fx-base: lightgreen", "-fx-base: lightblue",
			"-fx-base: white"};
	private final static String CONTROL_PANEL_STYLE = "-fx-font-size: 20;-fx-spacing: 20;-fx-padding: 30;-fx-background-color: #216699;";

	public static void toggleDrawing() {
		if (isDrawing) {
			isDrawing = false;
		} else {
			isDrawing = true;
		}
		
	}
	
	
	private static void penDown(GraphicsContext gContext, int x, int y) {
		
		points.add(new ColoredPoint(x, y, fillColor));
		gContext.strokeOval(x, y, 5, 5);
		gContext.closePath();	
	}
	
	public static Slider setupPenSlider() {
		// I looked up how to do this here http://docs.oracle.com/javafx/2/ui_controls/slider.htm
		penSizeSlider = new Slider();
		penSizeSlider.setMin(15);
		penSizeSlider.setMax(50);
		penSizeSlider.setValue(penSizeSlider.getMin());
		penSizeSlider.setMajorTickUnit(5);
		penSizeSlider.setMinorTickCount(1);
		penSizeSlider.setBlockIncrement(10);
		penSizeSlider.setTooltip(new Tooltip("Pen-size Slider."));
		penSize = (int)penSizeSlider.getMin();
		penSizeSlider.setPadding(new Insets(10, 15, 40, 15));
		penSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                
            	penSize = new_val.shortValue();
            	canvas.getGraphicsContext2D().setLineWidth(penSize);
            	
            }
        });
		
		return penSizeSlider;
		
	}
	
	public static void clearCanvas() {
		
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		points.clear();
	}
	
	public static void setupControlButtons(HBox controlPanel) {
		
		 // I referred to this tutorial to make the buttons http://docs.oracle.com/javafx/2/ui_controls/jfxpub-ui_controls.htm
		
		 ToggleGroup colorSelection = new ToggleGroup();
		 RadioButton colorSelectionYellow = new RadioButton("Yellow");
		 colorSelectionYellow.setStyle(DEFAULT_BUTTON_STYLES[1]);
		 colorSelectionYellow.setToggleGroup(colorSelection);
		 RadioButton colorSelectionBlack = new RadioButton("Black");
		 colorSelectionBlack.setSelected(true);
		 colorSelectionBlack.setToggleGroup(colorSelection);
		 colorSelectionBlack.setStyle(DEFAULT_BUTTON_STYLES[0]);
		 RadioButton colorSelectionBlue = new RadioButton("Blue");
		 colorSelectionBlue.setStyle(DEFAULT_BUTTON_STYLES[3]);
		 colorSelectionBlue.setToggleGroup(colorSelection);
		 RadioButton colorSelectionGreen = new RadioButton("Green");
		 colorSelectionGreen.setStyle(DEFAULT_BUTTON_STYLES[2]);
		 colorSelectionGreen.setToggleGroup(colorSelection);
		 RadioButton eraserButton = new RadioButton("Eraser");
		 eraserButton.setStyle(DEFAULT_BUTTON_STYLES[4]);
		 eraserButton.setToggleGroup(colorSelection);
		 

		 ChangeListener<Toggle> colorButtonListener = (new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> button,
						Toggle oldValue, Toggle newValue) {
					
					if (newValue.equals(colorSelectionBlack)) fillColor = Color.BLACK;
					else if (newValue.equals(colorSelectionGreen)) fillColor = Color.GREEN;
					else if (newValue.equals(colorSelectionBlue)) fillColor = Color.BLUE;
					else if (newValue.equals(colorSelectionYellow)) fillColor = Color.YELLOW;
					else fillColor = Color.WHITE;
					canvas.getGraphicsContext2D().setStroke(fillColor);
				}
		 });
		 
		 colorSelection.selectedToggleProperty().addListener(colorButtonListener);
		 
		 Button clearCanvasButton = new Button("Clear Canvas");
		 clearCanvasButton.setOnMouseClicked(e -> clearCanvas() );
		 
		 controlPanel.getChildren().addAll(colorSelectionBlack, colorSelectionYellow,colorSelectionGreen,
				 colorSelectionBlue, eraserButton, clearCanvasButton);
		 
		 controlPanel.setVisible(true);
		
	}
	
	public static HBox setupControlPanel() {
		
		 controlPanel = new HBox();
		 controlPanel.setPrefSize(1000,  100);
		 controlPanel.setStyle(CONTROL_PANEL_STYLE);
		 setupControlButtons(controlPanel);
		 setupPenSlider();
		 controlPanel.getChildren().add(penSizeSlider);
		 
		 return controlPanel; 
	}
	
	public static Canvas setupCanvas() {
		
		canvas = new Canvas(1000, 650);
		canvas.getGraphicsContext2D().setLineWidth(penSize);
		canvas.setOnMouseClicked(e ->  toggleDrawing());
		canvas.setOnMouseMoved(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent move) {
					
				if (isDrawing) {
				   penDown(canvas.getGraphicsContext2D(), (int)move.getX(), (int)move.getY());	
				}			
			}} );
		
		return canvas;
	}
	
	public static GridPane setupMainPane() {
		
		 mainPane = new GridPane();
		 mainPane.setPrefSize(1000, 800);
		 RowConstraints row1 = new RowConstraints();
		 row1.setPercentHeight(80);
		 RowConstraints row2 = new RowConstraints();
		 row2.setPercentHeight(20);
		 mainPane.getRowConstraints().addAll(row1, row2);
		 mainPane.add(canvas, 0, 0);
		 mainPane.add(controlPanel, 0, 1);
		 mainPane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		 
		 
		 return mainPane;
		
	}
	
	public static void setupUI() {
		
		controlPanel = setupControlPanel();
		canvas = setupCanvas();
		mainPane = setupMainPane();
	}

	@Override
	public void start(Stage mainStage) throws Exception {
		 points = new ArrayList<ColoredPoint>();
		 setupUI();
		 mainStage.setScene(new Scene(mainPane));
	     mainStage.show();
		 
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
