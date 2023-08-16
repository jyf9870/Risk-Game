package edu.duke.ece651.team2.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UpgradeTextActionTest {
  @Test
  public void test_init() {
    UpgradeTextAction t = new UpgradeTextAction("Upgrade", "A", "Elantris", "Level1Unit", "Level2Unit", 1);
    assertEquals("Upgrade", t.name);
    assertEquals("A", t.issuer);
    assertEquals("Elantris", t.upgrade_territory);
    assertEquals("Level1Unit", t.from_unit);
    assertEquals("Level2Unit", t.to_unit);
  }

  @Test
  public void test_equals() {
    TextAction t1 = new UpgradeTextAction("Upgrade", "A", "Elantris", "Level1Unit", "Level2Unit", 1);
    TextAction t2 = new UpgradeTextAction("Upgrade", "A", "Elantris", "Level1Unit", "Level2Unit", 1);
    TextAction t3 = new UpgradeTextAction("Upgrade", "B", "Elantris", "Level1Unit", "Level2Unit", 1);
    TextAction t4 = new UpgradeTextAction("Upgrade", "A", "Elantris", "Level1Unit", "Level3Unit", 1);
    assertEquals(t1, t2);
    assertNotEquals(t1, t3);
    assertNotEquals(t1, t4);
    assertNotEquals(t1, "1");
  }

  @Test
  public void test_toString() {
    TextAction t1 = new UpgradeTextAction("Upgrade", "A", "Elantris", "Level1Unit", "Level2Unit", 1);
    assertEquals("Player A is doing the Upgrade action, in territory Elantris, upgrade 1 Level1Unit to Level2Unit.", t1.toString());
  }

}
