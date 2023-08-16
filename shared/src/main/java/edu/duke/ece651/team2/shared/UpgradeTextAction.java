package edu.duke.ece651.team2.shared;

import java.io.Serializable;

public class UpgradeTextAction extends TextAction implements Serializable {
  final public String upgrade_territory;
  final public String from_unit;
  final public String to_unit;
  final public int unitCt;

  public UpgradeTextAction(String name, String issuer, String upgrade_territory, String from_unit, String to_unit,
                           int unitCt) {
    super(name, issuer);
    this.upgrade_territory = upgrade_territory;
    this.from_unit = from_unit;
    this.to_unit = to_unit;
    this.unitCt = unitCt;
  }

  /**
   * override equals to compare UpgradeTextAction
   *
   * @param o is the compared object
   * @return if the two objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      UpgradeTextAction c = (UpgradeTextAction) o;
      return c.name.equals(name) && c.issuer.equals(issuer) && c.upgrade_territory.equals(upgrade_territory)
          && c.from_unit.equals(from_unit) && c.to_unit.equals(to_unit) && c.unitCt == unitCt;
    }
    return false;
  }

  /**
   * @return the String of UpgradeTextAction contents
   */
  @Override
  public String toString() {
    return "Player " + issuer + " is doing the " + name + " action, in territory " +
        upgrade_territory + ", upgrade " + unitCt + " " + from_unit + " to " + to_unit + ".";
  }

}
