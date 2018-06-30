/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author ufabc
 */
public class CalculatorClient {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    try {

      // Localiza o registry. É possível usar endereço/IP porta
      Registry registry = LocateRegistry.getRegistry(null);
      // Consulta o registry e obtém o stub para o objeto remoto
      ICalculator calc = (ICalculator) registry
          .lookup("calculadora");
      // A partir deste momento, cahamadas à Caluladora podem ser
      // feitas como qualquer chamada a métodos

      INumber num1 = new Number(3);
      INumber num2 = new Number(4);
      //Aqui são feitas diversas chamadas remotas
      INumber soma = calc.soma(num1, num2);
      INumber sub = calc.subtrai(num1, num2);
      INumber mult = calc.multiplica(num1, num2);
      INumber div = calc.divide(num1, num2);
      System.out.println("Resultados obtidos do servidor:"
          + "\n\t+:" + soma.getValue()
          + "\n\t-:" + sub.getValue()
          + "\n\t*:" + mult.getValue()
          + "\n\t/:" + div.getValue());

      try {
        calc.divide(new Number(1), new Number(0));
      } catch (DivisionByZeroException e) {
        System.out.println(
            "Tentou dividir por zero! Esta é uma exceção remota.");
      }

    } catch (Exception e) {
      System.err.println("Ocorreu um erro no cliente: "
          + e.toString());
    }
  }

}
