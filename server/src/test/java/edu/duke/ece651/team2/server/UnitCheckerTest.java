package edu.duke.ece651.team2.server;


import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UnitCheckerTest {
  @Test
  public void test_rule() {
    Map map = new Map();
    LinkedHashMap<String, Integer> myInfantry = map.getTerritory("Narnia").getMyInfantry();
    myInfantry.put("Level1Unit", 10);
    myInfantry.put("Level2Unit", 8);
    myInfantry.put("SpyUnit", 1);
    LinkedHashMap<String, Integer> enemyInfantry = map.getTerritory("Narnia").getEnemyInfantry();
    enemyInfantry.put("SpyUnit", 2);
    ActionRuleChecker checker = new UnitChecker(null);
    MoveAttackTextAction invalid_a1 = new MoveAttackTextAction("M", "B", "Narnia", "Mid", 11, "Level2Unit");
    MoveAttackTextAction invalid_a2 = new MoveAttackTextAction("M", "B", "Narnia", "Mid", 2, "SpyUnit");
    MoveAttackTextAction invalid_a3 = new MoveAttackTextAction("M", "A", "Narnia", "Mid", 3, "SpyUnit");
    MoveAttackTextAction invalid_a4 = new MoveAttackTextAction("A", "B", "Narnia", "Elantris", 1, "SpyUnit");
    MoveAttackTextAction valid_a1 = new MoveAttackTextAction("M", "B", "Narnia", "Mid", 10, "Level1Unit");
    MoveAttackTextAction valid_a2 = new MoveAttackTextAction("M", "B", "Narnia", "Mid", 1, "SpyUnit");
    MoveAttackTextAction valid_a3 = new MoveAttackTextAction("M", "A", "Narnia", "Mid", 2, "SpyUnit");
    assertEquals("The number of units used in the action Player B is doing the M action, from territory Narnia to territory Mid, with 11 Level2Unit. exceeds the limit!", checker.checkAction(invalid_a1, map));
    assertEquals("The number of units used in the action Player B is doing the M action, from territory Narnia to territory Mid, with 2 SpyUnit. exceeds the limit!", checker.checkAction(invalid_a2, map));
    assertEquals("The number of units used in the action Player A is doing the M action, from territory Narnia to territory Mid, with 3 SpyUnit. exceeds the limit!", checker.checkAction(invalid_a3, map));
    assertEquals("The spy unit cannot attack!", checker.checkAction(invalid_a4, map));
    assertNull(checker.checkAction(valid_a1, map));
    assertNull(checker.checkAction(valid_a2, map));
    assertNull(checker.checkAction(valid_a3, map));
  }
}
