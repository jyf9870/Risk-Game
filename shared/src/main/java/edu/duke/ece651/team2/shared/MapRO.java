package edu.duke.ece651.team2.shared;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This interface represents basic behaviors of a read-only map
 */
public interface MapRO {
  /**
   * @return all the territories within the map
   */
  ArrayList<Territory> getTerritories();

  /**
   * @param ownerName represents the name of the player
   * @return all the territories owned by the input player name within the map
   */
  ArrayList<Territory> getPlayerTerritory(String ownerName);

  /**
   * @return a hash map representing territories owned by each player
   */
  HashMap<String, ArrayList<Territory>> getPlayerTerritories();

  /**
   * @param name represents the name of the territory
   * @return the territory having the input name if the map has it, null otherwise
   */
  Territory getTerritory(String name);
}
