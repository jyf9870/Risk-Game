package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.MapRO;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;

public abstract class ActionRuleChecker {
  private final ActionRuleChecker next;

  /**
   * @param next is the next ActionRuleChecker, it can be used in a nest
   */
  public ActionRuleChecker(ActionRuleChecker next) {
    this.next = next;
  }

  /**
   * @param t   is the TextAction needs to be checked by the rule
   * @param map is the current map
   * @return the result of rule checking
   */
  protected abstract String checkMyRule(MoveAttackTextAction t, MapRO map);

  /**
   * @param t   is the TextAction needs to be checked by the rule
   * @param map is the current map
   * @return the result of rule checking
   */
  public String checkAction(MoveAttackTextAction t, MapRO map) {
    if (checkMyRule(t, map) != null) {
      return checkMyRule(t, map);
    }
    if (next != null) {
      return next.checkAction(t, map);
    }
    return null;
  }
}
