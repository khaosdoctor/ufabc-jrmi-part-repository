/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrmipartssystem;
import java.util.Map;

/**
 *
 * @author khaosdoctor
 */
public interface IPart extends java.io.Serializable {

  /**
   * Obtém o ID da parte
   * @return ID da parte
   */
  public String getPartId ();

  /**
   * Obtém a descrição da parte
   * @return Decrição da parte
   */
  public String getPartDescription();
  /**
   * Seta a descrição da parte
   * @param desc Descrição da parte
   */
  public void setPartDescription(String desc);

  /**
   * Obtém o nome da parte
   * @return Nome da parte
   */
  public String getPartName();
  /**
   * Seta o nome da parte
   * @param name Nome da parte
   */
  public void setPartName(String name);

  /**
   * Retorna a lista de subpartes de uma parte
   * @return Um mapa contendo a parte e a quantidade requerida
   */
  public Map<IPart, Integer> listSubparts();
  /**
   * Adiciona uma nova subparte à parte em questão
   * @param part Parte a ser adicionada
   * @param qty Quantidade de partes necessárias
   * @return O novo mapa de partes
   */
  public Map<IPart, Integer> addSubPart(IPart part, int qty);
  /**
   * Remove uma subparte da lista de partes pela referência
   * @param part Parte a ser removida
   * @return O novo mapa de partes
   */
  public Map<IPart, Integer> removeSubPart(IPart part);
  /**
   * Remove uma subparte da lista de partes por ID
   * @param id ID da parte a ser removida
   * @return O novo mapa de partes
   */
  public Map<IPart, Integer> removeSubPart(String id);

  /**
   * Verifica se uma parte é primitiva ou composta
   * @return {boolean} True se for primitiva ou False caso contrário
   */
  public boolean isPrimitive();
  
  public boolean clearSubparts();
}
