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
public class Calculator implements ICalculator {
  public INumber soma (INumber a, INumber b) {
        return new Number (a.getValue() + b.getValue());
    };

    public INumber subtrai (INumber a, INumber b) {
        return new Number (a.getValue() - b.getValue());
    };

    public INumber multiplica (INumber a, INumber b)  {
        return new Number (a.getValue() * b.getValue());
    };

    public INumber divide (INumber a, INumber b)
        throws DivisionByZeroException  {
        if (b.getValue() == 0) throw new DivisionByZeroException();
        return new Number (a.getValue() / b.getValue());
    };
}
