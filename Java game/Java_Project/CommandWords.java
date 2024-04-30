import java.util.HashMap;

/**
 Class where we store commands
 */
public class CommandWords
{

	private static HashMap<String, CommandWord> validCommands;


	/**
	 * Constructor - initialise the command words.
	 */
	public static void initialiseCommandWords()
	{
		validCommands = new HashMap<>();
		validCommands.put("go", CommandWord.GO);
		validCommands.put("quit", CommandWord.QUIT);
		validCommands.put("help", CommandWord.HELP);
		validCommands.put("back", CommandWord.BACK);
		validCommands.put("pickup", CommandWord.PICKUP);
		validCommands.put("inspect", CommandWord.INSPECT);
		validCommands.put("inventory", CommandWord.INVENTORY);
		validCommands.put("use", CommandWord.USE);
		validCommands.put("drop",CommandWord.DROP);
		validCommands.put("create", CommandWord.CREATE);
		validCommands.put("pass", CommandWord.PASS);
		validCommands.put("roomInfo", CommandWord.INFO);
		validCommands.put("save", CommandWord.SAVE);
		validCommands.put("load", CommandWord.LOAD);
		validCommands.put("map", CommandWord.MAP);
		validCommands.put("die", CommandWord.DIE);
	}

	/**
	 * Check whether a given String is a valid command word.
	 * @return true if it is, false if it isn't.
	 */
	public static boolean isCommand(String aString)
	{

		return validCommands.containsKey(aString);
	}

	/**
	 * Print all valid commands to System.out.
	 */
	public static void showAll()
	{
		validCommands.forEach((name, command)->System.out.print(name + " "));
		System.out.println();
	}

	/**
	 *  creates a CommandWord from string
	 * @param command a string to be processed into CommandWord object
	 * @return a CommandWord object
	 */
	public static CommandWord getCommandWord(String command){
		if(!validCommands.containsKey(command)) {
			return CommandWord.UNKNOWN;
		}
		return validCommands.get(command);
	}

}
