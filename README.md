🚀 Projeto Backend Java Spring Boot com MySQL & Docker Compose
🌟 Visão Geral do Projeto
Este projeto demonstra uma aplicação backend desenvolvida em Java com Spring Boot, que se conecta a um banco de dados MySQL. O foco principal é a demonstração de um setup robusto e replicável utilizando Docker Compose para orquestrar os serviços da aplicação e do banco de dados, facilitando o desenvolvimento, testes e deploy.

A arquitetura do projeto prioriza:

Modularidade: Componentes bem definidos e desacoplados.
Facilidade de Implantação: Utilização de containers Docker para ambientes consistentes.
Manutenibilidade: Código limpo e configurável.
Segurança: Configuração inicial com foco em boas práticas de credenciais e comunicação.
🛠️ Tecnologias Utilizadas
Java 21 (LTS): Linguagem de programação principal.
Spring Boot 3.x: Framework para desenvolvimento rápido de aplicações Java.
Maven: Ferramenta de automação de build e gerenciamento de dependências.
MySQL 8.0: Sistema de gerenciamento de banco de dados relacional.
Docker: Plataforma para desenvolvimento, envio e execução de aplicações em containers.
Docker Compose: Ferramenta para definir e executar aplicações Docker multi-container.
�� Pré-requisitos
Para rodar este projeto, você precisará ter o seguinte software instalado em sua máquina:

Docker Desktop (inclui Docker Engine e Docker Compose)

www.docker.com

docs.docker.com
🚀 Primeiros Passos
Siga as instruções abaixo para configurar e executar a aplicação em seu ambiente local usando Docker Compose.

1. Clonar o Repositório
   bash
   Copiar

git clone [URL_DO_SEU_REPOSITORIO]
cd [NOME_DO_DIRETORIO_DO_PROJETO]
2. Configuração do Banco de Dados e Aplicação
   O projeto utiliza variáveis de ambiente para a conexão com o banco de dados. Essas variáveis são definidas no arquivo docker-compose.yml e no application.properties/application.yml da sua aplicação Spring Boot.

Arquivo: src/main/resources/application.properties (ou .yml)

properties
Copiar

# Exemplo para application.properties
server.port=8080
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
Nota: É crucial que sua aplicação leia essas variáveis de ambiente (SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD).

3. Executar a Aplicação com Docker Compose
   Navegue até o diretório raiz do projeto (onde o docker-compose.yml está localizado) e execute o seguinte comando:

bash
Copiar

docker-compose up --build
--build: Garante que as imagens (especialmente a da sua aplicação Java) sejam construídas a partir do Dockerfile mais recente.
Este comando irá:
Construir a imagem Docker para a sua aplicação Java.
Baixar a imagem do MySQL 8.0 (se ainda não estiver presente).
Iniciar ambos os serviços (mysql_db e java_app).
O serviço java_app só iniciará após o mysql_db estar completamente inicializado e saudável (graças ao healthcheck e depends_on).
Para rodar em segundo plano (detached mode), adicione -d:

bash
Copiar

docker-compose up --build -d
�� Acessando a Aplicação e o Banco de Dados
Aplicação Backend
A aplicação estará acessível no seu navegador ou via Postman/Insomnia em:
http://localhost:8081

Testes rápidos via Postman/curl:
- GET http://localhost:8081/auth/ping → deve retornar {"status":"ok"}
- POST http://localhost:8081/auth/login com JSON {"username":"user","password":"user123"} → deve retornar um token JWT

Importante sobre portas:
- Usando Docker Compose, a porta do host mapeia diretamente para 8081 do container (8081:8081). Portanto, use http://localhost:8081.
- Se alterar server.port no application.properties, ajuste também a porta no compose.yaml.

Banco de Dados MySQL
Você pode conectar-se ao banco de dados MySQL de sua máquina local (usando ferramentas como MySQL Workbench, DBeaver, DataGrip) com as seguintes credenciais:
Host: localhost
Porta: 3306
Usuário: ecommerceuser
Senha: ecommercepass
Banco de Dados: ecommerce_db
(Usuário Root): root
(Senha Root): mymainpassword
Atenção: As senhas acima são apenas para desenvolvimento. Em produção, utilize segredos gerenciados com segurança (ex: Secret Vaults).

📁 Estrutura do Projeto (Alta Nível)
.
├── src/                      # Código fonte da aplicação Java
│   └── main/
│       └── java/
│       └── resources/        # Arquivos de configuração (e.g., application.properties)
├── pom.xml                   # Configuração do Maven
├── Dockerfile                # Instruções para construir a imagem Docker da aplicação Java
├── docker-compose.yml        # Orquestração dos containers (aplicação Java e MySQL)
└── README.md                 # Este arquivo
🔑 Considerações de Segurança
Credenciais: As credenciais do banco de dados são injetadas via variáveis de ambiente no Docker Compose, o que é uma prática recomendada para evitar hardcoding. Em produção, considere usar soluções como HashiCorp Vault ou AWS Secrets Manager.
HTTPS: Para qualquer aplicação em produção, é mandatório configurar HTTPS (TLS/SSL) para criptografar a comunicação entre clientes e o backend.
Validação de Entrada: Toda entrada do usuário deve ser rigorosamente validada para prevenir ataques como injeção SQL, XSS, etc. (O Spring Boot oferece Bean Validation para isso).
Dependências: Manter as bibliotecas atualizadas (Maven, Spring Boot, etc.) é crucial para proteger contra vulnerabilidades conhecidas (CVEs).
⚙️ Desenvolvimento Local (Sem Docker Compose)
Para desenvolver e testar a aplicação Java diretamente em sua máquina (sem Docker Compose para o backend, mas ainda precisando de um MySQL local ou remoto):

Instale: JDK 21 e Maven.
Configure: As propriedades de conexão com o banco de dados diretamente no application.properties para apontar para um MySQL disponível (ex: spring.datasource.url=jdbc:mysql://localhost:3306/db_nutricionista).
Execute:
bash
Copiar

mvn clean install
mvn spring-boot:run
🤝 Contribuição
Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.