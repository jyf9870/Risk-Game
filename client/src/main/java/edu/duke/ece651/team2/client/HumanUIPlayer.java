package edu.duke.ece651.team2.client;

import edu.duke.ece651.team2.shared.CloakTextAction;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import edu.duke.ece651.team2.shared.TextAction;
import edu.duke.ece651.team2.shared.UpgradeTextAction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HumanUIPlayer extends HumanPlayer {
  // public for test purposes
  public String currentStage;
  public ArrayList<TextAction> actions;
  public HashMap<String, String> currentAction;
  public HashMap<String, String> seenTerritories;
  ObservableList<TextAction> myStack;

  /**
   * This class is served as the model and is used to process inputs from the controller and communicate with the server
   *
   * @throws IOException when there are input or output exceptions
   */
  public HumanUIPlayer() throws IOException {
    super();
    this.myStack = FXCollections.observableArrayList();
    // The default stage is "choose", where the user can choose actions
    this.currentStage = "choose";
    this.currentAction = new HashMap<>();
    this.actions = new ArrayList<>();
    this.seenTerritories = new HashMap<>();
  }

  /**
   * initialize the player name and map
   *
   * @throws IOException            when there are input or output exceptions
   * @throws ClassNotFoundException when there are class not found exceptions
   */
  public void init() throws IOException, ClassNotFoundException {
    receiveName();
    receiveMap();
    String res = receiveResourceInfo();
    updateResources(res);
  }

  public ObservableList<TextAction> getList() {
    return myStack;
  }

  public String getCurrentStage() {
    return currentStage;
  }

  public void updateCurrentStage(String s) {
    currentStage = s;
  }

  public boolean canDone() {
    return currentAction.size() == 0;
  }

  public boolean canInputFromTerritory() {
    return currentAction.size() == 0;
  }

  public boolean canInputToTerritory() {
    return currentAction.size() == 2;
  }

  public boolean canInputUnitNo() {
    return currentAction.size() == 3;
  }

  public boolean canInputUpgradeFromToLevel() {
    return currentAction.size() == 2;
  }

  public void addActionComponent(String k, String v) {
    currentAction.put(k, v);
  }

  public void clearCurrentAction() {
    currentAction.clear();
  }

  /**
   * This function validates the units input by the user and adds the action to the action lists if valid
   *
   * @return true if the addition is successful, false otherwise
   */
  public boolean addActionFromCurrent() {
    TextAction textAction;
    try {
      if (currentAction.get("actionName").equals(MOVE) || currentAction.get("actionName").equals(ATTACK)) {
        int unitCt = Integer.parseInt(currentAction.get("unitCt"));
        textAction = new MoveAttackTextAction(currentAction.get("actionName"), getName(),
            currentAction.get("from"), currentAction.get("to"), unitCt, currentAction.get("unitLevel"));
      } else if (currentAction.get("actionName").equals(UPGRADE)) {
        int unitCt = Integer.parseInt(currentAction.get("unitCt"));
        textAction = new UpgradeTextAction(currentAction.get("actionName"), getName(),
            currentAction.get("upgradeTerritory"), currentAction.get("fromUnit"),
            currentAction.get("toUnit"), unitCt);
      } else if (currentAction.get("actionName").equals(CLOAK)) {
        textAction = new CloakTextAction(currentAction.get("actionName"), getName(), currentAction.get("cloakTerritory"));
      } else {
        return false;
      }
      actions.add(textAction);
      myStack.add(textAction);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * This function sends the action list to the server and receives the validation feedback from the server
   *
   * @return the validation message
   */
  public String sendActions() {
    try {
      client.sendActions(actions);
      String validation = client.receiveTextMsg();
      return validation;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @return the string of wining status
   */
  public String receiveWin() {
    try {
      return receiveWinningStatus();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  public void disc() {
    try {
      disconnect();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This function clears up status and prepares for each new round
   */
  public void clearCurrentStatus() {
    actions.clear();
    currentAction.clear();
    myStack.clear();
    currentStage = "choose";
  }

  public void getUpdatedMap() {
    try {
      map = client.receiveMap();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

}
