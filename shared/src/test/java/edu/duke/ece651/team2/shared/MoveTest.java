package edu.duke.ece651.team2.shared;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MoveTest {
  @Test
  public void test_moveUnits() {
    Map map = new Map();
    LinkedHashMap<String, Integer> infantry = map.getTerritory("Elantris").getMyInfantry();
    infantry.put("Level2Unit", 6);
    infantry.put("SpyUnit", 2);
    LinkedHashMap<String, Integer> infantry2 = map.getTerritory("Scadrial").getMyInfantry();
    infantry2.put("Level2Unit", 5);
    LinkedHashMap<String, Integer> infantry3 = map.getTerritory("Mordor").getMyInfantry();
    infantry3.put("Level2Unit", 14);
    assertEquals(6, map.getTerritory("Elantris").getMyInfantry().get("Level2Unit"));
    assertEquals(5, map.getTerritory("Scadrial").getMyInfantry().get("Level2Unit"));
    assertEquals(14, map.getTerritory("Mordor").getMyInfantry().get("Level2Unit"));
    assertEquals(2, map.getTerritory("Elantris").getMyInfantry().get("SpyUnit"));
    Move valid_m1 = new Move("Elantris", "Scadrial", 5, "Level2Unit", "A");
    Move valid_m2 = new Move("Scadrial", "Mordor", 3, "Level2Unit", "A");
    Move valid_m3 = new Move("Elantris", "Narnia", 1, "SpyUnit", "A");
    Move valid_m4 = new Move("Narnia", "Mid", 1, "SpyUnit", "A");
    Move valid_m5 = new Move("Mid", "Scadrial", 1, "SpyUnit", "A");
    Move valid_m6 = new Move("Scadrial", "Elantris", 1, "SpyUnit", "A");

    valid_m1.moveUnits(map);
    assertEquals(1, map.getTerritory("Elantris").getMyInfantry().get("Level2Unit"));
    assertEquals(10, map.getTerritory("Scadrial").getMyInfantry().get("Level2Unit"));
    valid_m2.moveUnits(map);
    assertEquals(7, map.getTerritory("Scadrial").getMyInfantry().get("Level2Unit"));
    assertEquals(17, map.getTerritory("Mordor").getMyInfantry().get("Level2Unit"));

    valid_m3.moveUnits(map);
    assertEquals(1, map.getTerritory("Elantris").getMyInfantry().get("SpyUnit"));
    assertEquals(1, map.getTerritory("Narnia").getEnemyInfantry().get("SpyUnit"));
    valid_m4.moveUnits(map);
    assertEquals(0, map.getTerritory("Narnia").getEnemyInfantry().get("SpyUnit"));
    assertEquals(1, map.getTerritory("Mid").getEnemyInfantry().get("SpyUnit"));
    valid_m5.moveUnits(map);
    assertEquals(0, map.getTerritory("Mid").getEnemyInfantry().get("SpyUnit"));
    assertEquals(1, map.getTerritory("Scadrial").getMyInfantry().get("SpyUnit"));
    valid_m6.moveUnits(map);
    assertEquals(0, map.getTerritory("Scadrial").getMyInfantry().get("SpyUnit"));
    assertEquals(2, map.getTerritory("Elantris").getMyInfantry().get("SpyUnit"));
  }

  @Test
  public void test_equals() {
    Move move1 = new Move("Elantris", "Scadrial", 5, "Level2Unit", "A");
    Move move2 = new Move("Elantris", "Scadrial", 5, "Level2Unit", "A");
    Move move3 = new Move("Elantris", "Scadrial", 15, "Level2Unit", "A");
    assertEquals(move1, move2);
    assertNotEquals(move1, move3);
    assertNotEquals(move1, "ss");
  }

  @Test
  public void testGet() {
    Move move1 = new Move("Elantris", "Scadrial", 5, "Level2Unit", "A");
    assertEquals("Elantris", move1.getFromTerritory());
    assertEquals("Scadrial", move1.getToTerritory());
    assertEquals(5, move1.getUnitCt());
    assertEquals("Level2Unit", move1.getInfantryLevel());

  }

}
