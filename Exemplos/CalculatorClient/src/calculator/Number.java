/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

/**
 *
 * @author ufabc
 */
public class Number implements INumber {
  
  private double num;
  
  public Number (double val) {
    num = val;
  }

  @Override
  public double getValue() {
    return num;
  }
  
}
