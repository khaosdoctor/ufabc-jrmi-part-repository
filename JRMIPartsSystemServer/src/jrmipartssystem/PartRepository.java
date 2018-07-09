/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrmipartssystem;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author khaosdoctor
 */
public class PartRepository implements IPartRepository {

  private final String RepositoryName;
  private final Set<IPart> inventoryParts = new HashSet<>();

  public PartRepository (String name) {
    this.RepositoryName = name;
  }

  public PartRepository () {
    this.RepositoryName = String.format("repo_%d", System.currentTimeMillis());
  }

  @Override
  public IPart getPartById (String partId) {
    for (IPart part : this.inventoryParts) {
      if (part.getPartId().equals(partId)) {
        return part;
      }
    }
    return null;
  }

  @Override
  public void removePart (IPart part) {
    this.inventoryParts.remove(part);
  }

  @Override
  public boolean removePartById (String partId) {
    boolean found = false;
    for (IPart part : this.inventoryParts) {
      if (part.getPartId().equals(partId)) {
        found = true;
        this.inventoryParts.remove(part);
        break;
      }
    }
    return found;
  }

  @Override
  public String getRepositoryName () {
    return this.RepositoryName;
  }

  @Override
  public Set<IPart> getPartByName (String name) {
    Set<IPart> partsFound = new HashSet<>();
    this.inventoryParts.stream()
        .filter((part) -> (part.getPartName().equals(name)))
        .forEachOrdered((part) -> partsFound.add(part));
    return partsFound;
  }

  @Override
  public Set<IPart> getAllParts () {
    return this.inventoryParts;
  }

  @Override
  public Set<IPart> addNewPart (IPart part) {
    this.inventoryParts.add(part);
    return this.inventoryParts;
  }

  @Override
  public boolean clearSubPartList (IPart part) {
    part.clearSubparts();
    return true;
  }
  
  @Override
  public boolean addSubPart(IPart part, IPart subpart, int amount) {
    part.addSubPart(subpart, amount);
    return true;
  }

}
