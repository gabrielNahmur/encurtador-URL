# encurtador-URL
# Encurtador de URL com Spring Boot e Redis

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=spring&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-6.x-red?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-blue?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-red?style=for-the-badge&logo=apache-maven&logoColor=white)

---

### Tabela de Conteúdos
1. [Descrição do Projeto](#descrição-do-projeto)
2. [Funcionalidades](#funcionalidades)
3. [Conceitos e Estruturas de Dados Aplicados](#conceitos-e-estruturas-de-dados-aplicados)
4. [Tecnologias Utilizadas](#tecnologias-utilizadas)
5. [Como Executar o Projeto](#como-executar-o-projeto)
6. [Como Usar a API](#como-usar-a-api)
7. [Desafios e Aprendizados](#desafios-e-aprendizados)
8. [Autor](#autor)

---

## Descrição do Projeto

Este projeto nasceu da curiosidade em aplicar os conceitos teóricos de **Estrutura de Dados**, aprendidos durante a faculdade, em um projeto de back-end prático para conseguir entender como aplicar esses conceitos e me preparar para o mercado de trabalho. Após estudar algumas estruturas, a **Tabela Hash (Hash Table)** me pareceu bem interessante por sua eficiência em operações de busca.

A partir daí, pesquisei projetos que poderiam se adaptam bem a dessa estrutura e cheguei à ideia de um **Encurtador de URLs**. Este tipo de funcionalidade depende de uma associação rápida e direta entre um código curto (chave) e uma URL longa (valor), o que lembra muito um caso de uso para uma Tabela Hash.

A aplicação consiste em uma API RESTful desenvolvida em Java com Spring Boot que permite encurtar URLs longas e redirecionar usuários a partir das URLs encurtadas.

## Funcionalidades

✔️ **Encurtar URLs:** Endpoint para enviar uma URL longa e receber uma versão curta e única.  
✔️ **Redirecionamento Rápido:** Endpoint que recebe um código curto e redireciona o usuário para a URL original de forma quase instantânea.  
✔️ **API RESTful:** Interface clara e bem definida para interação com a funcionalidade.

## Conceitos e Estruturas de Dados Aplicados

O "coração" do programa foi projetado em torno da necessidade de uma busca de alta performance.

* **Tabela Hash (Hash Table):** A escolha desta estrutura foi o ponto de começo deste projeto. Para que um encurtador de URL seja eficiente, a busca da URL original a partir do código curto precisa ter complexidade de tempo constante, ou seja, $O(1)$. A Tabela Hash oferece exatamente essa complexidade.

* **Redis:** Em vez de implementar uma Tabela Hash do zero, optei por usar o **Redis**, um banco de dados NoSQL de chave-valor em memória. O Redis funciona, na prática, como uma Tabela Hash distribuída, sendo a ferramenta consideravelmente padrão (atualmente) para casos de como este, caches e gerenciamento de sessões.

## Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3
    * **Spring Web:** Para a criação dos endpoints RESTful.
    * **Spring Data Redis:** Para a integração com o banco de dados Redis.
* **Banco de Dados:** Redis 6 (executado em um container Docker).
* **Gerenciamento de Build e Dependências:** Apache Maven.
* **Ambiente de Containerização:** Docker.
* **Testes de API:** Postman.

## Como Executar o Projeto

Siga os passos abaixo para executar o projeto em seu ambiente local.

**Pré-requisitos:**
* [Git](https://git-scm.com/downloads)
* [Java 21 (JDK)](https://www.oracle.com/java/technologies/downloads/#jdk21-windows)
* [Docker Desktop](https://www.docker.com/products/docker-desktop/)

**Passos:**

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/gabrielNahmur/encurtador-URL.git](https://github.com/gabrielNahmur/encurtador-URL.git)
    cd encurtador-URL
    ```

2.  **Inicie o container do Redis:**
    Abra um terminal e execute o comando abaixo para iniciar um servidor Redis que não precisa de senha.
    ```bash
    docker run -d --name encurtador-redis -p 6379:6379 redis:6
    ```
    Você pode verificar se o container está rodando com o comando`docker ps`.

3.  **Construa o projeto com o Maven Wrapper:**
    Este comando irá baixar as dependências e compilar o código. O `clean` garante que nenhuma compilação antiga vai ser usada usada.
    ```bash
    # No Windows
    .\mvnw.cmd clean install

    # No Linux ou macOS
    ./mvnw clean install
    ```

4.  **Execute a aplicação:**
    ```bash
    # No Windows
    .\mvnw.cmd spring-boot:run

    # No Linux ou macOS
    ./mvnw spring-boot:run
    ```

A aplicação estará rodando e pronta para receber requisições em `http://localhost:8080`.

## Como Usar a API

Use uma ferramenta como o Postman ou Insomnia para interagir com os endpoints.

#### 1. Encurtar uma URL

* **Método:** `POST`
* **URL:** `http://localhost:8080/encurtar`
* **Body (raw, JSON):**
    ```json
    {
        "urlLonga": "[https://www.linkedin.com/in/gabriel-amaro-/](https://www.linkedin.com/in/gabriel-amaro-/)"
    }
    ```
* **Resposta de Sucesso (Status `201 Created`):**
    ```json
    {
        "urlEncurtada": "http://localhost:8080/XyZ123"
    }
    ```

#### 2. Redirecionar para a URL Original

* **Método:** `GET`
* **URL:** `http://localhost:8080/{shortCode}` (substitua `{shortCode}` pelo código recebido, ex: `http://localhost:8080/XyZ123`)
* **Ação:** Abra esta URL em qualquer navegador web. Você será redirecionado automaticamente para a URL longa original.

## Desafios e Aprendizados

Este projeto foi uma jornada de depuração e aprendizado muito legal. Vou listar abaixo alguns dos principais desafios que eu enfrentei foram:

* **`Loading Infinito` e o Bloqueio do `SecureRandom`:** Inicialmente, a aplicação travava a cada requisição. A depuração com logs (`System.out.println`) me permitiu isolar o problema na criação de um `new SecureRandom()` a cada chamada. A solução foi criar uma única instância da classe e reutilizá-la, o que me ensinou sobre o funcionamento interno de APIs de segurança e a importância do gerenciamento eficiente de recursos.

* **Erro de Autenticação `NOAUTH` com Redis:** Após resolver o travamento, a aplicação começou a falhar com um erro de autenticação no Redis. Isso me forçou a estudar o log de erro (`stack trace`) para entender a mensagem e descobrir que a imagem Docker do Redis que eu estava usando exigia uma senha. O aprendizado foi duplo: a importância de ler os logs de erro detalhadamente e como gerenciar configurar serviços externos com Docker, optando por uma versão que não exigia autenticação para simplificar o desenvolvimento.

## Autor

| [<img src="https://avatars.githubusercontent.com/u/175945596?v=4" width=115><br><sub>Gabriel Nahmur P. Amaro</sub>](https://github.com/gabrielNahmur) |
| :---: |

Projeto desenvolvido por **[Gabriel Nahmur Peres Amaro - Desenvolvedor, Estudante (3 Semestre) em Analise e Desenvolvimento de Sistemas]**.

[![LinkedIn](https://img.shields.io/badge/linkedin-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/gabriel-amaro-/)
[![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)](https://github.com/gabrielNahmur)