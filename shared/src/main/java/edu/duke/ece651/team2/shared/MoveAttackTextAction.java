package edu.duke.ece651.team2.shared;

import java.io.Serializable;

public class MoveAttackTextAction extends TextAction implements Serializable {
  final public String from_territory;
  final public String to_territory;
  final public int unitCt;
  public String infantryLevel;

  /**
   * This class is used to represent the action sent between the client and server
   *
   * @param name           is the name of the action, e.g., A / M
   * @param issuer         is the name of the issuer who issues the action
   * @param from_territory is the name of the initiating territory
   * @param to_territory   is the name of the destination territory
   * @param unitCt         is the unit count used in the action
   */
  public MoveAttackTextAction(String name, String issuer, String from_territory, String to_territory, int unitCt, String infantryLevel) {
    super(name, issuer);
    this.from_territory = from_territory;
    this.to_territory = to_territory;
    this.unitCt = unitCt;
    this.infantryLevel = infantryLevel;
  }

  /**
   * override equals to compare MoveAttackTextAction
   *
   * @param o is the compared object
   * @return if the two objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      MoveAttackTextAction c = (MoveAttackTextAction) o;
      return c.name.equals(name) && c.issuer.equals(issuer) && c.from_territory.equals(from_territory) &&
          c.to_territory.equals(to_territory) && c.unitCt == unitCt && c.infantryLevel.equals(infantryLevel);
    }
    return false;
  }

  /**
   * @return the String of MoveAttackTextAction contents
   */
  @Override
  public String toString() {
    return "Player " + issuer + " is doing the " + name + " action, from territory " + from_territory + " to territory "
        + to_territory + ", with " + unitCt + " " + infantryLevel + ".";
  }
}
