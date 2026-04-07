# 🛒 E-commerce-api

API REST de e-commerce desenvolvida com Spring Boot 3, focada em segurança, organização em camadas e boas práticas de mercado, incluindo autenticação com JWT, gestão de produtos, carrinho e processamento de pedidos.

---

## 🚀 Tecnologias

O projeto foi desenvolvido utilizando as seguintes tecnologias:

* **Linguagem:** Java 21+
* **Framework:** Spring Boot 3
* **Conversão de DTO's:** Map Struct
* **Persistência:** Spring Data JPA / MySQL
* **Testes:** JUnit / Mockito
* **Containerização:** Docker & Docker Compose
* **Documentação:** Swagger (OpenAPI 3)
* **Segurança:** Spring Security & JWT, autenticação com base em ROLES
* **Cache:** Armazenamento de responses da camada de service no REDIS
* **Geração de código boilerplate:** Lombok para getters e setters e construtores

---

## 🛠️ Funcionalidades Principais

* 🔐 **Autenticação Segura:** Cadastro e login de usuários utilizando tokens JWT
* 📦 **Catálogo de Produtos:** Gestão completa de produtos e categorias
* 🛒 **Carrinho Dinâmico:** Fluxo de adição, remoção e atualização de itens
* 💳 **Checkout & Pedidos:** Processamento completo de pedidos

---

## 🏗️ Arquitetura e Design

A API foi estruturada seguindo o padrão de camadas, garantindo a separação de responsabilidades e facilitando a manutenção:

* **Controller:** Exposição dos endpoints REST e tratamento de requisições
* **Service:** Camada de regras de negócio e validações
* **Repository:** Interface de comunicação com o banco de dados via Spring Data JPA
* **DTO (Data Transfer Object):** Segurança e performance no tráfego de dados

---

## 🏁 Como Executar o Projeto

### 📋 Pré-requisitos

Antes de começar, você precisará ter instalado em sua máquina:

* Java 21+
* Docker & Docker Compose
* Maven (Opcional, se usar o Docker)

---

### 🔧 Instalação

Clone o repositório:

```bash
git clone https://github.com/FilipeMateusMS/e-commerce.git
```

Acesse a pasta do projeto:

```bash
cd e-commerce-api
```

Suba o ambiente (Banco de Dados + Aplicação + Redis) via Docker:

```bash
docker-compose up -d
```

---

## 📖 Documentação da API

Após subir o projeto, você pode visualizar e testar todos os endpoints através do Swagger:

* 🔗 **Swagger UI:** http://localhost:8080/swagger-ui.html

---
# Rotas

## Authentication
| Método | Endpoint                | Auth | Role | Descrição                             |
| ------ | ----------------------- | ---- | ---- | ------------------------------------- |
| POST   | `/api/v1/auth/login`    | ❌    | —    | Autentica usuário e retorna token JWT |
| POST   | `/api/v1/auth/register` | ❌    | —    | Registra um novo usuário no sistema   |

## Carrinho
| Método | Endpoint                 | Auth | Role        | Descrição                           |
| ------ | ------------------------ | ---- | ----------- | ----------------------------------- |
| GET    | `/api/v1/carrinhos/{id}` | ✅    | USER, ADMIN | Busca um carrinho pelo ID           |
| GET    | `/api/v1/carrinhos`      | ✅    | ADMIN       | Lista todos os carrinhos (paginado) |
| DELETE | `/api/v1/carrinhos/{id}` | ✅    | USER, ADMIN | Remove um carrinho pelo ID          |

## Carrinho Itens
| Método | Endpoint                       | Auth | Role        | Descrição                                              |
| ------ | ------------------------------ | ---- | ----------- | ------------------------------------------------------ |
| POST   | `/api/v1/carrinhos/itens`      | ✅    | USER, ADMIN | Adiciona um item ao carrinho ou atualiza se já existir |
| PUT    | `/api/v1/carrinhos/itens/{id}` | ✅    | USER, ADMIN | Altera a quantidade de um item no carrinho             |
| DELETE | `/api/v1/carrinhos/itens/{id}` | ✅    | USER, ADMIN | Remove um item do carrinho                             |

## Categorias
| Método | Endpoint                                                | Auth | Role  | Descrição                                          |
| ------ | ------------------------------------------------------- | ---- | ----- | -------------------------------------------------- |
| GET    | `/api/v1/categorias/{id}`                               | ❌    | —     | Busca uma categoria pelo ID                        |
| GET    | `/api/v1/categorias`                                    | ❌    | —     | Lista categorias (com filtro por nome e paginação) |
| POST   | `/api/v1/categorias`                                    | ✅    | ADMIN | Cria uma nova categoria                            |
| PUT    | `/api/v1/categorias/{id}`                               | ✅    | ADMIN | Atualiza uma categoria                             |
| POST   | `/api/v1/categorias/{categoriaId}/produtos/{produtoId}` | ✅    | ADMIN | Associa um produto a uma categoria                 |
| DELETE | `/api/v1/categorias/{id}`                               | ✅    | ADMIN | Remove uma categoria                               |

Query params disponíveis em GET /api/v1/categorias
- nome → filtra categorias contendo o nome informado
- page → número da página
- size → quantidade de registros
- sort → campo de ordenação

## Imagem de um produto
| Método | Endpoint                               | Auth | Role  | Descrição                                |
| ------ | -------------------------------------- | ---- | ----- | ---------------------------------------- |
| GET    | `/api/v1/imagens/produtos/{idProduto}` | ❌    | —     | Lista imagens de um produto (paginado)   |
| GET    | `/api/v1/imagens`                      | ❌    | —     | Lista todas as imagens (paginado)        |
| GET    | `/api/v1/imagens/{idImagem}`           | ❌    | —     | Faz download de uma imagem               |
| POST   | `/api/v1/imagens/produtos/{idProduto}` | ✅    | ADMIN | Faz upload de uma imagem para um produto |
| PUT    | `/api/v1/imagens/{idImagem}`           | ✅    | ADMIN | Atualiza uma imagem                      |
| DELETE | `/api/v1/imagens/{idImagem}`           | ✅    | ADMIN | Remove uma imagem                     

## Pedidos
| Método | Endpoint                            | Auth | Role        | Descrição                                       |
| ------ | ----------------------------------- | ---- | ----------- | ----------------------------------------------- |
| GET    | `/api/v1/pedidos/{id}`              | ✅    | USER, ADMIN | Busca um pedido pelo ID                         |
| GET    | `/api/v1/pedidos/usuario`           | ✅    | USER, ADMIN | Lista pedidos do usuário autenticado (paginado) |
| POST   | `/api/v1/pedidos/usuario/finalizar` | ✅    | USER, ADMIN | Finaliza o pedido do carrinho atual             |
| PATCH  | `/api/v1/pedidos/{idPedido}`        | ✅    | ADMIN       | Altera o status de um pedido                    |

## Produtos
| Método | Endpoint                | Auth | Role  | Descrição                              |
| ------ | ----------------------- | ---- | ----- | -------------------------------------- |
| GET    | `/api/v1/produtos/{id}` | ❌    | —     | Busca um produto pelo ID               |
| GET    | `/api/v1/produtos`      | ❌    | —     | Lista produtos com filtros e paginação |
| POST   | `/api/v1/produtos`      | ✅    | ADMIN | Cria um novo produto                   |
| DELETE | `/api/v1/produtos/{id}` | ✅    | ADMIN | Remove um produto                      |
| PUT    | `/api/v1/produtos/{id}` | ✅    | ADMIN | Atualiza um produto                    |

## Usuários
| Método | Endpoint                | Auth | Role        | Descrição                          |
| ------ | ----------------------- | ---- |-------------| ---------------------------------- |
| GET    | `/api/v1/usuarios/{id}` | ✅    | USER, ADMIN | Busca um usuário pelo ID           |
| GET    | `/api/v1/usuarios`      | ✅    | ADMIN       | Lista todos os usuários (paginado) |
| POST   | `/api/v1/usuarios`      | ✅    | ADMIN       | Cria um novo usuário               |
| PUT    | `/api/v1/usuarios/{id}` | ✅    | USER, ADMIN | Atualiza os dados de um usuário    |
| DELETE | `/api/v1/usuarios/{id}` | ✅    | ADMIN       | Remove um usuário                  |

---

# Modelagem

<img width="884" height="545" alt="image" src="https://github.com/user-attachments/assets/ff3d802c-1d6a-495f-996c-732b679d5ab6" />

