package edu.duke.ece651.team2.shared;

public class CloakTextAction extends TextAction {
  final public String territory;

  /**
   * This class is used to represent the cloak action sent between the client and server
   *
   * @param name      is the name of the action, e.g., C
   * @param issuer    is the name of the issuer who issues the action
   * @param territory is the name of the territory to be cloaked
   */
  public CloakTextAction(String name, String issuer, String territory) {
    super(name, issuer);
    this.territory = territory;
  }

  @Override
  public String toString() {
    return "Player " + issuer + " is doing the " + name + " action to territory " + territory + ".";
  }

  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      CloakTextAction c = (CloakTextAction) o;
      return c.name.equals(name) && c.issuer.equals(issuer) && c.territory.equals(territory);
    }
    return false;
  }
}
