package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class IssuerOwnershipCheckerTest {
  @Test
  public void test_rule() {
    Map map = new Map();
    ActionRuleChecker checker = new IssuerOwnershipChecker(null);

    MoveAttackTextAction invalid_a1 = new MoveAttackTextAction("A", "A", "Narnia", "Mid", 4, "Level2Unit");
    MoveAttackTextAction invalid_a2 = new MoveAttackTextAction("A", "A", "Elantris", "Mordor", 4, "Level2Unit");
    MoveAttackTextAction invalid_a3 = new MoveAttackTextAction("M", "B", "Narnia", "Scadrial", 4, "Level2Unit");
    MoveAttackTextAction valid_a1 = new MoveAttackTextAction("M", "B", "Narnia", "Mid", 4, "Level2Unit");
    assertEquals("The from territory of the actionPlayer A is doing the A action, from territory Narnia to territory Mid, with 4 Level2Unit. does not belong to the issuer!", checker.checkAction(invalid_a1, map));
    assertEquals("The to territory of the attackPlayer A is doing the A action, from territory Elantris to territory Mordor, with 4 Level2Unit. cannot be your own territory!", checker.checkAction(invalid_a2, map));
    assertEquals("The to territory of the movePlayer B is doing the M action, from territory Narnia to territory Scadrial, with 4 Level2Unit. must be your own territory!", checker.checkAction(invalid_a3, map));

    assertNull(checker.checkAction(valid_a1, map));
  }

}

