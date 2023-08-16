package edu.duke.ece651.team2.client;

import edu.duke.ece651.team2.shared.*;
import org.junit.jupiter.api.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ComputerPlayerTest {

  @Test
  public void test_init() throws IOException, ClassNotFoundException {
    // Set up the server socket
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer player = new ComputerPlayer();
    // Receive winning status
    Socket serverSkt = serverSocket.accept();
    // Send name
    DataOutputStream dataOut = new DataOutputStream(serverSkt.getOutputStream());
    String expectedName = "Allen";
    dataOut.writeUTF(expectedName);
    // Send map
    Map m = new Map();
    ObjectOutputStream objectOut = new ObjectOutputStream(serverSkt.getOutputStream());
    objectOut.writeObject(m);
    // Send resource info
    dataOut.writeUTF("Your total resource:\t\tFood: 999\t\tTech: 999");
    player.init();
    assertEquals(expectedName, player.getName());
    assertEquals(m, player.getMap());
    // Disconnect
    player.disconnect();
    serverSkt.close();
    serverSocket.close();
  }

  @Test
  public void testFindWeakestTerritory() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    Map map = new Map();
    c.map = map;
    c.name = "A";
    String res = c.findWeakestTerritory();
    assertEquals("Mid", res);
    serverSocket.close();
    c.disconnect();
  }

  @Test
  public void testCalculateInfantry() throws IOException {
    LinkedHashMap<String, Integer> myInfantry = new LinkedHashMap<>();
    myInfantry.put("Level1Unit", 10);
    myInfantry.put("Level2Unit", 0);
    myInfantry.put("Level3Unit", 5);
    myInfantry.put("Level4Unit", 0);
    myInfantry.put("Level5Unit", 1);
    myInfantry.put("Level6Unit", 0);
    myInfantry.put("Level7Unit", 0);

    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    int res = c.calculateInfantry(myInfantry);
    assertEquals(183, res);
    serverSocket.close();
    c.disconnect();
  }

  @Test
  public void testCalculateSingleInfantry() throws IOException {
    HashMap<String, Integer> infantryValue = new HashMap<>();
    infantryValue.put("Level1Unit", 10);
    infantryValue.put("Level2Unit", 11);
    infantryValue.put("Level3Unit", 13);
    infantryValue.put("Level4Unit", 15);
    infantryValue.put("Level5Unit", 18);
    infantryValue.put("Level6Unit", 21);
    infantryValue.put("Level7Unit", 25);

    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    int res = c.calculateSingleInfantry("Level5Unit");
    assertEquals(18, res);
    serverSocket.close();
    c.disconnect();
  }

  @Test
  public void testUpdateResources() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    Client client = new Client("localhost", 12345);
    Map map = new Map();
    String name = "A";
    ComputerPlayer c = new ComputerPlayer(name, map, client, 10, 10);
    c.updateResources("");
    assertEquals(10, c.techResource);
    assertEquals(10, c.foodResource);
    c.updateResources("Your total resource:\t\tFood: 400\t\tTech: 998");
    assertEquals(400, c.foodResource);
    assertEquals(998, c.techResource);
    serverSocket.close();
    c.disconnect();
  }

  @Test
  public void testModifyTerritoryDummyMap() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    Map map = new Map();
    c.map = map;
    c.name = "A";
    c.techResource = 45;
    LinkedHashMap<Territory, LinkedHashMap<String, Integer>> terrInfantryMap = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> scadrialInfantry = new LinkedHashMap<>();
    scadrialInfantry.put("Level1Unit", 5);
    scadrialInfantry.put("Level2Unit", 0);
    scadrialInfantry.put("Level3Unit", 0);
    scadrialInfantry.put("Level4Unit", 0);
    scadrialInfantry.put("Level5Unit", 0);
    scadrialInfantry.put("Level6Unit", 0);
    scadrialInfantry.put("Level7Unit", 0);
    terrInfantryMap.put(c.map.getTerritory("Scadrial"), scadrialInfantry);
    LinkedHashMap<String, Integer> elantrisInfantry = new LinkedHashMap<>();
    elantrisInfantry.put("Level1Unit", 2);
    elantrisInfantry.put("Level2Unit", 0);
    elantrisInfantry.put("Level3Unit", 0);
    elantrisInfantry.put("Level4Unit", 0);
    elantrisInfantry.put("Level5Unit", 0);
    elantrisInfantry.put("Level6Unit", 0);
    elantrisInfantry.put("Level7Unit", 0);
    terrInfantryMap.put(c.map.getTerritory("Elantris"), elantrisInfantry);

    c.modifyTerritoryDummyMap(terrInfantryMap);
    assertEquals(2, terrInfantryMap.get(c.map.getTerritory("Elantris")).get("Level1Unit"));
    assertEquals(5, terrInfantryMap.get(c.map.getTerritory("Scadrial")).get("Level1Unit"));
    c.actions.add(new MoveAttackTextAction("M", "A", "Scadrial", "Elantris", 3, "Level1Unit"));
    c.modifyTerritoryDummyMap(terrInfantryMap);
    assertEquals(5, terrInfantryMap.get(c.map.getTerritory("Elantris")).get("Level1Unit"));
    assertEquals(2, terrInfantryMap.get(c.map.getTerritory("Scadrial")).get("Level1Unit"));
    assertEquals(0, terrInfantryMap.get(c.map.getTerritory("Elantris")).get("Level2Unit"));
    assertEquals(0, terrInfantryMap.get(c.map.getTerritory("Scadrial")).get("Level2Unit"));
    serverSocket.close();
    c.disconnect();
  }

  @Test
  public void testCheckIfAllZero() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    LinkedHashMap<String, Integer> testInfantry = new LinkedHashMap<>();
    testInfantry.put("Level1Unit", 0);
    testInfantry.put("Level2Unit", 0);
    testInfantry.put("Level3Unit", 0);
    testInfantry.put("Level4Unit", 0);
    testInfantry.put("Level5Unit", 0);
    testInfantry.put("Level6Unit", 0);
    testInfantry.put("Level7Unit", 0);
    assertTrue(c.checkIfAllZero(testInfantry));
    testInfantry.put("Level1Unit", 1);
    assertFalse(c.checkIfAllZero(testInfantry));
    serverSocket.close();
    c.disconnect();
  }

  @Test
  public void testPerformUpgrade() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    Map map = new Map();
    c.map = map;
    c.name = "A";
    c.techResource = 40;
    LinkedHashMap<String, Integer> testInfantry = new LinkedHashMap<>();
    testInfantry.put("Level1Unit", 0);
    testInfantry.put("Level2Unit", 0);
    testInfantry.put("Level3Unit", 0);
    testInfantry.put("Level4Unit", 0);
    testInfantry.put("Level5Unit", 0);
    testInfantry.put("Level6Unit", 1);
    testInfantry.put("Level7Unit", 0);
    c.performUpgrade("Elantris", testInfantry, 50);
    serverSocket.close();
    c.disconnect();
  }

  @Test
  public void testUpgradeEqually() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    Map map = new Map();
    ArrayList<TextAction> expectedActions = new ArrayList<>();
    expectedActions.add(new UpgradeTextAction("U", "A", "Elantris", "Level1Unit", "Level2Unit", 1));
    expectedActions.add(new UpgradeTextAction("U", "A", "Elantris", "Level1Unit", "Level2Unit", 1));
    expectedActions.add(new UpgradeTextAction("U", "A", "Scadrial", "Level1Unit", "Level2Unit", 1));
    expectedActions.add(new UpgradeTextAction("U", "A", "Scadrial", "Level1Unit", "Level2Unit", 1));
    expectedActions.add(new UpgradeTextAction("U", "A", "Mordor", "Level1Unit", "Level2Unit", 1));
    expectedActions.add(new UpgradeTextAction("U", "A", "Mordor", "Level1Unit", "Level2Unit", 1));
    expectedActions.add(new UpgradeTextAction("U", "A", "Roshar", "Level1Unit", "Level2Unit", 1));
    expectedActions.add(new UpgradeTextAction("U", "A", "Roshar", "Level1Unit", "Level2Unit", 1));
    expectedActions.add(new UpgradeTextAction("U", "A", "Hogwarts", "Level1Unit", "Level2Unit", 1));
    expectedActions.add(new UpgradeTextAction("U", "A", "Hogwarts", "Level1Unit", "Level2Unit", 1));

    c.map = map;
    c.name = "A";
    c.techResource = 40;
    c.upgradeEqually();
    assertEquals(expectedActions, c.actions);

    serverSocket.close();
    c.disconnect();
  }

  @Test
  public void testAttackFromClosest() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    Map map = new Map();
    c.map = map;
    c.map.updateTerritoryOwner("Narnia", "A");
    c.name = "A";
    boolean res = c.attackFromClosest("Mid");
    assertFalse(res);
    boolean res2 = c.attackFromClosest("Oz");
    assertFalse(res2);
    serverSocket.close();
    c.disconnect();

  }

  @Test
  public void testPlayOneTurn() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    Map map = new Map();
    c.map = map;
    c.name = "A";
    c.playOneTurn();
    c.foodResource = 100;
    c.map.getTerritory("Hogwarts").getMyInfantry().put("Level1Unit", 0);
    c.playOneTurn();
    c.playOneTurn();
    c.playOneTurn();
    c.playOneTurn();
    c.techResource = 100;
    c.playOneTurn();
    serverSocket.close();
    c.disconnect();

  }

  @Test
  public void testAttackAndMoveAction() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    Map map = new Map();
    c.map = map;
    c.name = "A";
    boolean res = c.attackAndMoveAction(9, "Scadrial", "Mid");
    assertFalse(res);
    c.foodResource = 200;
    res = c.attackAndMoveAction(9, "Scadrial", "Mid");
    assertTrue(res);

    c.name = "B";
    boolean res2 = c.attackAndMoveAction(9, "Mid", "Scadrial");
    assertTrue(res2);
    c.foodResource = 70;
    res2 = c.attackAndMoveAction(9, "Mid", "Scadrial");
    assertFalse(res2);

    serverSocket.close();
    c.disconnect();

  }

  @Test
  public void testTryAndDoMove() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    Map map = new Map();
    c.map = map;
    c.name = "A";
    LinkedHashMap<String, Integer> fromInfantry = new LinkedHashMap<>();
    fromInfantry.put("Level1Unit", 1);
    fromInfantry.put("Level2Unit", 1);

    Territory t1 = new Territory("AA", "A", 6, 0, 0);
    Territory t2 = new Territory("AB", "b", 6, 0, 0);
    c.foodResource = 5000000;
    c.tryAndDoMove(fromInfantry, t1, t2, 10, 0);
    c.map.updateTerritoryOwner("Elantris", "B");
    c.tryAndDoMove(fromInfantry, map.getTerritory("Scadrial"), map.getTerritory("Roshar"), 200000, 20);

    serverSocket.close();
    c.disconnect();
  }

  @Test
  public void testDoAttack() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    Map map = new Map();
    c.map = map;
    c.name = "A";
    c.foodResource = 500;
    Territory t = new Territory("A", "A", 0, 0, 0);
    Territory t2 = new Territory("B", "B", 0, 0, 0);
    t.getMyInfantry().clear();
    t.getMyInfantry().put("Level1Unit", 1);
    t2.getMyInfantry().put("Level7Unit", 1);
    c.doAttack(t, t2);

    serverSocket.close();
    c.disconnect();

  }

  @Test
  public void testTryAttack() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    Map map = new Map();
    c.map = map;
    c.name = "A";
    c.foodResource = 500;
    Territory t = new Territory("A", "A", 0, 0, 0);
    Territory t2 = new Territory("B", "B", 0, 0, 0);
    t.getMyInfantry().clear();
    t.getMyInfantry().put("Level1Unit", 1);
    t2.getMyInfantry().put("Level7Unit", 1);
    c.tryAttack(t, t2, 2);

    serverSocket.close();
    c.disconnect();

  }

  @Test
  public void testGetRsc() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    ComputerPlayer c = new ComputerPlayer();
    Map map = new Map();
    c.map = map;
    c.name = "A";
    assertEquals(0, c.getFoodResource());
    assertEquals(0, c.getTechResource());

    serverSocket.close();
    c.disconnect();

  }

  @Test
  public void testPlay() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    Client client = new Client("localhost", 12345);
    Socket serverSkt = serverSocket.accept();
    Map map = new Map();
    ComputerPlayer c = new ComputerPlayer("A", map, client, 40, 50);

    // c.play();
    serverSocket.close();
    c.disconnect();

  }

}
