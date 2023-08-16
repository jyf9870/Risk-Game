/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.team2.client;

import edu.duke.ece651.team2.client.controller.MainController;
import edu.duke.ece651.team2.shared.TextAction;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class App extends Application {
  @Override
  public void start(Stage stage) throws IOException, ClassNotFoundException {
    // Initialize the player
    HumanUIPlayer player = new HumanUIPlayer();
    player.init();
    // Load the XML input file
    URL xmlResource = getClass().getResource("/ui/Game.xml");
    FXMLLoader loader = new FXMLLoader(xmlResource);
    HashMap<Class<?>, Object> controllers = new HashMap<>();
    controllers.put(MainController.class, new MainController(player));
    loader.setControllerFactory((c) -> {
      return controllers.get(c);
    });
    GridPane gp = loader.load();
    Scene scene = new Scene(gp, 1200, 1050);
    // Link the ListView shared by the model and the view
    @SuppressWarnings("unchecked")
    ListView<TextAction> operands = (ListView<TextAction>) scene.lookup("#moves");
    operands.setItems(player.getList());
    // Load the CSS input file
    URL cssResource = getClass().getResource("/ui/Game.css");
    scene.getStylesheets().add(cssResource.toString());
    stage.setTitle("Player " + player.getName() + "'s Game");
    stage.setScene(scene);
    stage.show();
  }
}