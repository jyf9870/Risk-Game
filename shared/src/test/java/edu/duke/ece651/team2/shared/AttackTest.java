package edu.duke.ece651.team2.shared;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class AttackTest {
  @Test
  public void test_getToTerritory() {
    DiceCombatRules r = new DiceCombatRules();
    Attack valid_a1 = new Attack("A", "Elantris", "Mid", 5, "level2", r);
    assertEquals("Mid", valid_a1.getToTerritory());
  }


  @Test
  public void test_moveUnits() {
    Map map = new Map();
    DiceCombatRules r = new DiceCombatRules();

    Attack invalid_a1 = new Attack("A", "Elantris", "Scadrial", 5, "Level2Unit", r);
    Attack invalid_a2 = new Attack("A", "Elantris", "Narnia", 8, "Level2Unit", r);
    LinkedHashMap<String, Integer> infantry = map.getTerritory("Elantris").getMyInfantry();
    infantry.put("Level2Unit", 6);
    LinkedHashMap<String, Integer> infantry2 = map.getTerritory("Scadrial").getMyInfantry();
    infantry2.put("Level2Unit", 5);
    LinkedHashMap<String, Integer> infantry3 = map.getTerritory("Mid").getEnemyInfantry();
    infantry3.put("Level2Unit", 0);


    assertThrows(IllegalArgumentException.class, () -> invalid_a1.moveUnits(map));
    assertThrows(IllegalArgumentException.class, () -> invalid_a2.moveUnits(map));

    assertEquals(6, map.getTerritory("Elantris").getMyInfantry().get("Level2Unit"));
    assertEquals(5, map.getTerritory("Scadrial").getMyInfantry().get("Level2Unit"));
    assertEquals(0, map.getTerritory("Mid").getEnemyInfantry().get("Level2Unit"));
    Attack valid_a1 = new Attack("A", "Elantris", "Mid", 5, "Level2Unit", r);
    Attack valid_a2 = new Attack("A", "Scadrial", "Mid", 3, "Level2Unit", r);
    valid_a1.moveUnits(map);
    assertEquals(1, map.getTerritory("Elantris").getMyInfantry().get("Level2Unit"));
    assertEquals(5, map.getTerritory("Mid").getEnemyInfantry().get("Level2Unit"));
    valid_a2.moveUnits(map);
    assertEquals(2, map.getTerritory("Scadrial").getMyInfantry().get("Level2Unit"));
    assertEquals(8, map.getTerritory("Mid").getEnemyInfantry().get("Level2Unit"));
  }

  @Test
  public void test_combat() {
    Map map = new Map();
    AdvancedCombatRules r = new AdvancedCombatRules();
    LinkedHashMap<String, Integer> infantry = map.getTerritory("Mordor").getMyInfantry();
    infantry.put("Level2Unit", 14);
    LinkedHashMap<String, Integer> infantry2 = map.getTerritory("Oz").getMyInfantry();
    infantry2.put("Level2Unit", 8);
    LinkedHashMap<String, Integer> infantry3 = map.getTerritory("Elantris").getMyInfantry();
    infantry3.put("Level2Unit", 20);
    LinkedHashMap<String, Integer> infantry4 = map.getTerritory("Narnia").getEnemyInfantry();
    infantry4.put("Level2Unit", 1);


    Attack valid_a1 = new Attack("A", "Mordor", "Oz", 10, "Level2Unit", r);
    assertEquals(14, map.getTerritory("Mordor").getMyInfantry().get("Level2Unit"));
    valid_a1.moveUnits(map);
    assertEquals(4, map.getTerritory("Mordor").getMyInfantry().get("Level2Unit"));
    assertEquals(8, map.getTerritory("Oz").getMyInfantry().get("Level2Unit"));
    assertEquals(10, map.getTerritory("Oz").getEnemyInfantry().get("Level2Unit"));
    assertEquals("B", map.getTerritory("Oz").getOwnerName());
    valid_a1.combat(map);
    assertEquals(4, map.getTerritory("Oz").getMyInfantry().get("Level2Unit"));
    assertEquals(0, map.getTerritory("Oz").getEnemyInfantry().get("Level2Unit"));
    assertEquals("B", map.getTerritory("Oz").getOwnerName());

    Attack valid_a2 = new Attack("A", "Elantris", "Narnia", 19, "Level2Unit", r);
    valid_a2.combat(map);
    assertEquals(20, map.getTerritory("Elantris").getMyInfantry().get("Level2Unit"));
    assertEquals(0, map.getTerritory("Narnia").getEnemyInfantry().get("Level2Unit"));
    valid_a2.moveUnits(map);
    assertEquals(1, map.getTerritory("Elantris").getMyInfantry().get("Level2Unit"));
    assertEquals(0, map.getTerritory("Narnia").getMyInfantry().get("Level2Unit"));
    assertEquals(19, map.getTerritory("Narnia").getEnemyInfantry().get("Level2Unit"));
    assertEquals("B", map.getTerritory("Narnia").getOwnerName());
    valid_a2.combat(map);
    assertEquals(8, map.getTerritory("Narnia").getMyInfantry().get("Level2Unit"));
    assertEquals(0, map.getTerritory("Narnia").getEnemyInfantry().get("Level2Unit"));
    assertEquals("A", map.getTerritory("Narnia").getOwnerName());
  }

  @Test
  public void test_equals() {
    DiceCombatRules r = new DiceCombatRules();
    Attack valid_a1 = new Attack("A", "Elantris", "Mid", 5, "Level2Unit", r);
    Attack valid_a2 = new Attack("A", "Elantris", "Mid", 5, "Level2Unit", r);
    Attack valid_a3 = new Attack("B", "Elantris", "Mid", 5, "Level2Unit", r);
    assertEquals(valid_a1, valid_a2);
    assertNotEquals(valid_a1, valid_a3);
    assertNotEquals(valid_a1, "test");
  }

  @Test
  public void testGet() {
    DiceCombatRules r = new DiceCombatRules();
    Attack attack = new Attack("A", "Elantris", "Scadrial", 5, "Level2Unit", r);
    assertEquals("Elantris", attack.getFromTerritory());
    assertEquals("Scadrial", attack.getToTerritory());
    assertEquals(5, attack.getUnitCt());
    assertEquals("Level2Unit", attack.getInfantryLevel());
  }
}
