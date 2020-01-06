import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

abstract class Place extends Polygon {
	
	

	
	protected String name;    
	protected String category;
	protected Position position;
	
	public Place(Position position, String name, String category) {
		super(position.getValueX(), position.getValueY(), position.getValueX() - 15, position.getValueY() - 30,
				position.getValueX() + 15, position.getValueY() - 30);

		this.name = name;
		this.category = category;
		this.position = position;
		setColor();
		

	}
	

	
	public void setColor() {
		switch (category) {
		case ("Bus"):
			setFill(Color.RED);
		break;
		case("Underground"):
			setFill(Color.BLUE);
		break;
		case("Train"):
			setFill(Color.GREEN);
		break;
		
		default:
			setFill(Color.BLACK);
		
		}
		
	}
	
	public void show() {
		setFill(Color.DEEPPINK);
		setVisible(true);
	}
	
	public Polygon getPolygon() {
		Polygon temp  = new Polygon(position.getValueX(), position.getValueY(), position.getValueX() - 15, position.getValueY() - 30,
				position.getValueX() + 15, position.getValueY() - 30);
		return temp;
	}

	public Position getPosition() {
		return position;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCategory() {
		return category;
	}
	
	@Override
	public String toString() {
		return ( this.category+ ","+ this.position+"," +this.name);
	}
	
}


	

	




	
	
	


	

	

