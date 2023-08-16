package edu.duke.ece651.team2.shared;


public class Attack {
  private final String issuer;
  private final String fromTerritory;
  private final String toTerritory;
  private final int unitCt;
  private final CombatRules rules;
  private final String infantryLevel;


  /**
   * This class supports behaviors of an attack
   *
   * @param issuer        is the name of the issuer who issues the attack
   * @param fromTerritory is the name of attacking territory
   * @param toTerritory   is the name of territory being attacked
   * @param unitCt        is the unit count used to attack the territory
   * @param rules         controls how each combat is resolved
   */
  public Attack(String issuer, String fromTerritory, String toTerritory, int unitCt, String infantryLevel, CombatRules rules) {
    this.issuer = issuer;
    this.fromTerritory = fromTerritory;
    this.toTerritory = toTerritory;
    this.unitCt = unitCt;
    this.rules = rules;
    this.infantryLevel = infantryLevel;
  }

  /**
   * This method checks whether the attack is valid or not based on the following criteria:
   * 0. from_territory and to_territory exist in the map
   * 1. from_territory belongs to issuer
   * 2. to_territory does not belong to issuer
   * 3. from_territory has units more than unitCt
   * 4. from_territory and to_territory are adjacent
   *
   * @param m represents the map used to perform the attack
   * @return true if the attack is valid, false otherwise
   */
  private boolean validate(Map m) {
    Territory from = m.getTerritory(fromTerritory);
    Territory to = m.getTerritory(toTerritory);
    return from != null && to != null &&
        from.getOwnerName().equals(issuer) && !to.getOwnerName().equals(issuer) &&
        from.getMyInfantry().get(infantryLevel) >= unitCt &&
        from.hasNeighbor(to);
  }

  /**
   * @param m is the map to be move units
   */
  public void moveUnits(Map m) {
    if (!validate(m)) {
      throw new IllegalArgumentException("The attack order is not valid!");
    }
    Territory from = m.getTerritory(fromTerritory);
    Territory to = m.getTerritory(toTerritory);
    m.updateTerritoryUnitCt(fromTerritory, from.getMyInfantry().get(infantryLevel) - unitCt, infantryLevel, false);
    m.updateTerritoryUnitCt(toTerritory, to.getEnemyInfantry().get(infantryLevel) + unitCt, infantryLevel, true);
  }

  /**
   * @param m is the map to be attack
   */
  public String combat(Map m) {
    boolean isAttackerWin = rules.resolveCombat(m, toTerritory);
    if (isAttackerWin) {
      m.updateTerritoryOwner(toTerritory, issuer);
      return (issuer);
    }
    return null;
  }

  /**
   * @return the numebr of attacking units
   */
  public int getUnitCt() {
    return unitCt;
  }

  /**
   * @return the name of fromTerritory
   */
  public String getFromTerritory() {
    return fromTerritory;
  }

  /**
   * @return a string of toTerritory
   */
  public String getToTerritory() {
    return toTerritory;
  }

  /**
   * @return the infantry level
   */
  public String getInfantryLevel() {
    return infantryLevel;
  }


  /**
   * override equals to compare Attack
   *
   * @param o is the compared object
   * @return if the two objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      Attack attack = (Attack) o;
      return attack.fromTerritory.equals(fromTerritory) && attack.toTerritory.equals(toTerritory) && attack.unitCt == unitCt
          && attack.issuer.equals(issuer) && attack.infantryLevel.equals(infantryLevel);
    }
    return false;
  }
}
