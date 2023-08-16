package edu.duke.ece651.team2.shared;

import java.io.Serializable;

public abstract class TextAction implements Serializable {
  final public String name;
  final public String issuer;


  public TextAction(String name, String issuer) {
    this.name = name;
    this.issuer = issuer;

  }
}
