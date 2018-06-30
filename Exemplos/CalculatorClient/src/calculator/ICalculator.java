/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author ufabc
 */
public interface ICalculator extends Remote {
  public INumber soma (INumber a, INumber b)
        throws RemoteException;

    public INumber subtrai (INumber a, INumber b)
        throws RemoteException;

    public INumber multiplica (INumber a, INumber b)
        throws RemoteException;

    public INumber divide (INumber a, INumber b)
        throws RemoteException, DivisionByZeroException;
}
