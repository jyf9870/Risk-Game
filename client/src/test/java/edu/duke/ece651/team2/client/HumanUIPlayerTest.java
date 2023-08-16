package edu.duke.ece651.team2.client;

import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import edu.duke.ece651.team2.shared.TextAction;
import edu.duke.ece651.team2.shared.UpgradeTextAction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class HumanUIPlayerTest {
  private ArrayList<TextAction> receiveActions(ObjectInputStream objectIn) throws IOException, ClassNotFoundException {
    Object obj = objectIn.readObject();
    ArrayList<TextAction> textActions = (ArrayList<TextAction>) obj;
    return textActions;
  }

  @Test
  public void testReceive() throws IOException, ClassNotFoundException {
    // Set up the server socket
    ServerSocket serverSocket = new ServerSocket(12345);
    // Set up the player
    HumanUIPlayer player = new HumanUIPlayer();
    // Receive winning status
    Socket serverSkt = serverSocket.accept();
    String expected = "WIN";
    DataOutputStream dataOut = new DataOutputStream(serverSkt.getOutputStream());
    dataOut.writeUTF(expected);
    assertEquals(expected, player.receiveWin());
    // Receive name
    String expectedName = "Allen";
    dataOut.writeUTF(expectedName);
    player.receiveName();
    assertEquals(expectedName, player.getName());
    // Receive map
    Map m = new Map();
    ObjectOutputStream objectOut = new ObjectOutputStream(serverSkt.getOutputStream());
    objectOut.writeObject(m);
    player.receiveMap();
    assertEquals(m, player.getMap());
    // Disconnect
    player.disc();
    assertThrows(RuntimeException.class, () -> player.receiveWin());
    serverSkt.close();
    serverSocket.close();
  }

  @Test
  public void test_init() throws IOException, ClassNotFoundException {
    // Set up the server socket
    ServerSocket serverSocket = new ServerSocket(12345);
    HumanUIPlayer player = new HumanUIPlayer();
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
    player.disc();
    serverSkt.close();
    serverSocket.close();
  }

  @Test
  public void test_getList() throws IOException {
    // Set up the server socket
    ServerSocket serverSocket = new ServerSocket(12345);
    ObservableList<TextAction> expected = FXCollections.observableArrayList();
    HumanUIPlayer player = new HumanUIPlayer();
    assertEquals(expected, player.getList());
    // Disconnect
    player.disc();
    serverSocket.close();
  }

  @Test
  public void test_updateCurrentStage() throws IOException {
    // Set up the server socket
    ServerSocket serverSocket = new ServerSocket(12345);
    HumanUIPlayer player = new HumanUIPlayer();
    assertEquals("choose", player.getCurrentStage());
    String expected = "attack";
    player.updateCurrentStage(expected);
    assertEquals(expected, player.getCurrentStage());
    // Disconnect
    player.disc();
    serverSocket.close();
  }

  @Test
  public void test_canDone() throws IOException {
    // Set up the server socket
    ServerSocket serverSocket = new ServerSocket(12345);
    HumanUIPlayer player = new HumanUIPlayer();
    assertTrue(player.canDone());
    // Disconnect
    player.disc();
    serverSocket.close();
  }

  @Test
  public void test_canInput() throws IOException {
    // Set up the server socket
    ServerSocket serverSocket = new ServerSocket(12345);
    HumanUIPlayer player = new HumanUIPlayer();
    assertTrue(player.canInputFromTerritory());
    player.addActionComponent("actionName", "attack");
    player.addActionComponent("from", "Elantris");
    assertTrue(player.canInputToTerritory());
    assertTrue(player.canInputUpgradeFromToLevel());
    player.addActionComponent("to", "Mordor");
    assertTrue(player.canInputUnitNo());
    player.clearCurrentAction();
    assertTrue(player.canDone());
    // Disconnect
    player.disc();
    serverSocket.close();
  }

  @Test
  public void test_addActionFromCurrent() throws IOException, ClassNotFoundException {
    ServerSocket serverSocket = new ServerSocket(12345);
    HumanUIPlayer player = new HumanUIPlayer();
    // Receive winning status
    Socket serverSkt = serverSocket.accept();
    // Send name
    DataOutputStream dataOut = new DataOutputStream(serverSkt.getOutputStream());
    dataOut.writeUTF("Allen");
    // Send map
    ObjectOutputStream objectOut = new ObjectOutputStream(serverSkt.getOutputStream());
    objectOut.writeObject(new Map());
    // Send resource info
    dataOut.writeUTF("Your total resource:\t\tFood: 999\t\tTech: 999");
    player.init();
    // Test valid attack input
    player.addActionComponent("actionName", "A");
    player.addActionComponent("from", "Elantris");
    player.addActionComponent("to", "Narnia");
    player.addActionComponent("unitCt", "5");
    player.addActionComponent("unitLevel", "Level1Unit");
    ObservableList<TextAction> e1 = FXCollections.observableArrayList();
    e1.add(new MoveAttackTextAction("A", "Allen", "Elantris", "Narnia", 5, "Level1Unit"));
    assertTrue(player.addActionFromCurrent());
    assertEquals(e1, player.getList());
    player.clearCurrentStatus();
    // Test valid upgrade input
    player.addActionComponent("actionName", "U");
    player.addActionComponent("upgradeTerritory", "Narnia");
    player.addActionComponent("fromUnit", "Level1Unit");
    player.addActionComponent("toUnit", "Level4Unit");
    player.addActionComponent("unitCt", "1");
    ObservableList<TextAction> e2 = FXCollections.observableArrayList();
    e2.add(new UpgradeTextAction("U", "Allen", "Narnia", "Level1Unit", "Level4Unit", 1));
    assertTrue(player.addActionFromCurrent());
    assertEquals(e2, player.getList());
    player.clearCurrentStatus();
    // Test invalid input
    player.clearCurrentAction();
    player.addActionComponent("actionName", "A");
    player.addActionComponent("from", "Elantris");
    player.addActionComponent("to", "Narnia");
    player.addActionComponent("unitCt", "a5");
    player.addActionComponent("unitLevel", "Level1Unit");
    assertFalse(player.addActionFromCurrent());
    player.addActionComponent("actionName", "ABC");
    player.addActionComponent("unitCt", "5");
    assertFalse(player.addActionFromCurrent());
    // Disconnect
    player.disc();
    serverSkt.close();
    serverSocket.close();
  }

  @Test
  public void test_sendActions() throws IOException, ClassNotFoundException {
    ServerSocket serverSocket = new ServerSocket(12345);
    HumanUIPlayer player = new HumanUIPlayer();
    // Receive winning status
    Socket serverSkt = serverSocket.accept();
    // Send name
    DataOutputStream dataOut = new DataOutputStream(serverSkt.getOutputStream());
    dataOut.writeUTF("A");
    // Send map
    ObjectOutputStream objectOut = new ObjectOutputStream(serverSkt.getOutputStream());
    objectOut.writeObject(new Map());
    // Send resource info
    dataOut.writeUTF("Your total resource:\t\tFood: 999\t\tTech: 999");
    player.init();
    // Test valid input
    player.addActionComponent("actionName", "A");
    player.addActionComponent("from", "Elantris");
    player.addActionComponent("to", "Narnia");
    player.addActionComponent("unitCt", "5");
    player.addActionComponent("unitLevel", "Level1Unit");
    player.addActionFromCurrent();
    dataOut.writeUTF("True");
    assertEquals("True", player.sendActions());
    ArrayList<TextAction> textActions = receiveActions(new ObjectInputStream(serverSkt.getInputStream()));

    TextAction a1 = new MoveAttackTextAction("A", "A", "Elantris", "Narnia", 5, "Level1Unit");

    ArrayList<TextAction> expected = new ArrayList<>();
    expected.add(a1);
    assertEquals(expected, textActions);
    // Test invalid input
    player.addActionComponent("actionName", "A");
    player.addActionComponent("from", "Elantris");
    player.addActionComponent("to", "Narnia");
    player.addActionComponent("unitCt", "15");
    player.addActionComponent("unitLevel", "Level1Unit");
    player.addActionFromCurrent();
    dataOut.writeUTF("False");
    assertNotEquals("True", player.sendActions());

    player.disc();
    assertThrows(RuntimeException.class, () -> player.sendActions());

    // Disconnect
    serverSkt.close();
    serverSocket.close();
  }

  @Test
  public void test_clearCurrentStatus() throws IOException {
    // Set up the server socket
    ServerSocket serverSocket = new ServerSocket(12345);
    HumanUIPlayer player = new HumanUIPlayer();
    player.updateCurrentStage("attack");
    player.addActionComponent("actionName", "A");
    player.addActionComponent("from", "Elantris");
    player.addActionComponent("to", "Mordor");
    player.addActionComponent("unitCt", "5");
    player.addActionComponent("unitLevel", "Level1Unit");
    player.addActionFromCurrent();
    player.clearCurrentAction();
    player.addActionComponent("actionName", "A");
    player.addActionComponent("from", "Elantris");
    assertTrue(player.canInputToTerritory());
    assertEquals(1, player.getList().size());
    assertEquals("attack", player.getCurrentStage());
    player.clearCurrentStatus();
    assertTrue(player.canDone());
    assertEquals(0, player.getList().size());
    assertEquals("choose", player.getCurrentStage());
    // Disconnect
    player.disc();
    serverSocket.close();
  }

  @Test
  public void test_disc() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    HumanUIPlayer player = new HumanUIPlayer();
    Client client = mock(Client.class);
    doThrow(IOException.class).when(client).closeClient();
    player.disc();
    serverSocket.close();
    player.client = client;
    assertThrows(RuntimeException.class, () -> player.disc());
  }

  @Test
  void test_getUpdatedMap() throws IOException {
    ServerSocket serverSocket = new ServerSocket(12345);
    HumanUIPlayer player = new HumanUIPlayer();
    // Receive winning status
    Socket serverSkt = serverSocket.accept();
    Map expected = new Map();
    expected.updateTerritoryOwner("Narnia", "ABC");
    // Send map
    ObjectOutputStream objectOut = new ObjectOutputStream(serverSkt.getOutputStream());
    objectOut.writeObject(expected);
    player.getUpdatedMap();
    assertEquals(player.getMap(), expected);
    assertNotEquals(player.getMap(), new Map());
    // Disconnect
    player.disconnect();
    assertThrows(RuntimeException.class, () -> player.getUpdatedMap());
    serverSkt.close();
    serverSocket.close();
  }
}
