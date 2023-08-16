package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.MapRO;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;

public class BasicChecker extends ActionRuleChecker {
  public BasicChecker(ActionRuleChecker next) {
    super(next);
  }

  /**
   * @param a   is the TextAction needs to be checked by the rule
   * @param map is the current map
   * @return the result of rule checking
   */
  @Override
  protected String checkMyRule(MoveAttackTextAction a, MapRO map) {
    if (!a.name.equals("A") && !a.name.equals("M")) {
      return "The action" + a + " must be Attack or Move!";
    } else if (map.getTerritory(a.from_territory) == null) {
      return "The from territory of the action " + a + " does not exist!";
    } else if (map.getTerritory(a.to_territory) == null) {
      return "The to territory of the action " + a + " does not exist!";
    } else if (!map.getTerritory(a.from_territory).getMyInfantry().containsKey(a.infantryLevel)) {
      return "The infantry is invalid!";
    } else if (a.unitCt < 0) {
      return "The number of units used in the action " + a + " is less than zero!";
    } else {
      return null;
    }
  }
}
