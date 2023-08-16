package edu.duke.ece651.team2.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UpgradeTest {
  @Test
  public void test_equals() {
    Upgrade upgrade1 = new Upgrade("Mordor", "Level1Unit", "Level2Unit", 1);
    Upgrade upgrade2 = new Upgrade("Mordor", "Level1Unit", "Level2Unit", 1);
    Upgrade upgrade3 = new Upgrade("Mordor", "Level1Unit", "Level3Unit", 1);
    assertEquals(upgrade1, upgrade2);
    assertNotEquals(upgrade1, upgrade3);
    assertNotEquals(upgrade1, "d");
  }

  @Test
  public void test_upgradeUnit() {
    Map map1 = new Map();
    Upgrade upgrade1 = new Upgrade("Mordor", "Level1Unit", "Level2Unit", 1);
    assertEquals(map1.getTerritory("Mordor").getMyUnitCt("Level1Unit"), 10);
    assertEquals(map1.getTerritory("Mordor").getMyUnitCt("Level2Unit"), 0);
    upgrade1.upgradeUnit(map1);
    assertEquals(map1.getTerritory("Mordor").getMyUnitCt("Level2Unit"), 1);
    assertEquals(map1.getTerritory("Mordor").getMyUnitCt("Level1Unit"), 9);
  }

  @Test
  public void test_get() {
    Upgrade upgrade1 = new Upgrade("Mordor", "Level1Unit", "Level2Unit", 1);
    assertEquals(1, upgrade1.getUnitCt());
    assertEquals("Level1Unit", upgrade1.getFromUnit());
    assertEquals("Level2Unit", upgrade1.getToUnit());
    assertEquals("Mordor", upgrade1.getUpgrade_territory());
  }

}
