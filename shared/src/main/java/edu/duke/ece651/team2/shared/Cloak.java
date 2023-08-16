package edu.duke.ece651.team2.shared;


public class Cloak {
  private final String territory;

  /**
   * This class supports behaviors of a cloak
   *
   * @param territory is the name of territory to be cloaked
   */
  public Cloak(String territory) {
    this.territory = territory;
  }

  /**
   * @param m is the map having the territory to be cloaked
   */
  public void cloakTerritory(Map m) {
    m.getTerritory(territory).setCloak(true);
  }

  /**
   * @return the name of territory to be cloaked
   */
  public String getTerritory() {
    return territory;
  }

}
