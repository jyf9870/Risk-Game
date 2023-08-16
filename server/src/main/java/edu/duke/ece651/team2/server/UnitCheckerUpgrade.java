package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.MapRO;
import edu.duke.ece651.team2.shared.UpgradeTextAction;

import java.util.HashMap;

public class UnitCheckerUpgrade extends ActionRuleCheckerUpgrade {

  private final HashMap<String, Integer> unitRanking;

  public UnitCheckerUpgrade(ActionRuleCheckerUpgrade next) {
    super(next);
    this.unitRanking = new HashMap<>();
    this.unitRanking.put("Level1Unit", 1);
    this.unitRanking.put("Level2Unit", 2);
    this.unitRanking.put("Level3Unit", 3);
    this.unitRanking.put("Level4Unit", 4);
    this.unitRanking.put("Level5Unit", 5);
    this.unitRanking.put("Level6Unit", 6);
    this.unitRanking.put("Level7Unit", 7);
    this.unitRanking.put("SpyUnit", 8);
  }

  /**
   * @param from_unit is the start infantry level
   * @param to_unit   is the end infantry level
   * @return the true or false result for unit checking
   */
  private boolean unitRankingCheck(String from_unit, String to_unit) {
    return this.unitRanking.get(from_unit) < this.unitRanking.get(to_unit);
  }

  /**
   * @param a   is the TextAction needs to be checked by the rule
   * @param map is the current map
   * @return the result of rule checking
   */
  @Override
  protected String checkMyRule(UpgradeTextAction a, MapRO map) {
    if (map.getTerritory(a.upgrade_territory).getMyUnitCt(a.from_unit) < a.unitCt) {
      return "The from unit used in the action" + a + " are insufficient!";
    } else if (!unitRankingCheck(a.from_unit, a.to_unit)) {
      return "The from unit used in the action" + a + " is not less than the to unit!";
    } else {
      return null;
    }
  }
}
