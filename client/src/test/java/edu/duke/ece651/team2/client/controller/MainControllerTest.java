package edu.duke.ece651.team2.client.controller;

import edu.duke.ece651.team2.client.HumanUIPlayer;
import edu.duke.ece651.team2.shared.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(ApplicationExtension.class)
public class MainControllerTest {
  private Label serverPrompt, resourcePrompt;
  private TextField unitCount, upgradeUnitCount;
  private GridPane map;
  private HBox actions, units, upgradeUnits, playerType, currentActions;
  private HumanUIPlayer player;
  private MainController cont;
  private ComboBox unitLevel, upgradeFrom, upgradeTo;

  @Start
  private void start(Stage stage) {
    serverPrompt = new Label();
    resourcePrompt = new Label();
    unitCount = new TextField();
    upgradeUnitCount = new TextField();
    unitLevel = new ComboBox<String>();
    unitLevel.getItems().addAll("Level1Unit");
    upgradeFrom = new ComboBox<String>();
    upgradeFrom.getItems().addAll("Level1Unit");
    upgradeTo = new ComboBox<String>();
    upgradeTo.getItems().addAll("Level1Unit");
    // Create a few buttons for the test purposes
    map = new GridPane();
    Button b1 = new Button("Narnia");
    b1.setId("Narnia");
    Button b2 = new Button("Elantris");
    b2.setId("Elantris");
    Button b3 = new Button("Gondor");
    b3.setId("Gondor");
    Button b4 = new Button("Oz");
    b4.setId("Oz");
    map.add(b1, 0, 0);
    map.add(b2, 0, 1);
    map.add(b3, 0, 2);
    map.add(b4, 0, 3);
    map.add(new TextField(), 1, 1);
    // Create a few children for the test purposes
    actions = new HBox(new Button(), new TextField());
    units = new HBox(new Label(), new TextField(), new Button());
    upgradeUnits = new HBox(new Label(), new TextField(), new Button());
    playerType = new HBox(new Button(), new Button());
    currentActions = new HBox(new Button(), new Button());
    player = mock(HumanUIPlayer.class);
    cont = new MainController(player);
    cont.serverPrompt = serverPrompt;
    cont.resourcePrompt = resourcePrompt;
    cont.unitCount = unitCount;
    cont.upgradeUnitCount = upgradeUnitCount;
    cont.map = map;
    cont.actions = actions;
    cont.units = units;
    cont.upgradeUnits = upgradeUnits;
    cont.playerType = playerType;
    cont.currentActions = currentActions;
    cont.unitLevel = unitLevel;
    cont.upgradeFrom = upgradeFrom;
    cont.upgradeTo = upgradeTo;

  }

  private void addTerritory(String s) {
    Platform.runLater(() -> {
      Button b = new Button(s);
      b.setId(s);
      cont.onTerritory(new ActionEvent(b, null));
    });
    WaitForAsyncUtils.waitForFxEvents();
  }

  private void addAction(String text, String id) {
    Platform.runLater(() -> {
      Button b = new Button(text);
      b.setId(id);
      cont.onAction(new ActionEvent(b, null));
    });
    WaitForAsyncUtils.waitForFxEvents();
  }

  private void enterUnits(String units) {
    Platform.runLater(() -> {
      unitLevel.setValue("Level1Unit");
      unitCount.setText(units);
      Button b = new Button("Enter");
      b.setId("enter");
      cont.onUnitEnter(new ActionEvent(b, null));
    });
    WaitForAsyncUtils.waitForFxEvents();
  }

  private void enterUpgradeLevel(String from, String to, String units) {
    Platform.runLater(() -> {
      upgradeUnitCount.setText(units);
      upgradeFrom.setValue(from);
      upgradeTo.setValue(to);
      Button b = new Button("Enter");
      b.setId("enter");
      cont.onUpgradeEnter(new ActionEvent(b, null));
    });
    WaitForAsyncUtils.waitForFxEvents();
  }


  private void done() {
    Platform.runLater(() -> {
      Button b = new Button("Done");
      b.setId("D");
      cont.onDone(new ActionEvent(b, null));
    });
    WaitForAsyncUtils.waitForFxEvents();

  }

  @Test
  public void test_onTerritory(FxRobot robot) throws IOException, ClassNotFoundException {
    // Initialize the player and the server
    ServerSocket serverSocket = new ServerSocket(12345);
    player = new HumanUIPlayer();
    Socket serverSkt = serverSocket.accept();
    DataOutputStream dataOut = new DataOutputStream(serverSkt.getOutputStream());
    dataOut.writeUTF("A");
    ObjectOutputStream objectOut = new ObjectOutputStream(serverSkt.getOutputStream());
    Map m = new Map();
    m.getTerritory("Elantris").setCloak(true);
    objectOut.writeObject(m);
    dataOut.writeUTF("Your total resource:\t\tFood: 999\t\tTech: 999");
    player.init();
    cont.player = player;

    player.updateCurrentStage(player.MOVE);
    addTerritory("Elantris");
    assertEquals("Which territory do you want to move to?", serverPrompt.getText());
    addTerritory("Scadrial");
    assertEquals(player.MOVE, player.currentAction.get("actionName"));
    assertEquals("Elantris", player.currentAction.get("from"));
    assertEquals("Scadrial", player.currentAction.get("to"));
    player.clearCurrentStatus();

    addTerritory("Elantris");
    player.updateCurrentStage(player.ATTACK);
    addTerritory("Mordor");
    assertEquals("Which territory do you want to attack?", serverPrompt.getText());
    addTerritory("Gondor");
    addTerritory("Gondor");
    assertEquals(player.ATTACK, player.currentAction.get("actionName"));
    assertEquals("Mordor", player.currentAction.get("from"));
    assertEquals("Gondor", player.currentAction.get("to"));
    player.clearCurrentStatus();

    player.updateCurrentStage(player.UPGRADE);
    addTerritory("Elantris");
    assertEquals(player.UPGRADE, player.currentAction.get("actionName"));
    assertEquals("Elantris", player.currentAction.get("upgradeTerritory"));
    player.clearCurrentStatus();

    player.updateCurrentStage(player.CLOAK);
    addTerritory("Elantris");
    ArrayList<TextAction> actions = new ArrayList<>();
    actions.add(new CloakTextAction(player.CLOAK, "A", "Elantris"));
    assertEquals(actions, player.actions);
    assertEquals("Please choose your action from the choices below.", serverPrompt.getText());
    player.clearCurrentStatus();

    // Disconnect
    player.disconnect();
    serverSkt.close();
    serverSocket.close();
  }

  @Test
  public void test_onAction(FxRobot robot) throws IOException, ClassNotFoundException {
    // Initialize the player and the server
    ServerSocket serverSocket = new ServerSocket(12345);
    player = new HumanUIPlayer();
    Socket serverSkt = serverSocket.accept();
    DataOutputStream dataOut = new DataOutputStream(serverSkt.getOutputStream());
    dataOut.writeUTF("A");
    ObjectOutputStream objectOut = new ObjectOutputStream(serverSkt.getOutputStream());
    objectOut.writeObject(new Map());
    dataOut.writeUTF("Your total resource:\t\tFood: 999\t\tTech: 999");
    player.init();
    cont.player = player;

    assertEquals("choose", player.getCurrentStage());
    addAction("Move", player.MOVE);
    addAction("Attack", player.ATTACK);
    assertEquals("Which territory do you want to move from?", serverPrompt.getText());
    assertEquals(player.MOVE, player.getCurrentStage());
    player.clearCurrentStatus();

    addAction("Attack", player.ATTACK);
    addAction("Move", player.MOVE);
    assertEquals("Which territory do you want to attack from?", serverPrompt.getText());
    assertEquals(player.ATTACK, player.getCurrentStage());
    player.clearCurrentStatus();

    addAction("Upgrade", player.UPGRADE);
    assertEquals("Which territory do you want to upgrade?", serverPrompt.getText());
    assertEquals(player.UPGRADE, player.getCurrentStage());

    // Disconnect
    player.disconnect();
    serverSkt.close();
    serverSocket.close();
  }

  @Test
  public void test_onUnitEnter(FxRobot robot) throws IOException, ClassNotFoundException {
    // Initialize the player and the server
    ServerSocket serverSocket = new ServerSocket(12345);
    player = new HumanUIPlayer();
    Socket serverSkt = serverSocket.accept();
    DataOutputStream dataOut = new DataOutputStream(serverSkt.getOutputStream());
    dataOut.writeUTF("A");
    ObjectOutputStream objectOut = new ObjectOutputStream(serverSkt.getOutputStream());
    objectOut.writeObject(new Map());
    dataOut.writeUTF("Your total resource:\t\tFood: 999\t\tTech: 999");
    player.init();
    cont.player = player;

    enterUnits("1");
    player.updateCurrentStage(player.MOVE);
    player.addActionComponent("actionName", player.MOVE);
    player.addActionComponent("from", "Elantris");
    player.addActionComponent("to", "Mordor");
    enterUnits("2");
    assertEquals("", unitCount.getText());
    assertEquals("Please choose your action from the choices below.", serverPrompt.getText());
    ArrayList<TextAction> expected = new ArrayList<>();

    expected.add(new MoveAttackTextAction(player.MOVE, "A", "Elantris", "Mordor", 2, "Level1Unit"));

    assertEquals(player.actions, expected);

    player.updateCurrentStage(player.ATTACK);
    player.addActionComponent("actionName", player.ATTACK);
    player.addActionComponent("from", "Elantris");
    player.addActionComponent("to", "Narnia");
    enterUnits("abc");
    assertEquals("Invalid input! Please choose your action from the choices below.", serverPrompt.getText());

    // Disconnect
    player.disconnect();
    serverSkt.close();
    serverSocket.close();
  }

  @Test
  public void test_onUpgradeEnter(FxRobot robot) throws IOException, ClassNotFoundException {
    // Initialize the player and the server
    ServerSocket serverSocket = new ServerSocket(12345);
    player = new HumanUIPlayer();
    Socket serverSkt = serverSocket.accept();
    DataOutputStream dataOut = new DataOutputStream(serverSkt.getOutputStream());
    dataOut.writeUTF("A");
    ObjectOutputStream objectOut = new ObjectOutputStream(serverSkt.getOutputStream());
    objectOut.writeObject(new Map());
    dataOut.writeUTF("Your total resource:\t\tFood: 999\t\tTech: 999");
    player.init();
    cont.player = player;

    enterUpgradeLevel("Level1Unit", "Level2Unit", "1");
    player.updateCurrentStage(player.UPGRADE);
    player.addActionComponent("actionName", player.UPGRADE);
    player.addActionComponent("upgradeTerritory", "Elantris");
    enterUpgradeLevel("Level1Unit", "Level2Unit", "1");
    assertEquals("Please choose your action from the choices below.", serverPrompt.getText());
    ArrayList<TextAction> expected = new ArrayList<>();
    expected.add(new UpgradeTextAction(player.UPGRADE, "A", "Elantris", "Level1Unit", "Level2Unit", 1));
    assertEquals(player.actions, expected);


    player.updateCurrentStage(player.UPGRADE);
    player.addActionComponent("actionName", "ABC");
    player.addActionComponent("upgradeTerritory", "Elantris");
    enterUpgradeLevel("Level1Unit", "Level2Unit", "1");
    assertEquals("Invalid input! Please choose your action from the choices below.", serverPrompt.getText());

    // Disconnect
    player.disconnect();
    serverSkt.close();
    serverSocket.close();
  }

  @Test
  public void test_onDone(FxRobot robot) {
    // Initialize the player and the server
    when(player.canDone()).thenReturn(true);
    when(player.getMap()).thenReturn(new Map());
    when(player.getCurrentStage()).thenReturn("choose");
    when(player.sendActions()).thenReturn("True");
    when(player.receiveWin()).thenReturn("AWIN");
    player.seenTerritories = new HashMap<>();
    done();
    assertEquals("Player A wins! You may exit the game.", serverPrompt.getText());
    when(player.receiveWin()).thenReturn("");
    done();
    assertEquals("Please choose your action from the choices below.", serverPrompt.getText());
    when(player.sendActions()).thenReturn("False");
    done();
    assertEquals("False Please re-choose your action from the choices below.", serverPrompt.getText());
    when(player.canDone()).thenReturn(false);
    done();
  }

  @Test
  public void test_onPlayer(FxRobot robot) throws IOException, ClassNotFoundException {
    // Initialize the player and the server
    ServerSocket serverSocket = new ServerSocket(12345);
    player = new HumanUIPlayer();
    Socket serverSkt = serverSocket.accept();
    DataOutputStream dataOut = new DataOutputStream(serverSkt.getOutputStream());
    dataOut.writeUTF("A");
    ObjectOutputStream objectOut = new ObjectOutputStream(serverSkt.getOutputStream());
    Map m = new Map();
    m.updateTerritoryOwner("Mordor", "B");
    m.updateTerritoryOwner("Scadrial", "B");
    objectOut.writeObject(m);
    dataOut.writeUTF("Your total resource:\t\tFood: 999\t\tTech: 999");
    player.init();
    cont.player = player;
    HashMap<String, String> seenTerritories = new HashMap<>();
    seenTerritories.put("Oz", "<<>>");
    player.seenTerritories = seenTerritories;

    Platform.runLater(() -> {
      Button b = new Button("AI");
      b.setId("AI");
      cont.onPlayer(new ActionEvent(b, null));
    });
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals("", serverPrompt.getText());

    Platform.runLater(() -> {
      Button b = new Button("Human");
      b.setId("HumanPlayer");
      cont.onPlayer(new ActionEvent(b, null));
    });
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals("Please choose your action from the choices below.", serverPrompt.getText());
  }
}
