package edu.duke.ece651.team2.shared;

/**
 * This interface controls how each combat is resolved
 */
public interface CombatRules {
  /**
   * @param map  represents the map
   * @param name represents the name of the territory being attacked
   * @return true if the attacker wins the combat, false otherwise
   */
  boolean resolveCombat(Map map, String name);
}
