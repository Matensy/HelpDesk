# HelpDesk System

## Descrição

Este é um sistema simples de HelpDesk desenvolvido em Java usando Swing para a interface gráfica e MySQL para o banco de dados. O sistema permite que usuários registrem e visualizem chamados de suporte, e que atendentes gerenciem e respondam a esses chamados.

## Funcionalidades

### Usuário
- **Registrar**: O usuário pode criar uma nova conta fornecendo seu nome completo, login, senha e CPF.
- **Login**: O usuário pode fazer login usando seu login e senha.
- **Abrir Chamado**: Após o login, o usuário pode criar novos chamados descrevendo seu problema.
- **Visualizar Chamados**: O usuário pode listar e visualizar todos os seus chamados, incluindo as respostas fornecidas pelos atendentes.

### Atendente
- **Login**: O atendente pode fazer login usando seu login e senha.
- **Listar Chamados**: O atendente pode listar todos os chamados abertos.
- **Responder Chamado**: O atendente pode selecionar um chamado para responder, fornecendo uma solução para o problema descrito.
- **Visualizar Chamados**: O atendente pode ver o nome do usuário que abriu o chamado e o nome do atendente que respondeu ao chamado.

## Configuração

### Pré-requisitos

- Java JDK 8 ou superior
- MySQL
- IDE para Java (opcional, mas recomendado)

### Passos para Configuração

1. **Clone o Repositório**

   ```bash
   git clone https://github.com/seu-usuario/helpdesk-system.git
   cd helpdesk-system
   ```

2. **Configuração do Banco de Dados**

   Crie um banco de dados no MySQL e execute os seguintes comandos para criar as tabelas necessárias:

   ```sql
   CREATE DATABASE helpdesk;
   USE helpdesk;

   CREATE TABLE Usuarios (
       id INT AUTO_INCREMENT PRIMARY KEY,
       nome_completo VARCHAR(255) NOT NULL,
       login VARCHAR(255) NOT NULL UNIQUE,
       senha VARCHAR(255) NOT NULL,
       cpf VARCHAR(255) NOT NULL UNIQUE
   );

   CREATE TABLE Chamados (
       id INT AUTO_INCREMENT PRIMARY KEY,
       usuario_id INT,
       descricao VARCHAR(255) NOT NULL,
       status VARCHAR(50) DEFAULT 'aberto',
       resposta VARCHAR(255),
       FOREIGN KEY (usuario_id) REFERENCES Usuarios(id)
   );

   INSERT INTO Usuarios (nome_completo, login, senha, cpf) VALUES 
   ('Atendente 1', 'atendente1', 'senha1', '000.000.000-01'),
   ('Atendente 2', 'atendente2', 'senha2', '000.000.000-02'),
   ('Atendente 3', 'atendente3', 'senha3', '000.000.000-03');
   ```

3. **Configuração da Conexão com o Banco de Dados**

   Atualize a classe `ConexaoBD.java` com as credenciais do seu banco de dados:

   ```java
   public class ConexaoBD {
       public static Connection conectar() {
           try {
               return DriverManager.getConnection("jdbc:mysql://localhost:3306/helpdesk", "seu-usuario", "sua-senha");
           } catch (SQLException ex) {
               ex.printStackTrace();
               throw new RuntimeException("Erro ao conectar ao banco de dados", ex);
           }
       }
   }
   ```

4. **Executar o Sistema**

   Compile e execute o projeto a partir da sua IDE ou linha de comando.

   ```bash
   javac TelaLogin.java
   java TelaLogin
   ```

## Uso

1. **Registrar Usuário**
   - Execute o sistema e clique em "Registrar" para criar um novo usuário.

2. **Login**
   - Faça login com um usuário ou atendente existente.

3. **Usuário**
   - Após o login, o usuário pode criar novos chamados e visualizar seus chamados existentes.

4. **Atendente**
   - Após o login, o atendente pode listar todos os chamados, selecionar um chamado para responder e visualizar o histórico de chamados.

## Contribuição

Se você deseja contribuir com o projeto, sinta-se à vontade para abrir uma issue ou enviar um pull request.

## Licença

Este projeto está licenciado sob a MIT License. Veja o arquivo LICENSE para mais detalhes.

---

Se precisar de mais alguma coisa ou tiver alguma dúvida, estou à disposição para ajudar!
