package edu.duke.ece651.team2.shared;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiceCombatRulesTest {
  @Test
  public void test_resolveCombat() {
    DiceCombatRules r = new DiceCombatRules();
    Map map = new Map();
    map.updateTerritoryUnitCt("Elantris", 19, "Level1Unit", true);
    assertTrue(r.resolveCombat(map, "Elantris"));
    Territory t1 = map.getTerritory("Elantris");
    assertEquals(9, t1.getMyUnitCt("Level1Unit"));
    assertEquals(0, t1.getEnemyUnitCt("Level1Unit"));
    map.updateTerritoryUnitCt("Elantris", 5, "Level1Unit", true);
    map.updateTerritoryUnitCt("Elantris", 15, "Level1Unit", false);
    assertFalse(r.resolveCombat(map, "Elantris"));
  }
}
