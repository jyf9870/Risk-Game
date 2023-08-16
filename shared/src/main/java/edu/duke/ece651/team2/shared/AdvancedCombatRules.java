package edu.duke.ece651.team2.shared;

import java.util.*;

public class AdvancedCombatRules implements CombatRules {
  private final Random rand;
  private final HashMap<String, Integer> bonusMap;

  public AdvancedCombatRules() {
    this.rand = new Random();
    this.rand.setSeed(42);
    this.bonusMap = new HashMap<>();
    this.bonusMap.put("Level1Unit", 0);
    this.bonusMap.put("Level2Unit", 1);
    this.bonusMap.put("Level3Unit", 3);
    this.bonusMap.put("Level4Unit", 5);
    this.bonusMap.put("Level5Unit", 8);
    this.bonusMap.put("Level6Unit", 11);
    this.bonusMap.put("Level7Unit", 15);

  }

  /**
   * @param map  represents the map
   * @param name represents the name of the territory being attacked
   * @return true if the attacker wins the combat, false otherwise
   */
  @Override
  public boolean resolveCombat(Map map, String name) {
    Territory t = map.getTerritory(name);
    LinkedHashMap<String, Integer> attackerInfantry = t.getInfantry(true);
    LinkedHashMap<String, Integer> defenderInfantry = t.getInfantry(false);
    String currAttackUnit;
    String currDefendUnit;
    // need to build up array of attack orders
    ArrayList<String> attackerKeys = new ArrayList<>(attackerInfantry.keySet());
    ArrayList<String> defenderKeys = new ArrayList<>(defenderInfantry.keySet());
    ArrayList<String> attackerList = new ArrayList<>();
    ArrayList<String> defenderList = new ArrayList<>();
    // Spy units are not involved in combat
    attackerKeys.remove("SpyUnit");
    defenderKeys.remove("SpyUnit");

    for (String attackKey : attackerKeys) {
      for (int i = 0; i < attackerInfantry.get(attackKey); i++) {
        attackerList.add(attackKey);
      }
    }

    for (String defendKey : defenderKeys) {
      for (int i = 0; i < defenderInfantry.get(defendKey); i++) {
        defenderList.add(defendKey);
      }
    }
    Collections.reverse(attackerList);
    Collections.reverse(defenderList);
    boolean attackerFavored = true;
    while (attackerList.size() > 0 && defenderList.size() > 0) {
      int attackOutcome = rand.nextInt(20);
      int defendOutcome = rand.nextInt(20);
      if (attackerFavored) {
        currAttackUnit = attackerList.get(0);
        currDefendUnit = defenderList.get(defenderList.size() - 1);
        attackOutcome += this.bonusMap.get(currAttackUnit);
        defendOutcome += this.bonusMap.get(currDefendUnit);
        if (attackOutcome > defendOutcome) {
          defenderInfantry.put(currDefendUnit, defenderInfantry.get(currDefendUnit) - 1);
          defenderList.remove(defenderList.size() - 1);
        } else {
          attackerInfantry.put(currAttackUnit, attackerInfantry.get(currAttackUnit) - 1);
          attackerList.remove(0);
        }
        attackerFavored = false;
      } else {
        currAttackUnit = attackerList.get(attackerList.size() - 1);
        currDefendUnit = defenderList.get(0);
        attackOutcome += this.bonusMap.get(currAttackUnit);
        defendOutcome += this.bonusMap.get(currDefendUnit);
        if (attackOutcome > defendOutcome) {
          defenderInfantry.put(currDefendUnit, defenderInfantry.get(currDefendUnit) - 1);
          defenderList.remove(0);
        } else {
          attackerInfantry.put(currAttackUnit, attackerInfantry.get(currAttackUnit) - 1);
          attackerList.remove(attackerList.size() - 1);
        }
        attackerFavored = true;
      }
    }
    if (attackerList.size() > 0) {
      t.updateInfantry(attackerInfantry, false);
      t.updateInfantry(defenderInfantry, true);
      return true;
    } else {
      t.updateInfantry(defenderInfantry, false);
      t.updateInfantry(attackerInfantry, true);
      return false;
    }
  }
}
