/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author ufabc
 */
public class CalculatorServer {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    try {
      //Crio o objeto servidor
      Calculator calc = new Calculator();
      //Criamos o stub do objeto que será registrado
      ICalculator stub = (ICalculator) UnicastRemoteObject
          .exportObject(calc, 0);
      //Registra (binds) o stub no registry
      Registry registry = LocateRegistry.getRegistry();
      registry.bind("calculadora", stub);
      System.out.println("Servidor iniciado.");
    } catch (Exception e) {
      System.err.println("Ocorreu um erro no servidor: "
          + e.toString());
    }
  }

}
