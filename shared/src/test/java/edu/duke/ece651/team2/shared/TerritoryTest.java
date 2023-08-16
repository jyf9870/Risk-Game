package edu.duke.ece651.team2.shared;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TerritoryTest {
  private void addNeighborToEachOther(Territory x, Territory y, int distance) {
    x.addNeighbor(y, distance);
    y.addNeighbor(x, distance);
  }

  @Test
  public void test_addNeighbor() {
    Territory e = new Territory("Elantris", "A", 6, 3, 3);
    Territory s = new Territory("Scadrial", "A", 5, 3, 3);
    Territory n = new Territory("Narnia", "B", 10, 3, 3);
    Territory m = new Territory("Mid", "B", 12, 3, 3);
    assertFalse(e.hasNeighbor(s));
    assertFalse(e.hasNeighbor(n));
    assertFalse(e.hasNeighbor(m));
    e.addNeighbor(s, 3);
    assertTrue(e.hasNeighbor(s));
    assertFalse(e.hasNeighbor(n));
    assertFalse(e.hasNeighbor(m));
    e.addNeighbor(n, 6);
    assertTrue(e.hasNeighbor(s));
    assertTrue(e.hasNeighbor(n));
    assertFalse(e.hasNeighbor(m));
    e.addNeighbor(m, 8);
    assertTrue(e.hasNeighbor(s));
    assertTrue(e.hasNeighbor(n));
    assertTrue(e.hasNeighbor(m));
  }

  @Test
  public void testFindMinRoute() {
    Territory e = new Territory("Elantris", "A", 15, 12, 10);
    Territory s = new Territory("Scadrial", "A", 8, 10, 8);
    Territory md = new Territory("Mordor", "A", 10, 7, 6);
    Territory r = new Territory("Roshar", "A", 7, 14, 12);
    Territory h = new Territory("Hogwarts", "A", 5, 7, 9);
    Territory g = new Territory("Gondor", "B", 15, 17, 15);
    Territory n = new Territory("Narnia", "B", 12, 15, 14);
    Territory m = new Territory("Mid", "B", 7, 8, 7);
    Territory o = new Territory("Oz", "B", 11, 10, 9);
    Territory test = new Territory("test", "B", 8, 5, 5);

    addNeighborToEachOther(e, n, 7);
    addNeighborToEachOther(e, m, 8);
    addNeighborToEachOther(e, s, 3);
    addNeighborToEachOther(e, r, 3);
    addNeighborToEachOther(o, g, 4);
    addNeighborToEachOther(n, m, 4);
    addNeighborToEachOther(m, o, 3);
    addNeighborToEachOther(m, s, 9);
    addNeighborToEachOther(o, s, 9);
    addNeighborToEachOther(o, md, 8);
    addNeighborToEachOther(g, md, 10);
    addNeighborToEachOther(s, r, 3);
    addNeighborToEachOther(s, h, 4);
    addNeighborToEachOther(s, md, 4);
    addNeighborToEachOther(md, h, 4);
    addNeighborToEachOther(r, h, 3);

    Map map = new Map();
    assertEquals(7, r.findMinRoute(md, map, false));
    assertEquals(6, e.findMinRoute(h, map, false));
    assertEquals(11, n.findMinRoute(g, map, false));
    map.updateTerritoryOwner("Mordor", "B");
    md.updateOwner("B");
    assertEquals(15, n.findMinRoute(md, map, false));
    assertEquals(-1, r.findMinRoute(test, map, false));
    assertEquals(-1, h.findMinRoute(o, map, false));
    assertEquals(-1, md.findMinRoute(r, map, false));
    assertEquals(-1, r.findMinRoute(test, map, true));
    assertEquals(12, h.findMinRoute(o, map, true));
    assertEquals(7, md.findMinRoute(r, map, true));
  }

  @Test
  public void testFindDistance() {
    Territory md = new Territory("Mordor", "A", 14, 5, 5);
    Territory g = new Territory("Gondor", "B", 13, 5, 5);
    Territory o = new Territory("Oz", "B", 8, 5, 5);
    Territory h = new Territory("Hogwarts", "A", 3, 4, 4);
    addNeighborToEachOther(o, g, 5);
    addNeighborToEachOther(o, md, 4);
    addNeighborToEachOther(g, md, 6);
    addNeighborToEachOther(md, h, 6);

    assertEquals(4, o.findDistance(md));
    assertEquals(5, o.findDistance(g));
    assertEquals(6, g.findDistance(md));
    assertEquals(-1, g.findDistance(h));

  }

  @Test
  public void test_get() {
    Territory e = new Territory("Elantris", "A", 6, 3, 3);
    Territory s = new Territory("Scadrial", "A", 5, 3, 3);
    Territory n = new Territory("Narnia", "B", 10, 3, 3);
    Territory m = new Territory("Mid", "B", 12, 3, 3);
    HashSet<Territory> expectedNeighbors = new HashSet<>();
    assertEquals(expectedNeighbors, e.getNeighbors().keySet());
    e.addNeighbor(s, 3);
    e.addNeighbor(n, 6);
    e.addNeighbor(m, 8);
    expectedNeighbors.add(s);
    expectedNeighbors.add(n);
    expectedNeighbors.add(m);
    assertEquals("Elantris", e.getName());
    assertEquals("A", e.getOwnerName());
    assertEquals(6, e.getMyUnitCt("Level1Unit"));
    assertEquals(0, e.getEnemyUnitCt("Level1Unit"));
    assertEquals(expectedNeighbors, e.getNeighbors().keySet());
  }

  @Test
  public void test_update() {
    Territory e = new Territory("Elantris", "A", 6, 3, 3);
    e.updateUnit(10, "Level1Unit", false);
    e.updateOwner("B");
    assertEquals("B", e.getOwnerName());
    assertEquals(10, e.getMyUnitCt("Level1Unit"));
    e.updateUnit(20, "Level1Unit", false);
    e.updateOwner("C");
    assertEquals("C", e.getOwnerName());
    assertEquals(20, e.getMyUnitCt("Level1Unit"));
    e.updateUnit(30, "Level1Unit", true);
    assertEquals(30, e.getEnemyUnitCt("Level1Unit"));
  }

  @Test
  public void test_isOwnTerritory() {
    Territory e = new Territory("Elantris", "A", 6, 3, 3);
    Territory s = new Territory("Scadrial", "A", 5, 3, 3);
    Territory n = new Territory("Narnia", "B", 10, 3, 3);
    Territory m = new Territory("Mid", "B", 12, 3, 3);
    assertTrue(e.isOwnTerritory(s));
    assertFalse(e.isOwnTerritory(n));
    assertFalse(e.isOwnTerritory(m));
  }

  @Test
  public void test_updateInfantry() {
    Territory e = new Territory("Elantris", "A", 6, 3, 3);
    LinkedHashMap<String, Integer> test_map = new LinkedHashMap<>();
    test_map.put("Level1Unit", 0);
    test_map.put("Level2Unit", 0);
    test_map.put("Level3Unit", 0);
    test_map.put("Level4Unit", 0);
    test_map.put("Level5Unit", 0);
    test_map.put("Level6Unit", 0);
    test_map.put("Level7Unit", 5);
    test_map.put("SpyUnit", 0);
    assertNotEquals(e.getInfantry(false), test_map);
    e.updateInfantry(test_map, false);
    assertEquals(e.getInfantry(false), test_map);
    assertNotEquals(e.getInfantry(true), test_map);
    e.updateInfantry(test_map, true);
    assertEquals(e.getInfantry(true), test_map);
  }

  @Test
  public void test_getInfantryTypes() {
    Territory e = new Territory("Elantris", "A", 6, 3, 3);
    Set<String> testSet = new HashSet<>();
    testSet.add("Level1Unit");
    testSet.add("Level2Unit");
    testSet.add("Level3Unit");
    testSet.add("Level4Unit");
    testSet.add("Level5Unit");
    testSet.add("Level6Unit");
    testSet.add("Level7Unit");
    testSet.add("SpyUnit");
    assertEquals(e.getInfantryTypes(), testSet);
  }

  @Test
  public void test_incrementInfantry() {
    Territory e = new Territory("Elantris", "A", 6, 3, 3);
    e.incrementInfantry("Level1Unit", 3, true);
    assertEquals(3, e.getEnemyUnitCt("Level1Unit"));
  }

  @Test
  public void test_getInfantry() {
    Territory e = new Territory("Elantris", "A", 6, 3, 3);
    LinkedHashMap<String, Integer> test_map = new LinkedHashMap<>();
    test_map.put("Level1Unit", 0);
    test_map.put("Level2Unit", 0);
    test_map.put("Level3Unit", 0);
    test_map.put("Level4Unit", 0);
    test_map.put("Level5Unit", 0);
    test_map.put("Level6Unit", 0);
    test_map.put("Level7Unit", 0);
    test_map.put("SpyUnit", 0);
    assertEquals(test_map, e.getInfantry(true));
  }

  @Test
  public void test_hasRoute() {
    Territory e = new Territory("Elantris", "A", 15, 12, 10);
    Territory s = new Territory("Scadrial", "A", 8, 10, 8);
    Territory md = new Territory("Mordor", "A", 10, 7, 6);
    Territory r = new Territory("Roshar", "A", 7, 14, 12);
    Territory h = new Territory("Hogwarts", "A", 5, 7, 9);
    Territory g = new Territory("Gondor", "B", 15, 17, 15);
    Territory n = new Territory("Narnia", "B", 12, 15, 14);
    Territory m = new Territory("Mid", "B", 7, 8, 7);
    Territory o = new Territory("Oz", "B", 11, 10, 9);


    addNeighborToEachOther(e, n, 7);
    addNeighborToEachOther(e, m, 8);
    addNeighborToEachOther(e, s, 3);
    addNeighborToEachOther(e, r, 3);
    addNeighborToEachOther(o, g, 4);
    addNeighborToEachOther(n, m, 4);
    addNeighborToEachOther(m, o, 3);
    addNeighborToEachOther(m, s, 9);
    addNeighborToEachOther(o, s, 9);
    addNeighborToEachOther(o, md, 8);
    addNeighborToEachOther(g, md, 10);
    addNeighborToEachOther(s, r, 3);
    addNeighborToEachOther(s, h, 4);
    addNeighborToEachOther(s, md, 4);
    addNeighborToEachOther(md, h, 4);
    addNeighborToEachOther(r, h, 3);

    Map map = new Map();
    assertTrue(r.hasRoute(h, map, false));
    assertTrue(r.hasRoute(e, map, false));
    assertTrue(n.hasRoute(o, map, false));
    assertTrue(n.hasRoute(m, map, false));
    assertTrue(e.hasRoute(s, map, false));
    assertTrue(e.hasRoute(md, map, false));
    assertTrue(r.hasRoute(md, map, false));
    assertFalse(e.hasRoute(g, map, false));
    assertFalse(e.hasRoute(n, map, false));
    assertFalse(r.hasRoute(n, map, false));

    map.updateTerritoryOwner("Mordor", "B");
    map.updateTerritoryOwner("Gondor", "A");
    md.updateOwner("B");
    g.updateOwner("A");
    assertFalse(r.hasRoute(g, map, false));
    assertTrue(n.hasRoute(md, map, false));
    assertTrue(r.hasRoute(g, map, true));
    assertTrue(n.hasRoute(h, map, true));
  }

  @Test
  public void test_equals() {
    Territory t1 = new Territory("Elantris", "A", 6, 3, 3);
    Territory t2 = new Territory("Elantris", "A", 6, 3, 3);
    Territory t3 = new Territory("Flantris", "A", 6, 3, 3);
    Territory t4 = new Territory("Elantris", "B", 6, 3, 3);
    Territory t5 = new Territory("Elantris", "B", 5, 3, 3);
    assertEquals(t1, t2);
    assertNotEquals(t1, t3);
    assertNotEquals(t1, t4);
    assertNotEquals(t1, t5);
    assertNotEquals(t1, "1");
    t2.getMyInfantry().put("test", 1);
    assertNotEquals(t1, t2);
  }

  @Test
  public void test_copy() {
    Territory t1 = new Territory("Elantris", "A", 6, 3, 3);
    Territory t2 = new Territory(t1);
    t1.updateOwner("B");
    assertNotEquals(t1, t2);
    t1.updateOwner("A");
    t1.updateUnit(10, "Level1Unit", false);
    assertNotEquals(t1, t2);
  }

  @Test
  public void test_display() {
    Territory t1 = new Territory("Elantris", "A", 6, 3, 5);
    assertEquals("<<Elantris>>\n" +
        "Owner: A", t1.toString());
    assertEquals("<<Elantris>>\n" +
        "Owner: A\n" +
        "FR: 3\tTR: 5\n" +
        "--Units--\n" +
        "L1: 6", t1.display(true, true));
    assertEquals("<<Elantris>>\n" +
        "Owner: A\n" +
        "FR: 3\tTR: 5\n" +
        "--Units--\n" +
        "L1: 6", t1.display(false, true));
    t1.updateUnit(10, "SpyUnit", false);
    t1.updateUnit(20, "SpyUnit", true);
    assertEquals("<<Elantris>>\n" +
        "Owner: A\n" +
        "FR: 3\tTR: 5\n" +
        "--Units--\n" +
        "L1: 6\tYour Spy: 10", t1.display(true, true));
    assertEquals("<<Elantris>>\n" +
        "Owner: A\n" +
        "FR: 3\tTR: 5\n" +
        "--Units--\n" +
        "L1: 6\tYour Spy: 20", t1.display(false, true));
    assertEquals("<<Elantris>>\n" +
        "Owner: A\n" +
        "FR: 3\tTR: 5\n" +
        "--Units--\n" +
        "L1: 6", t1.display(false, false));
  }

  @Test
  public void test_hasNeighborOwnedByEnemy() {
    Map m = new Map();
    assertTrue(m.getTerritory("Elantris").hasNeighborOwnedByEnemy(m));
    assertTrue(m.getTerritory("Oz").hasNeighborOwnedByEnemy(m));
    assertFalse(m.getTerritory("Roshar").hasNeighborOwnedByEnemy(m));
  }

  @Test
  public void test_clock() {
    Territory t1 = new Territory("Elantris", "A", 6, 3, 5);
    assertFalse(t1.isCloaked());
    t1.setCloak(true);
    assertTrue(t1.isCloaked());
    t1.setCloak(false);
    assertFalse(t1.isCloaked());
  }
}
