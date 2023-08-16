package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.MapRO;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import edu.duke.ece651.team2.shared.Territory;

public class IssuerOwnershipChecker extends ActionRuleChecker {
  public IssuerOwnershipChecker(ActionRuleChecker next) {
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
    if (!from.getOwnerName().equals(a.issuer) && !a.infantryLevel.equals("SpyUnit")) {
      return "The from territory of the action" + a + " does not belong to the issuer!";
    } else if (a.name.equals("A") && to.getOwnerName().equals(a.issuer)) {
      return "The to territory of the attack" + a + " cannot be your own territory!";
    } else if (a.name.equals("M") && !to.getOwnerName().equals(a.issuer) && !a.infantryLevel.equals("SpyUnit")) {
      return "The to territory of the move" + a + " must be your own territory!";
    } else {
      return null;
    }
  }
}
