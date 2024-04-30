import java.util.*;

/**
 * class specifies behaviour of Player object
 */
public class Player extends Character {


	private final HashMap<String, Item> inventory = new HashMap<>();
	private final int maximumLoad;
	private int currentLoad;

	/**
	 * initilises player object
	 * @param maxWeight sets maximumLoad to maxWeight
	 */
	public Player(int maxWeight){
		maximumLoad = maxWeight;
		currentLoad = 0;
	}

	/**
	 * prints the string of all items in the inventory and information
	 * about currentLoad and maximumLoad
	 */
	public void printInventory(){
		if(!inventory.isEmpty()){
		System.out.println("your inventory: ");
		inventory.forEach((name, item)->{
			System.out.println(name +": ");
			item.printDescription();
		});
		System.out.println("your weight " + currentLoad + " out of " + maximumLoad);
		System.out.println();
		}
		else{
			System.out.println("you don`t have anything in the inventory");
			System.out.println();
		}
	}

	/**
	 * adds an Item object to inventory
	 * @param item object to be added to inventory
	 */
	public boolean pickUp(Item item){
		if(maximumLoad >= item.getWeight() + currentLoad && item.getIsPickable()){
			inventory.put(item.getName(), item);
			currentLoad += item.getWeight();
			System.out.println("you have picked up " + item.getName());
			return true;
		}
		else if(!item.getIsPickable()){
			System.out.println("you cannot pick that up");
			return false;
		}
		else{ //items weight + all items player carries  > max weight player can carry
			System.out.println("this item is too heavy for you to carry, drop some items to pick it up");
			return false;
		}
	}

	/**
	 * checks if there is an item with given name
	 * @param itemName item name to be checked
	 * @return true if there is item in inventory with itemName as name
	 */
	public boolean isInInventory(String itemName){
		return inventory.containsKey(itemName);
	}

	/**
	 * removes item from inventory
	 * @param itemName name of the item to be removed
	 */
	public void dropItem(String itemName){
		currentLoad -= inventory.get(itemName).getWeight();
		inventory.remove(itemName);
	}

	/**
	 * returns an item from invnetory
	 * @param itemName name of an Item object to be removed
	 * @return Item object with maching name to itemName
	 */
	public Item getItem(String itemName){
		return inventory.get(itemName);
	}

	/**
	 *
	 * @return currentLoad
	 */
	public int getCurrentLoad(){
		return currentLoad;
	}

	/**
	 * sets currentLoad value
	 * @param weight new value of currentLoad
	 */
	public  void setCurrentLoad(int weight){
		currentLoad = weight;
	}

	/**
	 *
	 * @return HashMap of all items in the inventory and its names
	 */
	public HashMap<String, Item> getInventory(){
		return  inventory;
	}

	/**
	 *
	 * @return maximumLoad
	 */
	public int getMaximumLoad(){
		return maximumLoad;
	}
}
