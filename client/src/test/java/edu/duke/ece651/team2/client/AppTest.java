package edu.duke.ece651.team2.client;


import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import edu.duke.ece651.team2.shared.TextAction;
import javafx.stage.Stage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.ListViewMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import java.io.IOException;

@Disabled
@ExtendWith(ApplicationExtension.class)
class AppTest {
  App a;

  @Start
  public void start(Stage stage) throws IOException, ClassNotFoundException {
    a = new App();
    a.start(stage);
  }

  @Test
  void test_attack_move(FxRobot robot) {
    robot.clickOn("#M");
    robot.clickOn("#Elantris");
    robot.clickOn("#Mordor");
    robot.clickOn("#unitCount").write("1");
    robot.clickOn("#enter");
    robot.clickOn("#A");
    robot.clickOn("#Gondor");
    robot.clickOn("#Mordor");
    robot.clickOn("#unitCount").write("10");
    robot.clickOn("#enter");

    TextAction m1 = new MoveAttackTextAction("M", "B", "Elantris", "Mordor", 1, "Level1Unit");
    TextAction a1 = new MoveAttackTextAction("A", "B", "Gondor", "Mordor", 10, "Level1Unit");

    FxAssert.verifyThat("#unitCount", TextInputControlMatchers.hasText(""));
    FxAssert.verifyThat("#moves", ListViewMatchers.hasItems(2));
    FxAssert.verifyThat("#moves", ListViewMatchers.hasListCell(m1));
    FxAssert.verifyThat("#moves", ListViewMatchers.hasListCell(a1));
  }
}
