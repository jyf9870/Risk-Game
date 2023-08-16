package edu.duke.ece651.team2.shared;

import java.util.LinkedHashMap;

public class Upgrade {
  private final String upgrade_territory;
  private final String fromUnit;
  private final String toUnit;
  private final int unitCt;

  public Upgrade(String upgrade_territory, String fromUnit, String toUnit, int unitCt) {
    this.upgrade_territory = upgrade_territory;
    this.fromUnit = fromUnit;
    this.toUnit = toUnit;
    this.unitCt = unitCt;
  }

  /**
   * @param m is the map being inputted to upgrade some unit on it
   */
  public void upgradeUnit(Map m) {
    Territory t = m.getTerritory(upgrade_territory);
    LinkedHashMap<String, Integer> oldInfantry = t.getInfantry(false);
    for (int i = 0; i < this.unitCt; i++) {
      oldInfantry.put(toUnit, oldInfantry.get(toUnit) + 1);
      oldInfantry.put(fromUnit, oldInfantry.get(fromUnit) - 1);
    }
  }

  /**
   * @return the from unit
   */
  public String getFromUnit() {
    return fromUnit;
  }

  /**
   * @return the to unit
   */
  public String getToUnit() {
    return toUnit;
  }

  /**
   * @return the upgrade territory
   */
  public String getUpgrade_territory() {
    return upgrade_territory;
  }

  /**
   * @return the unit count
   */
  public int getUnitCt() {
    return unitCt;
  }

  /**
   * override equals to compare Upgrade
   *
   * @param o is the compared object
   * @return if the two objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      Upgrade upgrade = (Upgrade) o;
      return upgrade.fromUnit.equals(fromUnit) && upgrade.toUnit.equals(toUnit) &&
          upgrade.upgrade_territory.equals(upgrade_territory) && upgrade.unitCt == unitCt;
    }
    return false;
  }

}
