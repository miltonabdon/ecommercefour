# 🚀 Ecommercefour — Backend Java Spring Boot com MySQL & Docker Compose

Este repositório contém uma API REST de e-commerce construída com Spring Boot 3 (Java 21), persistência em MySQL 8 e empacotada para execução em Docker. O projeto inclui autenticação via JWT, CRUD de produtos, processamento de pedidos com regras de negócio (estoque, cancelamento automático, cálculo de total), e endpoints analíticos otimizados para MySQL.


## 🧱 Tecnologias
- Java 21
- Spring Boot 3.x (Web, Security, Data JPA)
- MySQL 8
- Maven
- Docker e Docker Compose


## 📦 Arquitetura (alto nível)
- Camada Controller: expõe endpoints REST (Auth, Produtos, Pedidos, Analytics)
- Camada Service: regras de negócio (ex.: processar pagamento, atualizar estoque, cancelar pedido)
- Repositórios JPA: acesso ao MySQL
- Entidades: Produto, Pedido, User, Status


## 🔐 Autenticação e Autorização
- Usuários in-memory para login inicial (configurados em SecurityConfig):
  - admin / admin123 (ROLE_ADMIN)
  - user / user123 (ROLE_USER)
- Fluxo:
  1. POST /auth/login com {"username","password"}
  2. Recebe token JWT
  3. Enviar nas próximas requisições: Header Authorization: Bearer <token>
- Endpoint público de saúde: GET /auth/ping → {"status":"ok"}


## 🗃️ Banco de Dados e Seed
- MySQL é criado via Docker Compose com as credenciais abaixo
- Ao iniciar a aplicação, dados de exemplo são inseridos (seed) em transação:
  - 15 Produtos com IDs UUID e metadados
  - 5 Pedidos (Status PENDENTE, pago=false, valorTotal calculado, createdBy="admin")
  - 4 Users (persistidos em tabela users)
- Observação: o DDL está configurado como create-drop (dados são recriados a cada start em dev)


## ⚙️ Como executar com Docker Compose (recomendado)
Pré-requisitos: Docker Desktop instalado.

1. Clonar o repositório
   - git clone <URL_DO_REPO>
   - cd ecommercefour

2. Subir os serviços
   - docker compose up --build
   - Para rodar em segundo plano: docker compose up --build -d

3. Acessos
   - API: http://localhost:8081
   - MySQL: localhost:3306

O Compose usa compose.yaml (já incluído no repo) com variáveis de ambiente compatíveis com application.properties.


## 🔧 Variáveis de Ambiente relevantes (já definidas no compose)
- SPRING_DATASOURCE_URL=jdbc:mysql://mysql_db:3306/ecommerce_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
- SPRING_DATASOURCE_USERNAME=ecommerceuser
- SPRING_DATASOURCE_PASSWORD=ecommercepass

application.properties principais:
- server.port=8081
- spring.jpa.hibernate.ddl-auto=create-drop
- spring.jpa.show-sql=true


## 🧪 Testes rápidos (curl/Postman)
- Saúde
  - GET http://localhost:8081/auth/ping → {"status":"ok"}
- Login (JWT)
  - POST http://localhost:8081/auth/login
    - Body: {"username":"user","password":"user123"}
    - Resposta: { token, username, roles }
- Produtos
  - GET /produtos (requer JWT ROLE_USER ou ROLE_ADMIN)
  - GET /produtos/{id}
  - POST /produtos (ROLE_USER/ADMIN)
  - PUT /produtos/{id} (ROLE_ADMIN)
  - DELETE /produtos/{id} (ROLE_ADMIN)
- Pedidos
  - POST /pedidos/pagamento → processa pagamento, valida estoque e atualiza; cancela automaticamente se faltou estoque
  - GET /pedidos/me → lista pedidos do usuário autenticado (createdBy)
- Analytics (requer JWT)
  - GET /analytics/top-usuarios?limit=5 → top usuários por gasto total
  - GET /analytics/avg-ticket → ticket médio por usuário
  - GET /analytics/receita-por-mes → receita agregada por mês

Observações de negócio:
- Pagamento com estoque insuficiente: pedido é salvo/cancelado e retorna 400 (EstoqueInsuficienteException)
- Alteração de preço do produto recalcula automaticamente o valorTotal dos pedidos que o contêm


## 💻 Executar localmente (sem Docker)
Pré-requisitos: JDK 21 e Maven, além de um MySQL acessível.

1. Configure seu MySQL local (ou remoto) e crie o schema ecommerce_db
2. Ajuste src/main/resources/application.properties para apontar para seu MySQL, por exemplo:
   - spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   - spring.datasource.username=seu_usuario
   - spring.datasource.password=sua_senha
3. Rodar:
   - mvn clean install
   - mvn spring-boot:run
A aplicação subirá em http://localhost:8081


## 🧰 Troubleshooting
- Public Key Retrieval is not allowed (MySQL)
  - Incluímos allowPublicKeyRetrieval=true no JDBC URL do compose; use o mesmo em dev local
- ECONNRESET ao chamar /auth/ping em Docker
  - Garanta que a imagem foi reconstruída (docker compose up --build) e que a aplicação está expondo 8081
- Falha ao iniciar por mapeamentos JPA
  - O projeto já está ajustado para Records (Produto, User) e Classe (Pedido) com colunas e relacionamentos compatíveis
- Dados sumindo entre restarts
  - ddl-auto=create-drop é intencional em dev; altere para update se quiser persistir dados


## 📂 Estrutura do projeto (resumo)
- src/main/java/com/milton/ecommercefour
  - controller/ (Auth, Produto, Pedido, Analytics)
  - service/ (interfaces + impls)
  - repository/
  - domain/ (Pedido, Produto, User, Status)
  - config/ (Security/JWT)
  - exception/ (handlers e exceções personalizadas)
- src/main/resources/application.properties
- compose.yaml
- Dockerfile
- pom.xml


## 🤝 Contribuição
Contribuições são bem-vindas! Abra uma issue ou envie um PR.

## 📜 Licença
Uso livre para fins educacionais e de demonstração. Ajuste conforme a política da sua organização.