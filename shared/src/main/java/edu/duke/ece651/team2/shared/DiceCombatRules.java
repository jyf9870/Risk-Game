package edu.duke.ece651.team2.shared;

import java.util.Random;

public class DiceCombatRules implements CombatRules {
  private final Random rand;

  /**
   * This class supports behaviors of a dice-based combat rules
   */
  public DiceCombatRules() {
    this.rand = new Random();
    // Ensure deterministic random results for testing
    this.rand.setSeed(42);
  }


  /**
   * @param map  represents the map
   * @param name represents the name of the territory being attacked
   * @return true if the attacker wins the combat, false otherwise
   */
  @Override
  public boolean resolveCombat(Map map, String name) {
    Territory t = map.getTerritory(name);
    int attackerUnit = t.getEnemyUnitCt("Level1Unit");
    int defenderUnit = t.getMyUnitCt("Level1Unit");
    while (attackerUnit > 0 && defenderUnit > 0) {
      int attackerOutcome = rand.nextInt(20);
      int defenderOutcome = rand.nextInt(20);
      if (attackerOutcome > defenderOutcome) {
        defenderUnit -= 1;
      } else {
        attackerUnit -= 1;
      }
    }
    map.updateTerritoryUnitCt(name, 0, "Level1Unit", true);
    if (attackerUnit > 0) {
      map.updateTerritoryUnitCt(name, attackerUnit, "Level1Unit", false);
      return true;
    } else {
      map.updateTerritoryUnitCt(name, defenderUnit, "Level1Unit", false);
      return false;
    }
  }
}
