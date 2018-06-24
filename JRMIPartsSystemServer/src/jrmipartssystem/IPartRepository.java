/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrmipartssystem;
import java.util.Set;

/**
 *
 * @author khaosdoctor
 */
public interface IPartRepository {

  /**
   * Obtém todas as partes do repositório
   * @return Uma lista de partes e suas subpartes
   */
  public Set<IPart> getAllParts();

  /**
   * Obtém partes pelo nome
   * @param name Nome da parte
   * @return Um array de partes com o mesmo nome
   */
  public Set<IPart> getPartByName(String name);

  /**
   * Obtém uma parte pelo ID
   * @param partId ID da parte
   * @return Parte obtida
   */
  public IPart getPartById(String partId);

  /**
   * Adiciona uma nova parte
   * @param part Parte a ser adicionada
   * @return Novo set de partes
   */
  public Set<IPart> addNewPart(IPart part);

  /**
   * Remove uma parte do repositório
   * @param part Parte a ser removida
   * @return True se removido com sucesso, false caso contrário
   */
  public void removePart(IPart part);

  /**
   * Remove uma parte pelo ID
   * @param partId ID da parte
   * @return True se removido com sucesso, falso caso contrário
   */
  public boolean removePartById(String partId);
  
  public String getRepositoryName();
}
