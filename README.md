# AV5-WEBIII

APIs REST em Java/Spring Boot para a atividade AV5 do sistema AutoManager.

## Tecnologias usadas

- Java 17
- Spring Boot 2.7.18
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- Spring HATEOAS
- H2 Database
- Maven Wrapper

## Microservicos

| Pasta | Porta | Funcao |
| --- | ---: | --- |
| `clientes` | `8081` | Clientes por empresa |
| `funcionarios` | `8082` | Funcionarios por empresa |
| `produtos` | `8083` | Mercadorias e servicos por empresa |
| `vendas` | `8084` | Vendas por empresa e periodo |
| `veiculos` | `8085` | Veiculos por empresa e veiculos atendidos |

## Como rodar

Cada microservico e independente e deve ser rodado separadamente. Escolha uma das opcoes abaixo.

### Opcao 1: Maven Wrapper (linha de comando)

Entre na pasta do microservico desejado:

```bash
cd clientes
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

No Windows (PowerShell ou CMD):

```powershell
.\mvnw.cmd spring-boot:run
```

Para rodar outro microservico, troque `clientes` pela pasta desejada e repita o comando:

```bash
cd funcionarios
./mvnw spring-boot:run
```

### Opcao 2: pela IDE (IntelliJ, Eclipse, VS Code)

1. Abra cada microservico como um projeto Maven separado (ou importe todos como modulos, se a IDE suportar).
2. Aguarde a IDE baixar as dependencias do Maven.
3. Localize a classe principal da aplicacao (a anotada com `@SpringBootApplication`), geralmente em `src/main/java/.../*Application.java`.
4. Clique com o botao direito na classe e selecione **Run** (ou **Debug**).
5. Repita o processo para cada microservico que quiser executar, em janelas/instancias separadas.

> Cada microservico sobe em uma porta diferente (veja a tabela acima), entao varios podem rodar ao mesmo tempo sem conflito.

## Como testar

Dentro de cada pasta, execute:

```bash
./mvnw test
```

Ou, pela IDE, clique com o botao direito na pasta `src/test/java` e selecione **Run Tests**.

## Autenticacao

Todos os microservicos possuem login em:

```http
POST /auth/login
```

Exemplo de corpo:

```json
{
  "nomeUsuario": "usuario",
  "senha": "senha"
}
```

Use o token retornado nas proximas requisicoes:

```http
Authorization: Bearer seu-token
```

## Endpoints para teste

### Clientes - porta 8081

Base: `http://localhost:8081`

```http
POST /auth/login
GET /empresa/{empresaId}/clientes
GET /empresa/{empresaId}/clientes/{clienteId}
POST /empresa/{empresaId}/clientes
PUT /empresa/{empresaId}/clientes/{clienteId}
DELETE /empresa/{empresaId}/clientes/{clienteId}
```

### Funcionarios - porta 8082

Base: `http://localhost:8082`

```http
POST /auth/login
GET /empresa/{empresaId}/funcionarios
GET /empresa/{empresaId}/funcionarios/{funcionarioId}
POST /empresa/{empresaId}/funcionarios
PUT /empresa/{empresaId}/funcionarios/{funcionarioId}
DELETE /empresa/{empresaId}/funcionarios/{funcionarioId}
```

### Produtos e servicos - porta 8083

Base: `http://localhost:8083`

```http
POST /auth/login
GET /empresa/{empresaId}/produtos-servicos
GET /empresa/{empresaId}/produtos-servicos/mercadorias/{mercadoriaId}
POST /empresa/{empresaId}/produtos-servicos/mercadorias
PUT /empresa/{empresaId}/produtos-servicos/mercadorias/{mercadoriaId}
DELETE /empresa/{empresaId}/produtos-servicos/mercadorias/{mercadoriaId}
GET /empresa/{empresaId}/produtos-servicos/servicos/{servicoId}
POST /empresa/{empresaId}/produtos-servicos/servicos
PUT /empresa/{empresaId}/produtos-servicos/servicos/{servicoId}
DELETE /empresa/{empresaId}/produtos-servicos/servicos/{servicoId}
```

### Vendas - porta 8084

Base: `http://localhost:8084`

```http
POST /auth/login
GET /empresa/{empresaId}/vendas
GET /empresa/{empresaId}/vendas?dataInicio=2026-01-01&dataFim=2026-01-31
GET /empresa/{empresaId}/vendas/{vendaId}
POST /empresa/{empresaId}/vendas
PUT /empresa/{empresaId}/vendas/{vendaId}
DELETE /empresa/{empresaId}/vendas/{vendaId}
```

### Veiculos - porta 8085

Base: `http://localhost:8085`

```http
POST /auth/login
GET /empresa/{empresaId}/veiculos
GET /empresa/{empresaId}/veiculos/atendidos
GET /empresa/{empresaId}/veiculos/{veiculoId}
POST /empresa/{empresaId}/veiculos
PUT /empresa/{empresaId}/veiculos/{veiculoId}
DELETE /empresa/{empresaId}/veiculos/{veiculoId}
```

## Easter Egg: 18092022