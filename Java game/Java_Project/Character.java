/**
 * class which focuses on character related modules
 */
public class Character {

	//private int health;
	private Room currentRoom;
	private boolean isAlive = true;
	private int turnCounter = 0;
	/**
	 * @return currentRoom
	 */
	public Room getCurrentRoom()
	{
		return currentRoom;
	}

	/**
	 * @param newRoom is set as currentRoom
	 */
	public void setCurrentRoom(Room newRoom)
	{
		currentRoom = newRoom;
	}

	/**
	 * @return isAlive
	 */
	public boolean getIsAlive(){
		return isAlive;
	}

	/**
	 * sets isAlive to false
	 */

	public void die(){
		isAlive = false;
	}

	/**
	 * @return turnCounter
	 */
	public int getTurnCounter(){
		return turnCounter;
	}

	/**
	 *
	 * @param counter , turnCounter is set to value of counter
	 */
	public void setTurnCounter(int counter){
		turnCounter = counter;
	}
}
