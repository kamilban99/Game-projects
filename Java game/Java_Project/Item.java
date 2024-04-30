/**
 * Class where we describe behaviour of items in game
 */
public class Item {

	private long weight;
	private String name;
	private String description;
	private boolean isPickable;

	/**
	 * initialises Item object and sets field values
	 * @param name is set to Item name
	 * @param weight is set to Item weight
	 * @param description is set to Item description
	 * @param isPickable is set to isPickable
	 */
	public Item(String name, long weight,  String description, boolean isPickable){
		this.name = name;
		this.weight = weight;
		this.description = description;
		this.isPickable = isPickable;
	}
	/**
	 * @return true if the it is possible to pick up the item.
	 */
	public boolean getIsPickable(){
		return isPickable;
	}
	/**
	 * @return weight.
	 */

	public long getWeight(){
		return weight;
	}

	/**
	 * @return name
	 */
	public String getName(){
		return name;
	}

	/**
	 * print description of an item
	 */
	public void printDescription(){
		System.out.println(description);
	}
}
