package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import edu.duke.ece651.team2.shared.TextAction;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {
  private static final int PORT = 12345;

  @Test
  public void testConnectClient() throws IOException {
    Server server = new Server(PORT);
    Socket client = new Socket("localhost", PORT);
    Socket serverSkt = server.connectClient();
    assertNotNull(serverSkt);
    client.close();
    serverSkt.close();
    server.closeSocket();
  }


  @Test
  public void testSendTextMsg() throws IOException {
    Server server = new Server(PORT);
    Socket client = new Socket("localhost", PORT);
    Socket serverSkt = server.connectClient();
    String expected = "expected message!";
    server.sendTextMsg(expected, serverSkt);
    DataInputStream in = new DataInputStream(client.getInputStream());
    String res = in.readUTF();
    assertEquals(expected, res);
    // Teardown
    client.close();
    serverSkt.close();
    server.closeSocket();
  }


  @Test
  public void testReceiveActions() throws IOException, ClassNotFoundException {
    Server server = new Server(PORT);
    Socket client = new Socket("localhost", PORT);
    Socket serverSkt = server.connectClient();

    TextAction textAction = new MoveAttackTextAction("M", "Alice", "Elantris", "Scadrial", 1, "Level2Unit");

    ArrayList<TextAction> expected = new ArrayList<>();
    expected.add(textAction);
    ObjectOutputStream objectOut = new ObjectOutputStream(client.getOutputStream());
    objectOut.writeObject(expected);
    ArrayList<TextAction> res = server.receiveActions(serverSkt);
    assertEquals(expected, res);
    // Teardown
    client.close();
    serverSkt.close();
    server.closeSocket();
  }

  @Test
  public void testReceiveTextMsg() throws IOException {
    Server server = new Server(PORT);
    Socket client = new Socket("localhost", PORT);
    Socket serverSkt = server.connectClient();

    String expected = "test Action";
    DataOutputStream dataOut = new DataOutputStream(client.getOutputStream());
    dataOut.writeUTF(expected);
    String res = server.receiveTextMsg(serverSkt);
    assertEquals(expected, res);
    client.close();
    serverSkt.close();
    server.closeSocket();
  }


  @Test
  public void testSendMap() throws IOException, ClassNotFoundException {
    Server server = new Server(PORT);
    Socket client = new Socket("localhost", PORT);
    Socket serverSkt = server.connectClient();
    Map expected = new Map();
    server.sendMap(expected, serverSkt);
    ObjectInputStream in = new ObjectInputStream(client.getInputStream());
    Map received = (Map) in.readObject();
    assertEquals(expected.getTerritories(), received.getTerritories());
    // Teardown
    client.close();
    serverSkt.close();
    server.closeSocket();
  }


  @Test
  public void testCloseSocket() throws IOException {
    Server server = new Server(PORT);
    Socket client = new Socket("localhost", PORT);
    Socket serverSkt = server.connectClient();
    server.closeSocket();
    assertTrue(server.listener.isClosed());
  }
}


