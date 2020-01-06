import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Cursor;
import javafx.event.Event;

public class MainWindow extends Application {
	
	private Stage primaryStage;
	private ImageView imageView = new ImageView();
	private ToggleGroup knapplas = new ToggleGroup();
	private RadioButton named = new RadioButton("Named          ");
	private RadioButton described = new RadioButton("Described      ");
	private BorderPane root = new BorderPane();
	private ScrollPane center = new ScrollPane();
	private Pane pane = new Pane();
	private TextField field = new TextField();
	private boolean changed = false;

	private HashMap<String, List<Place>> listOfPlaces = new HashMap<>();
	private HashMap<Position, Place> positionsOfMap = new HashMap<Position, Place>();

	private HashMap<String, List<Place>> listOfNames = new HashMap<>();
	private LinkedList<Place> marked = new LinkedList<Place>();
	
	private ObservableList<String> items = FXCollections.observableArrayList("Bus", "Underground", "Train");
	private ListView<String> list = new ListView<String>();
	
	

	public void start(Stage primaryStage) {

		pane.setDisable(true);
		named.setSelected(true);
		this.primaryStage = primaryStage;

		Menu filemenu = new Menu("File");
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(filemenu);
		MenuItem loadMap = new MenuItem("Load Map");
		MenuItem loadPlaces = new MenuItem("Load Places");
		MenuItem save = new MenuItem("Save");
		MenuItem exit = new MenuItem("Exit");
		VBox topBox = new VBox();
		topBox.getChildren().add(menuBar);
		exit.setOnAction(e -> primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST)));

		filemenu.getItems().addAll(loadMap, loadPlaces, save, exit);
		save.setOnAction(new FileSaver());
		loadMap.setOnAction(new LoadMap());
		loadPlaces.setOnAction(new FileHandler());

		HBox top = new HBox();

		named.setToggleGroup(knapplas);
		described.setToggleGroup(knapplas);

		VBox buttonBox = new VBox();
		buttonBox.getChildren().addAll(named, described);

		Button newButton = new Button("new");

		Button search = new Button("Search");
		search.setOnAction(new SearchHandler());
		Button hide = new Button("Hide");
		Button remove = new Button("Remove");
		remove.setOnAction(new FileRemover());

		hide.setOnAction(new HiddenHandler());
		Button coordinates = new Button("Coordinates");
		coordinates.setOnAction(new PositionHandler());

		top.getChildren().addAll(newButton, buttonBox, field, search, hide, remove, coordinates);
		top.setSpacing(10);
		top.setAlignment(Pos.TOP_CENTER);
		top.setPrefWidth(100);
		top.setStyle("-fx-font-weight: bold");
		topBox.getChildren().add(top);
		VBox right = new VBox();
		right.setPrefWidth(150);
		right.setSpacing(10);
		right.setAlignment(Pos.CENTER);
		Label categoryLabel = new Label("Categories");
		Button category = new Button("Hide category");
		category.setOnAction(new HideCategoryHandler());
		right.setPadding(new Insets(10));

		list.setPrefHeight(100);
		list.setPrefWidth(75);
		list.setItems(items);
		list.getSelectionModel().selectedItemProperty().addListener(new ListHandler());
		right.getChildren().addAll(categoryLabel, list, category);
		VBox left = new VBox();
		left.setPrefWidth(100);
		left.setPrefHeight(300);
		root.setTop(topBox);
		root.setCenter(center);

		pane.getChildren().add(imageView);
		center.setContent(pane);
		root.setRight(right);

		Scene scene = new Scene(root, 1000, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new ExitHandler());
		newButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				pane.setDisable(false);
				pane.setCursor(Cursor.CROSSHAIR);
				pane.setOnMouseClicked(new ClickHandler());
			}
		});

	}

	public String getSearchField() {
		return field.getText();
	}

	class SearchHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {

			String item = getSearchField();
			if (listOfNames.get(item) == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(item + " finns ej");
				alert.showAndWait();
			} else {
				
				for (Place place : marked) {
					place.setColor();
				}
				marked.clear();
				for (Place place : listOfNames.get(item)) {

					place.show();
					marked.add(place);

				}
			}

		}
	}

	class PositionHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			CoordinatesWindow coordinates = new CoordinatesWindow();
			Optional<ButtonType> result = coordinates.showAndWait();
			Alert alert = new Alert(AlertType.ERROR);
			Position position;

			if (result.isPresent() && result.get() == ButtonType.OK) {

				try {
					double x = coordinates.getNewX();
					double y = coordinates.getNewY();
					position = new Position(x, y);
                    
					if (positionsOfMap.get(position) != null) {
						for(Place p: marked) {
							p.setColor();
						}
						marked.clear();
						Place place = positionsOfMap.get(position);
						place.show();
						marked.push(place);				
					} else {
						alert.setContentText(position + " finns ej");
						alert.showAndWait();
					}
				} catch (NumberFormatException nfe) {
					alert.setContentText("Denna plats finns ej");
					alert.showAndWait();
				}
			}
		}

	}

	class ExitHandler implements EventHandler<WindowEvent> {
		@Override
		public void handle(WindowEvent event) {
			warnUnsavedChanges(event);

		}

	}

	public boolean warnUnsavedChanges(Event event) {
		if (changed) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText("Osparade �ndringar. Vill du forts�tta?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get().equals(ButtonType.CANCEL)) {
				event.consume();
				return false;
			} else {

				return true;
			}

		}
		return true;
	}

	private void clearData() {
		positionsOfMap.clear();
		listOfPlaces.clear();
		listOfNames.clear();
		marked.clear();
		changed = false;
		pane.getChildren().clear();
		pane.getChildren().add(imageView);
	}

	class ListHandler implements ChangeListener<String> {
		public void changed(ObservableValue obs, String old, String nev) {
			if (listOfPlaces.containsKey(nev)) {
				marked.clear();
				for (Place place : listOfPlaces.get(nev)) {
					place.setColor();
					place.setVisible(true);
					
				}
			}
		}
	}

	class HiddenHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent event) {
			for (Place place : marked) {
				place.setColor();
				place.setVisible(false);
			}
			marked.clear();

		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	class FileRemover implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			for (Place place : marked) {
				if (place != null) {
					pane.getChildren().remove(place);
					place.setVisible(false);
					if(positionsOfMap.containsKey(place.getPosition())) {
						positionsOfMap.remove(place.getPosition());
					}
					if(listOfPlaces.containsKey(place.category)) {
						listOfPlaces.get(place.category).remove(place);
					}
					if(listOfNames.containsKey(place.name)){
						listOfNames.get(place.getName()).remove(place);
					}
					if(listOfNames.get(place.getName()).isEmpty()) {
						listOfNames.remove(place.getName());
					}
					if (positionsOfMap.isEmpty()) {
						changed = false;
					} else {
						changed = true;
					}
					

				}
				
			}
			marked.clear();

		}
	}

	class FileSaver implements EventHandler<ActionEvent> {

		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text", "*.txt"),
					new FileChooser.ExtensionFilter("Alla", "*.*"));
			File file = fileChooser.showSaveDialog(primaryStage);
			if (file != null) {
				try {

					FileWriter fileOut = new FileWriter(file.getAbsolutePath());
					PrintWriter printFile = new PrintWriter(fileOut);
					for (Place place : positionsOfMap.values())
						printFile.println(place);
					printFile.close();
					changed = false;
				} catch (IOException e) {
					new Alert(AlertType.ERROR, "Fel!").showAndWait();
				}
			}
		}
	}

	class FileHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent loadPlaces) {
			if (warnUnsavedChanges(loadPlaces) == false)
				return;
			pane.setDisable(false);
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text", "*.txt"),
					new FileChooser.ExtensionFilter("Alla", "*.*"));

			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null && imageView.getImage() != null) {
				clearData();
				try {

					fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text", "*.txt"),
							new FileChooser.ExtensionFilter("Alla", "*.*"));

					FileReader fileReader = new FileReader(file.getAbsolutePath());
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						String[] tokens = line.split(",");

						if (tokens[0].equalsIgnoreCase("Named")) {

							double x = Double.parseDouble(tokens[2]);
							double y = Double.parseDouble(tokens[3]);
							Position position = new Position(x, y);
							String name = tokens[4];
							String category = tokens[1];
							NamedPlace namedPlace = new NamedPlace(position, name, category);
							List<Place> placeOfList = listOfPlaces.get(category);
							if (placeOfList == null) {
								placeOfList = new ArrayList<Place>();
								listOfPlaces.put(category, placeOfList);
								
							}

							if (!listOfNames.containsKey(name)) {

								listOfNames.put(name, new ArrayList<Place>());
							}
							listOfNames.get(name).add(namedPlace);
							List<Place> placesByName = listOfNames.get(name);
							placesByName.add(namedPlace);
							placeOfList.add(namedPlace);
							positionsOfMap.put(position, namedPlace);

							pane.getChildren().addAll(namedPlace);
							namedPlace.setOnMouseClicked(new MarkHandler());

						} else {
							double x = Double.parseDouble(tokens[2]);
							double y = Double.parseDouble(tokens[3]);
							Position position = new Position(x, y);
							String name = tokens[4];
							String describtion = tokens[5];
							String category = tokens[1];
							DescribedPlace place = new DescribedPlace(position, name, describtion, category);
							List<Place> placeOfList = listOfPlaces.get(category);
							if (placeOfList == null) {
								placeOfList = new ArrayList<Place>();
								listOfPlaces.put(category, placeOfList);
							}
							if (!listOfNames.containsKey(name)) {

								listOfNames.put(name, new ArrayList<Place>());
							}
							listOfNames.get(name).add(place);
							placeOfList.add(place);
							positionsOfMap.put(position, place);

							pane.getChildren().addAll(place);
							place.setOnMouseClicked(new MarkHandler());

						}

					}
				

					bufferedReader.close();
					fileReader.close();
				} catch (FileNotFoundException e) {
					new Alert(AlertType.ERROR, "Fel!").showAndWait();
				} catch (IOException e) {
					new Alert(AlertType.ERROR, "Fel!").showAndWait();
				}
			}
		}
	}

	class HideCategoryHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			String category = list.getSelectionModel().getSelectedItem();
			if (listOfPlaces.containsKey(category)) {
				for (Place place : listOfPlaces.get(category)) {
					place.setVisible(false);
				}
			}
			marked.removeAll(listOfPlaces.get(category));
		}
	}
	

	class ClickHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY) {
				Place triangle = null;
				double x = event.getX();
				double y = event.getY();
				String category = list.getSelectionModel().getSelectedItem();

				if (category == null) {
					category = "None";

				}
				if (checkIfExist(x, y)) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText(x + " " + y + " finns redan");
					alert.showAndWait();
				} else {

					Position position = new Position(x, y);
					if (named.isSelected()) {
						
						NewNamedPlace newPlace = new NewNamedPlace();
						Optional<ButtonType> result1 = newPlace.showAndWait();
						if (result1.isPresent() && result1.get() == ButtonType.OK) {
							if(newPlace.getNewNamedPlaceName().isEmpty()) {
								triangle = null;
								pane.setOnMouseClicked(null);
								pane.setCursor(Cursor.DEFAULT);
								Alert alert = new Alert(AlertType.ERROR);
								alert.setHeaderText("Var god ange namn");
								alert.showAndWait();
								return;
							}
							else {
							triangle = new NamedPlace(position, newPlace.getNewNamedPlaceName(), category);
							changed = true;
							pane.setOnMouseClicked(null);
							pane.setCursor(Cursor.DEFAULT);
							pane.getChildren().addAll(triangle);
							}
							if (triangle != null) {
								triangle.setOnMouseClicked(new MarkHandler());
							}
							

							positionsOfMap.put(position, triangle);
							if (!listOfPlaces.containsKey(category)) {
								listOfPlaces.put(category, new LinkedList<Place>());

							}

							if (!listOfNames.containsKey(triangle.getName())) {
								listOfNames.put(triangle.getName(), new LinkedList<Place>());
							}
							listOfPlaces.get(category).add(triangle);
							listOfNames.get(triangle.getName()).add(triangle);

						} else if (result1.get() == ButtonType.CANCEL) {
							triangle = null;
							pane.setOnMouseClicked(null);
							pane.setCursor(Cursor.DEFAULT);

						}

					} else if (described.isSelected()) {
						NewDescribedPlace descPlace = new NewDescribedPlace();
						Optional<ButtonType> result1 = descPlace.showAndWait();
						if (result1.isPresent() && result1.get() == ButtonType.OK) {
							if(descPlace.getNewDescribedPlaceName().isEmpty() || descPlace.getNewNamedPlaceDescription().isEmpty()) {
								triangle = null;
								pane.setOnMouseClicked(null);
								pane.setCursor(Cursor.DEFAULT);
								Alert alert = new Alert(AlertType.ERROR);
								alert.setHeaderText("Var god ange namn och/eller beskrivning");
								alert.showAndWait();
								return;
							}
							else {

							triangle = new DescribedPlace(position, descPlace.getNewDescribedPlaceName(),
									descPlace.getNewNamedPlaceDescription(), category);
							changed = true;
							pane.setOnMouseClicked(null);
							pane.setCursor(Cursor.DEFAULT);
							pane.getChildren().addAll(triangle);
							}
							if (triangle != null) {
								triangle.setOnMouseClicked(new MarkHandler());
							}

							positionsOfMap.put(position, triangle);
							if (!listOfPlaces.containsKey(category)) {
								listOfPlaces.put(category, new LinkedList<Place>());

							}

							if (!listOfNames.containsKey(triangle.getName())) {
								listOfNames.put(triangle.getName(), new LinkedList<Place>());
							}
							listOfPlaces.get(category).add(triangle);
							listOfNames.get(triangle.getName()).add(triangle);

						} else if ((result1.get() == ButtonType.CANCEL)) {
							triangle = null;
							pane.setOnMouseClicked(null);
							pane.setCursor(Cursor.DEFAULT);
						}

					}
					list.getSelectionModel().clearSelection();
				}
			
			}
		}

		public boolean checkIfExist(double x, double y) {
			return positionsOfMap.containsKey(new Position(x, y));
		}
	}

	class MarkHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {

			if (event.getButton() == MouseButton.PRIMARY) {
				Place namedPlace1 = (Place) event.getSource();
				if (marked.contains(namedPlace1)) {
					namedPlace1.setColor();
					marked.remove(namedPlace1);
				} else {
					namedPlace1.show();
					marked.add(namedPlace1);
				}

			} else if (event.getButton() == MouseButton.SECONDARY) {
				if (event.getSource() instanceof NamedPlace) {

					NamedPlace namedPlace = (NamedPlace) event.getSource();
					new NamedPlaceAlert(namedPlace).showAndWait();
				} else {
					DescribedPlace describedPlace = (DescribedPlace) event.getSource();
					new DescribedPlaceAlert(describedPlace).showAndWait();

				}
			}
		}

	}

	public class NamedPlaceAlert extends Alert {
		public NamedPlaceAlert(NamedPlace namedPlace) {
			super(AlertType.INFORMATION);
			setTitle("Message");
			setHeaderText(null);
			setContentText(namedPlace.getName() + "[" + namedPlace.getPosition() + "]");

		}
	}

	public class DescribedPlaceAlert extends Alert {
		public DescribedPlaceAlert(DescribedPlace describedPlace) {
			super(AlertType.INFORMATION);
			setHeaderText(describedPlace.getName() + "[" + describedPlace.getPosition() + "]");
			setContentText(describedPlace.getDescription());
			setTitle("Message");

		}
	}

	class LoadMap implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent loadMap) {
			if (warnUnsavedChanges(loadMap) == false)
				return;
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Bildfiler", "*.jpg", "*.png"),
					new FileChooser.ExtensionFilter("Alla filer", "*.*"));
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) {
				String name = file.getAbsolutePath();
				Image image = new Image("file:" + name);
				imageView.setImage(image);
				primaryStage.sizeToScene();
				clearData();

			}
		}

	}
}
