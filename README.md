# Verzel Events — Plataforma de Eventos e Ingressos

Desafio técnico Elite Dev. Plataforma onde um organizador publica eventos a partir de um catálogo externo, e clientes reservam, pagam (simulado) e recebem ingressos com QR code assinado, validados na portaria na entrada do evento.

> **Status do projeto**: aplicação completa e funcional — back-end (autenticação, catálogo, eventos, reservas com trava de concorrência, pagamento simulado, ingressos com QR, validação na portaria) e front-end (login, home, detalhe do evento com mapa de assentos, pagamento, meus ingressos, painel do organizador e tela de portaria), todos testados manualmente ponta a ponta.

---

## Stack

- **Back-end**: Java 21, Spring Boot 4.1.0, Maven
- **Front-end**: React (JavaScript) + Vite, Tailwind CSS (via `@theme`, sem `tailwind.config.js`), React Router, Context API para autenticação, Framer Motion para animações
- **Banco de dados**: PostgreSQL 16 (via Docker Compose)
- **Autenticação**: JWT (jjwt), três papéis (Cliente, Organizador, Portaria)
- **API externa**: TMDb (The Movie Database) — catálogo de filmes para criação de eventos

---

## Como rodar o projeto

### Pré-requisitos
- Java 21+
- Maven (ou use o `mvnw` incluso no projeto)
- Node.js 18+ e npm
- Docker Desktop
- Uma API key gratuita do [TMDb](https://www.themoviedb.org/) (Configurações > API > API Read Access Token)

### 1. Subir o banco de dados

```bash
docker compose up -d
```

Isso sobe um container Postgres 16 já com o banco `eventos_db` criado, na porta padrão `5432`.

### 2. Configurar variáveis de ambiente (back-end)

Copie o arquivo de exemplo:

```bash
cp application-example.properties src/main/resources/application.properties
```

Edite `src/main/resources/application.properties` e preencha:

```properties
app.jwt.secret=<gere uma chave aleatória — veja o comando abaixo>
app.qr.secret=<gere outra chave aleatória, diferente da anterior>
app.tmdb.api-key=<seu token do TMDb>
```

Para gerar uma chave aleatória segura (PowerShell):
```powershell
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

Ou (Linux/Mac):
```bash
openssl rand -base64 32
```

### 3. Rodar o back-end

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. Na primeira execução, o `DataSeeder` popula o banco automaticamente com os dados de teste (veja abaixo).
A documentação interativa da API estará disponível em: **http://localhost:8080/swagger-ui.html**

### 4. Rodar o front-end

O front-end está em um repositório separado. As instruções acima são apenas para o back-end.
A aplicação sobe em `http://localhost:5173` e já consome a API em `http://localhost:8080`.


## Dados de teste (seed)

Criados automaticamente na primeira inicialização com o banco vazio:

| Papel | Email | Senha |
|---|---|---|
| Organizador | `organizador@verzel.com` | `123456` |
| Cliente 1 | `cliente1@verzel.com` | `123456` |
| Cliente 2 | `cliente2@verzel.com` | `123456` |
| Portaria | `portaria@verzel.com` | `123456` |

Também é criado um evento publicado ("Matrix Resurrections", Cinema, Sala 1 - Cine Verzel, R$ 35,00, 10 assentos A1-A10 livres) para permitir testar o fluxo completo sem precisar montar dados manualmente.


## Documentação da API (Swagger)

Após iniciar a aplicação, acesse a documentação interativa e completa da API no Swagger UI:

**http://localhost:8080/swagger-ui.html**

Lá você poderá ver todos os endpoints, seus parâmetros, corpos de requisição/resposta e testá-los diretamente. Para usar os endpoints protegidos, clique no botão "Authorize" e insira seu token JWT no formato `Bearer <seu_token>`.


## Decisões técnicas e trade-offs

Esta seção documenta escolhas que podem parecer não-óbvias numa leitura rápida do código, mas foram deliberadas.

**Chave primária UUID em vez de Long sequencial**
Evita que IDs previsíveis (1, 2, 3...) enfraqueçam a segurança de rotas que expõem recursos por ID, especialmente o ingresso.

**Um único `Usuario` com campo `role`, sem herança JPA**
Cliente, Organizador e Portaria são o mesmo registro no banco, diferenciados só pelo papel. Optei por não usar herança de entidade (`@Inheritance`) porque, para três papéis com poucos atributos exclusivos, a complexidade de mapear `JOINED` ou `SINGLE_TABLE` não se paga — a diferenciação de comportamento fica na camada de `service`, que é onde ela realmente pertence.

**Lock otimista (`@Version`) em vez de lock pessimista para reserva de assento**
O cenário de conflito real (dois clientes reservando o mesmo assento no mesmo instante) é raro. Lock otimista evita o custo de performance e o risco de deadlock do lock pessimista (`SELECT FOR UPDATE`), e ainda garante a integridade: se dois clientes tentarem reservar o mesmo assento, o segundo recebe um erro `409 Conflict` claro, sem sobrescrever a reserva do primeiro. Testado manualmente com sucesso.

**QR code com HMAC-SHA256, não um UUID simples**
O conteúdo do QR (`reservaId:hash`) é assinado com uma chave secreta do servidor. A portaria recalcula o hash a partir da reserva e do evento e compara — se não bater, o QR foi forjado ou alterado. Isso atende ao requisito de "ingresso que não possa ser forjado" sem exigir infraestrutura de certificado digital.

**Chave JWT e chave do QR versionadas fora do código**
Ambas ficam em `application.properties`, que está no `.gitignore`. Um `application-example.properties` com placeholders é versionado, com instruções de como gerar as chaves reais. Isso evita expor segredos no repositório público, mas exige uma etapa extra de configuração antes de rodar (documentada acima).
 
**TTL de reserva com liberação automática**
Reservas pendentes de pagamento expiram após 5 minutos. Um job agendado (`@Scheduled`, a cada 60 segundos) libera automaticamente o assento de reservas expiradas, evitando que assentos fiquem "presos" indefinidamente por clientes que abandonam o checkout.

**Log de auditoria da portaria registra toda tentativa, inclusive fraudes**
Cada chamada a `/portaria/validar` grava um registro em `validacoes`, mesmo quando o código não corresponde a nenhum ingresso real. Isso permite reconstruir o histórico completo de tentativas de acesso a um evento, incluindo tentativas de fraude.

**Idempotência na criação de reserva**
`POST /cliente/reservas` aceita uma chave de idempotência opcional; se uma reserva já existir com a mesma chave, ela é retornada em vez de criar uma nova. Isso evita reservas duplicadas em caso de reenvio da requisição (ex.: duplo clique, retry de rede).

**API externa escolhida: TMDb**
Optei por TMDb em vez de Ticketmaster Discovery por ter uma integração mais simples (sem necessidade de lidar com preços/localização vindos da API, já que esses dados são definidos pelo organizador na criação do evento).



## Limitações conhecidas

- Testes automatizados não implementados devido ao prazo do desafio.
- Painel avançado do organizador (métricas de vendas, disponibilidade de assentos em tempo real, cancelamento de evento) não implementado — apenas criação e listagem de eventos.
- Deploy (Render + Neon + Vercel) planejado mas não realizado até esta versão do README; instruções acima cobrem apenas execução local.