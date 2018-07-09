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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.InputMethodTextRun;

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

        case "getpid":
          getpidCommand();
          break;

        case "getp":
          getpCommand();
          break;

        case "describep":
          describepCommand();
          break;

        case "removep":
          removepCommand();
          break;

        case "clearsub":
          clearsubCommand();
          break;

        case "addsub":
          addsubCommand();
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
      if (id == null) {
        currentPart = null;
        say("A peça atual foi resetada, encontre outra usando o comando setp ou getp");
        return;
      }

      IPart newPart = (IPart) currentRepository.getPartById(id);
      if (newPart == null) {
        say("Parte não encontrada neste repositório");
        return;
      }

      currentPart = newPart;
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

  private static boolean isCurrentPartNull () {
    if (currentPart == null) {
      say("Você precisa setar uma peça para trabalhar, use o comando getp ou setp");
      return true;
    }
    return false;
  }

  /*
   * COMMANDOS
   */
  private static void bindCommand () {
    prompt("Digite o nome do repositório:");
    input.nextLine();
    String repoName = input.nextLine();
    if (repoName.length() <= 0) {
      say("Nome inválido");
      return;
    }
    setRepo(repoName);
    setPart(null);
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
      input.nextLine();
      String partName = input.nextLine();

      prompt("Digite a descrição da peça");
      String partDesc = input.nextLine();

      while (partName.length() <= 0) {
        prompt(" >> ERRO: O nome da peça não pode estar em branco");
        partName = input.nextLine();
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
      setRepo(currentRepoName);
    } catch (RemoteException ex) {
      say(String.format("Erro ao listar todas as peças: %s", ex.getMessage()));
    } catch (NotBoundException ex) {
      Logger.getLogger(JRMIPartsSystemClient.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private static void setpCommand () {
    prompt("Digite o ID da peça");
    input.nextLine();
    String partId = input.nextLine();
    if (partId.length() <= 0) {
      return;
    }
    setPart(partId);
  }

  private static void getpidCommand () {
    try {
      prompt("Digite o ID da peça");
      input.nextLine();
      String partId = input.nextLine();
      if (partId.length() <= 0) {
        prompt("ID inválido");
        return;
      }
      IPart part = currentRepository.getPartById(partId);

      if (part != null) {
        say(String.format("%s (ID: %s): %s (%d subpartes)", part.getPartName(), part.getPartId(), part.getPartDescription(), part.listSubparts().size()));
        setPart(partId);
        return;
      }

      say("Parte não encontrada");
    } catch (RemoteException ex) {
      say("Erro ao obter parte: " + ex.getMessage());
    }
  }

  private static void getpCommand () {
    try {
      prompt("Digite o nome da peça");
      input.nextLine();
      String partName = input.nextLine();
      if (partName.length() <= 0) {
        prompt("Nome inválido");
        return;
      }
      Set<IPart> partsFound = currentRepository.getPartByName(partName);

      if (partsFound.size() <= 0) {
        say("Nenhuma peça com este nome foi encontrada");
        return;
      }

      say("Partes encontradas:");
      for (IPart part : partsFound) {
        if (part != null) {
          say(String.format("- %s (ID: %s): %s (%d subpartes)", part.getPartName(), part.getPartId(), part.getPartDescription(), part.listSubparts().size()));
          return;
        }
      }
    } catch (RemoteException ex) {
      say("Erro ao obter parte: " + ex.getMessage());
    }
  }

  private static void describepCommand () {
    if (isCurrentPartNull()) return;

    say(String.format("%s (ID: %s): %s (%d subpartes)", currentPart.getPartName(), currentPart.getPartId(), currentPart.getPartDescription(), currentPart.listSubparts().size()));
    if (!currentPart.listSubparts().isEmpty()) {
      say("Subpartes:");
      currentPart.listSubparts()
          .entrySet()
          .forEach((subpart) -> say(String.format(" - %d %s: %s", subpart.getValue(), subpart.getKey().getPartName(), subpart.getKey().getPartDescription())));
    }

  }

  private static void removepCommand () {
    try {
      if (isCurrentPartNull()) return;

      currentRepository.removePartById(currentPart.getPartId());
      say("Peça removida");
      setPart(null);
    } catch (RemoteException ex) {
      say("Erro ao remover peça: " + ex.getMessage());
    }
  }

  private static void clearsubCommand () {
    try {
      if (isCurrentPartNull()) return;

      currentRepository.clearSubPartList(currentPart);
      setPart(currentPart.getPartId());
    } catch (RemoteException ex) {
      say("Erro ao remover subpartes: " + ex.getMessage());
    }
  }

  private static void addsubCommand () {
    try {
      if (isCurrentPartNull()) return;

      prompt("Digite o nome da subpeça");
      input.nextLine();
      String subName = input.nextLine();

      prompt("Digite a descrição da subpeça");
      String subDesc = input.nextLine();
      
      prompt("Digite a quantidade de subpeças");
      int subAmount = Integer.parseInt(input.nextLine());

      while (subName.length() <= 0) {
        prompt("O Nome da subpeça não pode ser nulo");
        input.nextLine();
        subName = input.nextLine();
      }

      while (subAmount <= 0) {
        prompt("A quantidade de peças deve ser maior do que 0");
        input.nextLine();
        subAmount = Integer.parseInt(input.nextLine());
      }

      currentRepository.addSubPart(currentPart, new Part(subName, subDesc), subAmount);
      setPart(currentPart.getPartId());
    } catch (RemoteException ex) {
      say("Erro ao adicionar subpeça");
    }
  }
}
