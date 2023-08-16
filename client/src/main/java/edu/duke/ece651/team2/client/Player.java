package edu.duke.ece651.team2.client;

import edu.duke.ece651.team2.shared.Map;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Player {

  public Client client;
  protected String name;
  protected Map map;
  protected int techResource;

  protected int foodResource;


  /**
   * Serves as the player side of the client
   */
  public Player() throws IOException {
    this.client = new Client("localhost", 12345);
  }

  public Player(String name, Map map, Client client, int techResource, int foodResource) {
    this.name = name;
    this.map = map;
    this.client = client;
    this.techResource = techResource;
    this.foodResource = foodResource;
  }


  /**
   * get the player's name
   *
   * @return own name identifier
   */
  public String getName() {
    return this.name;
  }

  /**
   * receive an updated map
   *
   * @return a Map from server
   */
  public Map getMap() {
    return this.map;
  }

  /**
   * return the winner name and the status
   *
   * @return a string of the result. e.g. If A wins, return AWIN
   * @throws IOException when there are input or output exceptions
   */
  public String receiveWinningStatus() throws IOException {
    return client.receiveTextMsg();
  }

  /**
   * receive the player's name when init
   *
   * @throws IOException when there are input or output exceptions
   */
  public void receiveName() throws IOException {
    name = client.receiveTextMsg();
  }

  /**
   * receive the player's name when init
   *
   * @throws IOException when there are input or output exceptions
   */
  public String receiveResourceInfo() throws IOException {
    return client.receiveTextMsg();
  }

  /**
   * receive the init map
   *
   * @throws IOException            when there are input or output exceptions
   * @throws ClassNotFoundException when there are class not found exceptions
   */
  public void receiveMap() throws IOException, ClassNotFoundException {
    map = client.receiveMap();
  }

  /**
   * close the client socket
   *
   * @throws IOException when there are input or output exceptions
   */
  public void disconnect() throws IOException {
    client.closeClient();
  }

  public void updateResources(String resourceInfo) {
    String pattern = "(\\d+)";
    Pattern r = Pattern.compile(pattern);

    Matcher m = r.matcher(resourceInfo);
    if (m.find()) {
      this.foodResource = Integer.parseInt(m.group(0));
    }
    if (m.find()) {
      this.techResource = Integer.parseInt(m.group(0));
    }
  }

  public int getTechResource() {
    return techResource;
  }

  public int getFoodResource() {
    return foodResource;
  }

}
