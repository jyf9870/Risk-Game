package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.UpgradeTextAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class IssuerOwnershipCheckerUpgradeTest {
  @Test
  public void test_rule() {
    Map map = new Map();
    ActionRuleCheckerUpgrade checker = new IssuerOwnershipCheckerUpgrade(null);
    UpgradeTextAction valid_a1 = new UpgradeTextAction("U", "A", "Elantris", "Level1Unit", "Level2Unit", 1);
    UpgradeTextAction invalid_a1 = new UpgradeTextAction("U", "A", "Narnia", "Level1Unit", "Level2Unit", 1);
    assertEquals(
        "The upgrade territory of the actionPlayer A is doing the U action, in territory Narnia, upgrade 1 Level1Unit to Level2Unit. does not belong to the issuer!",
        checker.checkAction(invalid_a1, map));
    assertNull(checker.checkAction(valid_a1, map));
  }

}
