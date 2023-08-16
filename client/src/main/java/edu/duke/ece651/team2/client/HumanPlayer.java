package edu.duke.ece651.team2.client;

import edu.duke.ece651.team2.shared.Map;

import java.io.IOException;

public abstract class HumanPlayer extends Player {

  public final String MOVE = "M";
  public final String ATTACK = "A";
  public final String UPGRADE = "U";
  public final String CLOAK = "C";
  public final String DONE = "D";

  public HumanPlayer() throws IOException {
    super();
  }

  public HumanPlayer(String name, Map map, Client client, int techResource, int foodResource) {
    super(name, map, client, techResource, foodResource);
  }
}
