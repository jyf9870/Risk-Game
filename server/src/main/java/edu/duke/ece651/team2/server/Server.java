package edu.duke.ece651.team2.server;

import edu.duke.ece651.team2.shared.Map;
import edu.duke.ece651.team2.shared.TextAction;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
  final ServerSocket listener;
  private final ArrayList<Socket> clientSockets;

  /**
   * @param port is the server port, should as same as client port
   * @throws IOException when there are input or output exceptions
   */
  public Server(int port) throws IOException {
    this.clientSockets = new ArrayList<>();
    this.listener = new ServerSocket(port);
  }

  /**
   * @throws IOException when there are input or output exceptions
   */
  public Socket connectClient() throws IOException {
    Socket clientSkt = this.listener.accept();
    this.clientSockets.add(clientSkt);
    return clientSkt;
  }

  /**
   * @param sendMsg is the text message needs to be sent to client
   * @param socket  is the connected client socket
   * @throws IOException when there are input or output exceptions
   */
  public void sendTextMsg(String sendMsg, Socket socket) throws IOException {
    DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
    dataOut.writeUTF(sendMsg); // send a string to the client
  }

  /**
   * client receive a string from client
   *
   * @return the text message received from client
   * @throws IOException when there are input or output exceptions
   */
  public String receiveTextMsg(Socket socket) throws IOException {
    DataInputStream dataIn = new DataInputStream(socket.getInputStream());
    return dataIn.readUTF();
  }

  /**
   * @param socket is the connected client socket
   * @return the action string received from client
   * @throws IOException            when there are input or output exceptions
   * @throws ClassNotFoundException when casting to ArrayList<TextAction> failed
   */
  @SuppressWarnings("unchecked")
  public ArrayList<TextAction> receiveActions(Socket socket) throws IOException, ClassNotFoundException {
    ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
    Object obj = objectIn.readObject();
    ArrayList<TextAction> textActions = (ArrayList<TextAction>) obj;
    return textActions;
  }

  /**
   * @param map    the result map needs to be sent to client
   * @param socket is the connected client socket
   * @throws IOException when there are input or output exceptions
   */
  public void sendMap(Map map, Socket socket) throws IOException {
    ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
    objectOut.writeObject(map);
  }

  /**
   * @throws IOException when there are input or output exceptions
   */
  public void closeSocket() throws IOException {
    listener.close();
  }
}
