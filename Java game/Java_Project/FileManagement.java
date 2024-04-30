import java.io.*;
import java.util.HashMap;

/**
 * class that contains methods required to save game progress to a file and
 * load game progress from a file
 */
public class FileManagement {
	/**
	 * saves game progress to a file
	 * file format
	 * position,playerPosition,enemyPosition
	 * inventory,list of item in inventory seperated with ","
	 * cooldown,playerCounter,EnemyCounter
	 * for every room
	 * room roomName(for example room1 (in code room.getName()) List of items seperated with ","
	 *
	 * @param map      map object of currently running game
	 * @param player   player object of currently running game
	 * @param enemy    enemy object of currently running game
	 * @param filePath file path to a file in which we want to save game progress
	 *                 file path must be valid (leading to .csv file and be in the same drive as game to
	 *                 have permission for creating and saving file there)
	 */
	public static void saveProgress(Map map, Player player, Enemy enemy, String filePath) {
		if (player.getIsAlive()) {
			File file = new File(filePath);
			try {
				FileWriter outputFile = new FileWriter(file);
				BufferedWriter writer = new BufferedWriter(outputFile);
				writer.write("position," + player.getCurrentRoom().getName() + "," + enemy.getCurrentRoom().getName() + "," + "\n");
				writer.write("inventory," + player.getCurrentLoad() + "," + hashmapItemToString(player.getInventory()) + "\n");
				writer.write("cooldown," + player.getTurnCounter() + "," + enemy.getTurnCounter() + ",\n");
				map.getRooms().forEach((name, room) -> {
					try {
						writer.write("room," + name + "," + hashmapItemToString(room.getItems()) + "\n");
					} catch (IOException e) {
						System.out.println("error while saving :" + e.toString());
					}
				});
				writer.close();
			} catch (IOException e) {
				System.out.println("error while saving :" + e.toString());
			}
		}
	}

	/**
	 * loads from a file game progress
	 * file format
	 * position,playerPosition,enemyPosition
	 * inventory,list of item in inventory seperated with ","
	 * cooldown,playerCounter,EnemyCounter
	 * for every room
	 * room roomName(for example room1 (in code room.getName()) List of items seperated with ","
	 * @param map current game map object
	 * @param player current game player object
	 * @param enemy current game enemy object
	 * @param filePath file path to an object in which game progress is saved ,
	 *                 must be .csv and exist
	 */
	public static void readProgress(Map map,Player player,Enemy enemy,String filePath) {
		if (player.getIsAlive()) {
			try {
				FileReader file = new FileReader(filePath);
				BufferedReader reader = new BufferedReader(file);
				try {
					String unparsedLine;
					while ((unparsedLine = reader.readLine()) != null) {
						String[] parsedLine = unparsedLine.split(",");
						//first word of a line is an identifier i.e position, inventory, cooldown, room
						if (parsedLine[0].equals("position")) {
							//based on the way how we saved file
							if (parsedLine.length < 3) {
								System.out.println("file is corrupted, missing information");
							}
							else {
								if (map.getRooms().containsKey(parsedLine[1]) && map.getRooms().containsKey(parsedLine[2])) {
									Room playerRoom = map.getRooms().get(parsedLine[1]);
									Room enemyRoom = map.getRooms().get(parsedLine[2]);
									player.setCurrentRoom(playerRoom);
									enemy.setCurrentRoom(enemyRoom);
								}
								else if (map.getRooms().containsKey(parsedLine[1]) && parsedLine[2].equals("teleportRoom")) {
									Room playerRoom = map.getRooms().get(parsedLine[1]);
									Room enemyRoom = map.getTeleportRoom();
									player.setCurrentRoom(playerRoom);
									enemy.setCurrentRoom(enemyRoom);
								}
								else if (parsedLine[1].equals("teleportRoom") && map.getRooms().containsKey(parsedLine[2])) {
									Room playerRoom = map.getTeleportRoom();
									Room enemyRoom = map.getRooms().get(parsedLine[2]);
									player.setCurrentRoom(playerRoom);
									enemy.setCurrentRoom(enemyRoom);
								}
							}
						}
						else if (parsedLine[0].equals("inventory")) {
							player.getInventory().clear();
							player.setCurrentLoad(0);
							for (int i = 1; i < parsedLine.length; i++) {
								if (map.containsItem(parsedLine[i])) {
									player.pickUp(map.getItem(parsedLine[i]));
								}
							}
						}
						else if (parsedLine[0].equals("room")) {
							if (parsedLine.length < 2) {
								System.out.println("corrupted file, missing information");
							}
							else {
								Room room = map.getRooms().get(parsedLine[1]);
								room.getItems().clear();
								for (int i = 2; i < parsedLine.length; i++) {
									if (map.containsItem(parsedLine[i])) {
										room.putItemRoom(map.getItem(parsedLine[i]));
									}
								}
							}
						}
						else if (parsedLine[0].equals("cooldown")) {
							if (parsedLine.length > 2) {
								player.setTurnCounter(Integer.parseInt(parsedLine[1]));
								enemy.setTurnCounter(Integer.parseInt(parsedLine[2]));
							}
							else {
								System.out.println("missing information");
								player.setTurnCounter(0);
								enemy.setTurnCounter(0);
							}
						}
						else {
							System.out.println("corrupted file, wrong information");
						}
					}
				} catch (IOException e) {
					System.out.println("error: " + e.getMessage());
				}

			} catch (FileNotFoundException e) {
				System.out.println("error " + e.getMessage());
			}
		}
	}

	/**
	 * get a string of all item names in a hashmap
	 * @param hashMap a hashmap of item and its name from which we want to get string of all items in it
	 * @return string of names of items in the hashMap
	 */
	private static String hashmapItemToString(HashMap<String, Item> hashMap){
		String string = "";
		String[] items = hashMap.keySet().toArray(new String[hashMap.size()]);
		for(int i =0;i<items.length;i++){

				string +=items[i]+",";

		}
		return string;
	}

}
