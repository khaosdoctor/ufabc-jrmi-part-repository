/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrmipartssystem;

import static java.lang.System.out;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Lucas Santos
 */
public class JRMIPartsSystemServer {

  public static void main(String[] args) {
    try {
      Part minhaParte = new Part("martelo", "um martelo");
      minhaParte.addSubPart(new Part("cabo", "O cabo de um martelo"), 1);
      minhaParte.addSubPart(new Part("cabeça", "A cabeça de um martelo"), 1);

      PartRepository repo = new PartRepository("Armazem0");
      repo.addNewPart(minhaParte);
      repo.addNewPart(new Part("Pregos", "Prego simples"));
      
      PartRepository repo1 = new PartRepository("Armazem1");

      IPartRepository stub = (IPartRepository) UnicastRemoteObject.exportObject(repo, 0);
      IPartRepository stub1 = (IPartRepository) UnicastRemoteObject.exportObject(repo1, 0);
      Registry registry = LocateRegistry.getRegistry();
      registry.bind(repo.getRepositoryName(), repo);
      registry.bind(repo1.getRepositoryName(), repo1);
      out.println("Servidor de peças iniciado");
    } catch (Exception e) {
      System.err.println("Ocorreu um erro no servidor: " + e.toString());
    }
  }
}
