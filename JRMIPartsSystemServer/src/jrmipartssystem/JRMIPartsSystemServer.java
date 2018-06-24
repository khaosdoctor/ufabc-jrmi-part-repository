/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrmipartssystem;

import static java.lang.System.out;

/**
 *
 * @author khaosdoctor
 */
public class JRMIPartsSystemServer {
  
  public static void main (String[] args) {
    
    Part minhaParte = new Part("martelo", "um martelo");
    PartRepository repo = new PartRepository();
    minhaParte.addSubPart(new Part("cabo", "O cabo de um martelo"), 1);
    minhaParte.addSubPart(new Part("cabeça", "A cabeça de um martelo"), 1);
    
    repo.addNewPart(minhaParte);
    repo.addNewPart(new Part("Pregos", "Prego simples"));
    
    out.println(String.format("Obtendo peças de %s", repo.getRepositoryName()));
    
    repo.getAllParts().stream().forEach(part -> {
      out.println(String.format("- Parte: %s (ID: %s) -> %s", part.getPartName(), part.getPartId(), part.getPartDescription()));
      
      if (!part.listSubparts().isEmpty()) {
        out.println("Subpartes:");
        part.listSubparts()
            .entrySet()
            .forEach((subpart) -> out.println(String.format("---> %d %s: %s", subpart.getValue(), subpart.getKey().getPartName(), subpart.getKey().getPartDescription())));
      }
    });
    
  }
}