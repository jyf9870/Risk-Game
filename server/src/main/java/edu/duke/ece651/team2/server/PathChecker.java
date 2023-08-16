package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.MapRO;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import edu.duke.ece651.team2.shared.Territory;

public class PathChecker extends ActionRuleChecker {
  public PathChecker(ActionRuleChecker next) {
    super(next);
  }

  /**
   * @param a   is the TextAction needs to be checked by the rule
   * @param map is the current map
   * @return the result of rule checking
   */
  @Override
  protected String checkMyRule(MoveAttackTextAction a, MapRO map) {
    Territory from = map.getTerritory(a.from_territory);
    Territory to = map.getTerritory(a.to_territory);
    if (a.name.equals("A") && !from.hasNeighbor(to)) {
      return "The to territory of the attack" + a + " must be the neighbor of the from territory!";
    } else if (a.name.equals("M") && !from.hasRoute(to, map, false) && !a.infantryLevel.equals("SpyUnit")) {
      return "The to territory of the move" + a + " must have a route from the from territory!";
    } else if (a.name.equals("M") && a.infantryLevel.equals("SpyUnit") && !from.hasNeighbor(to)
        && !from.getOwnerName().equals(a.issuer)) {
      return "Spies can only move 1 territory at a time in enemy territory!";
    } else {
      return null;
    }
  }
}
