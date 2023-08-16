package edu.duke.ece651.team2.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloakTest {

  @Test
  public void cloakTerritory() {
    Cloak cloak = new Cloak("Elantris");
    Map m = new Map();
    assertFalse(m.getTerritory("Elantris").isCloaked());
    cloak.cloakTerritory(m);
    assertTrue(m.getTerritory("Elantris").isCloaked());
  }

  @Test
  public void getTerritory() {
    Cloak cloak = new Cloak("Elantris");
    assertEquals("Elantris", cloak.getTerritory());
  }
}