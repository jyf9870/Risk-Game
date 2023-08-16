package edu.duke.ece651.team2.shared;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents the territory on the map
 */
public class Territory implements Serializable {
  private final String name;
  private final HashMap<Territory, Integer> neighbors;
  private final HashMap<String, Integer> resourceRate;
  private String ownerName;
  private LinkedHashMap<String, Integer> myInfantry;
  private LinkedHashMap<String, Integer> enemyInfantry;
  private boolean isCloaked;

  /**
   * Constructs a territory
   *
   * @param name      represents the name of the territory, e.g.,"Hogwarts"
   * @param ownerName represents the owner name of the territory
   * @param myUnitCt  represents the count of units within the territory
   * @param foodRate  represents the food rate of the territory
   * @param techRate  represents the technology rate of the territory
   */
  public Territory(String name, String ownerName, int myUnitCt, int foodRate, int techRate) {
    this.name = name;
    this.resourceRate = new HashMap<>();
    this.resourceRate.put("technology", techRate);
    this.resourceRate.put("food", foodRate);
    this.ownerName = ownerName;
    this.neighbors = new HashMap<>();
    this.myInfantry = new LinkedHashMap<>();
    this.enemyInfantry = new LinkedHashMap<>();
    this.initInfantries(myUnitCt, 0);
    this.isCloaked = false;
  }

  public Territory(String name, String ownerName, int foodRate, int techRate, boolean isCloaked) {
    this.name = name;
    this.ownerName = ownerName;
    this.neighbors = new HashMap<>();
    this.resourceRate = new HashMap<>();
    this.resourceRate.put("technology", techRate);
    this.resourceRate.put("food", foodRate);
    this.myInfantry = new LinkedHashMap<>();
    this.enemyInfantry = new LinkedHashMap<>();
    this.isCloaked = isCloaked;
  }


  public Territory(Territory another) {
    this(another.getName(), another.getOwnerName(), another.getFoodRate(), another.getTechRate(), another.isCloaked());
    for (Territory t : another.neighbors.keySet()) {
      this.neighbors.put(t, another.neighbors.get(t));
    }
    for (String s : another.getMyInfantry().keySet()) {
      this.myInfantry.put(s, another.getMyInfantry().get(s));
    }
    for (String s : another.getEnemyInfantry().keySet()) {
      this.enemyInfantry.put(s, another.getEnemyInfantry().get(s));
    }
  }

  private void initInfantries(int myStartingUnits, int enemyStartingUnits) {
    this.myInfantry.put("Level1Unit", myStartingUnits);
    this.myInfantry.put("Level2Unit", 0);
    this.myInfantry.put("Level3Unit", 0);
    this.myInfantry.put("Level4Unit", 0);
    this.myInfantry.put("Level5Unit", 0);
    this.myInfantry.put("Level6Unit", 0);
    this.myInfantry.put("Level7Unit", 0);
    this.myInfantry.put("SpyUnit", 0);

    this.enemyInfantry.put("Level1Unit", enemyStartingUnits);
    this.enemyInfantry.put("Level2Unit", 0);
    this.enemyInfantry.put("Level3Unit", 0);
    this.enemyInfantry.put("Level4Unit", 0);
    this.enemyInfantry.put("Level5Unit", 0);
    this.enemyInfantry.put("Level6Unit", 0);
    this.enemyInfantry.put("Level7Unit", 0);
    this.enemyInfantry.put("SpyUnit", 0);
  }

  /**
   * @param t is the territory to be added
   */
  public void addNeighbor(Territory t, int distance) {
    neighbors.put(t, distance);
  }

  /**
   * find the minimum distance
   *
   * @param t is the destination territory
   * @return the minimum distance between from territory this and destination
   * Territory t is the destination territory
   * MapRO m is the read-only map
   */
  public int findMinRoute(Territory t, MapRO m, boolean ignoreOwner) {
    HashMap<Territory, Integer> distance = new HashMap<>();
    HashSet<String> visited = new HashSet<>();
    PriorityQueue<Territory> pq = new PriorityQueue<>(
        Comparator.comparingInt(a -> distance.getOrDefault(a, Integer.MAX_VALUE)));

    distance.put(this, 0);
    pq.offer(this);

    while (!pq.isEmpty()) {
      Territory curr = pq.poll();
      visited.add(curr.getName());

      if (curr.getName().equals(t.getName())) {
        return distance.get(curr);
      }

      for (Territory neighbor : curr.neighbors.keySet()) {
        if (!visited.contains(neighbor.getName()) &&
            (ignoreOwner || ownerName.equals(m.getTerritory(neighbor.getName()).getOwnerName()))) {
          int dist = distance.get(curr) + curr.neighbors.get(neighbor);
          if (dist < distance.getOrDefault(neighbor, Integer.MAX_VALUE)) {
            distance.put(neighbor, dist);
            pq.offer(neighbor);
          }
        }
      }
    }

    return -1; // if t is not reachable from this Territory
  }

  /**
   * find the distance between two neared territories
   *
   * @param t is the destination territory
   * @return the distance between from territory this and destination territory t
   */
  public int findDistance(Territory t) {
    for (Territory neighbor : neighbors.keySet()) {
      if (neighbor.getName().equals(t.getName())) {
        return neighbors.get(neighbor);
      }
    }
    return -1;
  }


  /**
   * @return name of territory
   */
  public String getName() {
    return name;
  }

  /**
   * @return ownerName of territory
   */
  public String getOwnerName() {
    return ownerName;
  }

  /**
   * @return myUnitCt of territory
   */
  public int getMyUnitCt(String unitType) {
    return this.myInfantry.get(unitType);
  }

  /**
   * @return enemyUnitCt of territory
   */
  public int getEnemyUnitCt(String unitType) {
    return this.enemyInfantry.get(unitType);
  }

  /**
   * @return neighbors of territory
   */
  public HashMap<Territory, Integer> getNeighbors() {
    return neighbors;
  }

  public void updateUnit(int newUnit, String unitType, boolean isEnemy) {
    if (isEnemy) {
      enemyInfantry.put(unitType, newUnit);
    } else {
      myInfantry.put(unitType, newUnit);
    }
  }

  /**
   * @param newOwner is the new ownerName of territory
   */
  public void updateOwner(String newOwner) {
    ownerName = newOwner;
  }

  /**
   * @param t is the territory
   * @return if this territory has neighbor
   */
  public boolean hasNeighbor(Territory t) {
    for (Territory n : neighbors.keySet()) {
      if (n.getName().equals(t.getName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param m is the read-only map
   * @return true if this territory has neighbor owned by ownerName, false otherwise
   */
  public boolean hasNeighborOwnedByEnemy(MapRO m) {
    for (Territory n : neighbors.keySet()) {
      if (!m.getTerritory(n.getName()).getOwnerName().equals(ownerName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param t is the territory
   * @return if the ownerName equals to the territory's ownerName
   */
  public boolean isOwnTerritory(Territory t) {
    return ownerName.equals(t.getOwnerName());
  }

  /**
   * This method uses BFS to search for a path between two territories
   * connected by territories controlled by the same owner
   *
   * @param t represents the destination territory
   * @param m represents the read-only map
   * @return true is there exists a path, false otherwise
   */
  public boolean hasRoute(Territory t, MapRO m, boolean ignoreOwner) {
    if (!isOwnTerritory(t) && !ignoreOwner) {
      return false;
    }
    HashSet<String> visited = new HashSet<>();
    LinkedList<Territory> queue = new LinkedList<>();
    visited.add(name);
    queue.add(this);
    while (queue.size() != 0) {
      Territory next = queue.poll();
      for (Territory n : next.getNeighbors().keySet()) {
        if (n.getName().equals(t.getName()) &&
            (ignoreOwner || m.getTerritory(n.getName()).getOwnerName().equals(t.getOwnerName()))) {
          return true;
        }
        if ((ignoreOwner || ownerName.equals(m.getTerritory(n.getName()).getOwnerName())) && !visited.contains(n.getName())) {
          visited.add(n.getName());
          queue.add(n);
        }
      }
    }
    return false;
  }

  /**
   * @param infantryType is the type of the infantry
   * @param amount       is the amount number of infantry
   * @param isEnemy      is to decide if is find my infantry or enemy infantry
   */
  public void incrementInfantry(String infantryType, int amount, boolean isEnemy) {
    if (isEnemy) {
      enemyInfantry.put(infantryType, enemyInfantry.get(infantryType) + amount);
    } else {
      myInfantry.put(infantryType, myInfantry.get(infantryType) + amount);
    }
  }


  /**
   * @return the infantry type
   */
  public Set<String> getInfantryTypes() {
    return myInfantry.keySet();
  }

  /**
   * Get the linked hashmap of my infantry or enemy infantry
   *
   * @param isEnemy is to decide if is find my infantry or enemy infantry
   * @return the corresponding infantry of different levels
   */
  public LinkedHashMap<String, Integer> getInfantry(boolean isEnemy) {
    if (isEnemy) {
      return enemyInfantry;
    } else {
      return myInfantry;
    }
  }

  /**
   * need this so that if upgrade order fails, can just update it with the original
   */
  public void updateInfantry(LinkedHashMap<String, Integer> updatedInfantry, boolean isEnemy) {
    if (isEnemy) {
      this.enemyInfantry = updatedInfantry;
    } else {
      this.myInfantry = updatedInfantry;
    }
  }

  /**
   * @return the food rate
   */
  public int getFoodRate() {
    return resourceRate.get("food");
  }

  /**
   * @return the tech rate
   */
  public int getTechRate() {
    return resourceRate.get("technology");
  }

  /**
   * @return a linked hashmap of enemy infantry
   */
  public LinkedHashMap<String, Integer> getEnemyInfantry() {
    return enemyInfantry;
  }

  /**
   * @return a linked hashmap of my infantry
   */
  public LinkedHashMap<String, Integer> getMyInfantry() {
    return myInfantry;
  }


  /**
   * override equals to compare Territory
   *
   * @param o is the compared object
   * @return if the two objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      Territory c = (Territory) o;
      if (myInfantry.size() != c.getMyInfantry().size()) {
        return false;
      }
      for (String s : myInfantry.keySet()) {
        if (!myInfantry.get(s).equals(c.myInfantry.get(s))) {
          return false;
        }
      }
      return name.equals(c.getName()) && ownerName.equals(c.getOwnerName()) &&
          Objects.equals(resourceRate.get("food"), c.resourceRate.get("food")) &&
          Objects.equals(resourceRate.get("tech"), c.resourceRate.get("tech"));
    }
    return false;
  }

  /**
   * @return the String of a territory contents
   */
  @Override
  public String toString() {
    String res = "<<" + name + ">>" + "\nOwner: " + ownerName;
    return res;
  }

  public String display(boolean isMyOwnTerritory, boolean hasSpy) {
    HashMap<String, String> unitLevelNameMapper = new HashMap<>();
    unitLevelNameMapper.put("Level1Unit", "L1");
    unitLevelNameMapper.put("Level2Unit", "L2");
    unitLevelNameMapper.put("Level3Unit", "L3");
    unitLevelNameMapper.put("Level4Unit", "L4");
    unitLevelNameMapper.put("Level5Unit", "L5");
    unitLevelNameMapper.put("Level6Unit", "L6");
    unitLevelNameMapper.put("Level7Unit", "L7");
    unitLevelNameMapper.put("SpyUnit", "Your Spy");
    LinkedHashMap<String, Integer> unitCounter = new LinkedHashMap<>();
    for (String s : myInfantry.keySet()) {
      if ((isMyOwnTerritory && myInfantry.get(s) > 0) || (!isMyOwnTerritory && !s.equals("SpyUnit") && myInfantry.get(s) > 0)) {
        unitCounter.put(unitLevelNameMapper.get(s), myInfantry.get(s));
      }
    }
    String res = this + "\n" + "FR: " + resourceRate.get("food") + "\t" +
        "TR: " + resourceRate.get("technology") + "\n" + "--Units--" + "\n" +
        unitCounter.keySet().stream()
            .map(key -> key + ": " + unitCounter.get(key))
            .collect(Collectors.joining("\t"));
    if (!isMyOwnTerritory && enemyInfantry.get("SpyUnit") > 0 && hasSpy) {
      res += "\tYour Spy: " + enemyInfantry.get("SpyUnit");
    }
    return res;
  }

  public void setCloak(boolean isCloaked) {
    this.isCloaked = isCloaked;
  }

  public boolean isCloaked() {
    return isCloaked;
  }
}
