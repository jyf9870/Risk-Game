package edu.duke.ece651.team2.client.controller;

import edu.duke.ece651.team2.client.ComputerPlayer;
import edu.duke.ece651.team2.client.HumanUIPlayer;
import edu.duke.ece651.team2.shared.Territory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class MainController {
  @FXML
  Label serverPrompt, resourcePrompt;
  @FXML
  TextField unitCount, upgradeUnitCount;
  @FXML
  ComboBox<String> unitLevel, upgradeFrom, upgradeTo;
  @FXML
  GridPane map;
  @FXML
  HBox actions, units, upgradeUnits, playerType, currentActions;

  HumanUIPlayer player;

  /**
   * This class supports the controller behaviors in the game
   *
   * @param player is the model used to interact with the controller based on user inputs from GUI
   */
  public MainController(HumanUIPlayer player) {
    this.player = player;
  }

  /**
   * This function sets the GUI to the default stage where the user is allowed to select a new action
   *
   * @param s is the prompt message shown to the user
   */
  private void toDefault(String s) {
    upgradeUnits.setVisible(false);
    units.setVisible(false);
    serverPrompt.setVisible(true);
    serverPrompt.setText(s);
    player.clearCurrentAction();
    player.updateCurrentStage("choose");
  }

  /**
   * This helper function adds clicked territory to the model and adjusts prompt message accordingly
   *
   * @param btn    is the territory button clicked by the user
   * @param action is the action type selected by the user
   */
  private void addFromTo(Button btn, String action) {
    if (player.canInputFromTerritory()) {
      btn.getStyleClass().add("TerritorySelected");
      player.addActionComponent("actionName", action);
      player.addActionComponent("from", btn.getId());
      if (action.equals(player.MOVE)) {
        serverPrompt.setText("Which territory do you want to move to?");
      } else {
        serverPrompt.setText("Which territory do you want to attack?");
      }
    } else if (player.canInputToTerritory()) {
      btn.getStyleClass().add("TerritorySelected");
      player.addActionComponent("to", btn.getId());
      serverPrompt.setVisible(false);
      units.setVisible(true);
    }
  }

  private boolean isMoveAttack(String s) {
    return s.equals(player.MOVE) || s.equals(player.ATTACK);
  }

  private boolean isUpgrade(String s) {
    return s.equals(player.UPGRADE);
  }

  private boolean isCloak(String s) {
    return s.equals(player.CLOAK);
  }

  /**
   * This helper function will update the territory color based on the updated map
   */
  private void updateTerritoryColorStyle() {
    for (Node node : map.getChildren()) {
      if (node instanceof Button) {
        Button btn = (Button) node;
        Territory t = player.getMap().getTerritory(node.getId());
        btn.textProperty().unbind();
        if (t.getOwnerName().equals(player.getName())) {
          btn.setText(t.display(true, true));
        } else if ((t.hasNeighborOwnedByEnemy(player.getMap()) && !t.isCloaked()) ||
            (!t.getOwnerName().equals(player.getName()) && t.getEnemyUnitCt("SpyUnit") > 0)) {
          player.seenTerritories.put(t.getName(), t.display(false, false));
          btn.setText(t.display(false, true));
        } else if (player.seenTerritories.containsKey(t.getName())) {
          btn.setText("OLD INFO:  " + player.seenTerritories.get(t.getName()));
        } else {
          btn.setText(t.toString());
        }
        if (t.getOwnerName().equals("A")) {
          btn.getStyleClass().removeAll("TerritoryB");
          btn.getStyleClass().removeAll("TerritorySelected");
          btn.getStyleClass().addAll("TerritoryA");
        } else {
          btn.getStyleClass().removeAll("TerritoryA");
          btn.getStyleClass().removeAll("TerritorySelected");
          btn.getStyleClass().addAll("TerritoryB");
        }
        if (t.getOwnerName().equals(player.getName()) && t.isCloaked()) {
          btn.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-background-insets: 0;");
        } else {
          btn.setStyle("-fx-border-color: black;");
        }
      }
    }
  }

  /**
   * This function controls the behavior when the user clicked a territory button
   *
   * @param ae is the action event triggered by the user
   */
  @FXML
  public void onTerritory(ActionEvent ae) {
    Object source = ae.getSource();
    Button btn = (Button) source;
    if (isMoveAttack(player.getCurrentStage())) {
      addFromTo(btn, player.getCurrentStage());
    } else if (isUpgrade(player.getCurrentStage()) && player.canInputFromTerritory()) {
      btn.getStyleClass().add("TerritorySelected");
      player.addActionComponent("actionName", player.getCurrentStage());
      player.addActionComponent("upgradeTerritory", btn.getId());
      serverPrompt.setVisible(false);
      upgradeUnits.setVisible(true);
    } else if (isCloak(player.getCurrentStage()) && player.canInputFromTerritory()) {
      player.addActionComponent("actionName", player.getCurrentStage());
      player.addActionComponent("cloakTerritory", btn.getId());
      player.addActionFromCurrent();
      toDefault("Please choose your action from the choices below.");
      updateTerritoryColorStyle();
    }
  }

  /**
   * This function controls the behavior when the user clicked an action button
   *
   * @param ae is the action event triggered by the user
   */
  @FXML
  public void onAction(ActionEvent ae) {
    Object source = ae.getSource();
    Button btn = (Button) source;
    if (player.getCurrentStage().equals("choose")) {
      player.updateCurrentStage(btn.getId());
      if (isMoveAttack(btn.getId())) {
        serverPrompt.setText("Which territory do you want to " + btn.getText().toLowerCase() + " from?");
      } else {
        serverPrompt.setText("Which territory do you want to " + btn.getText().toLowerCase() + "?");
      }
    }
  }

  /**
   * This function controls the behavior when the user clicked the enter button during the unit input stage
   *
   * @param ae is the action event triggered by the user
   */
  @FXML
  public void onUnitEnter(ActionEvent ae) {
    String unitNo = unitCount.getText().trim();
    String unitLe = unitLevel.getValue();
    if (player.canInputUnitNo() && !unitNo.equals("") && !unitLe.equals("") && isMoveAttack(player.getCurrentStage())) {
      player.addActionComponent("unitCt", unitNo);
      player.addActionComponent("unitLevel", unitLe);
      boolean addActionResult = player.addActionFromCurrent();
      unitCount.setText("");
      if (addActionResult) {
        toDefault("Please choose your action from the choices below.");
      } else {
        toDefault("Invalid input! Please choose your action from the choices below.");
      }
      updateTerritoryColorStyle();
    }
  }

  @FXML
  public void onUpgradeEnter(ActionEvent ae) {
    String unitNo = upgradeUnitCount.getText().trim();
    String upFrom = upgradeFrom.getValue();
    String upTo = upgradeTo.getValue();
    if (player.canInputUpgradeFromToLevel() && !unitNo.equals("") && !upFrom.equals("") && !upTo.equals("") && isUpgrade(player.getCurrentStage())) {
      player.addActionComponent("unitCt", unitNo);
      player.addActionComponent("fromUnit", upFrom);
      player.addActionComponent("toUnit", upTo);
      boolean addActionResult = player.addActionFromCurrent();
      upgradeUnitCount.setText("");
      if (addActionResult) {
        toDefault("Please choose your action from the choices below.");
      } else {
        toDefault("Invalid input! Please choose your action from the choices below.");
      }
      updateTerritoryColorStyle();
    }
  }

  /**
   * This function controls the behavior when the user clicked the done button
   *
   * @param ae is the action event triggered by the user
   */
  @FXML
  public void onDone(ActionEvent ae) {
    if (player.canDone() && player.getCurrentStage().equals("choose")) {
      String validation = player.sendActions();
      player.clearCurrentStatus();
      if (validation.equals("True")) {
        player.getUpdatedMap();
        updateTerritoryColorStyle();
        String combatWinner = player.receiveWin();
        resourcePrompt.textProperty().unbind();
        resourcePrompt.setText(combatWinner);
        // Detect winning situation
        String gameWinner = player.receiveWin();
        if (gameWinner.contains("WIN")) {
          serverPrompt.setText("Player " + gameWinner.charAt(0) + " wins! You may exit the game.");
          player.disc();
          actions.setDisable(true);
        } else {
          serverPrompt.setText("Please choose your action from the choices below.");
        }
      } else {
        serverPrompt.setText(validation + " Please re-choose your action from the choices below.");
      }
    }
  }

  /**
   * This function controls the behavior when the user chooses the player type
   *
   * @param ae is the action event triggered by the user
   */
  @FXML
  public void onPlayer(ActionEvent ae) {
    Object source = ae.getSource();
    Button btn = (Button) source;
    if (btn.getId().equals("HumanPlayer")) {
      playerType.setVisible(false);
      resourcePrompt.setVisible(true);
      actions.setVisible(true);
      currentActions.setVisible(true);
      serverPrompt.setText("Please choose your action from the choices below.");
      updateTerritoryColorStyle();
    } else if (btn.getId().equals("AIPlayer")) {
      ComputerPlayer cp = new ComputerPlayer(player.getName(), player.getMap(), player.client, player.getTechResource(), player.getFoodResource());
      cp.play();
    }
  }

}
