public class NamedPlace extends Place {
	

	public NamedPlace(Position position,String name, String category) {
	super(position , name, category);
	this.name = name;

	}
	
	public String getName() {
		return name;
	}


	public String toString() {
		return "Named," +super.toString();
	}
		


}
