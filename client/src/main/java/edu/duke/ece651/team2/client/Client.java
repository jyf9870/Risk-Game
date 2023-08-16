package edu.duke.ece651.team2.client;

import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.TextAction;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Client {
  final private Socket serverSkt;

  /**
   * @param hostname is the server host name
   * @param port     is the server port, should as same as Server's port
   * @throws IOException when there are input or output exceptions
   */

  public Client(String hostname, int port) throws IOException {
    this.serverSkt = new Socket(hostname, port);
  }

  /**
   * client send an ArrayList of TextAction to server
   *
   * @param textActions is the list of text actions needs to be sent to server
   * @throws IOException when there are input or output exceptions
   */
  public void sendActions(ArrayList<TextAction> textActions) throws IOException {
    ObjectOutputStream objectOut = new ObjectOutputStream(this.serverSkt.getOutputStream());
    objectOut.writeObject(textActions);
  }

  /**
   * @param sendMsg is the text message needs to be sent to server
   * @throws IOException when there are input or output exceptions
   */
  public void sendTextMsg(String sendMsg) throws IOException {
    DataOutputStream dataOut = new DataOutputStream(this.serverSkt.getOutputStream());
    dataOut.writeUTF(sendMsg);
  }

  /**
   * client receive a string from server
   *
   * @return the text message received from server
   * @throws IOException when there are input or output exceptions
   */
  public String receiveTextMsg() throws IOException {
    DataInputStream dataIn = new DataInputStream(this.serverSkt.getInputStream());
    return dataIn.readUTF();
  }

  /**
   * client receive a Map from server
   *
   * @return the map object received from server
   * @throws IOException            when there are input or output exceptions
   * @throws ClassNotFoundException when there are class not found exceptions
   */
  public Map receiveMap() throws IOException, ClassNotFoundException {
    ObjectInputStream objectIn = new ObjectInputStream(serverSkt.getInputStream());
    Map map = (Map) objectIn.readObject(); // read an object from server
    return map;
  }

  /**
   * close the client socket
   *
   * @throws IOException when there are input or output exceptions
   */
  public void closeClient() throws IOException {
    this.serverSkt.close();
  }

}
