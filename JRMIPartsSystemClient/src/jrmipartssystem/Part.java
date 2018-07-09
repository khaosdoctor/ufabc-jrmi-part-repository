/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrmipartssystem;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author khaosdoctor
 */
public class Part implements IPart {

  private final String partId;
  private String name, description;
  private final Map<IPart, Integer> subparts = new HashMap<>();

  public Part (String name, String description) {
    this.partId = String.format("%d_%s", System.currentTimeMillis(), name); // Gera um ID aleatório para a peça
    this.name = name;
    this.description = description;
  }

  @Override
  public String getPartId () {
    return this.partId;
  }

  @Override
  public String getPartDescription () {
    return this.description;
  }

  @Override
  public void setPartDescription (String desc) {
    this.description = desc;
  }

  @Override
  public String getPartName () {
    return this.name;
  }

  @Override
  public void setPartName (String name) {
    this.name = name;
  }

  @Override
  public Map<IPart, Integer> listSubparts () {
    return this.subparts;
  }

  @Override
  public Map<IPart, Integer> addSubPart (IPart part, int qty) {
    this.subparts.put(part, qty);
    return this.subparts;
  }

  @Override
  public boolean isPrimitive () {
    return this.subparts.isEmpty();
  }

  @Override
  public Map<IPart, Integer> removeSubPart (IPart part) {
    this.subparts.remove(part);
    return this.subparts;
  }

  @Override
  public Map<IPart, Integer> removeSubPart (String id) {
    for (IPart part : this.subparts.keySet()) {
      if (id.equals(part.getPartId())) {
        this.subparts.remove(part);
        break;
      }
    }
    return this.subparts;
  }

  @Override
  public boolean clearSubparts () {
    this.subparts.clear();
    return true;
  }
}
