package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PathCheckerTest {
  @Test
  public void test_rule() {
    Map map = new Map();
    map.updateTerritoryOwner("Oz", "A");
    ActionRuleChecker checker = new PathChecker(null);

    MoveAttackTextAction invalid_a1 = new MoveAttackTextAction("A", "A", "Elantris", "Oz", 4, "Level1Unit");
    MoveAttackTextAction invalid_a2 = new MoveAttackTextAction("M", "B", "Narnia", "Elantris", 4, "Level1Unit");
    MoveAttackTextAction invalid_a3 = new MoveAttackTextAction("M", "A", "Narnia", "Gondor", 1, "SpyUnit");
    MoveAttackTextAction valid_a1 = new MoveAttackTextAction("M", "B", "Narnia", "Mid", 4, "Level1Unit");

    assertEquals("The to territory of the attackPlayer A is doing the A action, from territory Elantris to territory Oz, with 4 Level1Unit. must be the neighbor of the from territory!", checker.checkAction(invalid_a1, map));
    assertEquals("The to territory of the movePlayer B is doing the M action, from territory Narnia to territory Elantris, with 4 Level1Unit. must have a route from the from territory!", checker.checkAction(invalid_a2, map));
    assertEquals("Spies can only move 1 territory at a time in enemy territory!", checker.checkAction(invalid_a3, map));

    assertNull(checker.checkAction(valid_a1, map));
  }
}

