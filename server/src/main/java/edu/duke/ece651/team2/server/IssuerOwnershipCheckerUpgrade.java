package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.MapRO;
import edu.duke.ece651.team2.shared.Territory;
import edu.duke.ece651.team2.shared.UpgradeTextAction;

public class IssuerOwnershipCheckerUpgrade extends ActionRuleCheckerUpgrade {

  public IssuerOwnershipCheckerUpgrade(ActionRuleCheckerUpgrade next) {
    super(next);
  }

  /**
   * @param a   is the TextAction needs to be checked by the rule
   * @param map is the current map
   * @return the result of rule checking
   */
  @Override
  protected String checkMyRule(UpgradeTextAction a, MapRO map) {
    Territory upgrade_territory = map.getTerritory(a.upgrade_territory);
    if (!upgrade_territory.getOwnerName().equals(a.issuer)) {
      return "The upgrade territory of the action" + a + " does not belong to the issuer!";
    } else {
      return null;
    }
  }

}
