# Repositório de Peças JRMI

> Implementação do Java Remote Method Invocation para um repositório de peças

## Uso

### Sistemas não *nix

1. Navegue até a pasta `JRMIPartsSystemServer/build/classes`
2. Execute `rmiregistry` a partir desta pasta
3. Abra um novo terminal
4. Execute o servidor
5. Vá até a pasta `JRMIPartsSystemClient` e execute o cliente

### Sistema *nix

- Execute `initRMI.sh` para inicializar o Registro RMI (é importante que você execute este comando de dentro da pasta raiz)
- Execute `initServer.sh` para carregar o servidor de repositórios
- Execute `initClient.sh` para iniciar o programa

Faça isso em três sessões de terminais separadas

### Comandos disponíveis

Digite `help` na linha de comando do client para ver os comandos disponíveis
