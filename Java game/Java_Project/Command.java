/**
	Class where we implement how to issue a command
 */
public class Command
{
	private String firstWord;
	private String secondWord;
	private String thirdWord;

	/**
	 * Create a command object. First and second word must be supplied, but
	 * either one (or both) can be null.
	 * @param firstWord The first word of the command. Null if the command
	 *                  was not recognised.
	 * @param secondWord The second word of the command.
	 * @param thirdWord The second third of the command.
	 */
	public Command(String firstWord, String secondWord, String thirdWord)
	{
		this.firstWord = firstWord;
		this.secondWord = secondWord;
		this.thirdWord = thirdWord;
	}

	/**
	 * Return the command word (the first word) of this command. If the
	 * command was not understood, the result is null.
	 * @return The command word.
	 */
	public String getFirstWord()
	{
		return firstWord;
	}

	/**
	 * @return The second word of this command. Returns null if there was no
	 * second word.
	 */
	public String getSecondWord()
	{
		return secondWord;
	}
	/**
	 * @return The third word of this command. Returns null if there was no
	 * second word.
	 */
	public String getThirdWord()
	{
		return thirdWord;
	}

	/**
	 * @return true if this command was not understood.
	 */
	public boolean isUnknown()
	{
		return (firstWord == null);
	}

	/**
	 * @return true if the command has a second word.
	 */
	public boolean hasSecondWord()
	{
		return (secondWord != null);
	}
	/**
	 * @return true if the command has a third word.
	 */
	public boolean hasThirdWord()
	{
		return (thirdWord != null);
	}
}

