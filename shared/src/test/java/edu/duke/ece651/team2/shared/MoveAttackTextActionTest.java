package edu.duke.ece651.team2.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MoveAttackTextActionTest {

  @Test
  public void test_init() {
    MoveAttackTextAction t = new MoveAttackTextAction("Move", "A", "Elantris", "Scadrial", 5, "Level1Unit");

    assertEquals("Move", t.name);
    assertEquals("A", t.issuer);
    assertEquals("Elantris", t.from_territory);
    assertEquals("Scadrial", t.to_territory);
    assertEquals(5, t.unitCt);

    assertEquals("Level1Unit", t.infantryLevel);

  }

  @Test
  public void test_equals() {

    MoveAttackTextAction t1 = new MoveAttackTextAction("Move", "A", "Elantris", "Scadrial", 5, "Level1Unit");
    MoveAttackTextAction t2 = new MoveAttackTextAction("Move", "A", "Elantris", "Scadrial", 5, "Level1Unit");
    MoveAttackTextAction t3 = new MoveAttackTextAction("Move", "B", "Elantris", "Scadrial", 5, "Level1Unit");
    MoveAttackTextAction t4 = new MoveAttackTextAction("Move", "A", "Elantris", "Scadrial", 15, "Level1Unit");
    MoveAttackTextAction t5 = new MoveAttackTextAction("Move", "A", "Elantris", "Scadrial", 5, "Level2Unit");
    assertEquals(t1, t2);
    assertNotEquals(t1, t3);
    assertNotEquals(t1, t4);
    assertNotEquals(t1, t5);

    assertNotEquals(t1, "1");
  }

  @Test
  public void test_toString() {

    MoveAttackTextAction t1 = new MoveAttackTextAction("Move", "A", "Elantris", "Scadrial", 5, "Level1Unit");
    assertEquals("Player A is doing the Move action, from territory Elantris to territory Scadrial, with 5 Level1Unit.", t1.toString());

  }

}
