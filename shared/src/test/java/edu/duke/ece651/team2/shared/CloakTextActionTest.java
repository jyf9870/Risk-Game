package edu.duke.ece651.team2.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CloakTextActionTest {
  @Test
  public void testToString() {
    CloakTextAction cloakTextAction = new CloakTextAction("C", "Alice", "A");
    assertEquals("C", cloakTextAction.name);
    assertEquals("Alice", cloakTextAction.issuer);
    assertEquals("A", cloakTextAction.territory);
    assertEquals("Player Alice is doing the C action to territory A.", cloakTextAction.toString());
  }

  @Test
  public void testEquals() {
    CloakTextAction t1 = new CloakTextAction("C", "Alice", "A");
    CloakTextAction t2 = new CloakTextAction("C", "Alice", "A");
    CloakTextAction t3 = new CloakTextAction("C", "Betty", "A");
    CloakTextAction t4 = new CloakTextAction("C", "Alice", "B");
    assertEquals(t1, t2);
    assertNotEquals(t1, t3);
    assertNotEquals(t1, t4);
    assertNotEquals(t1, "1");
  }
}