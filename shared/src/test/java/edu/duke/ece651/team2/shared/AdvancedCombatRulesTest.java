package edu.duke.ece651.team2.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdvancedCombatRulesTest {
  @Test
  public void test_resolveCombat() {
    AdvancedCombatRules a = new AdvancedCombatRules();
    Map map = new Map();
    map.updateTerritoryUnitCt("Elantris", 3, "Level7Unit", true);
    map.updateTerritoryUnitCt("Elantris", 2, "SpyUnit", true);
    map.updateTerritoryUnitCt("Elantris", 4, "SpyUnit", false);
    assertTrue(a.resolveCombat(map, "Elantris"));
    Territory t1 = map.getTerritory("Elantris");
    assertEquals(3, t1.getMyUnitCt("Level7Unit"));
    assertEquals(2, t1.getMyUnitCt("SpyUnit"));
    assertEquals(0, t1.getEnemyUnitCt("Level1Unit"));
    assertEquals(4, t1.getEnemyUnitCt("SpyUnit"));
    map.updateTerritoryUnitCt("Elantris", 5, "Level1Unit", true);
    map.updateTerritoryUnitCt("Elantris", 15, "Level1Unit", false);
    map.updateTerritoryUnitCt("Elantris", 0, "Level2Unit", false);
    assertFalse(a.resolveCombat(map, "Elantris"));
    assertEquals(13, t1.getMyUnitCt("Level1Unit"));
    assertEquals(0, t1.getEnemyUnitCt("Level1Unit"));
    assertEquals(0, t1.getMyUnitCt("Level2Unit"));
  }

}
