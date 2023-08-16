package edu.duke.ece651.team2.shared;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
  @Test
  public void test_getTerritories() {
    Map map = new Map();
    ArrayList<Territory> t = map.getTerritories();
    Territory e = new Territory("Elantris", "A", 15, 12, 10);
    Territory s = new Territory("Scadrial", "A", 8, 10, 8);
    Territory md = new Territory("Mordor", "A", 10, 7, 6);
    Territory r = new Territory("Roshar", "A", 7, 14, 12);
    Territory h = new Territory("Hogwarts", "A", 5, 7, 9);
    Territory g = new Territory("Gondor", "B", 15, 17, 15);
    Territory n = new Territory("Narnia", "B", 12, 15, 14);
    Territory m = new Territory("Mid", "B", 7, 8, 7);
    Territory o = new Territory("Oz", "B", 11, 10, 9);
    assertTrue(t.contains(e));
    assertTrue(t.contains(s));
    assertTrue(t.contains(md));
    assertTrue(t.contains(r));
    assertTrue(t.contains(h));
    assertTrue(t.contains(g));
    assertTrue(t.contains(n));
    assertTrue(t.contains(m));
    assertTrue(t.contains(o));
  }

  @Test
  public void test_getPlayerTerritory() {
    Map map = new Map();
    ArrayList<Territory> t = map.getPlayerTerritory("A");
    Territory e = new Territory("Elantris", "A", 15, 12, 10);
    Territory s = new Territory("Scadrial", "A", 8, 10, 8);
    Territory md = new Territory("Mordor", "A", 10, 7, 6);
    Territory r = new Territory("Roshar", "A", 7, 14, 12);
    Territory h = new Territory("Hogwarts", "A", 5, 7, 9);
    Territory g = new Territory("Gondor", "B", 15, 17, 15);
    Territory n = new Territory("Narnia", "B", 12, 15, 14);
    Territory m = new Territory("Mid", "B", 7, 8, 7);
    Territory o = new Territory("Oz", "B", 11, 10, 9);
    assertTrue(t.contains(e));
    assertTrue(t.contains(s));
    assertTrue(t.contains(md));
    assertTrue(t.contains(r));
    assertTrue(t.contains(h));
    assertFalse(t.contains(g));
    assertFalse(t.contains(n));
    assertFalse(t.contains(m));
    assertFalse(t.contains(o));
  }

  @Test
  public void test_getPlayerTerritories() {
    Map map = new Map();
    HashMap<String, ArrayList<Territory>> t = map.getPlayerTerritories();
    Territory e = new Territory("Elantris", "A", 15, 12, 10);
    Territory s = new Territory("Scadrial", "A", 8, 10, 8);
    Territory md = new Territory("Mordor", "A", 10, 7, 6);
    Territory r = new Territory("Roshar", "A", 7, 14, 12);
    Territory h = new Territory("Hogwarts", "A", 5, 7, 9);
    Territory g = new Territory("Gondor", "B", 15, 17, 15);
    Territory n = new Territory("Narnia", "B", 12, 15, 14);
    Territory m = new Territory("Mid", "B", 7, 8, 7);
    Territory o = new Territory("Oz", "B", 11, 10, 9);
    assertTrue(t.get("A").contains(e));
    assertTrue(t.get("A").contains(s));
    assertTrue(t.get("A").contains(md));
    assertTrue(t.get("A").contains(r));
    assertTrue(t.get("A").contains(h));
    assertTrue(t.get("B").contains(g));
    assertTrue(t.get("B").contains(n));
    assertTrue(t.get("B").contains(m));
    assertTrue(t.get("B").contains(o));

    assertFalse(t.get("B").contains(e));
    assertFalse(t.get("B").contains(s));
    assertFalse(t.get("B").contains(md));
    assertFalse(t.get("B").contains(r));
    assertFalse(t.get("B").contains(h));
    assertFalse(t.get("A").contains(g));
    assertFalse(t.get("A").contains(n));
    assertFalse(t.get("A").contains(m));
    assertFalse(t.get("A").contains(o));
  }

  @Test
  public void test_getTerritory() {
    Map map = new Map();
    assertEquals(new Territory("Elantris", "A", 15, 12, 10), map.getTerritory("Elantris"));
    assertEquals(new Territory("Scadrial", "A", 8, 10, 8), map.getTerritory("Scadrial"));
    assertNull(map.getTerritory("Elan"));
    assertNull(map.getTerritory("ElantrisElantris"));
  }

  @Test
  public void test_updateTerritoryUnitCt() {
    Map map = new Map();
    Territory oldElan = map.getTerritory("Elantris");
    assertEquals(15, oldElan.getMyUnitCt("Level1Unit"));
    map.updateTerritoryUnitCt("Elantris", 2, "Level1Unit", false);
    map.updateTerritoryUnitCt("Elantris", 4, "Level1Unit", true);
    Territory newElan = map.getTerritory("Elantris");
    assertEquals(2, newElan.getMyUnitCt("Level1Unit"));
    assertEquals(4, newElan.getEnemyUnitCt("Level1Unit"));
    assertThrows(IllegalArgumentException.class, () -> map.updateTerritoryUnitCt("E", 2, "Level1Unit", false));
  }

  @Test
  public void test_updateTerritoryOwner() {
    Map map = new Map();
    Territory oldElan = map.getTerritory("Elantris");
    assertEquals("A", oldElan.getOwnerName());
    map.updateTerritoryOwner("Elantris", "B");
    Territory newElan = map.getTerritory("Elantris");
    assertEquals("B", newElan.getOwnerName());
    assertThrows(IllegalArgumentException.class, () -> map.updateTerritoryOwner("E", "B"));
  }

  @Test
  public void test_incrementTerritoryUnitCt() {
    Map map = new Map();
    assertEquals(15, map.getTerritory("Elantris").getMyUnitCt("Level1Unit"));
    assertEquals(12, map.getTerritory("Narnia").getMyUnitCt("Level1Unit"));
    assertEquals(15, map.getTerritory("Gondor").getMyUnitCt("Level1Unit"));
    assertEquals(5, map.getTerritory("Hogwarts").getMyUnitCt("Level1Unit"));
    map.incrementTerritoryUnitCt(1);
    assertEquals(16, map.getTerritory("Elantris").getMyUnitCt("Level1Unit"));
    assertEquals(13, map.getTerritory("Narnia").getMyUnitCt("Level1Unit"));
    assertEquals(16, map.getTerritory("Gondor").getMyUnitCt("Level1Unit"));
    assertEquals(6, map.getTerritory("Hogwarts").getMyUnitCt("Level1Unit"));
    map.incrementTerritoryUnitCt(-1);
    assertEquals(15, map.getTerritory("Elantris").getMyUnitCt("Level1Unit"));
    assertEquals(12, map.getTerritory("Narnia").getMyUnitCt("Level1Unit"));
    assertEquals(15, map.getTerritory("Gondor").getMyUnitCt("Level1Unit"));
    assertEquals(5, map.getTerritory("Hogwarts").getMyUnitCt("Level1Unit"));
  }

  @Test
  public void test_equals() {
    Map map1 = new Map();
    Map map2 = new Map();
    assertEquals(map1, map2);
    map1.incrementTerritoryUnitCt(-1);
    assertNotEquals(map1, map2);
    assertNotEquals(map1, 1);
  }

  @Test
  public void test_copy() {
    Map m1 = new Map();
    Map m2 = new Map(m1);
    m1.updateTerritoryUnitCt("Narnia", 15, "Level1Unit", false);
    assertNotEquals(m1, m2);
  }

}
