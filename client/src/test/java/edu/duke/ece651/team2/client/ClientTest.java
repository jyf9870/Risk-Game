package edu.duke.ece651.team2.client;

import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.MoveAttackTextAction;
import edu.duke.ece651.team2.shared.Territory;
import edu.duke.ece651.team2.shared.TextAction;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {
  @Test
  public void testSendActions() throws IOException, ClassNotFoundException {
    // Set up the server socket
    ServerSocket serverSocket = new ServerSocket(12345);
    // Set up the client instance
    Client client = new Client("localhost", 12345);
    Socket serverSkt = serverSocket.accept();

    TextAction textAction1 = new MoveAttackTextAction("M", "Alice", "Elantris", "Scadrial", 1, "Level2Unit");
    TextAction textAction2 = new MoveAttackTextAction("A", "Mike", "Scadrial", "Elantris", 10, "Level2Unit");

    ArrayList<TextAction> expected = new ArrayList<>();
    expected.add(textAction1);
    expected.add(textAction2);
    client.sendActions(expected);
    ObjectInputStream dataIn = new ObjectInputStream(serverSkt.getInputStream());
    Object obj = dataIn.readObject();
    ArrayList<String> res = (ArrayList<String>) obj;
    assertEquals(expected, res);
    serverSkt.close();
    client.closeClient();
    serverSocket.close();
  }

  @Test
  public void testSendActionValidity() throws IOException {
    // Set up the server socket
    ServerSocket serverSocket = new ServerSocket(12345);
    // Set up the client instance
    Client client = new Client("localhost", 12345);
    Socket serverSkt = serverSocket.accept();
    String expected = "expected message!";
    client.sendTextMsg(expected);

    DataInputStream in = new DataInputStream(serverSkt.getInputStream());
    String res = in.readUTF();
    assertEquals(expected, res);
    // Teardown
    serverSkt.close();
    client.closeClient();
    serverSocket.close();
  }

  @Test
  public void testReceiveTextMsg() throws IOException {
    // Set up the server socket
    ServerSocket serverSocket = new ServerSocket(12345);
    // Set up the client instance
    Client client = new Client("localhost", 12345);
    Socket serverSkt = serverSocket.accept();
    String expected = "test Action";
    DataOutputStream dataOut = new DataOutputStream(serverSkt.getOutputStream());
    dataOut.writeUTF(expected);
    String res = client.receiveTextMsg();
    assertEquals(expected, res);
    serverSkt.close();
    client.closeClient();
    serverSocket.close();
  }

  @Test
  public void testReceiveMap() throws IOException, ClassNotFoundException {
    // Initialize the map
    Map m = new Map();
    // Set up the server socket
    ServerSocket listener = new ServerSocket(12345);
    // Set up the client instance
    Client client = new Client("localhost", 12345);
    Socket serverSkt = listener.accept();
    ObjectOutputStream objectOut = new ObjectOutputStream(serverSkt.getOutputStream());
    objectOut.writeObject(m);
    Map receiveMap = client.receiveMap();
    ArrayList<Territory> res = receiveMap.getTerritories();
    ArrayList<Territory> expected = m.getTerritories();
    assertEquals(expected, res);
    // Disconnect
    client.closeClient();
    serverSkt.close();
    listener.close();
  }

}

