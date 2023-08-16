package edu.duke.ece651.team2.server;


import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BasicCheckerTest {
  @Test
  public void test_rule() {
    Map map = new Map();
    ActionRuleChecker checker = new BasicChecker(null);
    MoveAttackTextAction invalid_a1 = new MoveAttackTextAction("K", "A", "Elantris", "Oz", 4, "Level2Unit");
    MoveAttackTextAction invalid_a2 = new MoveAttackTextAction("M", "B", "Nar", "Gondor", 4, "Level2Unit");
    MoveAttackTextAction invalid_a3 = new MoveAttackTextAction("M", "B", "Narnia", "Gon", 4, "Level2Unit");
    MoveAttackTextAction invalid_a4 = new MoveAttackTextAction("M", "B", "Narnia", "Mid", -1, "Level2Unit");
    MoveAttackTextAction invalid_a5 = new MoveAttackTextAction("M", "B", "Narnia", "Mid", -1, "lll");
    MoveAttackTextAction valid_a1 = new MoveAttackTextAction("M", "B", "Narnia", "Mid", 4, "Level2Unit");
    assertEquals("The actionPlayer A is doing the K action, from territory Elantris to territory Oz, with 4 Level2Unit. must be Attack or Move!", checker.checkAction(invalid_a1, map));
    assertEquals("The from territory of the action Player B is doing the M action, from territory Nar to territory Gondor, with 4 Level2Unit. does not exist!", checker.checkAction(invalid_a2, map));
    assertEquals("The to territory of the action Player B is doing the M action, from territory Narnia to territory Gon, with 4 Level2Unit. does not exist!", checker.checkAction(invalid_a3, map));
    assertEquals("The number of units used in the action Player B is doing the M action, from territory Narnia to territory Mid, with -1 Level2Unit. is less than zero!", checker.checkAction(invalid_a4, map));
    assertEquals("The infantry is invalid!", checker.checkAction(invalid_a5, map));

    assertNull(checker.checkAction(valid_a1, map));
  }

  @Test
  public void test_combined_check_my_rule() {
    Map map = new Map();
    LinkedHashMap<String, Integer> infantry = map.getTerritory("Narnia").getMyInfantry();
    LinkedHashMap<String, Integer> infantry2 = map.getTerritory("Scadrial").getMyInfantry();
    LinkedHashMap<String, Integer> infantry3 = map.getTerritory("Elantris").getMyInfantry();
    infantry.put("Level2Unit", 11);
    infantry2.put("Level2Unit", 8);
    infantry3.put("Level2Unit", 5);
    ActionRuleChecker checker = new BasicChecker(new IssuerOwnershipChecker(new PathChecker(new UnitChecker(null))));
    MoveAttackTextAction invalid_a1 = new MoveAttackTextAction("A", "A", "Elantris", "Mid", 7, "Level2Unit");
    MoveAttackTextAction valid_a1 = new MoveAttackTextAction("M", "B", "Narnia", "Gondor", 10, "Level2Unit");
    MoveAttackTextAction valid_a2 = new MoveAttackTextAction("A", "A", "Scadrial", "Mid", 5, "Level2Unit");
    assertNull(checker.checkAction(valid_a1, map));
    assertNull(checker.checkAction(valid_a2, map));
    assertEquals("The number of units used in the action Player A is doing the A action, from territory Elantris to territory Mid, with 7 Level2Unit. exceeds the limit!", checker.checkAction(invalid_a1, map));

  }
}


