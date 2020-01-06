import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class NewNamedPlace extends Alert {
	private TextField nameField = new TextField();
	
	public NewNamedPlace() {
		super(AlertType.CONFIRMATION);
		setTitle("Named Place");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(10));
		grid.setHgap(1);
		grid.setVgap(1);
		grid.addRow(0, new Label("Namn:"), nameField);
		setHeaderText(null);
		getDialogPane().setContent(grid);
	}
	
	
	public String getNewNamedPlaceName() {
		return nameField.getText();
	}
}
