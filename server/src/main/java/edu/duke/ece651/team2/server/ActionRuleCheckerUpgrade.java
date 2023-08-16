package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.MapRO;
import edu.duke.ece651.team2.shared.UpgradeTextAction;

public abstract class ActionRuleCheckerUpgrade {
  private final ActionRuleCheckerUpgrade next;

  public ActionRuleCheckerUpgrade(ActionRuleCheckerUpgrade next) {
    this.next = next;
  }

  /**
   * @param t   is the TextAction needs to be checked by the rule
   * @param map is the current map
   * @return the result of rule checking
   */
  protected abstract String checkMyRule(UpgradeTextAction t, MapRO map);

  public String checkAction(UpgradeTextAction t, MapRO map) {
    if (checkMyRule(t, map) != null) {
      return checkMyRule(t, map);
    }
    if (next != null) {
      return next.checkAction(t, map);
    }
    return null;
  }
}
