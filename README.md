# API Clientes

API REST para gerenciamento de clientes, desenvolvida em Java com Spring Boot. Permite criar, listar, buscar, atualizar e remover clientes, com persistência em PostgreSQL.

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- Spring Validation
- PostgreSQL
- Docker / Docker Compose
- Maven

## Pré-requisitos

- JDK 21
- Maven 3.9+ (ou usar o wrapper `./mvnw`)
- Docker e Docker Compose

## Como executar

### 1. Subir o banco de dados

O projeto usa PostgreSQL via Docker Compose:

```bash
docker compose up -d
```

Isso sobe um container Postgres na porta `5432` com o banco `clientes` (usuário `postgres`, senha `postgres`).

### 2. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação sobe por padrão em `http://localhost:8080`.

> As tabelas são criadas/atualizadas automaticamente pelo Hibernate (`ddl-auto: update`), configurado em `src/main/resources/application.yaml`.

## Endpoints

Base path: `/clientes`

| Método | Endpoint         | Descrição                          |
|--------|------------------|-------------------------------------|
| GET    | `/clientes`      | Lista todos os clientes             |
| GET    | `/clientes/{id}` | Busca um cliente pelo ID            |
| POST   | `/clientes`      | Cria um novo cliente                |
| PUT    | `/clientes/{id}` | Atualiza um cliente existente       |
| DELETE | `/clientes/{id}` | Remove um cliente                   |

### Exemplo — Criar cliente

**Request** `POST /clientes`
```json
{
  "name": "Maria Silva",
  "email": "maria.silva@email.com"
}
```

**Response** `201 Created`
```json
{
  "id": 1,
  "name": "Maria Silva",
  "email": "maria.silva@email.com"
}
```

### Exemplo — Atualizar cliente

**Request** `PUT /clientes/1`
```json
{
  "name": "Maria S. Silva",
  "email": "maria.s@email.com"
}
```

### Regras de validação

- `name`: obrigatório, não pode ser vazio.
- `email`: obrigatório, precisa ser um e-mail válido e único (não pode já estar cadastrado por outro cliente).

## Estrutura do projeto

```
src/main/java/api_clientes
├── ApiClientesApplication.java     # Classe principal (entry point)
├── controllers
│   └── ClienteController.java      # Endpoints REST
├── services
│   └── ClienteService.java         # Regras de negócio
├── repository
│   └── ClienteRepository.java      # Acesso a dados (Spring Data JPA)
├── model
│   └── Cliente.java                # Entidade JPA
└── dto
    ├── ClienteDTO.java             # Retorno da API
    ├── ClienteCreateDTO.java       # Payload de criação
    └── ClienteUpdateDTO.java       # Payload de atualização
```

## Configuração

As configurações de conexão com o banco ficam em `src/main/resources/application.yaml`. Por padrão:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/clientes
    username: postgres
    password: postgres
```

> Para ambientes diferentes de desenvolvimento local, recomenda-se externalizar essas credenciais (variáveis de ambiente ou um `application-prod.yaml`).