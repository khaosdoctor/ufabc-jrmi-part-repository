/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrmipartssystem;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import static java.lang.System.out;
import java.lang.reflect.InvocationTargetException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lucas Santos
 */
public class JRMIPartsSystemClient {

  private static IPartRepository currentRepository;
  private static IPart currentPart = null;
  private static Registry rmiServer;
  private static final Scanner input = new Scanner(System.in);

  /**
   * @param args the command line arguments
   */
  public static void main (String[] args) {
    try {
      say("Conectando ao servidor de peças...");
      rmiServer = LocateRegistry.getRegistry(null);

      say("Servidor conectado, listando repositórios disponíveis");
      String[] repositories = rmiServer.list();
      if (repositories.length <= 0) {
        throw new Error("Não há nenhum servidor de peças para se conectar");
      }

      say(String.format("Repositórios encontrados: %s", Arrays.toString(repositories)));
      setRepo(repositories[0]);

      say("Aguardando comandos. Digite 'help' para a lista de comandos...");
      while (true) {
        out.print(String.format("(Repo: %s || Peça: %s) > ", currentRepository.getRepositoryName(), currentPart == null ? "Nenhum" : currentPart.getPartId()));
        String command = input.next();
        run(command);
      }

    } catch (RemoteException e) {
      say(String.format("Erro ao inicializar servidor: %s", e.getMessage()));
    }
  }

  private static void run (String command) {
    try {
      switch (command) { // Foi a solução mais simples, usar reflection faria o programa muito genérico e demandaria mais tempo
        case "bind":
          bindCommand();
          break;

        case "listr":
          listrCommand();
          break;

        case "listp":
          listpCommand();
          break;

        case "listall":
          listallCommand();
          break;

        case "addp":
          addpCommand();
          break;
          
        case "setp":
          setpCommand();
          break;

        case "help":
          out.println("-- Comandos disponíveis --");
          out.println(" # Comandos gerais");
          out.println("  help: Mostra essa ajuda");
          out.println("  quit: Encerra o programa");

          out.println(" # Comandos de repositório");
          out.println("  listr: Lista repositórios de peças disponíveis");
          out.println("  bind: Altera o repositório de peças atual");
          out.println("  listp: Lista todas as peças do repositório atual");
          out.println("  listall: Lista todas as peças de todos os repositórios encontrados");
          out.println("  addp: Adiciona uma peça ao repositório atual (a peça adicionada passa a ser a nova peça atual)");
          out.println("  setp: Altera a peça atual pela peça selecionada");

          out.println(" # Comandos de peças");
          out.println("  getpid: Obtém uma peça por ID (A peça encontrada passa a ser a nova peça atual)");
          out.println("  getp: Obtém uma peça por nome");
          out.println("  describep: Obtém a descrição da peça atual");
          out.println("  clearsub: Limpa a lista de subpeças da peça atual");
          out.println("  addsub: Adiciona uma subpeça a peça atual");
          out.println("  removep: Remove a peça atual do servidor");
          break;
        case "quit":
          say("Encerrando client");
          System.exit(0);
          break;
        default:
          say("Comando não reconhecido");
          break;
      }
    } catch (Exception e) {
      out.println(e.getMessage());
    }
  }

  private static void setRepo (String name) {
    try {
      currentRepository = (IPartRepository) rmiServer.lookup(name);
      say(String.format("Repositório atual alterado para: %s", name));
    } catch (RemoteException ex) {
      say("Não foi possível encontrar o repositório especificado");
    } catch (NotBoundException ex) {
      say(ex.getMessage());
    }
  }

  private static void setPart (String id) {
    try {
      currentPart = (IPart) currentRepository.getPartById(id);
      say(String.format("Parte atual alterada para: %s", id));
    } catch (RemoteException ex) {
      say("Não foi possível encontrar a peça especificada");
    }
  }
  
  private static void prompt (String message) {
    out.println(String.format(" >> %s", message));
    out.print("  > ");
  }
  
  private static void say (String message) {
    out.println(String.format(" >> %s", message));
  }
  
  /*
   * COMMANDOS
   */
  private static void bindCommand () {
    prompt("Digite o nome do repositório:");
    String repoName = input.next();
    if (repoName.length() <= 0) {
      say("Nome inválido");
      return;
    }
    setRepo(repoName);
  }

  private static void listrCommand () {
    try {
      String[] repos = rmiServer.list();
      say(String.format("Repositórios disponíveis: %s", Arrays.toString(repos)));
    } catch (RemoteException ex) {
      say("Erro ao listar repositórios: " + ex.getMessage());
    }
  }

  private static void listpCommand () {
    try {
      currentRepository.getAllParts().stream().forEach(part -> {
        out.println(String.format("  - %s (ID: %s) -> %s (%d subpartes)", part.getPartName(), part.getPartId(), part.getPartDescription(), part.listSubparts().size()));
      });
    } catch (RemoteException e) {
      say("Erro ao listar peça: " + e.getMessage());
    }
  }

  private static void addpCommand () {
    try {
      prompt("Digite o nome da peça");
      String partName = input.next();

      prompt("Digite a descrição da peça");
      String partDesc = input.next();

      while (partName.length() <= 0) {
        prompt(" >> ERRO: O nome da peça não pode estar em branco");
        partName = input.next();
      }

      Part newPart = new Part(partName, partDesc);
      currentRepository.addNewPart(newPart);
      say("Peça adicionada ao repositório");
      setPart(newPart.getPartId());

    } catch (RemoteException e) {
      say("Erro ao adicionar peça: " + e.getMessage());
    }
  }

  private static void listallCommand () {
    try {
      String currentRepoName = currentRepository.getRepositoryName(); // Guarda o repositório anterior
      String[] allRepos = rmiServer.list(); // Lista todos os repositórios
      
      for (String repo : allRepos) { // Para cada repositório, passa listando as peças
        out.println(String.format("-- Peças do repositório '%s'", repo));
        currentRepository = (IPartRepository) rmiServer.lookup(repo); // Troca o repositório atual
        listpCommand(); // Roda o comando de lista de peças
      }
    } catch (RemoteException ex) {
      say(String.format("Erro ao listar todas as peças: %s", ex.getMessage()));
    } catch (NotBoundException ex) {
      Logger.getLogger(JRMIPartsSystemClient.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  private static void setpCommand () {
    
  }
}
