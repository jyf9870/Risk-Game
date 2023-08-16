package edu.duke.ece651.team2.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the map
 */
public class Map implements MapRO, Serializable {
  private final ArrayList<Territory> territories;

  /**
   * Constructs a map with pre-set territories
   */
  public Map() {
    this.territories = new ArrayList<>();
    this.initialize();
  }

  /**
   * copy constructor of map
   *
   * @param another is another map
   */
  public Map(Map another) {
    this.territories = new ArrayList<>();
    for (Territory t : another.territories) {
      this.territories.add(new Territory(t));
    }
  }

  /**
   * @param t is the territory needs to be added to the map
   */
  private void addTerritory(Territory t) {
    territories.add(t);
  }

  private void addNeighborToEachOther(Territory x, Territory y, int distance) {
    x.addNeighbor(y, distance);
    y.addNeighbor(x, distance);
  }

  /**
   * initialize the map
   */
  private void initialize() {
    Territory e = new Territory("Elantris", "A", 15, 12, 10);
    Territory s = new Territory("Scadrial", "A", 8, 10, 8);
    Territory md = new Territory("Mordor", "A", 10, 7, 6);
    Territory r = new Territory("Roshar", "A", 7, 14, 12);
    Territory h = new Territory("Hogwarts", "A", 5, 7, 9);
    Territory g = new Territory("Gondor", "B", 15, 17, 15);
    Territory n = new Territory("Narnia", "B", 12, 15, 14);
    Territory m = new Territory("Mid", "B", 7, 8, 7);
    Territory o = new Territory("Oz", "B", 11, 10, 9);


    addNeighborToEachOther(e, n, 7);
    addNeighborToEachOther(e, m, 8);
    addNeighborToEachOther(e, s, 3);
    addNeighborToEachOther(e, r, 3);
    addNeighborToEachOther(o, g, 4);
    addNeighborToEachOther(n, m, 4);
    addNeighborToEachOther(m, o, 3);
    addNeighborToEachOther(m, s, 9);
    addNeighborToEachOther(o, s, 9);
    addNeighborToEachOther(o, md, 8);
    addNeighborToEachOther(g, md, 10);
    addNeighborToEachOther(s, r, 3);
    addNeighborToEachOther(s, h, 4);
    addNeighborToEachOther(s, md, 4);
    addNeighborToEachOther(md, h, 4);
    addNeighborToEachOther(r, h, 3);

    addTerritory(e);
    addTerritory(s);
    addTerritory(md);
    addTerritory(r);
    addTerritory(h);
    addTerritory(g);
    addTerritory(n);
    addTerritory(m);
    addTerritory(o);

  }

  /**
   * @return all the territories within the map
   */
  @Override
  public ArrayList<Territory> getTerritories() {
    return territories;
  }

  /**
   * @param ownerName represents the name of the player
   * @return all the territories owned by the input player name within the map
   */
  @Override
  public ArrayList<Territory> getPlayerTerritory(String ownerName) {
    ArrayList<Territory> res = new ArrayList<>();
    for (Territory t : territories) {
      if (t.getOwnerName().equals(ownerName)) {
        res.add(t);
      }
    }
    return res;
  }

  /**
   * @return a hash map representing territories owned by each player
   */
  @Override
  public HashMap<String, ArrayList<Territory>> getPlayerTerritories() {
    HashMap<String, ArrayList<Territory>> res = new HashMap<>();
    for (Territory t : territories) {
      String ownerName = t.getOwnerName();
      if (!res.containsKey(ownerName)) {
        res.put(ownerName, new ArrayList<>());
      }
      res.get(ownerName).add(t);
    }
    return res;
  }

  /**
   * @param name represents the name of the territory
   * @return the territory having the input name if the map has it, null otherwise
   */
  @Override
  public Territory getTerritory(String name) {
    for (Territory t : territories) {
      if (t.getName().equals(name)) {
        return t;
      }
    }
    return null;
  }


  /**
   * Update the unit count of the territory having the input name with the input unitCt
   *
   * @param name   represents the name of the territory
   * @param unitCt represents the updated unit count
   */
  public void updateTerritoryUnitCt(String name, int unitCt, String unitType, boolean isEnemy) {
    for (Territory t : territories) {
      if (t.getName().equals(name)) {
        t.updateUnit(unitCt, unitType, isEnemy);
        return;
      }
    }
    throw new IllegalArgumentException("Input territory is not found!");
  }


  /**
   * Update the owner of the territory having the input territoryName with the input ownerName
   *
   * @param territoryName represents the name of the territory
   * @param ownerName     represents the name of the new owner
   */
  public void updateTerritoryOwner(String territoryName, String ownerName) {
    for (Territory t : territories) {
      if (t.getName().equals(territoryName)) {
        t.updateOwner(ownerName);
        return;
      }
    }
    throw new IllegalArgumentException("Input territory is not found!");
  }

  /**
   * @param unitCt is the number of units being incremented for each territory
   */
  public void incrementTerritoryUnitCt(int unitCt) {
    for (Territory t : territories) {
      t.incrementInfantry("Level1Unit", unitCt, false);
    }
  }

  /**
   * override equals to compare Map
   *
   * @param o is the compared object
   * @return if the two objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      Map map = (Map) o;
      return map.territories.equals(territories);
    }
    return false;
  }

}
