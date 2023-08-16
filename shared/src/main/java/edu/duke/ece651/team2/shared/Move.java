package edu.duke.ece651.team2.shared;

public class Move {
  private final String fromTerritory;
  private final String toTerritory;
  private final int unitCt;
  private final String infantryLevel;
  private final String issuer;

  /**
   * This class supports behaviors of an attack
   *
   * @param fromTerritory is the name of territory where units are moving from
   * @param toTerritory   is the name of territory where units are moving to
   * @param unitCt        is the count of moving unit
   * @param infantryLevel is the level of moving unit
   * @param issuer        is the name of the issuer who issues the action
   */
  public Move(String fromTerritory, String toTerritory, int unitCt, String infantryLevel, String issuer) {
    this.fromTerritory = fromTerritory;
    this.toTerritory = toTerritory;
    this.unitCt = unitCt;
    this.infantryLevel = infantryLevel;
    this.issuer = issuer;
  }

  /**
   * @param m is the map to be move units in
   */
  public void moveUnits(Map m) {
    Territory from = m.getTerritory(fromTerritory);
    Territory to = m.getTerritory(toTerritory);
    if (infantryLevel.equals("SpyUnit")) {
      if (from.getOwnerName().equals(issuer)) {
        m.updateTerritoryUnitCt(fromTerritory, from.getMyInfantry().get(infantryLevel) - unitCt, infantryLevel, false);
      } else {
        m.updateTerritoryUnitCt(fromTerritory, from.getEnemyInfantry().get(infantryLevel) - unitCt, infantryLevel, true);
      }
      if (to.getOwnerName().equals(issuer)) {
        m.updateTerritoryUnitCt(toTerritory, to.getMyInfantry().get(infantryLevel) + unitCt, infantryLevel, false);
      } else {
        m.updateTerritoryUnitCt(toTerritory, to.getEnemyInfantry().get(infantryLevel) + unitCt, infantryLevel, true);
      }
    } else {
      m.updateTerritoryUnitCt(fromTerritory, from.getMyInfantry().get(infantryLevel) - unitCt, infantryLevel, false);
      m.updateTerritoryUnitCt(toTerritory, to.getMyInfantry().get(infantryLevel) + unitCt, infantryLevel, false);
    }
  }

  /**
   * @return the number of unit
   */
  public int getUnitCt() {
    return unitCt;
  }

  /**
   * @return the name of from territory
   */
  public String getFromTerritory() {
    return fromTerritory;
  }

  /**
   * @return the name of to territory
   */
  public String getToTerritory() {
    return toTerritory;
  }

  public String getInfantryLevel() {
    return infantryLevel;
  }

  /**
   * override equals to compare Move
   *
   * @param o is the compared object
   * @return if the two objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      Move move = (Move) o;
      return move.fromTerritory.equals(fromTerritory) && move.toTerritory.equals(toTerritory) && move.unitCt == unitCt && move.infantryLevel.equals(infantryLevel);
    }
    return false;
  }
}
