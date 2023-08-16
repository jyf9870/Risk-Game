package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.MapRO;
import edu.duke.ece651.team2.shared.UpgradeTextAction;

public class BasicCheckerUpgrade extends ActionRuleCheckerUpgrade {

  public BasicCheckerUpgrade(ActionRuleCheckerUpgrade next) {
    super(next);
  }

  /**
   * @param a   is the TextAction needs to be checked by the rule
   * @param map is the current map
   * @return the result of rule checking
   */
  @Override
  protected String checkMyRule(UpgradeTextAction a, MapRO map) {
    if (!a.name.equals("U")) {
      return "The action" + a + " must be Upgrade!";
    } else if (map.getTerritory(a.upgrade_territory) == null) {
      return "The upgrade territory of the action" + a + " does not exist!";
    } else if (a.unitCt < 0) {
      return "The number of units used in the action " + a + " is less than zero!";
    } else if
    (!map.getTerritory(a.upgrade_territory).getInfantryTypes().contains(a.from_unit)) {
      return "The from unit level type of the action" + a + " does not exist!";
    } else if
    (!map.getTerritory(a.upgrade_territory).getInfantryTypes().contains(a.to_unit)) {
      return "The to unit level type of the action" + a + " does not exist!";
    } else {
      return null;
    }
  }
}
