package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class Master {
  final String MOVE = "M", ATTACK = "A", UPGRADE = "U", CLOAK = "C";
  final Server server;
  private final ActionRuleChecker actionRuleChecker;
  private final ActionRuleCheckerUpgrade actionRuleCheckerUpgrade;
  private final int cloakingCost;
  private final HashMap<String, Integer> upgradeCostTotals;
  public HashMap<String, HashMap<String, Integer>> resourceUnits;
  public HashMap<String, Integer> mainCloak;
  Socket client1, client2;
  Map map;


  /**
   * @param actionRuleChecker is the action rules used in serve to check client actions
   * @throws IOException when there are input or output exceptions
   */

  public Master(ActionRuleChecker actionRuleChecker,
                ActionRuleCheckerUpgrade actionRuleCheckerUpgrade) throws IOException {
    this.actionRuleChecker = actionRuleChecker;
    this.actionRuleCheckerUpgrade = actionRuleCheckerUpgrade;
    this.map = new Map();
    this.server = new Server(12345);
    this.cloakingCost = 25;
    this.resourceUnits = new HashMap<>();
    HashMap<String, Integer> foodRsc = new HashMap<>();
    HashMap<String, Integer> techRsc = new HashMap<>();
    foodRsc.put("A", 50);
    foodRsc.put("B", 50);
    resourceUnits.put("foodRsc", foodRsc);
    techRsc.put("A", 40);
    techRsc.put("B", 40);
    resourceUnits.put("techRsc", techRsc);
    this.upgradeCostTotals = new HashMap<>();
    this.upgradeCostTotals.put("Level1Unit", 0);
    this.upgradeCostTotals.put("Level2Unit", 3);
    this.upgradeCostTotals.put("Level3Unit", 11);
    this.upgradeCostTotals.put("Level4Unit", 30);
    this.upgradeCostTotals.put("Level5Unit", 55);
    this.upgradeCostTotals.put("Level6Unit", 90);
    this.upgradeCostTotals.put("Level7Unit", 140);
    this.upgradeCostTotals.put("SpyUnit", 50);
    this.mainCloak = new HashMap<>();
  }

  /**
   * @throws IOException when there are input or output exceptions
   */

  public Master() throws IOException {
    this(new BasicChecker(new IssuerOwnershipChecker(new PathChecker(new UnitChecker(null)))),
        new BasicCheckerUpgrade(new IssuerOwnershipCheckerUpgrade(new UnitCheckerUpgrade(null))));
  }

  /**
   * initialize the server, send name and map to client
   *
   * @throws IOException when there are input or output exceptions
   */
  public void initPlayers() throws IOException {
    this.client1 = this.server.connectClient();
    this.client2 = this.server.connectClient();

    server.sendTextMsg("A", client1);
    server.sendMap(map, client1);
    server.sendTextMsg("Your total resource:\t\tFood: " + getFoodRsc("A") + "\t\tTech: " + getTechRsc("A"), client1);

    server.sendTextMsg("B", client2);
    server.sendMap(map, client2);
    server.sendTextMsg("Your total resource:\t\tFood: " + getFoodRsc("B") + "\t\tTech: " + getTechRsc("B"), client2);
  }

  /**
   * @param from_unit is the start unit to be calculated
   * @param to_unit   is the end unit to be calculated
   * @return the upgrade cost
   */
  private int calculateUpgradeCost(String from_unit, String to_unit) {
    return this.upgradeCostTotals.get(to_unit) - this.upgradeCostTotals.get(from_unit);
  }

  /**
   * @param socket    is the connected socket
   * @param ownerName is the player's name
   * @return the relevant text actions
   * @throws IOException            when there are input or output exceptions
   * @throws ClassNotFoundException when there are class not found exceptions
   */
  public ArrayList<TextAction> receiveValidTextActions(Socket socket, String ownerName) throws IOException, ClassNotFoundException {
    int foodRequire = 0;
    int techRequire = 0;
    ArrayList<TextAction> textActions = null;
    Map originalMap = new Map(map);
    boolean isValid = false;
    while (!isValid) {
      try {
        textActions = server.receiveActions(socket);
        foodRequire = validateFoodHelper(textActions, foodRequire);
        techRequire = validateTechHelper(textActions, techRequire);

        if (techRequire > resourceUnits.get("techRsc").get(ownerName)) {
          techRequire = 0;
          throw new IllegalArgumentException("Invalid action order!");
        }
        if (foodRequire > resourceUnits.get("foodRsc").get(ownerName)) {
          foodRequire = 0;
          throw new IllegalArgumentException("Your food resource is not enough!");
        }

        isValid = true;
        map = new Map(originalMap);
        server.sendTextMsg("True", socket);
      } catch (IllegalArgumentException e) {
        server.sendTextMsg(e.getMessage(), socket);
        map = new Map(originalMap);

      }
    }
    return textActions;
  }

  public int validateFoodHelper(ArrayList<TextAction> textActions, int foodRequire) {
    ArrayList<TextAction> moveActions = new ArrayList<>();
    ArrayList<TextAction> attackActions = new ArrayList<>();

    for (TextAction t : textActions) {
      switch (t.name) {
        case MOVE:
          moveActions.add(t);
          break;
        case ATTACK:
          attackActions.add(t);
          break;
        case UPGRADE:
        case CLOAK:
          break;
        default:
          throw new IllegalArgumentException("Invalid action type!");
      }
    }
    foodRequire = checkMoveList(moveActions, foodRequire);
    foodRequire = checkAttackList(attackActions, foodRequire);
    return foodRequire;
  }

  public int validateTechHelper(ArrayList<TextAction> textActions, int techRequire) {
    ArrayList<TextAction> upgradeActions = new ArrayList<>();
    ArrayList<TextAction> cloakActions = new ArrayList<>();
    for (TextAction t : textActions) {
      switch (t.name) {
        case MOVE:
        case ATTACK:
          break;
        case UPGRADE:
          upgradeActions.add(t);
          break;
        case CLOAK:
          cloakActions.add(t);
          break;
      }
    }
    techRequire = checkUpgradeList(upgradeActions, techRequire);
    techRequire = checkCloakList(cloakActions, techRequire);

    return techRequire;
  }


  /**
   * @param actions     are the relevant text actions
   * @param foodRequire is the required food resource until now
   * @return the food resource after updating
   */
  public int checkMoveList(ArrayList<TextAction> actions, int foodRequire) {
    for (TextAction t : actions) {
      MoveAttackTextAction moveTextAction = (MoveAttackTextAction) t;
      if (actionRuleChecker.checkAction(moveTextAction, map) == null) {
        //add distance
        Territory from = map.getTerritory(moveTextAction.from_territory);
        Territory to = map.getTerritory(moveTextAction.to_territory);
        foodRequire += moveTextAction.unitCt * from.findMinRoute(to, map, moveTextAction.infantryLevel.equals("SpyUnit"));
        Move moveAction = new Move(moveTextAction.from_territory, moveTextAction.to_territory, moveTextAction.unitCt,
            moveTextAction.infantryLevel, moveTextAction.issuer);
        moveAction.moveUnits(map);
      } else {
        throw new IllegalArgumentException(actionRuleChecker.checkAction(moveTextAction, map));
      }
    }
    return foodRequire;
  }

  /**
   * @param actions     are the relevant text actions
   * @param foodRequire is the required food resource until now
   * @return the food resource after updating
   */
  public int checkAttackList(ArrayList<TextAction> actions, int foodRequire) {
    for (TextAction t : actions) {
      MoveAttackTextAction tAttack = (MoveAttackTextAction) t;
      if (actionRuleChecker.checkAction(tAttack, map) == null) {
        //add distance
        Territory from = map.getTerritory(tAttack.from_territory);
        Territory to = map.getTerritory(tAttack.to_territory);
        foodRequire += tAttack.unitCt * from.findDistance(to);
        Attack attackAction = new Attack(t.issuer, tAttack.from_territory, tAttack.to_territory, tAttack.unitCt, tAttack.infantryLevel, new AdvancedCombatRules());
        attackAction.moveUnits(map);
      } else {
        throw new IllegalArgumentException(actionRuleChecker.checkAction(tAttack, map));
      }
    }
    return foodRequire;
  }

  /**
   * @param actions     are the relevant text actions
   * @param techRequire is the required technology resource until now
   * @return the technology resource after updating
   */
  public int checkUpgradeList(ArrayList<TextAction> actions, int techRequire) {
    for (TextAction t : actions) {
      UpgradeTextAction upgradeTextAction = (UpgradeTextAction) t;
      String test = actionRuleCheckerUpgrade.checkAction(upgradeTextAction, map);
      if (test == null) {
        techRequire += calculateUpgradeCost(upgradeTextAction.from_unit, upgradeTextAction.to_unit) * upgradeTextAction.unitCt;
        Upgrade upgradeAction =
            new Upgrade(upgradeTextAction.upgrade_territory, upgradeTextAction.from_unit, upgradeTextAction.to_unit, upgradeTextAction.unitCt);
        upgradeAction.upgradeUnit(map);
      } else {
        throw new IllegalArgumentException("Invalid upgrade order!");
      }
    }
    return techRequire;
  }

  /**
   * @param actions     are the relevant text actions
   * @param techRequire is the required technology resource until now
   * @return the technology resource after updating
   */
  public int checkCloakList(ArrayList<TextAction> actions, int techRequire) {
    HashSet<String> currentCloak = new HashSet<>();
    for (TextAction t : actions) {
      CloakTextAction cloakTextAction = (CloakTextAction) t;
      if (map.getTerritory(cloakTextAction.territory).getOwnerName().equals(cloakTextAction.issuer) &&
          !currentCloak.contains(cloakTextAction.territory) && !mainCloak.containsKey(cloakTextAction.territory)) {
        currentCloak.add(cloakTextAction.territory);
        techRequire += cloakingCost;
      } else {
        throw new IllegalArgumentException("Invalid cloak order!");
      }
    }
    return techRequire;
  }

  /**
   * server plays one turn of the game
   *
   * @throws IOException            when there are input or output exceptions
   * @throws ClassNotFoundException when there are class not found exceptions
   */
  public void playOneTurn() throws IOException, ClassNotFoundException {
    ArrayList<TextAction> textActionsA = receiveValidTextActions(client1, "A");
    ArrayList<TextAction> textActionsB = receiveValidTextActions(client2, "B");

    ArrayList<String> aWins = new ArrayList<>();
    ArrayList<String> bWins = new ArrayList<>();

    doMove(textActionsA, "A");
    doMove(textActionsB, "B");

    HashSet<String> territoryAttackedA = new HashSet<>();
    HashSet<String> territoryAttackedB = new HashSet<>();
    ArrayList<Attack> attackActionsA = doAttack(textActionsA, "A");
    ArrayList<Attack> attackActionsB = doAttack(textActionsB, "B");
    recordCombatWinner(attackActionsA, territoryAttackedA, aWins, bWins);
    recordCombatWinner(attackActionsB, territoryAttackedB, aWins, bWins);

    doUpgrade(textActionsA, "A");
    doUpgrade(textActionsB, "B");

    doCloak(textActionsA, "A");
    doCloak(textActionsB, "B");

    StringBuilder combatRes = new StringBuilder();
    addWinner(aWins, combatRes, "A");
    addWinner(bWins, combatRes, "B");

    addFoodRsc();
    addTechRsc();
    updateMap();
    server.sendMap(map, client1);
    server.sendTextMsg(combatRes + "Your total resource:\t\tFood: " + getFoodRsc("A") + "\t\tTech: " + getTechRsc("A"), client1);
    server.sendMap(map, client2);
    server.sendTextMsg(combatRes + "Your total resource:\t\tFood: " + getFoodRsc("B") + "\t\tTech: " + getTechRsc("B"), client2);
  }

  private void updateMap() {
    map.incrementTerritoryUnitCt(1);
    HashSet<String> cloakedTerritory = new HashSet<>(mainCloak.keySet());
    for (String name : cloakedTerritory) {
      if (mainCloak.get(name) >= 3) {
        mainCloak.remove(name);
        map.getTerritory(name).setCloak(false);
      } else {
        mainCloak.put(name, mainCloak.get(name) + 1);
      }
    }
  }

  /**
   * @param textActions is the corresponding text action needs to be extracted to move action
   * @param name        is the player's name
   */
  public void doMove(ArrayList<TextAction> textActions, String name) {
    ArrayList<Move> moveActions = extractMove(textActions);
    for (Move move : moveActions) {
      move.moveUnits(map);
      Territory from = map.getTerritory(move.getFromTerritory());
      Territory to = map.getTerritory(move.getToTerritory());
      setMinus("foodRsc", move.getUnitCt() * from.findMinRoute(to, map, move.getInfantryLevel().equals("SpyUnit")), name);
    }
  }

  /**
   * @param textActions is the corresponding text action need to be extracted to move action
   * @param name        is the player's name
   * @return the attack action list
   */
  public ArrayList<Attack> doAttack(ArrayList<TextAction> textActions, String name) {
    ArrayList<Attack> attackActions = extractAttack(textActions);
    for (Attack attack : attackActions) {
      attack.moveUnits(map);
      Territory from = map.getTerritory(attack.getFromTerritory());
      Territory to = map.getTerritory(attack.getToTerritory());
      setMinus("foodRsc", attack.getUnitCt() * from.findDistance(to), name);
    }
    return attackActions;
  }

  /**
   * @param textActions is the corresponding text action need to be extracted to move action
   * @param name        is the player's name
   */
  public void doUpgrade(ArrayList<TextAction> textActions, String name) {
    ArrayList<Upgrade> upgradeActions = extractUpgrade(textActions);
    for (Upgrade upgrade : upgradeActions) {
      if (map.getTerritory(upgrade.getUpgrade_territory()).getOwnerName().equals(name)) {
        upgrade.upgradeUnit(map);
        setMinus("techRsc", upgrade.getUnitCt() * calculateUpgradeCost(upgrade.getFromUnit(), upgrade.getToUnit()), name);
      }
    }
  }

  public void doCloak(ArrayList<TextAction> textActions, String name) {
    ArrayList<Cloak> cloakActions = extractCloak(textActions);
    for (Cloak cloak : cloakActions) {
      cloak.cloakTerritory(map);
      setMinus("techRsc", cloakingCost, name);
      mainCloak.put(cloak.getTerritory(), 0);
    }
  }


  /**
   * @param attackActions     is a list of attack actions
   * @param territoryAttacked is a set of attacked territories
   * @param aWins             is a list of the territory name which player A wins
   * @param bWins             is a list of the territory name which player B wins
   */
  public void recordCombatWinner(ArrayList<Attack> attackActions, HashSet<String> territoryAttacked, ArrayList<String> aWins, ArrayList<String> bWins) {
    for (Attack attack : attackActions) {
      if (!territoryAttacked.contains(attack.getToTerritory())) {
        territoryAttacked.add(attack.getToTerritory());
        String winner = attack.combat(map);
        if (winner != null) {
          Territory to = map.getTerritory(attack.getToTerritory());
          if (winner.equals("A")) {
            aWins.add(to.getName());
          } else {
            bWins.add(to.getName());
          }
        }
      }
    }
  }

  /**
   * Add the winning territory name to a string builder
   *
   * @param wins is a list of the territory name which changed owner name
   * @param sb   is a string builder to record
   * @param name is the player name
   */
  public void addWinner(ArrayList<String> wins, StringBuilder sb, String name) {
    if (wins.size() > 0) {
      sb.append(name);
      sb.append(" successfully occupies ");
      for (int i = 0; i < wins.size(); i++) {
        if (i == wins.size() - 1) {
          sb.append(wins.get(i)).append("\n");
        } else {
          sb.append(wins.get(i)).append(", ");
        }
      }
    }
  }


  /**
   * server plays multi turn of the game
   *
   * @throws IOException            when there are input or output exceptions
   * @throws ClassNotFoundException when there are class not found exceptions
   */
  public void playMultiTurns() throws IOException, ClassNotFoundException {
    String winner;
    while (true) {
      playOneTurn();
      winner = checkWin();
      if (winner.equals("")) {
        server.sendTextMsg(winner, client1);
        server.sendTextMsg(winner, client2);
      } else {
        server.sendTextMsg(winner + "WIN", client1);
        server.sendTextMsg(winner + "WIN", client2);
        break;
      }
    }
  }

  /**
   * check the result of the game
   *
   * @return the name of the winner. If no one wins yet, return an empty String "".
   */
  public String checkWin() {
    Territory flag = map.getTerritories().get(0);
    for (Territory territory : map.getTerritories()) {
      if (!territory.getOwnerName().equals(flag.getOwnerName())) {
        return "";
        // Neither A nor B win
      }
    }
    return flag.getOwnerName();
  }


  /**
   * extract Attack list from TextAction list
   *
   * @param textActions is the list of Test actions need to be extracted
   * @return the list of Attack actions
   */
  public ArrayList<Attack> extractAttack(ArrayList<TextAction> textActions) {
    ArrayList<Attack> attackActions = new ArrayList<>();
    AdvancedCombatRules rule = new AdvancedCombatRules();
    for (TextAction textAction : textActions) {
      if (textAction.name.equals(ATTACK)) {
        MoveAttackTextAction attackTextAction = (MoveAttackTextAction) textAction;
        Attack attackAction = new Attack(attackTextAction.issuer, attackTextAction.from_territory, attackTextAction.to_territory, attackTextAction.unitCt, attackTextAction.infantryLevel, rule);
        attackActions.add(attackAction);
      }
    }
    return attackActions;
  }


  /**
   * extract Move list from TextAction list
   *
   * @param textActions is the list of Test actions need to be extracted
   * @return the list of Move actions
   */
  public ArrayList<Move> extractMove(ArrayList<TextAction> textActions) {
    ArrayList<Move> moveActions = new ArrayList<>();
    for (TextAction textAction : textActions) {
      if (textAction.name.equals(MOVE)) {
        MoveAttackTextAction moveTextAction = (MoveAttackTextAction) textAction;
        Move moveAction = new Move(moveTextAction.from_territory, moveTextAction.to_territory, moveTextAction.unitCt,
            moveTextAction.infantryLevel, moveTextAction.issuer);
        moveActions.add(moveAction);
      }
    }
    return moveActions;
  }

  /**
   * extract Upgrade list from TextAction list
   *
   * @param textActions is the list of Test actions need to be extracted
   * @return the list of Upgrade actions
   */
  public ArrayList<Upgrade> extractUpgrade(ArrayList<TextAction> textActions) {
    ArrayList<Upgrade> upgradeActions = new ArrayList<>();
    for (TextAction textAction : textActions) {
      if (textAction.name.equals(UPGRADE)) {
        UpgradeTextAction upgradeTextAction = (UpgradeTextAction) textAction;
        Upgrade upgradeAction = new Upgrade(upgradeTextAction.upgrade_territory, upgradeTextAction.from_unit,
            upgradeTextAction.to_unit, upgradeTextAction.unitCt);
        upgradeActions.add(upgradeAction);
      }
    }
    return upgradeActions;
  }

  public ArrayList<Cloak> extractCloak(ArrayList<TextAction> textActions) {
    ArrayList<Cloak> cloakActions = new ArrayList<>();
    for (TextAction textAction : textActions) {
      if (textAction.name.equals(CLOAK)) {
        CloakTextAction cloakTextAction = (CloakTextAction) textAction;
        cloakActions.add(new Cloak(cloakTextAction.territory));
      }
    }
    return cloakActions;
  }

  /**
   * @param ownerName is the owner of the food resource
   * @return the number of food resource for this owner
   */
  public int getFoodRsc(String ownerName) {
    if (ownerName.equals("A")) {
      return resourceUnits.get("foodRsc").get("A");
    }
    if (ownerName.equals("B")) {
      return resourceUnits.get("foodRsc").get("B");
    } else {
      return -1;
    }
  }

  /**
   * @param foodRscA is the number of new foodRscA
   * @param foodRscB is the number of new foodRscB
   */
  public void setFoodRsc(int foodRscA, int foodRscB) {
    this.resourceUnits.get("foodRsc").put("A", foodRscA);
    this.resourceUnits.get("foodRsc").put("B", foodRscB);
  }

  /**
   * to decrease the number in the corresponding resource
   *
   * @param resource is the resource type
   * @param minus    is the number to be subtracted
   * @param name     is the player name
   */
  public void setMinus(String resource, int minus, String name) {
    int num = this.resourceUnits.get(resource).get(name);
    num -= minus;
    this.resourceUnits.get(resource).put(name, num);
  }

  /**
   * add the food resource during each turn
   */
  public void addFoodRsc() {
    int foodRscA = 0;
    int foodRscB = 0;
    for (Territory t : map.getTerritories()) {
      if (t.getOwnerName().equals("A")) {
        foodRscA += t.getFoodRate();
      } else {
        foodRscB += t.getFoodRate();
      }
    }
    setFoodRsc(foodRscA + getFoodRsc("A"), foodRscB + getFoodRsc("B"));
  }


  /**
   * @param ownerName is the player's name
   * @return the number of tech resource
   */
  public int getTechRsc(String ownerName) {
    if (ownerName.equals("A")) {
      return resourceUnits.get("techRsc").get("A");
    }
    if (ownerName.equals("B")) {
      return resourceUnits.get("techRsc").get("B");
    } else {
      return -1;
    }
  }

  /**
   * @param techRscA is the number of new techRscA
   * @param techRscB is the number of new techRscB
   */
  public void setTechRsc(int techRscA, int techRscB) {
    this.resourceUnits.get("techRsc").put("A", techRscA);
    this.resourceUnits.get("techRsc").put("B", techRscB);
  }

  /**
   * add the food techRsc during each turn
   */
  public void addTechRsc() {
    int techRscA = 0;
    int techRscB = 0;
    for (Territory t : map.getTerritories()) {
      if (t.getOwnerName().equals("A")) {
        techRscA += t.getTechRate();
      } else {
        techRscB += t.getTechRate();
      }
    }
    setTechRsc(techRscA + getTechRsc("A"), techRscB + getTechRsc("B"));
  }

  /**
   * close all the sockets in server
   *
   * @throws IOException when there are input or output exceptions
   */
  public void closeSocket() throws IOException {
    client1.close();
    client2.close();
    server.closeSocket();
  }

}
