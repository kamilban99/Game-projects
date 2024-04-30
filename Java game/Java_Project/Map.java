import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
/**
 * class which initialises rooms, sets up exits and items, contains information about map of the game
 */
public class Map {
	private Room startingRoom;
	private Room enemySpawn;
	private Room teleportRoom;
	private String mapStructure;
	private final HashMap<String, Room> rooms;
	private final HashMap<String, Item> items;

	/**
	 * Initialises map object
	 */
	public Map(String path_to_map)  {
		items = new HashMap<>();
		rooms = new HashMap<>();
		createRooms(path_to_map);

	}

	/**
	 * Create all the rooms and link their exits together.
	 * creates and puts items in the rooms
	 */
	private void createRooms(String path)  {
		// path_to_map - path do pliku z encodingiem mapy, wczytujesz plik path_to_map, tworzysz z niego pokoje, i itemy i ustawiasz start i end room;
		try {
			Object obj = new JSONParser().parse(new FileReader(path));
			JSONObject jo = (JSONObject) obj;
			JSONObject encodedMap = (JSONObject) jo.get("map_name");
			encodedMap.get("rooms");
			for (Object roomName  :(JSONArray) encodedMap.get("rooms"))
			{
				Room room = new Room((String) roomName);
				rooms.put((String)roomName, room);
			}

			startingRoom = rooms.get(encodedMap.get("start_spawn_room"));
			enemySpawn = rooms.get(encodedMap.get("enemy_spawn_room"));
			teleportRoom = rooms.get(encodedMap.get( "teleport_room"));
			mapStructure = (String)encodedMap.get( "map_structure");
			encodedMap.get("items");
			for (Object itemName  : ((JSONObject) encodedMap.get("items")).keySet())
			{
				JSONObject encodedItem = (JSONObject)((JSONObject) encodedMap.get("items")).get((String)itemName);
				long weight = (long) encodedItem.get("weight");
				String description = (String) encodedItem.get("description");
				boolean isPickable = (boolean) encodedItem.get("isPickable");
				Item item = new Item((String)itemName, weight, description, isPickable);
				items.put((String)itemName, item);
			}
			System.out.println(items.toString());
			for (Object roomName  :(JSONArray) encodedMap.get("rooms"))
			{
				JSONObject encodedRoom = (JSONObject) encodedMap.get(roomName);
				JSONObject entries = (JSONObject) encodedRoom.get("entries");
				JSONArray encodedItems = (JSONArray) encodedRoom.get("items");
				Room room = rooms.get(roomName);
				for (Object entriesName  : entries.keySet())
				{
					String nextRoomName = (String) entries.get(entriesName);
					Room nextRoom = rooms.get(nextRoomName);
					room.setExit((String)entriesName, nextRoom);
				}

				for (Object itemName  : encodedItems)
				{
					Item item = items.get(itemName);
					room.putItemRoom(item);
				}
				String description = (String) encodedRoom.get("description");
				room.setDescription(description);
			}
		} catch (IOException | ParseException exc) {
			// report error later
			exc.printStackTrace();
		}

	}

	/**
	 * @return startingRoom field value
	 */
	public Room getStartingRoom() {
		return startingRoom;
	}

	/**
	 * finds the shortest path between character1 and character2 with bfs (Breadth-first search) algorithm
	 *
	 * @return distance between character1 and character2, returns -1 if something went wrong
	 */
	public int getShortestPathLength(Character character1, Character character2) {
		Room startingRoom = character1.getCurrentRoom();
		Room goalRoom = character2.getCurrentRoom();
		HashSet<Room> visitedRooms = new HashSet<>();
		LinkedList<Room> roomsToBeSearched = new LinkedList<>();
		HashMap<Room, Room> history = new HashMap<>();
		roomsToBeSearched.addFirst(startingRoom);

		while (!roomsToBeSearched.isEmpty()) {
			Room currentlySearched = roomsToBeSearched.getFirst();
			roomsToBeSearched.removeFirst();
			if (currentlySearched == goalRoom)
				return readHistory(currentlySearched, startingRoom, history);
			//if we got here then roomsToBeSearched is not goalRoom
			Room[] exits = currentlySearched.getExits();
			for (Room exit : exits) {
				if (visitedRooms.contains(exit)) {
					continue;
				} else {
					roomsToBeSearched.addLast(exit);
					history.put(exit, currentlySearched);
				}
				visitedRooms.add(currentlySearched);
			}
		}
		return -1; //if we got here then something is wrong
	}

	/**
	 * from the map of child and parents crated by findTheShortestPath gets the shortest path
	 *
	 * @param node         node to which we want to find shortest path
	 * @param startingRoom node from which we what to find shortest path
	 * @param history      map of child and parent of each graph node (Rooms in both cases)
	 * @return returns lenght of shortest path
	 */
	private int readHistory(Room node, Room startingRoom, HashMap<Room, Room> history) {
		int distance = 0;
		while (node != startingRoom) {
			node = history.get(node);
			distance++;
		}
		return distance;
	}

	/**
	 * @return value of enemySpawn field
	 */
	public Room getEnemySpawn() {
		return enemySpawn;
	}

	/**
	 * @return value of teleportRoom field
	 */
	public Room getTeleportRoom() {
		return teleportRoom;
	}

	/**
	 * @return a random room from all rooms in the map
	 */
	public Room getRandomRoom() {
		Random rand = new Random();
		int arrayLength = rooms.size();
		int randomIndex = rand.nextInt(arrayLength);
		String randomKey = rooms.keySet().toArray(new String[arrayLength])[randomIndex];
		return rooms.get(randomKey);
	}

	/**
	 * @return HashMap with all rooms in the map
	 */
	public HashMap<String, Room> getRooms() {
		return rooms;
	}

	/**
	 * checks if the item with given name exists inGame on can be created
	 *
	 * @param itemName String name of an item we want to find
	 * @return true if item is initialised and exists in game (or can be created)
	 */
	public boolean containsItem(String itemName) {
		return items.containsKey(itemName);
	}

	/**
	 * @param itemName itemName we want to get
	 * @return an item of specified itemName
	 */
	public Item getItem(String itemName) {
		return items.get(itemName);
	}

	/**
	 * prints a simple diagram of room setup
	 */
	public void printMap(Player player) {
		System.out.println(mapStructure);
		System.out.println("You are in " + player.getCurrentRoom().getName());
	}
}