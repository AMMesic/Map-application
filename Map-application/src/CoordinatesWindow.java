
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


public class CoordinatesWindow extends Alert {
private TextField x = new TextField();
private TextField y = new TextField();
	public CoordinatesWindow(){
	super(AlertType.CONFIRMATION);
	setTitle("Input Coordinates: ");
	GridPane coordinates = new GridPane();
	coordinates.setAlignment(Pos.CENTER);
	coordinates.setPadding(new Insets(10));
	coordinates.setHgap(10);
	coordinates.setVgap(10);
	coordinates.addRow(0, new Label("x: "), x);
	coordinates.addRow(1, new Label("y: "), y);
	setHeaderText(null);
	getDialogPane().setContent(coordinates);
	


	}
	public double getNewX() {
		return Double.parseDouble(x.getText());
	}
	public double getNewY() {
		return Double.parseDouble(y.getText());
	}
	
	
	}

