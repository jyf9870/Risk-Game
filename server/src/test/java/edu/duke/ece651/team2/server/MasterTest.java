package edu.duke.ece651.team2.server;


import edu.duke.ece651.team2.shared.*;
import org.junit.jupiter.api.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class MasterTest {
  private static final int PORT = 12345;

  private Map helpReceiveMap(Socket client) throws IOException, ClassNotFoundException {
    ObjectInputStream in = new ObjectInputStream(client.getInputStream());
    Map map = (Map) in.readObject();
    return map;
  }

  private String helpReceiveMsg(Socket client) throws IOException {
    DataInputStream in = new DataInputStream(client.getInputStream());
    String res = in.readUTF();
    return res;
  }

  private void helpSendActions(ArrayList<TextAction> list, Socket client) throws IOException {
    ObjectOutputStream objectOut = new ObjectOutputStream(client.getOutputStream());
    objectOut.writeObject(list);
  }

  @Test
  public void testInitPlayers() throws IOException, ClassNotFoundException {
    Map expectedMap = new Map();
    Master master = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    master.initPlayers();
    assertEquals("A", helpReceiveMsg(client1));
    assertEquals(expectedMap, helpReceiveMap(client1));
    assertEquals("Your total resource:\t\tFood: 50\t\tTech: 40", helpReceiveMsg(client1));
    assertEquals("B", helpReceiveMsg(client2));
    assertEquals(expectedMap, helpReceiveMap(client2));
    assertEquals("Your total resource:\t\tFood: 50\t\tTech: 40", helpReceiveMsg(client2));
    client1.close();
    client2.close();
    master.closeSocket();
  }

  @Test
  public void testPlayOneTurn_2() throws IOException, ClassNotFoundException {
    Master master = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    master.initPlayers();

    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    master.resourceUnits.get("techRsc").put("B", 999);
    ArrayList<TextAction> invalid = new ArrayList<>();
    invalid.add(new MoveAttackTextAction("M", "A", "Mordor", "Elantris", 10, "Level1Unit"));
    helpSendActions(invalid, client1);
    ArrayList<TextAction> invalid2 = new ArrayList<>();
    invalid2.add(new UpgradeTextAction("U", "A", "Elantris", "Level1Unit", "Level7Unit", 1));
    helpSendActions(invalid2, client1);
    ArrayList<TextAction> invalid3 = new ArrayList<>();
    invalid3.add(new CloakTextAction("C", "A", "Oz"));
    helpSendActions(invalid3, client1);

    // Valid input 1 from Player A
    ArrayList<TextAction> valid_a1 = new ArrayList<>();
    valid_a1.add(new MoveAttackTextAction("M", "A", "Elantris", "Scadrial", 10, "Level1Unit"));
    valid_a1.add(new UpgradeTextAction("U", "A", "Elantris", "Level1Unit", "Level2Unit", 1));

    helpSendActions(valid_a1, client1);

    // Valid input 1 from Player B
    ArrayList<TextAction> valid_b1 = new ArrayList<>();
    valid_b1.add(new MoveAttackTextAction("A", "B", "Narnia", "Elantris", 6, "Level1Unit"));
    valid_b1.add(new CloakTextAction("C", "B", "Oz"));
    helpSendActions(valid_b1, client2);

    master.playOneTurn();
    assertEquals("Your food resource is not enough!", helpReceiveMsg(client1));
    assertEquals("Invalid action order!", helpReceiveMsg(client1));
    assertEquals("Invalid cloak order!", helpReceiveMsg(client1));
    assertEquals("True", helpReceiveMsg(client1));
    assertEquals("True", helpReceiveMsg(client2));
    assertEquals(1, master.mainCloak.get("Oz"));

    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);

    // Round 2
    helpSendActions(new ArrayList<>(), client1);
    helpSendActions(new ArrayList<>(), client2);
    master.playOneTurn();
    assertEquals("True", helpReceiveMsg(client1));
    assertEquals("True", helpReceiveMsg(client2));
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    assertEquals(2, master.mainCloak.get("Oz"));

    // Round 3
    helpSendActions(new ArrayList<>(), client1);
    helpSendActions(new ArrayList<>(), client2);
    master.playOneTurn();
    assertEquals("True", helpReceiveMsg(client1));
    assertEquals("True", helpReceiveMsg(client2));
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    assertEquals(3, master.mainCloak.get("Oz"));

    // Round 4
    helpSendActions(new ArrayList<>(), client1);
    helpSendActions(new ArrayList<>(), client2);
    master.playOneTurn();
    assertEquals("True", helpReceiveMsg(client1));
    assertEquals("True", helpReceiveMsg(client2));
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    assertFalse(master.mainCloak.containsKey("Oz"));

    client1.close();
    client2.close();
    master.closeSocket();
  }


  @Test
  public void testCloseSocket() throws IOException, ClassNotFoundException {
    Master master = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    master.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    client1.close();
    client2.close();
    master.closeSocket();
    assertTrue(master.client1.isClosed());
    assertTrue(master.client2.isClosed());
  }


  @Test
  public void testAddWinner() throws IOException, ClassNotFoundException {
    Master master = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    master.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);

    ArrayList<String> wins = new ArrayList<>();
    wins.add("Elantris");
    wins.add("Scadrial");
    StringBuilder sb = new StringBuilder();
    master.addWinner(wins, sb, "A");
    assertEquals("A successfully occupies Elantris, Scadrial\n", sb.toString());
    client1.close();
    client2.close();
    master.closeSocket();
  }

  @Test
  public void testPlayOneTurn() throws IOException, ClassNotFoundException {
    Master master = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    master.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);

    master.resourceUnits.get("foodRsc").put("A", 999);
    master.resourceUnits.get("techRsc").put("A", 999);
    master.resourceUnits.get("foodRsc").put("B", 999);
    master.resourceUnits.get("techRsc").put("B", 999);

    // Invalid input 1 from Player A: from_territory does not exist!
    ArrayList<TextAction> invalid_a1 = new ArrayList<>();
    invalid_a1.add(new MoveAttackTextAction("M", "A", "eee", "Scadrial", 5, "Level1Unit"));
    helpSendActions(invalid_a1, client1);
    // Invalid input 2 from Player A: from_territory does not belong to the issuer!
    ArrayList<TextAction> invalid_a2 = new ArrayList<>();
    invalid_a2.add(new MoveAttackTextAction("M", "A", "Narnia", "Scadrial", 5, "Level1Unit"));
    helpSendActions(invalid_a2, client1);
    // Invalid input 3 from Player A: invalid action type!
    ArrayList<TextAction> invalid_a3 = new ArrayList<>();
    invalid_a3.add(new MoveAttackTextAction("ABC", "A", "Narnia", "Narnia", 5, "Level1Unit"));
    helpSendActions(invalid_a3, client1);
    // Invalid input 4 from Player A: invalid upgrade!
    ArrayList<TextAction> invalid_a4 = new ArrayList<>();
    invalid_a4.add(new UpgradeTextAction("U", "AU", "Scadrial", "Level1Unit", "Level2Unit", 1));
    helpSendActions(invalid_a4, client1);

    // Valid input 1 from Player A: 2 moves and s attacks
    ArrayList<TextAction> valid_a1 = new ArrayList<>();
    valid_a1.add(new MoveAttackTextAction("M", "A", "Elantris", "Scadrial", 5, "Level1Unit"));
    valid_a1.add(new MoveAttackTextAction("M", "A", "Scadrial", "Mordor", 4, "Level1Unit"));
    valid_a1.add(new MoveAttackTextAction("A", "A", "Mordor", "Oz", 1, "Level1Unit"));
    valid_a1.add(new MoveAttackTextAction("A", "A", "Scadrial", "Oz", 1, "Level1Unit"));
    valid_a1.add(new UpgradeTextAction("U", "A", "Elantris", "Level1Unit", "Level2Unit", 1));
    helpSendActions(valid_a1, client1);

    // Invalid input 1 from Player B: the number of issued units exceeds the limit!
    ArrayList<TextAction> invalid_b1 = new ArrayList<>();
    invalid_b1.add(new MoveAttackTextAction("M", "B", "Narnia", "Mid", 13, "Level1Unit"));
    helpSendActions(invalid_b1, client2);
    // Invalid input 2 from Player B: attack non-adjacent territory!
    ArrayList<TextAction> invalid_b2 = new ArrayList<>();
    invalid_b2.add(new MoveAttackTextAction("A", "B", "Narnia", "Scadrial", 5, "Level1Unit"));
    helpSendActions(invalid_b2, client2);
    // Valid input 1 from Player B: 2 moves and 2 attacks
    ArrayList<TextAction> valid_b1 = new ArrayList<>();
    valid_b1.add(new MoveAttackTextAction("M", "B", "Narnia", "Oz", 4, "Level1Unit"));
    valid_b1.add(new MoveAttackTextAction("M", "B", "Gondor", "Mid", 8, "Level1Unit"));
    valid_b1.add(new MoveAttackTextAction("A", "B", "Mid", "Scadrial", 1, "Level1Unit"));
    valid_b1.add(new MoveAttackTextAction("A", "B", "Oz", "Scadrial", 2, "Level1Unit"));
    valid_b1.add(new UpgradeTextAction("U", "B", "Mid", "Level1Unit", "Level2Unit", 1));
    helpSendActions(valid_b1, client2);

    master.playOneTurn();
    assertEquals("The from territory of the action Player A is doing the M action, from territory eee to territory Scadrial, with 5 Level1Unit. does not exist!", helpReceiveMsg(client1));
    assertEquals("The from territory of the actionPlayer A is doing the M action, from territory Narnia to territory Scadrial, with 5 Level1Unit. does not belong to the issuer!", helpReceiveMsg(client1));
    assertEquals("Invalid action type!", helpReceiveMsg(client1));
    assertEquals("Invalid upgrade order!", helpReceiveMsg(client1));
    assertEquals("True", helpReceiveMsg(client1));

    assertEquals("The number of units used in the action Player B is doing the M action, from territory Narnia to territory Mid, with 13 Level1Unit. exceeds the limit!", helpReceiveMsg(client2));
    assertEquals("The to territory of the attackPlayer B is doing the A action, from territory Narnia to territory Scadrial, with 5 Level1Unit. must be the neighbor of the from territory!", helpReceiveMsg(client2));
    assertEquals("True", helpReceiveMsg(client2));

    Map expectedMap = new Map();
    expectedMap.updateTerritoryUnitCt("Elantris", 9, "Level1Unit", false);
    expectedMap.updateTerritoryUnitCt("Scadrial", 1, "Level1Unit", false);
    expectedMap.updateTerritoryUnitCt("Mordor", 13, "Level1Unit", false);
    expectedMap.updateTerritoryUnitCt("Gondor", 7, "Level1Unit", false);
    expectedMap.updateTerritoryUnitCt("Narnia", 8, "Level1Unit", false);
    expectedMap.updateTerritoryUnitCt("Mid", 13, "Level1Unit", false);
    expectedMap.updateTerritoryUnitCt("Oz", 9, "Level1Unit", false);
    expectedMap.updateTerritoryUnitCt("Elantris", 1, "Level2Unit", false);
    expectedMap.updateTerritoryUnitCt("Mid", 1, "Level2Unit", false);
    expectedMap.updateTerritoryOwner("Oz", "B");
    expectedMap.incrementTerritoryUnitCt(1);
    Map map1 = helpReceiveMap(client1);
    helpReceiveMsg(client1);
    Map map2 = helpReceiveMap(client2);
    helpReceiveMsg(client2);
    StringBuilder s1 = new StringBuilder();
    StringBuilder s2 = new StringBuilder();
    StringBuilder s3 = new StringBuilder();
    for (Territory t : map1.getTerritories()) {
      s1.append(t.toString());
    }
    for (Territory t : map2.getTerritories()) {
      s3.append(t.toString());
    }
    for (Territory t : expectedMap.getTerritories()) {
      s2.append(t.toString());
    }

    assertEquals(s2.toString(), s1.toString());
    assertEquals(s2.toString(), s3.toString());
    client1.close();
    client2.close();
    master.closeSocket();
  }

  @Test
  public void testPlayMultiTurns() throws IOException, ClassNotFoundException {
    Master master = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    master.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    master.map.updateTerritoryOwner("Gondor", "A");
    master.map.updateTerritoryOwner("Narnia", "A");
    master.map.updateTerritoryOwner("Mid", "A");
    master.map.incrementTerritoryUnitCt(5);
    ArrayList<TextAction> valid_a1 = new ArrayList<>();
    valid_a1.add(new MoveAttackTextAction("M", "A", "Mordor", "Scadrial", 10, "Level1Unit"));
    ArrayList<TextAction> valid_b1 = new ArrayList<>();
    helpSendActions(valid_a1, client1);
    helpSendActions(valid_b1, client2);
    ArrayList<TextAction> valid_a2 = new ArrayList<>();
    valid_a2.add(new MoveAttackTextAction("A", "A", "Mordor", "Oz", 1, "Level1Unit"));
    helpSendActions(valid_a2, client1);
    helpSendActions(valid_b1, client2);
    ArrayList<TextAction> valid_a3 = new ArrayList<>();
    valid_a3.add(new MoveAttackTextAction("A", "A", "Scadrial", "Oz", 11, "Level1Unit"));
    helpSendActions(valid_a3, client1);
    helpSendActions(valid_b1, client2);
    master.playMultiTurns();
    assertEquals("True", helpReceiveMsg(client1));
    assertEquals("True", helpReceiveMsg(client2));
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    assertEquals("", helpReceiveMsg(client1));
    assertEquals("", helpReceiveMsg(client2));
    assertEquals("True", helpReceiveMsg(client1));
    assertEquals("True", helpReceiveMsg(client2));
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    assertEquals("", helpReceiveMsg(client1));
    assertEquals("", helpReceiveMsg(client2));
    assertEquals("True", helpReceiveMsg(client1));
    assertEquals("True", helpReceiveMsg(client2));
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    assertEquals("AWIN", helpReceiveMsg(client1));
    assertEquals("AWIN", helpReceiveMsg(client2));
    client1.close();
    client2.close();
    master.closeSocket();
  }

  @Test
  public void testCheckWin() throws IOException, ClassNotFoundException {
    Master master = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    master.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    assertEquals(master.checkWin(), "");
    master.map.updateTerritoryOwner("Gondor", "A");
    master.map.updateTerritoryOwner("Narnia", "A");
    master.map.updateTerritoryOwner("Mid", "A");
    master.map.updateTerritoryOwner("Oz", "A");
    assertEquals(master.checkWin(), "A");
    client1.close();
    client2.close();
    master.closeSocket();
  }


  @Test
  public void testExtractAttack() throws IOException, ClassNotFoundException {
    Master master = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    master.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    ArrayList<TextAction> list = new ArrayList<>();
    list.add(new MoveAttackTextAction("M", "A", "Elantris", "Scadrial", 5, "Level2Unit"));
    list.add(new MoveAttackTextAction("M", "A", "Mordor", "Scadrial", 5, "Level2Unit"));
    list.add(new MoveAttackTextAction("A", "A", "Mordor", "Oz", 5, "Level2Unit"));
    list.add(new MoveAttackTextAction("A", "A", "Elantris", "Narnia", 15, "Level2Unit"));
    ArrayList<Attack> res = master.extractAttack(list);
    DiceCombatRules r = new DiceCombatRules();
    ArrayList<Attack> expected = new ArrayList<>();
    expected.add(new Attack("A", "Mordor", "Oz", 5, "Level2Unit", r));
    expected.add(new Attack("A", "Elantris", "Narnia", 15, "Level2Unit", r));
    assertEquals(expected, res);
    client1.close();
    client2.close();
    master.closeSocket();
  }

  @Test
  public void testExtractMove() throws IOException, ClassNotFoundException {
    Master master = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    master.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    ArrayList<TextAction> list = new ArrayList<>();
    list.add(new MoveAttackTextAction("M", "A", "Elantris", "Scadrial", 5, "Level2Unit"));
    list.add(new MoveAttackTextAction("M", "A", "Mordor", "Scadrial", 5, "Level2Unit"));
    list.add(new MoveAttackTextAction("A", "A", "Mordor", "Oz", 5, "Level2Unit"));
    list.add(new MoveAttackTextAction("A", "A", "Elantris", "Narnia", 15, "Level2Unit"));
    ArrayList<Move> res = master.extractMove(list);

    ArrayList<Move> expected = new ArrayList<>();
    expected.add(new Move("Elantris", "Scadrial", 5, "Level2Unit", "A"));
    expected.add(new Move("Mordor", "Scadrial", 5, "Level2Unit", "A"));
    assertEquals(expected, res);
    client1.close();
    client2.close();
    master.closeSocket();
  }

  @Test
  public void testGetTechRsc() throws IOException, ClassNotFoundException {
    Master master = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    master.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    assertEquals(40, master.getTechRsc("A"));
    assertEquals(40, master.getTechRsc("B"));
    assertEquals(-1, master.getTechRsc("C"));
    client1.close();
    client2.close();
    master.closeSocket();
  }

  @Test
  public void testGetFoodRsc() throws IOException, ClassNotFoundException {
    Master master = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    master.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    assertEquals(50, master.getFoodRsc("A"));
    assertEquals(50, master.getFoodRsc("B"));
    assertEquals(-1, master.getFoodRsc("C"));
    client1.close();
    client2.close();
    master.closeSocket();
  }

  @Test
  public void testSetTechRsc() throws IOException, ClassNotFoundException {
    Master m1 = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    m1.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    m1.setTechRsc(50, 20);
    assertEquals(50, m1.getTechRsc("A"));
    assertEquals(20, m1.getTechRsc("B"));
    client1.close();
    client2.close();
    m1.closeSocket();
  }

  @Test
  public void testSetFoodRsc() throws IOException, ClassNotFoundException {
    Master m1 = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    m1.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    m1.setFoodRsc(50, 20);
    assertEquals(50, m1.getFoodRsc("A"));
    assertEquals(20, m1.getFoodRsc("B"));
    client1.close();
    client2.close();
    m1.closeSocket();
  }

  @Test
  public void testAddTechRsc() throws IOException, ClassNotFoundException {
    Master m1 = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    m1.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    m1.addTechRsc();
    assertEquals(40 + 45, m1.getTechRsc("A"));
    client1.close();
    client2.close();
    m1.closeSocket();

  }

  @Test
  public void testAddFoodRsc() throws IOException, ClassNotFoundException {
    Master m1 = new Master();
    Socket client1 = new Socket("localhost", PORT);
    Socket client2 = new Socket("localhost", PORT);
    m1.initPlayers();
    helpReceiveMsg(client1);
    helpReceiveMap(client1);
    helpReceiveMsg(client1);
    helpReceiveMsg(client2);
    helpReceiveMap(client2);
    helpReceiveMsg(client2);
    m1.addFoodRsc();
    assertEquals(50 + 50, m1.getFoodRsc("A"));
    client1.close();
    client2.close();
    m1.closeSocket();

  }


}
