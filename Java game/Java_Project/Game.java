/**
	Class that describes what is happening after launching the game
 */

import java.io.File;
import java.util.*;

public class Game {
	private Parser parser;
	private Player player;
	private Enemy polish;
	private Map map;
	Stack<Room> roomsVisited;
	private HashMap<String, Enemy> enemies;
	/**
	 * Create the game
	 */
	public Game() {
		roomsVisited = new Stack<>();
		enemies = new HashMap<>();
		player = new Player(15);
		parser = new Parser();
		polish = new Enemy("polish");
		enemies.put(polish.getName(), polish);
		CommandWords.initialiseCommandWords();
		System.out.println("list of available maps");
		setupMap();


		// wypisz wszystkie pliki z folderu
		// wez input od usera ktora mape wybrac
		// https://stackabuse.com/java-list-files-in-a-directory - jak listowac pliki z folderu (zlistuj mapy z maps_directory)
		// potem user musi wybrac mape z tych zlistowanych

		//List<String> results = new ArrayList<String>(// od

	}

	/**
	 *  Main play routine.  Loops until end of play.
	 */
	public void play() {
		printWelcome();
		// Enter the main command loop.  Here we repeatedly read commands and
		// execute them until the game is over.

		boolean finished = false;
		while (!finished && enemies.get("polish").getIsAlive() && player.getIsAlive()) {

			Command command = parser.getCommand();
			finished = processCommand(command);
			if (polish.getCurrentRoom() == player.getCurrentRoom()) {
				playerEnemyFight();
			}
		}
		//ending the game
		if (!enemies.get("polish").getIsAlive()) {
			Victory();
		}
		else if (!player.getIsAlive()) {
			defeat();
		}
		else {
			System.out.println("Thank you for playing.  Good bye.");
		}
	}

	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println("Welcome to the Math vs Polish the Game!");
		System.out.println("Math vs Polish is a game where you need to hide from polish and later destroy it,");
		System.out.println("the worst nightmare of mathematicians, every room here is filled with some useful item");
		System.out.println("explore those rooms and find a way to destroy polish; only then you will be able to leave from this nightmare");
		System.out.println("Type 'help' if you need help.");
		System.out.println();
		map.printMap(player);
		System.out.println();
		//printRoomInfo(player.getCurrentRoom());
	}

	/**
	 * Print out the victory message
	 */
	private void Victory(){
		System.out.println("with the great power you destroy polish");
		System.out.println("with defeat of polish, the eternal nightmare of all mathematicians has ended");
		System.out.println("you may leave now, you won and stories about your victory will be told for many years");
		System.out.println("or not, since probably someone else will be remembered for what you have done today");
		System.out.println("THE END");
	}

	/**
	 * print out defeat message and restart the game
	 */
	private void defeat(){
		System.out.println("you have fallen like others, by the merciless polish");
		System.out.println("you have lost");
		System.out.println("restarting in 5 sec");
		try {
			Thread.sleep(5000); //sleeps for 5 sec and restarts the game
		} catch (InterruptedException ignored) {
		} //do nothing, since it should not happen as there is no other thread that can interrupt it

		roomsVisited = new Stack<>();
		enemies = new HashMap<>();
		player = new Player(15);
		parser = new Parser();
		polish = new Enemy("polish");
		enemies.put(polish.getName(), polish);
		CommandWords.initialiseCommandWords();
		setupMap();
		parser = new Parser();
		play();
	}

	private void setupMap()
	{
		final File folder = new File("maps_directory");
		for (int i = 0; i < folder.listFiles().length; i++) {
			System.out.println(i + ":" +  folder.listFiles()[i].getName());
		}

		System.out.println("Choose index of map you want to play ");

		int choice = Integer.parseInt(System.console().readLine());

		String pathToMap = folder.listFiles()[choice].getAbsolutePath();

		map = new Map(pathToMap); // gives constructor path to chosen map
		player.setCurrentRoom(map.getStartingRoom());
		polish.setCurrentRoom(map.getEnemySpawn());
	}

	/**
	 * Given a command, process (that is: execute) the command.
	 * @param command The command to be processed.
	 * @return true If the command ends the game, false otherwise.
	 */
	private boolean processCommand(Command command) {
		boolean wantToQuit = false;

		CommandWord commandWord = CommandWords.getCommandWord(command.getFirstWord());

		switch (commandWord) {
			case GO -> goRoom(command);
			case HELP -> printHelp();
			case QUIT -> wantToQuit = quit(command);
			case UNKNOWN -> {
				System.out.println("I don't know what you mean...");
				return false;
			}
			case BACK -> goBack();
			case PICKUP -> pickUp(command);
			case INSPECT -> inspect(command);
			case INVENTORY -> player.printInventory();
			case USE -> useItem(command);
			case DROP -> dropItem(command);
			case CREATE -> create(command);
			case PASS -> pass();
			case INFO -> printRoomInfo(player.getCurrentRoom());
			case LOAD -> load(command);
			case SAVE -> save(command);
			case MAP -> map.printMap(player);
			case DIE -> die();
			default -> throw new IllegalStateException("Unexpected value: " + commandWord);
		}
		// else command not recognised.
		return wantToQuit;
	}

	/**
	 * prints information about the room
	 * @param room, which information we want to print
	 */
	private void printRoomInfo(Room room) {
		System.out.println(room.getLongDescription());
		room.printItems();
	}

	// implementations of user commands:

	/**
	 * Print out some help information.
	 * Here we print cryptic message and a list of the
	 * command words.
	 */
	private void printHelp() {
		System.out.println("You are lost. You are alone. You wander");
		System.out.println("around at the university.");
		System.out.println();
		System.out.println("Your command words are:");
		parser.showCommands();
	}

	/**
	 * Try to go to one direction. If there is an exit, enter the new
	 * room, otherwise print an error message.
	 */
	private void goRoom(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println("Go where?");
			return;
		}

		String direction = command.getSecondWord();
		// Try to leave current room.
		Room nextRoom = player.getCurrentRoom().getExit(direction);

		if (nextRoom == null) {
			System.out.println("There is no door!");
		}
		else {
			if (nextRoom == map.getTeleportRoom()) {
				System.out.println("You went into teleport " + map.getTeleportRoom().getName() + " !");
				System.out.println("You got teleported into another room!");
				updateCounters();
				polish.move();
				roomsVisited.push(player.getCurrentRoom());
				player.setCurrentRoom(map.getRandomRoom());
				printRoomInfo(player.getCurrentRoom());
			}
			else {
				updateCounters();
				polish.move();
				roomsVisited. push(player.getCurrentRoom());
				player.setCurrentRoom(nextRoom);
				printRoomInfo(nextRoom);
			}
		}
	}

	/**
	 * "Quit" was entered. Check the rest of the command to see
	 * whether we really quit the game.
	 * @return true, if this command quits the game, false otherwise.
	 */
	private boolean quit(Command command) {
		if (command.hasSecondWord()) {
			System.out.println("Quit what?");
			return false;
		} else {
			return true;  // signal that we want to quit
		}
	}

	/**
	 * "back" is entered returned to the previous room player was in, (history of rooms that player visited is in stack roomsVisted)
	 */
	private void goBack() {
		if (roomsVisited.empty()) {
			System.out.println("you are where you started, you can`t go back more");
		} else {
			updateCounters();
			polish.move();
			Room previousRoom = roomsVisited.pop();
			player.setCurrentRoom(previousRoom);
			printRoomInfo(previousRoom);
			System.out.println();

		}
	}

	/**
	 * pickup item , remove it from the room and put it in the players inventory
	 * @param command to get item name
	 */
	private void pickUp(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("pick up what?");
		} else {
			Room currentRoom = player.getCurrentRoom();
			String itemName = command.getSecondWord();
			if (currentRoom.containsItem(itemName)) {
				Item itemToPick = currentRoom.getItem(itemName);
				if(player.pickUp(itemToPick)) {
					currentRoom.removeItem(itemToPick);
				}
			} else {
				System.out.println("there is no such item here");
			}
		}
	}

	/**
	 * prints description of a item specified in command
	 * @param command to get item name
	 */
	private void inspect(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("inspect what?");
		} else {
			String itemName = command.getSecondWord();
			if (!player.getCurrentRoom().containsItem(itemName)) {
				System.out.println("there is no such item here");
			} else {
				player.getCurrentRoom().getItem(itemName).printDescription();
			}
		}
	}

	/**
	 * use item (only magicCompass for now)
	 * @param command to get item to use and other parameter
	 */
	private void useItem(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("use what?");
		} else if (!command.getSecondWord().equals("magicCompass")) {
			System.out.println("you cannot use that");
		} else {
			String itemToBeUsed = command.getSecondWord();
			String thirdWord = command.getThirdWord();
			if (itemToBeUsed.equals("magicCompass")) {
				if(player.isInInventory("magicCompass")){
				if (!command.hasThirdWord()) {
					System.out.println("use " + command.getSecondWord() + " on what distance?");
				}
				else {
					if (thirdWord.matches("-?\\d+")) {
						//using regular expression to check if it is an int
						if (player.getTurnCounter() <= 0) {  //check cooldown
							int distance = map.getShortestPathLength(player, polish);
							if (Integer.parseInt(thirdWord) > 0) {

								if (distance <= Integer.parseInt(thirdWord)) {
									System.out.println("polish is closer than (or equal) " + thirdWord + " rooms");
								}
								else {
									System.out.println("polish is further away than " + thirdWord + " rooms");
								}
								player.setTurnCounter(2);
							}
							else {
								System.out.println("distance must be positive");
							}
						}
						else {
								System.out.println("magicCompass is still on cooldown");
							}

						}
					else {
						System.out.println("you need to give distance as integer");
						}
					}
				}
				else{
					System.out.println("you don`t have magicCompass");
				}
			}
		}
	}

	/**
	 * remove item from player inventory
	 * @param command contains information about item to be dropped
	 */
	private void dropItem(Command command) {
		String itemName = command.getSecondWord();
		if (player.isInInventory(itemName)) {
			Item itemToBeDropped = player.getItem(itemName);
			player.getCurrentRoom().putItemRoom(itemToBeDropped);
			player.dropItem(itemName);
			System.out.println("you have dropped " + itemName);
		} else {
			System.out.println("you cannot drop this item, because there is no such item in your inventory");
		}
	}

	/**
	 * create an item if player has all the components required
	 * @param command contains information about which item to create (second word)
	 */
	private void create(Command command) {

		if (!command.hasSecondWord()) {
			System.out.println("create what?");
		} else {
			if (command.getSecondWord().equals("TheSolution")) {
				if (!player.isInInventory("Geometry")) {
					System.out.println("you don`t have Geometry");
				}
				if (!player.isInInventory("Analysis")) {
					System.out.println("you don`t have Analysis");
				}
				if (!player.isInInventory("Combinatorics")) {
					System.out.println("you don`t have Combinatorics");
				}
				if(player.isInInventory("Combinatorics") && player.isInInventory("Analysis")
						&& player.isInInventory("Geometry")){
					long itemWeight = player.getItem("Geometry").getWeight() + player.getItem("Analysis").getWeight()+ player.getItem("Combinatorics").getWeight();
					if(player.getCurrentLoad() + map.getItem("TheSolution").getWeight() - itemWeight <= player.getMaximumLoad()) {
						player.dropItem("Geometry");
						player.dropItem("Analysis");
						player.dropItem("Combinatorics");
						player.pickUp(map.getItem("TheSolution"));
					}
					else{
						System.out.println("you cannot carry anymore, please drop items");

					}
				}
			}
			else if (command.getSecondWord().equals("GreatBluff")) {
				if (!player.isInInventory("thesisAssumption")) {
					System.out.println("you don`t have thesisAssumption");
				}
				if (!player.isInInventory("qed")) {
					System.out.println("you don`t have qed");
				}
				if (!player.isInInventory("bluff")) {
					System.out.println("you don`t have bluff");
				}
				if(player.isInInventory("bluff") && player.isInInventory("qed")
						&& player.isInInventory("thesisAssumption")) {
						long itemWeight = player.getItem("bluff").getWeight() + player.getItem("thesisAssumption").getWeight()+ player.getItem("qed").getWeight();
						if(player.getCurrentLoad() + map.getItem("GreatBluff").getWeight() - itemWeight <= player.getMaximumLoad()){
							player.dropItem("bluff");
							player.dropItem("qed");
							player.dropItem("thesisAssumption");
							player.pickUp(map.getItem("GreatBluff"));
						}
						else{
							System.out.println("you cannot carry anymore, please drop items");
						}

				}
			}
			else {
				System.out.println("there is no such item");
			}
		}
	}

	/**
	 * method that is called when player and enemy are in the same room
	 */
	private void playerEnemyFight() {
		System.out.println("polish has found you");
		if (player.isInInventory("TheSolution")) {
			polish.die();
		}
		else if (player.isInInventory("GreatBluff")) {
			polish.setStunned();
			System.out.println("GreatBluff is so well designed that even polish believes \n" +
					"that it is a true solution and gets stunned for your next 3 moves, however you can use " +
					"GreatBluff only once");
			player.dropItem("GreatBluff");
		}
		else if (polish.isStunned()) {
			System.out.println("you are in the same room as polish, however polish is stunned, you will live for now");
		}
		else {
			player.die();
		}
	}

	/**
	 * method that updates counters for cooldown and stun in player and enemy objects
	 */
	private void updateCounters() {
		player.setTurnCounter(player.getTurnCounter() - 1);
		polish.setTurnCounter(polish.getTurnCounter() - 1);
	}

	/**
	 * method that is called when input is "pass",  lets polish make a move while player stays in the same room
	 */
	private void pass() {
		updateCounters();
		polish.move();
	}

	/**
	 * method that is called when input is "save"
	 * saves game progress to a file
	 * @param command contains in second word path to file in which we want to save the game progress
	 */
	private void save(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("save where?");
			System.out.println("make sure path is format of for example c:\\file.csv (use double backslash for every backslash in path and of type .csv)");
			System.out.println("pleas take care that the path leads to the same drive as the game`s");
		}
		else {
			FileManagement.saveProgress(map, player, polish, command.getSecondWord());
		}
	}

	/**
	 * loads game progess from a file
	 * @param command contains path to a file from which game loads progress (second word)
	 */
	private void load(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("read from where?");
			System.out.println("make sure path is format of for example c:\\folder (use double backslash for every backslash in path)");
			System.out.println("please take care that the file exists");
		}
		else {
			FileManagement.readProgress(map, player, polish, command.getSecondWord());
		}
	}
	private void die(){
		player.die();
	}
}
