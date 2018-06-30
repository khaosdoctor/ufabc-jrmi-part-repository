/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrmipartssystem;
import java.util.Set;
import java.rmi.*;

/**
 *
 * @author khaosdoctor
 */
public interface IPartRepository extends Remote {

  /**
   * Obtém todas as partes do repositório
   * @return Uma lista de partes e suas subpartes
   * @throws java.rmi.RemoteException
   */
  public Set<IPart> getAllParts() throws RemoteException;

  /**
   * Obtém partes pelo nome
   * @param name Nome da parte
   * @return Um array de partes com o mesmo nome
   * @throws java.rmi.RemoteException
   */
  public Set<IPart> getPartByName(String name) throws RemoteException;

  /**
   * Obtém uma parte pelo ID
   * @param partId ID da parte
   * @return Parte obtida
   * @throws java.rmi.RemoteException
   */
  public IPart getPartById(String partId) throws RemoteException;

  /**
   * Adiciona uma nova parte
   * @param part Parte a ser adicionada
   * @return Novo set de partes
   * @throws java.rmi.RemoteException
   */
  public Set<IPart> addNewPart(IPart part) throws RemoteException;

  /**
   * Remove uma parte do repositório
   * @param part Parte a ser removida
   * @throws java.rmi.RemoteException
   */
  public void removePart(IPart part) throws RemoteException;

  /**
   * Remove uma parte pelo ID
   * @param partId ID da parte
   * @return True se removido com sucesso, falso caso contrário
   * @throws java.rmi.RemoteException
   */
  public boolean removePartById(String partId) throws RemoteException;
  
  public String getRepositoryName() throws RemoteException;
}
