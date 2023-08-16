package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.UpgradeTextAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BasicCheckerUpgradeTest {
  @Test
  public void test_rule() {
    Map map = new Map();
    ActionRuleCheckerUpgrade checker = new BasicCheckerUpgrade(null);
    ActionRuleCheckerUpgrade bigChecker = new BasicCheckerUpgrade(new IssuerOwnershipCheckerUpgrade(null));
    UpgradeTextAction valid_a1 = new UpgradeTextAction("U", "A", "Elantris", "Level1Unit", "Level2Unit", 1);
    UpgradeTextAction invalid_a1 = new UpgradeTextAction("K", "A", "Elantris", "Level1Unit", "Level2Unit", 1);
    UpgradeTextAction invalid_a2 = new UpgradeTextAction("U", "A", "Elantris2", "Level1Unit", "Level2Unit", 1);
    UpgradeTextAction invalid_a3 = new UpgradeTextAction("U", "A", "Elantris", "Level8Unit", "Level2Unit", 1);
    UpgradeTextAction invalid_a4 = new UpgradeTextAction("U", "A", "Elantris", "Level1Unit", "Level8Unit", 1);
    UpgradeTextAction invalid_a5 = new UpgradeTextAction("U", "A", "Elantris", "Level1Unit", "Level2Unit", -1);
    assertNull(bigChecker.checkAction(valid_a1, map));
    assertEquals("The actionPlayer A is doing the K action, in territory Elantris, upgrade 1 Level1Unit to Level2Unit. must be Upgrade!", checker.checkAction(invalid_a1, map));
    assertEquals("The upgrade territory of the actionPlayer A is doing the U action, in territory Elantris2, upgrade 1 Level1Unit to Level2Unit. does not exist!", checker.checkAction(invalid_a2, map));
    assertEquals("The from unit level type of the actionPlayer A is doing the U action, in territory Elantris, upgrade 1 Level8Unit to Level2Unit. does not exist!", checker.checkAction(invalid_a3, map));
    assertEquals("The to unit level type of the actionPlayer A is doing the U action, in territory Elantris, upgrade 1 Level1Unit to Level8Unit. does not exist!", checker.checkAction(invalid_a4, map));
    assertEquals("The number of units used in the action Player A is doing the U action, in territory Elantris, upgrade -1 Level1Unit to Level2Unit. is less than zero!", checker.checkAction(invalid_a5, map));
    assertNull(checker.checkAction(valid_a1, map));
  }

}
