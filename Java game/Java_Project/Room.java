import java.util.*;
/**
 * Class where we store information about toom
 */

public class Room
{
	private String description;
	private String name;
	private HashMap<String, Room> exits;        // stores exits of this room.
	private HashMap<String, Item> items;

	public Room(String name){
		this.name = name;
		exits = new HashMap<>();
		items = new HashMap<>();
	}
	/**
	 * Create a room described "description". Initially, it has
	 * no exits. "description" is something like "a kitchen" or
	 * "an open court yard".
	 * @param description The room's description.
	 */
	public Room(String description, String name)
	{
		this.description = description;
		this.name = name;
		exits = new HashMap<>();
		items = new HashMap<>();
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Define an exit from this room.
	 * @param direction The direction of the exit.
	 * @param neighbor  The room to which the exit leads.
	 */
	public void setExit(String direction, Room neighbor)
	{
		exits.put(direction, neighbor);
	}

	/**
	 * @return The short description of the room
	 * (the one that was defined in the constructor).
	 */
	public String getShortDescription()
	{
		return description;
	}

	/**
	 * Return a description of the room in the form:
	 *     You are in the kitchen.
	 *     Exits: north west
	 * @return A long description of this room
	 */
	public String getLongDescription()
	{
		return "you arrived to the following problem: " + description + "\n\n" + getExitString();
	}

	/**
	 * Return a string describing the room's exits, for example
	 * "Exits: north west".
	 * @return Details of the room's exits.
	 */
	private String getExitString()
	{
		String returnString = "Exits:";
		Set<String> keys = exits.keySet();
		for(String exit : keys) {
			returnString += " " + exit +",";
		}
		return returnString;
	}

	/**
	 * Return the room that is reached if we go from this room in direction
	 * "direction". If there is no room in that direction, return null.
	 * @param direction The exit's direction.
	 * @return The room in the given direction.
	 */
	public Room getExit(String direction)
	{
		return exits.get(direction);
	}

	/**
	 * puts a Item object to items hashmap in room object
	 * @param item item to be put to hashmap
	 */
	public void putItemRoom(Item item){
		items.put(item.getName(), item);
	}

	/**
	 * prints all the items stored in items hashmap (all items in room)
	 */
	public void printItems() {
		if (items.isEmpty()){
			System.out.println("there are no items here ");
		}
		else{
		System.out.print("items in the room: ");
		items.forEach((name, item)->System.out.print(name + " "));
		System.out.println();
		}
	}

	/**
	 * @param name name of the item to be returned
	 * @return item with a specifed name
	 */
	public Item getItem(String name){
		return items.get(name);
	}

	/**
	 * removes item with specifed name from room (items hashmap)
	 * @param item name of the item to be removed
	 */
	public void removeItem(Item item){
		items.remove(item.getName());
	}

	/**
	 * method used to generate random movment returns either the same room or one of
	 * the exits (chosen by random)
	 * @return a random exit or a current room
	 */
	public Room getRandomExit(){
		Random rand = new Random();
		int arrayLength = exits.size() + 1;
		int randomIndex = rand.nextInt(arrayLength);
		if(randomIndex != arrayLength-1) {
			String randomKey = exits.keySet().toArray(new String[arrayLength - 1])[randomIndex];
			return exits.get(randomKey);
		}
		else {
			return this; //dont move

		}
	}

	/**
	 *
	 * @return returns array of Room objects with all of the exits
	 */
	public Room[] getExits(){
		return exits.values().toArray(new Room[exits.size()]);
	}

	/**
	 * checks if there is a specifed item in room
	 * @param item name of the item to be checked
	 * @return true if a item with given name is in the room
	 */
	public boolean containsItem(String item){
		return items.containsKey(item);
	}

	/**
	 *
	 * @return room name
	 */
	public String getName(){
		return name;
	}

	/**
	 *
	 * @return hashmap of all items in the room
	 */
	public HashMap<String, Item>getItems(){
		return items;
	}
}

