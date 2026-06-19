# Sistema de Comercio Eletronico

Projeto desenvolvido para a disciplina de Programacao Orientada a Objetos (POO)
do curso de EISC - 2o Ano, 2o Semestre - Universidade do Mindelo.

## Descricao

Sistema completo de e-commerce com gestao de produtos, usuarios, carrinho de compras,
pedidos e relatorios. Desenvolvido em Java com interface grafica Swing e banco de
dados SQLite.

## Conceitos de POO Aplicados

| Conceito | Aplicacao |
|----------|-----------|
| **Encapsulamento** | Atributos privados com validacao nos setters |
| **Heranca** | Classe abstrata Pessoa -> Usuario |
| **Polimorfismo** | Metodo abstrato getTipo() implementado nas subclasses |
| **Composicao** | Carrinho contem ItemCarrinho; Pedido contem ItemPedido |
| **Agregacao** | Carrinho/Pedido referenciam Usuario |
| **Enums** | StatusPedido, CategoriaProduto, TipoUsuario |
| **Excecoes** | Excecoes customizadas para cada regra de negocio |
| **Interfaces** | Comparable em Produto para ordenacao |

## Estrutura do Projeto

```
src/main/java/com/ecommerce/
├── Main.java                    # Ponto de entrada
├── enums/                       # Enumeracoes
├── exception/                   # Excecoes customizadas
├── model/                       # Entidades (POJO)
│   ├── Pessoa.java              # Classe abstrata
│   ├── Usuario.java             # Herda Pessoa
│   ├── Produto.java             # Implementa Comparable
│   ├── ItemCarrinho.java        # Composicao com Produto
│   ├── Carrinho.java            # Composicao com ItemCarrinho
│   ├── ItemPedido.java          # Item imutavel do pedido
│   └── Pedido.java              # Composicao com ItemPedido
├── dao/                         # Data Access Objects
│   ├── ConexaoBD.java           # Singleton de conexao
│   ├── ProdutoDAO.java          # CRUD Produto
│   ├── UsuarioDAO.java          # CRUD Usuario
│   └── PedidoDAO.java           # CRUD Pedido
├── service/                     # Logica de negocio
│   ├── ProdutoService.java
│   ├── UsuarioService.java
│   ├── CarrinhoService.java
│   ├── PedidoService.java
│   └── RelatorioService.java
├── controller/                  # Controladores
│   ├── ProdutoController.java
│   ├── UsuarioController.java
│   ├── CarrinhoController.java
│   └── PedidoController.java
├── view/                        # Interface grafica (Swing)
│   ├── TelaLogin.java
│   ├── TelaCadastroUsuario.java
│   ├── TelaPrincipal.java
│   ├── TelaProdutos.java
│   ├── TelaCarrinho.java
│   ├── TelaPedidos.java
│   ├── TelaAdmin.java
│   └── TelaRelatorios.java
├── security/                    # Seguranca
│   └── Seguranca.java           # Hash BCrypt
└── util/                        # Utilitarios
    ├── ValidadorUtil.java
    └── FormatoUtil.java
```

## Como Executar

### Requisitos
- Java JDK 17+
- Maven 3.8+

### Compilar
```bash
mvn clean install
```

### Executar
```bash
mvn exec:java -Dexec.mainClass="com.ecommerce.Main"
```

Ou execute o JAR:
```bash
java -jar target/sistema-ecommerce-1.0.0.jar
```

## Credenciais Padrao

| Perfil | Email | Senha |
|--------|-------|-------|
| Administrador | admin@ecommerce.cv | admin123 |

## Autor

Desenvolvido para a disciplina de Programacao Orientada a Objetos
Universidade do Mindelo - Cabo Verde
