import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;



public class NewDescribedPlace extends Alert {
	private TextField nameField = new TextField();
	private TextField descriptionField = new TextField();
	public NewDescribedPlace() {
		super(AlertType.CONFIRMATION);
		setTitle("Described Place");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(10));
		grid.setHgap(1);
		grid.setVgap(1);
		grid.addRow(0, new Label("Namn:"), nameField);
		grid.addRow(1, new Label("Beskrivning:"), descriptionField);
		setHeaderText(null);
		getDialogPane().setContent(grid);
	}
	
	public String getNewDescribedPlaceName() {
		return nameField.getText();
	}
	
	public String getNewNamedPlaceDescription() {
		return descriptionField.getText();
	}
}
