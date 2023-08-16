package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.MapRO;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import edu.duke.ece651.team2.shared.Territory;

public class UnitChecker extends ActionRuleChecker {
  public UnitChecker(ActionRuleChecker next) {
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
    if (a.unitCt > from.getMyInfantry().get(a.infantryLevel) && !a.infantryLevel.equals("SpyUnit")) {
      return "The number of units used in the action " + a + " exceeds the limit!";
    } else if (a.name.equals("M") && a.infantryLevel.equals("SpyUnit")) {
      if (from.getOwnerName().equals(a.issuer) && a.unitCt > from.getMyInfantry().get(a.infantryLevel)) {
        return "The number of units used in the action " + a + " exceeds the limit!";
      } else if (!from.getOwnerName().equals(a.issuer) && a.unitCt > from.getEnemyInfantry().get(a.infantryLevel)) {
        return "The number of units used in the action " + a + " exceeds the limit!";
      } else {
        return null;
      }
    } else if (a.name.equals("A") && a.infantryLevel.equals("SpyUnit")) {
      return "The spy unit cannot attack!";
    } else {
      return null;
    }
  }
}

