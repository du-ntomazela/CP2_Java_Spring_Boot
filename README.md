# Estoque CP — Spring Boot

## Integrantes
- **Eduardo Tomazela** — rm556807  
- **Léo Masago** — rm557768  
- **Luiz Henrique** — rm555235  

---

## Resumo
Evolução do projeto da CP1 migrado para Spring Boot. O sistema expõe APIs REST para gerenciamento de **Alimentos** e **Materiais**, integradas ao Oracle via Spring Data JPA. Inclui validações com Spring Validation, regras de negócio adicionais, tratamento de erros centralizado e retorno consistente de códigos HTTP.

## Tecnologias
- Java 17  
- Spring Boot 3.5.x (Web, Validation, Data JPA)  
- Oracle JDBC (ojdbc11)  
- HikariCP  
- Logback/SLF4J  
- Postman (coleção + environment)

---

## Arquitetura e organização

```
br.com.restaurante.estoque_cp
├─ EstoqueCpApplication.java
├─ config
│  └─ GlobalExceptionHandler.java
├─ Controller
│  ├─ AlimentoController.java
│  └─ MaterialController.java
├─ Service
│  ├─ AlimentoService.java
│  └─ MaterialService.java
├─ Repositories
│  ├─ AlimentoRepository.java
│  └─ MaterialRepository.java
└─ model
   └─ entidades
      ├─ Alimento.java
      └─ Material.java
```

- **Controller**: endpoints REST e status HTTP.  
- **Service**: regras de negócio e orquestração.  
- **Repository**: persistência com Spring Data JPA.  
- **Config**: tratamento centralizado de exceções.

---

## Entidades

### Alimento
- `id` (Long, PK, auto)  
- `name` (String, obrigatório, ≤ 100)  
- `fornecedor` (String, obrigatório)  
- `data_de_validade` (Date, obrigatório)  
- `quantidade` (int, obrigatório, ≥ 1)

### Material
- `material_id` (Long, PK, auto)  
- `name` (String, obrigatório)  
- `quantidade` (int, obrigatório, ≥ 1)

Validações com Jakarta/Spring Validation; violações retornam **400 Bad Request**.

---

## Endpoints

### Base URL
- Local: `http://localhost:8080`  
- Prefixos  
  - Alimentos: `/api/v1/estoque/alimentos`  
  - Materiais: `/api/v1/estoque/materiais`

### Alimentos

1. **Criar** — `POST /api/v1/estoque/alimentos`  
   **Body**
   ```json
   {
     "name": "Arroz Agulhinha",
     "fornecedor": "Fornecedor X",
     "data_de_validade": "2025-12-31",
     "quantidade": 50
   }
   ```
   **Respostas**: 201 | 400

2. **Listar com filtro opcional** — `GET /api/v1/estoque/alimentos?nome=Arroz`  
   **Respostas**: 200

3. **Buscar por ID** — `GET /api/v1/estoque/alimentos/{id}`  
   **Respostas**: 200 | 404

4. **Atualizar** — `PUT /api/v1/estoque/alimentos/{id}`  
   **Body**: igual ao de criação  
   **Respostas**: 200 | 400 | 404

5. **Deletar** — `DELETE /api/v1/estoque/alimentos/{id}`  
   **Respostas**: 204 | 404

6. **Consumir (diminuir quantidade)** — `PUT /api/v1/estoque/alimentos/{id}/consumir`  
   **Body**
   ```json
   { "quantidade": 5 }
   ```
   **Regras**: quantidade > 0, não pode ficar negativo  
   **Respostas**: 200 | 400 | 404

7. **Verificar validade e remover vencidos** — `DELETE /api/v1/estoque/alimentos/verificar_validade/{data}`  
   **Exemplo**: `/api/v1/estoque/alimentos/verificar_validade/2025-10-20`  
   **Respostas**: 204 | 400

### Materiais

1. **Criar** — `POST /api/v1/estoque/materiais`  
   **Body**
   ```json
   { "name": "Guardanapo", "quantidade": 200 }
   ```
   **Respostas**: 201 | 400

2. **Listar com filtro opcional** — `GET /api/v1/estoque/materiais?nome=Guar`  
   **Respostas**: 200

3. **Buscar por ID** — `GET /api/v1/estoque/materiais/{id}`  
   **Respostas**: 200 | 404

4. **Atualizar** — `PUT /api/v1/estoque/materiais/{id}`  
   **Respostas**: 200 | 400 | 404

5. **Deletar** — `DELETE /api/v1/estoque/materiais/{id}`  
   **Respostas**: 204 | 404

6. **Consumir (diminuir quantidade)** — `PUT /api/v1/estoque/materiais/{id}/consumir`  
   **Body**
   ```json
   { "quantidade": 10 }
   ```
   **Respostas**: 200 | 400 | 404

---

## Exemplos cURL

**Criar alimento**
```bash
curl -i -X POST "http://localhost:8080/api/v1/estoque/alimentos"   -H "Content-Type: application/json"   -d '{
    "name": "Feijão Preto",
    "fornecedor": "Fornecedor Z",
    "data_de_validade": "2026-01-15",
    "quantidade": 30
  }'
```

**Consumir alimento**
```bash
curl -i -X PUT "http://localhost:8080/api/v1/estoque/alimentos/1/consumir"   -H "Content-Type: application/json"   -d '{ "quantidade": 3 }'
```

**Verificar validade**
```bash
curl -i -X DELETE "http://localhost:8080/api/v1/estoque/alimentos/verificar_validade/2025-10-20"
```

---

## Regras de negócio implementadas
1. Cadastro e atualização de Alimentos e Materiais com validação.  
2. Consumo de estoque (redução controlada, sem valores negativos).  
3. Remoção por validade (alimentos com data anterior à informada são excluídos).

---

## Integração com banco de dados

**Configuração (`src/main/resources/application.properties`)**
```properties
spring.application.name=estoque_CP
spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.username=<USUARIO>
spring.datasource.password=<SENHA>
spring.jpa.hibernate.ddl-auto=update
```

**Observações**
- `ddl-auto=update` para desenvolvimento.  
- HikariCP como pool.  
- Logs indicam inicialização do Tomcat e conexão Oracle.

---

## Validações
- Anotações nas entidades (`@NotBlank`, `@Size`, etc.).  
- Em violações, retorna **400 Bad Request** com corpo padronizado pelo `GlobalExceptionHandler` (timestamp, status, erro e lista de campos com mensagens).

---

## Tratamento de erros e códigos HTTP

**Estratégia (GlobalExceptionHandler)**
- **400**: validação, JSON malformado, parâmetros inválidos.  
- **404**: recurso não encontrado (ID).  
- **409**: conflitos de integridade.  
- **500**: erros inesperados.

**Motivação**
- **Prós**: respostas consistentes, melhor DX, menos duplicação nos controllers.  
- **Contras**: exige manutenção contínua para novos cenários.

**Locais de retorno**
- **APIs**: 201 criação, 200 leitura/atualização, 204 deleção.  
- **Validações de negócio**: 400 (ex.: consumo inválido).  
- **Erros de aplicação**: 500.  
- **Demais**: 404 (IDs ausentes), 415 (Content-Type incorreto).

---

## Postman

**Environment sugerido**
```json
{
  "name": "Estoque-Local",
  "values": [
    { "key": "baseUrl", "value": "http://localhost:8080", "enabled": true }
  ]
}
```

Use `{{baseUrl}}` em todos os requests.  
Inclua testes para status, header `Content-Type: application/json` e presença de `id` no corpo de criação/atualização.

---

## Como executar

**Pré-requisitos**
- Java 17  
- Maven 3.9+  
- Oracle acessível e credenciais válidas

**Build/Run**
```bash
mvn clean package
mvn spring-boot:run
```

**Smoke test**
```
GET http://localhost:8080/api/v1/estoque/materiais
```

---

## Boas práticas aplicadas
- Nomeação clara de pacotes, classes e variáveis.  
- Separação de camadas (Controller/Service/Repository).  
- Respostas HTTP explícitas e consistentes.  
- Filtro por query param (`nome`) com valor opcional.  
- Logs padrão do Spring.

---

## Pontos de melhoria (futuros)
- DTOs de request/response.  
- Paginação e ordenação nos GETs.  
- Autenticação/autorização (Spring Security).  
- Observabilidade (Micrometer/Prometheus).  
- Documentação OpenAPI/Swagger.

---

## Checklist da entrega
- Controllers com endpoints seguindo padrões. ✅  
- Regras de negócio mínimas (2 entidades, 3 funcionalidades) e exception handler customizado. ✅  
- Integração com banco via Spring Data. ✅  
- Validações com Spring Validation. ✅  
- Boas práticas e padrões de projeto. ✅  
- Tratamento robusto de erros e códigos HTTP, com justificativa. ✅

---

## Formato da entrega
- **Repositório Git** do projeto com README e instruções de execução.  
- **Arquivo .zip** com o código anexado no Teams.
