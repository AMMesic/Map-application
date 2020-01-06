public class DescribedPlace extends Place {
	private String description;
	
	public DescribedPlace(Position position, String name, String description, String category) {
		super(position, name, category);
		this.description = description;
		this.name =name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getName() {
		return name;
	}

	public String toString() {
		return "Described," +super.toString()+ ","+(this.description);
	}
	
	


}
