package edu.duke.ece651.team2.client;

import edu.duke.ece651.team2.shared.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ComputerPlayer extends HumanPlayer {
  private final HashMap<String, Integer> upgradeCostTotals;
  private final HashMap<Integer, String> levelMap1;
  private final HashMap<String, Integer> levelMap2;
  public ArrayList<TextAction> actions;
  private int turnNo;

  public ComputerPlayer() throws IOException {
    super();
    this.actions = new ArrayList<>();
    this.turnNo = 1;
    this.upgradeCostTotals = new HashMap<>();
    this.upgradeCostTotals.put("Level1Unit", 0);
    this.upgradeCostTotals.put("Level2Unit", 3);
    this.upgradeCostTotals.put("Level3Unit", 11);
    this.upgradeCostTotals.put("Level4Unit", 30);
    this.upgradeCostTotals.put("Level5Unit", 55);
    this.upgradeCostTotals.put("Level6Unit", 90);
    this.upgradeCostTotals.put("Level7Unit", 140);
    this.upgradeCostTotals.put("SpyUnit", 50);
    this.levelMap1 = new HashMap<>();
    this.levelMap1.put(1, "Level1Unit");
    this.levelMap1.put(2, "Level2Unit");
    this.levelMap1.put(3, "Level3Unit");
    this.levelMap1.put(4, "Level4Unit");
    this.levelMap1.put(5, "Level5Unit");
    this.levelMap1.put(6, "Level6Unit");
    this.levelMap1.put(7, "Level7Unit");
    this.levelMap2 = new HashMap<>();
    this.levelMap2.put("Level1Unit", 1);
    this.levelMap2.put("Level2Unit", 2);
    this.levelMap2.put("Level3Unit", 3);
    this.levelMap2.put("Level4Unit", 4);
    this.levelMap2.put("Level5Unit", 5);
    this.levelMap2.put("Level6Unit", 6);
    this.levelMap2.put("Level7Unit", 7);
  }

  /**
   * constructor for computer player, uses maps to keep track of unit levels
   */
  public ComputerPlayer(String name, Map map, Client client, int techResource, int foodResource) {
    super(name, map, client, techResource, foodResource);
    this.actions = new ArrayList<>();
    this.turnNo = 1;
    this.upgradeCostTotals = new HashMap<>();
    this.upgradeCostTotals.put("Level1Unit", 0);
    this.upgradeCostTotals.put("Level2Unit", 3);
    this.upgradeCostTotals.put("Level3Unit", 11);
    this.upgradeCostTotals.put("Level4Unit", 30);
    this.upgradeCostTotals.put("Level5Unit", 55);
    this.upgradeCostTotals.put("Level6Unit", 90);
    this.upgradeCostTotals.put("Level7Unit", 140);
    this.upgradeCostTotals.put("SpyUnit", 50);
    this.levelMap1 = new HashMap<>();
    this.levelMap1.put(1, "Level1Unit");
    this.levelMap1.put(2, "Level2Unit");
    this.levelMap1.put(3, "Level3Unit");
    this.levelMap1.put(4, "Level4Unit");
    this.levelMap1.put(5, "Level5Unit");
    this.levelMap1.put(6, "Level6Unit");
    this.levelMap1.put(7, "Level7Unit");
    this.levelMap2 = new HashMap<>();
    this.levelMap2.put("Level1Unit", 1);
    this.levelMap2.put("Level2Unit", 2);
    this.levelMap2.put("Level3Unit", 3);
    this.levelMap2.put("Level4Unit", 4);
    this.levelMap2.put("Level5Unit", 5);
    this.levelMap2.put("Level6Unit", 6);
    this.levelMap2.put("Level7Unit", 7);
  }

  /**
   * initialize the computer player
   */
  public void init() throws IOException, ClassNotFoundException {
    receiveName();
    receiveMap();
    receiveResourceInfo();
  }

  /**
   * will upgrade an infantry as much as it can with the given resources
   *
   * @param territoryName:        name of which territory to perform upgrade on
   * @param dummyInfantry:        temporary infantry to keep track of upgrades
   *                              that may involve
   *                              multiple levels
   * @param resourcePerTerritory: amount of resources that this territory can use
   *                              to upgrade
   */
  public void performUpgrade(String territoryName, LinkedHashMap<String, Integer> dummyInfantry,
                             int resourcePerTerritory) {
    int resourceSpent = 0;
    int additionalCurr = 0;
    int additionalNext = 0;
    for (String type : dummyInfantry.keySet()) {
      if (type.equals("Level7Unit")) {
        return;
      }
      String nextType = this.levelMap1.get(this.levelMap2.get(type) + 1);
      for (int i = 0; i < dummyInfantry.get(type) + additionalCurr; i++) {
        resourceSpent += this.upgradeCostTotals.get(nextType) - this.upgradeCostTotals.get(type);
        if (resourceSpent > resourcePerTerritory) {
          return;
        } else {
          this.actions.add(new UpgradeTextAction(UPGRADE, name,
              map.getTerritory(territoryName).getName(), type, nextType, 1));
          additionalNext += 1;
        }
      }
      additionalCurr = additionalNext;
      additionalNext = 0;
    }
  }

  /**
   * @param territoryToDummyMap: temporary map of each territory and its
   *                             infantries that will be modified
   */
  public void modifyTerritoryDummyMap(LinkedHashMap<Territory, LinkedHashMap<String, Integer>> territoryToDummyMap) {
    for (TextAction action : this.actions) {
      if (action.name.equals(ATTACK)) {
        MoveAttackTextAction tAttack = (MoveAttackTextAction) action;
        territoryToDummyMap.remove(map.getTerritory(tAttack.from_territory));
      } else if (action.name.equals(MOVE)) {
        MoveAttackTextAction tMove = (MoveAttackTextAction) action;
        LinkedHashMap<String, Integer> dummyInfantryFrom = territoryToDummyMap
            .get(map.getTerritory(tMove.from_territory));
        LinkedHashMap<String, Integer> dummyInfantryTo = territoryToDummyMap.get(map.getTerritory(tMove.to_territory));
        dummyInfantryFrom.put(tMove.infantryLevel, dummyInfantryFrom.get(tMove.infantryLevel) - tMove.unitCt);
        dummyInfantryTo.put(tMove.infantryLevel, dummyInfantryTo.get(tMove.infantryLevel) + tMove.unitCt);
      }
    }
  }

  /**
   * @param infantry: infantry to check
   * @return boolean of whether all infantry types are empty
   */
  public boolean checkIfAllZero(LinkedHashMap<String, Integer> infantry) {
    for (Integer count : infantry.values()) {
      if (count != 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * will make sure to do some sanity checks on the infantries of each territory
   * then will upgrade all equally
   */
  public void upgradeEqually() {
    LinkedHashMap<Territory, LinkedHashMap<String, Integer>> territoryToDummyMap = new LinkedHashMap<>();

    for (Territory t : map.getPlayerTerritory(name)) {
      territoryToDummyMap.put(t, new LinkedHashMap<>());
      for (String s : t.getMyInfantry().keySet()) {
        territoryToDummyMap.get(t).put(s, t.getMyInfantry().get(s));
      }
    }
    modifyTerritoryDummyMap(territoryToDummyMap);
    ArrayList<Territory> terrToRemove = new ArrayList<>();
    for (Territory t : territoryToDummyMap.keySet()) {
      if (checkIfAllZero(territoryToDummyMap.get(t))) {
        terrToRemove.add(t);
      }
    }
    for (Territory t : terrToRemove) {
      territoryToDummyMap.remove(t);
    }
    if (territoryToDummyMap.size() > 0) {
      int resourcePerTerritory = this.techResource / territoryToDummyMap.size();
      for (Territory t : territoryToDummyMap.keySet()) {
        performUpgrade(t.getName(), territoryToDummyMap.get(t), resourcePerTerritory);
      }
    }
  }

  /**
   * @return the name of the target territory to attack
   */
  public String findWeakestTerritory() {
    int value = Integer.MAX_VALUE;
    String target = "";
    for (Territory t : map.getTerritories()) {
      if (t.getOwnerName().equals(name)) {
        for (Territory neighbor : t.getNeighbors().keySet()) {
          if (!map.getTerritory(neighbor.getName()).getOwnerName().equals(name)) {
            int temp = calculateInfantry(map.getTerritory(neighbor.getName()).getMyInfantry());
            value = Math.min(temp, value);
            if (value == temp) {
              target = neighbor.getName();
            }
          }
        }
      }
    }
    return target;
  }

  /**
   * @param infantry is a Hashmap of infantry
   * @return the value of this territory
   */
  public int calculateInfantry(LinkedHashMap<String, Integer> infantry) {
    int res = 0;
    res += infantry.get("Level1Unit") * 10;
    res += infantry.get("Level2Unit") * 11;
    res += infantry.get("Level3Unit") * 13;
    res += infantry.get("Level4Unit") * 15;
    res += infantry.get("Level5Unit") * 18;
    res += infantry.get("Level6Unit") * 21;
    res += infantry.get("Level7Unit") * 25;
    return res;
  }

  /**
   * @param infantryLevel is a level of infantry
   * @return the value of a single infantry
   */
  public int calculateSingleInfantry(String infantryLevel) {
    HashMap<String, Integer> infantryValue = new HashMap<>();
    infantryValue.put("Level1Unit", 10);
    infantryValue.put("Level2Unit", 11);
    infantryValue.put("Level3Unit", 13);
    infantryValue.put("Level4Unit", 15);
    infantryValue.put("Level5Unit", 18);
    infantryValue.put("Level6Unit", 21);
    infantryValue.put("Level7Unit", 25);
    return infantryValue.get(infantryLevel);
  }

  /**
   * cloak the strongest territory
   */
  public void tryCloak() {
    String strongest = "";
    int strongestStrength = 0;
    for (Territory t : map.getPlayerTerritory(name)) {
      if (calculateInfantry(t.getMyInfantry()) > strongestStrength && t.hasNeighborOwnedByEnemy(map) && !t.isCloaked()) {
        strongest = t.getName();
        strongestStrength = calculateInfantry(t.getMyInfantry());
      }
    }
    if (this.techResource > 25 && !strongest.equals("")) {
      this.techResource -= 25;
      this.actions.add(new CloakTextAction(CLOAK, name, strongest));
    }
  }

  /**
   * play one turn for AI player
   */
  public void playOneTurn() {
    String to = findWeakestTerritory();
    boolean result = attackFromClosest(to);
    // every 3 turns, try cloaking
    if (this.turnNo % 3 == 0) {
      tryCloak();
    }
    upgradeEqually();
    this.turnNo += 1;
  }

  /**
   * return the name of the from territory, if this is an invalid attack(server
   * return false), then don't do attack in this round.
   *
   * @return the name of the from territory
   */
  public boolean attackFromClosest(String target) {

    Territory to = map.getTerritory(target);
    int minRoute = Integer.MAX_VALUE;
    String from = "";
    for (Territory t : to.getNeighbors().keySet()) {
      if (map.getTerritory(t.getName()).getOwnerName().equals(name)) {
        minRoute = Math.min(minRoute, to.findDistance(t));
        if (minRoute == to.findDistance(t)) {
          from = t.getName();
        }
      }
    }

    boolean result = attackAndMoveAction(minRoute, from, to.getName());
    return result;
  }

  /**
   * @param minRoute is the minimum route used to attack
   * @param from     is the name of the from territory
   * @param to       is the name of the to territory
   * @return true if the move and attack is valid, false if not
   */
  public boolean attackAndMoveAction(int minRoute, String from, String to) {
    Territory fromTerr = map.getTerritory(from);
    Territory toTerr = map.getTerritory(to);
    int currValue = calculateInfantry(fromTerr.getMyInfantry());
    int targetValue = calculateInfantry(toTerr.getMyInfantry());

    // no need to move
    if (currValue > targetValue) {
      // validate attack
      if (tryAttack(fromTerr, toTerr, minRoute)) {
        // do attack
        doAttack(fromTerr, toTerr);
        return true;
      }
    } else { // need to move first
      // validate move
      LinkedHashMap<String, Integer> fromInfantry = new LinkedHashMap<>();
      for (String s : fromTerr.getMyInfantry().keySet()) {
        fromInfantry.put(s, fromTerr.getMyInfantry().get(s));
      }
      // do move and attack
      return tryAndDoMove(fromInfantry, fromTerr, toTerr, targetValue, currValue);
    }
    return false;
  }

  /**
   * @param fromInfantry is the infantry of the from territory
   * @param from         is the from territory
   * @param to           is the to territory
   * @param targetValue  is the target value we want to achieve to win the move
   * @param currValue    is the current value of the territory
   * @return true if the move is valid, false if not
   */
  public boolean tryAndDoMove(LinkedHashMap<String, Integer> fromInfantry, Territory from, Territory to,
                              int targetValue, int currValue) {
    int currFoodRsc = 0;
    int prevValue = currValue;
    outerLoop:
    for (Territory t : map.getTerritories()) {
      if (t.getOwnerName().equals(name) && !t.getName().equals(from.getName())) {
        int route = t.findMinRoute(from, map, false);
        if (route != -1) {
          for (String s : map.getTerritory(t.getName()).getMyInfantry().keySet()) {
            for (int i = 0; i < t.getMyInfantry().get(s); i++) {
              currFoodRsc += route;
              if (currFoodRsc > foodResource) {
                return false;
              }
              currValue += calculateSingleInfantry(s);
              fromInfantry.put(s, fromInfantry.get(s) + 1);
              if (currValue > targetValue) {
                break outerLoop;
              }
            }
          }
        }
      }

    }
    int minRoute = from.findDistance(to);
    if (tryAttack(fromInfantry, minRoute, currFoodRsc)) {
      doMove(fromInfantry, from, to, targetValue, prevValue);
      return true;
    }
    return false;
  }

  /**
   * @param fromInfantry is the infantry of the from territory
   * @param from         is the from territory
   * @param to           is the to territory
   * @param targetValue  is the target value we want to achieve to win the attack
   * @param currValue    is the current value of the territory
   */
  public void doMove(LinkedHashMap<String, Integer> fromInfantry, Territory from, Territory to, int targetValue,
                     int currValue) {
    outerLoop:
    for (Territory t : map.getTerritories()) {
      if (t.getOwnerName().equals(name) && !t.getName().equals(from.getName())) {
        int route = t.findMinRoute(from, map, false);
        if (route != -1) {
          for (String s : map.getTerritory(t.getName()).getMyInfantry().keySet()) {
            for (int i = 0; i < t.getMyInfantry().get(s); i++) {
              currValue += calculateSingleInfantry(s);
              actions.add(new MoveAttackTextAction(MOVE, name, t.getName(), from.getName(), 1, s));
              if (currValue > targetValue) {
                break outerLoop;
              }

            }
          }
        }
      }
    }
    doAttack(fromInfantry, from, to);
  }

  /**
   * @param fromInfantry is the infantry of the from territory for attacking
   * @param fromTerr     is the from territory for attacking
   * @param toTerr       is the to territory for attacking
   */
  public void doAttack(LinkedHashMap<String, Integer> fromInfantry, Territory fromTerr, Territory toTerr) {
    for (String s : fromInfantry.keySet()) {
      for (int i = 0; i < fromInfantry.get(s); i++) {
        actions.add(new MoveAttackTextAction(ATTACK, name, fromTerr.getName(), toTerr.getName(), 1, s));
      }
    }
  }

  /**
   * @param fromTerr is the from territory for attacking
   * @param minRoute is the minimum route used to attack
   * @return true if this attack is valid, false if not
   */
  public boolean tryAttack(Territory fromTerr, Territory toTerr, int minRoute) {
    int currFoodRsc = 0;
    int target = calculateInfantry(toTerr.getMyInfantry());
    int curr = 0;
    for (String s : fromTerr.getMyInfantry().keySet()) {
      for (int i = 0; i < fromTerr.getMyInfantry().get(s); i++) {
        curr += calculateSingleInfantry(s);
        currFoodRsc += minRoute;
        if (currFoodRsc > foodResource) {
          return false;
        }
        if (curr > target) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * @param fromInfantry is the infantry of the from territory for attacking
   * @param minRoute     is the minimum route used to attack
   * @param currFoodRsc  is the current food resource
   * @return true if this attack is valid, false if not
   */
  public boolean tryAttack(LinkedHashMap<String, Integer> fromInfantry, int minRoute, int currFoodRsc) {
    for (String s : fromInfantry.keySet()) {
      for (int i = 0; i < fromInfantry.get(s); i++) {
        currFoodRsc += minRoute;
        if (currFoodRsc > foodResource) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * @param fromTerr is the from territory for attacking
   * @param toTerr   is the to territory for attacking
   */
  public void doAttack(Territory fromTerr, Territory toTerr) {
    int target = calculateInfantry(toTerr.getMyInfantry());
    int curr = 0;
    for (String s : fromTerr.getMyInfantry().keySet()) {
      for (int i = 0; i < fromTerr.getMyInfantry().get(s); i++) {
        actions.add(new MoveAttackTextAction(ATTACK, name, fromTerr.getName(), toTerr.getName(), 1, s));
        curr += calculateSingleInfantry(s);
        if (curr > target) {
          return;
        }
      }
    }
  }

  /**
   * play multi turns for AI
   */
  public void play() {
    do {
      try {
        playOneTurn();

        // to print the AI actions
        for (TextAction t : actions) {
          System.out.println(t.toString());
        }
        System.out.println("DONE");

        client.sendActions(actions);
        client.receiveTextMsg();
        map = client.receiveMap();
        String resourceInfo = client.receiveTextMsg();
        updateResources(resourceInfo);

        String gameWinner = receiveWinningStatus();
        if (gameWinner.contains("WIN")) {
          disconnect();
          break;
        } else {
          actions.clear();
        }
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    } while (true);
  }

}
