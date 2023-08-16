package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.UpgradeTextAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UnitCheckerUpgradeTest {
  @Test
  public void test_rule() {
    Map map = new Map();
    ActionRuleCheckerUpgrade checker = new UnitCheckerUpgrade(null);
    UpgradeTextAction invalid_a1 = new UpgradeTextAction("U", "B", "Elantris", "Level2Unit", "Level5Unit", 1);
    UpgradeTextAction invalid_a2 = new UpgradeTextAction("U", "B", "Elantris", "Level1Unit", "Level1Unit", 1);
    UpgradeTextAction invalid_a3 = new UpgradeTextAction("U", "B", "Elantris", "Level1Unit", "Level2Unit", 100);
    UpgradeTextAction valid_a1 = new UpgradeTextAction("U", "B", "Elantris", "Level1Unit", "Level2Unit", 1);
    assertEquals(
        "The from unit used in the actionPlayer B is doing the U action, in territory Elantris, upgrade 1 Level2Unit to Level5Unit. are insufficient!",
        checker.checkAction(invalid_a1, map));
    assertEquals(
        "The from unit used in the actionPlayer B is doing the U action, in territory Elantris, upgrade 1 Level1Unit to Level1Unit. is not less than the to unit!",
        checker.checkAction(invalid_a2, map));
    assertEquals(
        "The from unit used in the actionPlayer B is doing the U action, in territory Elantris, upgrade 100 Level1Unit to Level2Unit. are insufficient!",
        checker.checkAction(invalid_a3, map));
    assertNull(checker.checkAction(valid_a1, map));
  }

}
