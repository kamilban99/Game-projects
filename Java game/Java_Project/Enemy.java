/**
 * Class describing enemy
 */
public class Enemy extends Character {
	private String name;

	/**
	 * constructor that initialises an object
	 * @param name sets enemy name to value of name
	 */
	public Enemy(String name){
		this.name = name;
	}

	/**
	 * moves enemy to another room (which room is specified by getRandomExit) if a turnCounter is not positive
	 */
	public void move(){
		if(getTurnCounter() <= 0){ //turnCounter is decrement by round each turn so after 3 turns of setting enemy to stun
			//enemies stun is turned off hence enemy can move again
		Room roomToMove = getCurrentRoom().getRandomExit();
		setCurrentRoom(roomToMove);
		}
	}

	/**
	 * sets turnCounter to 3
	 */
	public void setStunned(){
		setTurnCounter(3);
	}

	/**
	 *
	 * @return name
	 */
	public String getName(){
		return name;
	}

	/**
	 * checks if turnCounter is positive
	 * @return true if turnCounter is positive
	 */
	public boolean isStunned(){
		return getTurnCounter() > 0; // if the turn counter is positive than geometry is still stunned
	}
}
