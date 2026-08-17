# Verzel Events — Plataforma de Eventos e Ingressos

Desafio técnico Elite Dev (Verzel). Plataforma onde um organizador publica eventos a partir de um catálogo externo, e clientes reservam, pagam (simulado) e recebem ingressos com QR code assinado, validados na portaria na entrada do evento.

> **Status do projeto**: back-end funcional completo e testado (autenticação, catálogo, eventos, reservas com trava de concorrência, pagamento simulado, ingressos com QR, validação na portaria). Front-end em desenvolvimento — esta seção será atualizada conforme progride.

---

## Stack

- **Back-end**: Java 21, Spring Boot 4.1.0, Maven
- **Front-end**: React + Tailwind CSS *(em desenvolvimento)*
- **Banco de dados**: PostgreSQL 16 (via Docker Compose)
- **Autenticação**: JWT (jjwt), três papéis (Cliente, Organizador, Portaria)
- **API externa**: TMDb (The Movie Database) — catálogo de filmes para criação de eventos

---

## Como rodar o projeto

### Pré-requisitos
- Java 21+
- Maven (ou use o `mvnw` incluso no projeto)
- Docker Desktop
- Uma API key gratuita do [TMDb](https://www.themoviedb.org/) (Configurações > API > API Read Access Token)

### 1. Subir o banco de dados

```bash
docker compose up -d
```

Isso sobe um container Postgres 16 já com o banco `eventos_db` criado, na porta padrão `5432`.

### 2. Configurar variáveis de ambiente

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

### 3. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. Na primeira execução, o `DataSeeder` popula o banco automaticamente com os dados de teste (veja abaixo).

---

## Dados de teste (seed)

Criados automaticamente na primeira inicialização com o banco vazio:

| Papel | Email | Senha |
|---|---|---|
| Organizador | `organizador@verzel.com` | `123456` |
| Cliente 1 | `cliente1@verzel.com` | `123456` |
| Cliente 2 | `cliente2@verzel.com` | `123456` |
| Portaria | `portaria@verzel.com` | `123456` |

Também é criado um evento publicado ("Matrix Resurrections", 10 assentos disponíveis) para permitir testar o fluxo completo sem precisar montar dados manualmente.

---

## Principais endpoints da API

### Autenticação (públicos)
- `POST /auth/register` — cadastro (nome, email, senha, role)
- `POST /auth/login` — login, retorna JWT

### Eventos (públicos para leitura)
- `GET /eventos` — lista eventos publicados
- `GET /eventos/{id}` — detalhes de um evento

### Organizador (requer role ORGANIZADOR)
- `GET /organizador/eventos/catalogo?query=` — busca filmes no TMDb
- `POST /organizador/eventos` — cria evento a partir do catálogo
- `GET /organizador/eventos` — lista eventos do organizador logado

### Cliente (requer role CLIENTE)
- `POST /cliente/reservas` — reserva um assento
- `POST /cliente/reservas/{id}/pagamento` — processa pagamento simulado e emite o ingresso
- `GET /cliente/ingressos` — lista os ingressos do cliente logado

### Público (link compartilhado)
- `GET /tickets/share/{token}` — visualiza um ingresso compartilhado, sem necessidade de login

### Portaria (requer role PORTARIA)
- `POST /portaria/validar` — valida um ingresso (código manual ou QR), retorna `VALIDO`, `INVALIDO`, `JA_UTILIZADO` ou `EVENTO_ERRADO`
- `GET /portaria/eventos/{eventoId}/historico` — histórico de validações de um evento

---

## Decisões técnicas e trade-offs

Esta seção documenta escolhas que podem parecer não-óbvias numa leitura rápida do código, mas foram deliberadas.

**Chave primária UUID em vez de Long sequencial**
Evita que IDs previsíveis (1, 2, 3...) enfraqueçam a segurança de rotas que expõem recursos por ID, especialmente o ingresso.

**Um único `Usuario` com campo `role`, sem herança JPA**
Cliente, Organizador e Portaria são o mesmo registro no banco, diferenciados só pelo papel. Optei por não usar herança de entidade (`@Inheritance`) porque, para três papéis com poucos atributos exclusivos, a complexidade de mapear `JOINED` ou `SINGLE_TABLE` não se paga — a diferenciação de comportamento fica na camada de `service`, que é onde ela realmente pertence.

**Lock otimista (`@Version`) em vez de lock pessimista para reserva de assento**
O cenário de conflito real (dois clientes reservando o mesmo assento no mesmo instante) é raro. Lock otimista evita o custo de performance e o risco de deadlock do lock pessimista (`SELECT FOR UPDATE`), e ainda garante a integridade: se dois clientes tentarem reservar o mesmo assento, o segundo recebe um erro `409 Conflict` claro, sem sobrescrever a reserva do primeiro. Testado manualmente com sucesso.

**QR code com HMAC-SHA256, não um UUID simples**
O conteúdo do QR (`reservaId:hash`) é assinado com uma chave secreta do servidor. A portaria recalcula o hash e compara — se não bater, o QR foi forjado ou alterado. Isso atende ao requisito de "ingresso que não possa ser forjado" sem exigir infraestrutura de certificado digital.

**Chave JWT e chave do QR versionadas fora do código**
Ambas ficam em `application.properties`, que está no `.gitignore`. Um `application-example.properties` com placeholders é versionado, com instruções de como gerar as chaves reais. Isso evita expor segredos no repositório público, mas exige uma etapa extra de configuração antes de rodar (documentada acima).

**TTL de reserva com liberação automática**
Reservas pendentes de pagamento expiram após 5 minutos. Um job agendado (`@Scheduled`, a cada 60 segundos) libera automaticamente o assento de reservas expiradas, evitando que assentos fiquem "presos" indefinidamente por clientes que abandonam o checkout.

**Log de auditoria da portaria registra toda tentativa, inclusive fraudes**
Cada chamada a `/portaria/validar` grava um registro em `validacoes`, mesmo quando o código não corresponde a nenhum ingresso real. Isso permite reconstruir o histórico completo de tentativas de acesso a um evento, incluindo tentativas de fraude.

**API externa escolhida: TMDb**
Optei por TMDb em vez de Ticketmaster Discovery por ter uma integração mais simples (sem necessidade de lidar com preços/localização vindos da API, já que esses dados são definidos pelo organizador na criação do evento).

---

## Limitações conhecidas

- Front-end ainda em desenvolvimento no momento desta versão do README.
- Testes automatizados não implementados devido ao prazo do desafio.
- Painel avançado do organizador (métricas de vendas, disponibilidade de assentos em tempo real, cancelamento de evento) não implementado — apenas criação e listagem de eventos.
