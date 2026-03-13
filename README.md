Authentication
| Método | Endpoint                | Auth | Role | Descrição                             |
| ------ | ----------------------- | ---- | ---- | ------------------------------------- |
| POST   | `/api/v1/auth/login`    | ❌    | —    | Autentica usuário e retorna token JWT |
| POST   | `/api/v1/auth/register` | ❌    | —    | Registra um novo usuário no sistema   |

Carrinho
| Método | Endpoint                 | Auth | Role        | Descrição                           |
| ------ | ------------------------ | ---- | ----------- | ----------------------------------- |
| GET    | `/api/v1/carrinhos/{id}` | ✅    | USER, ADMIN | Busca um carrinho pelo ID           |
| GET    | `/api/v1/carrinhos`      | ✅    | ADMIN       | Lista todos os carrinhos (paginado) |
| DELETE | `/api/v1/carrinhos/{id}` | ✅    | USER, ADMIN | Remove um carrinho pelo ID          |

Carrinho Itens
| Método | Endpoint                       | Auth | Role        | Descrição                                              |
| ------ | ------------------------------ | ---- | ----------- | ------------------------------------------------------ |
| POST   | `/api/v1/carrinhos/itens`      | ✅    | USER, ADMIN | Adiciona um item ao carrinho ou atualiza se já existir |
| PUT    | `/api/v1/carrinhos/itens/{id}` | ✅    | USER, ADMIN | Altera a quantidade de um item no carrinho             |
| DELETE | `/api/v1/carrinhos/itens/{id}` | ✅    | USER, ADMIN | Remove um item do carrinho                             |

Categorias
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

Imagem de um produto
| Método | Endpoint                               | Auth | Role  | Descrição                                |
| ------ | -------------------------------------- | ---- | ----- | ---------------------------------------- |
| GET    | `/api/v1/imagens/produtos/{idProduto}` | ❌    | —     | Lista imagens de um produto (paginado)   |
| GET    | `/api/v1/imagens`                      | ❌    | —     | Lista todas as imagens (paginado)        |
| GET    | `/api/v1/imagens/{idImagem}`           | ❌    | —     | Faz download de uma imagem               |
| POST   | `/api/v1/imagens/produtos/{idProduto}` | ✅    | ADMIN | Faz upload de uma imagem para um produto |
| PUT    | `/api/v1/imagens/{idImagem}`           | ✅    | ADMIN | Atualiza uma imagem                      |
| DELETE | `/api/v1/imagens/{idImagem}`           | ✅    | ADMIN | Remove uma imagem                     

Pedidos
| Método | Endpoint                            | Auth | Role        | Descrição                                       |
| ------ | ----------------------------------- | ---- | ----------- | ----------------------------------------------- |
| GET    | `/api/v1/pedidos/{id}`              | ✅    | USER, ADMIN | Busca um pedido pelo ID                         |
| GET    | `/api/v1/pedidos/usuario`           | ✅    | USER, ADMIN | Lista pedidos do usuário autenticado (paginado) |
| POST   | `/api/v1/pedidos/usuario/finalizar` | ✅    | USER, ADMIN | Finaliza o pedido do carrinho atual             |
| PATCH  | `/api/v1/pedidos/{idPedido}`        | ✅    | ADMIN       | Altera o status de um pedido                    |

Produtos
| Método | Endpoint                | Auth | Role  | Descrição                              |
| ------ | ----------------------- | ---- | ----- | -------------------------------------- |
| GET    | `/api/v1/produtos/{id}` | ❌    | —     | Busca um produto pelo ID               |
| GET    | `/api/v1/produtos`      | ❌    | —     | Lista produtos com filtros e paginação |
| POST   | `/api/v1/produtos`      | ✅    | ADMIN | Cria um novo produto                   |
| DELETE | `/api/v1/produtos/{id}` | ✅    | ADMIN | Remove um produto                      |
| PUT    | `/api/v1/produtos/{id}` | ✅    | ADMIN | Atualiza um produto                    |

Usuários
| Método | Endpoint                | Auth | Role        | Descrição                          |
| ------ | ----------------------- | ---- | ----------- | ---------------------------------- |
| GET    | `/api/v1/usuarios/{id}` | ✅    | USER        | Busca um usuário pelo ID           |
| GET    | `/api/v1/usuarios`      | ✅    | ADMIN       | Lista todos os usuários (paginado) |
| POST   | `/api/v1/usuarios`      | ✅    | ADMIN       | Cria um novo usuário               |
| PUT    | `/api/v1/usuarios/{id}` | ✅    | USER, ADMIN | Atualiza os dados de um usuário    |
| DELETE | `/api/v1/usuarios/{id}` | ✅    | ADMIN       | Remove um usuário                  |

Modelagem

<img width="884" height="545" alt="image" src="https://github.com/user-attachments/assets/ff3d802c-1d6a-495f-996c-732b679d5ab6" />

