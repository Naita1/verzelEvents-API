# Verzel Events — Plataforma de Eventos e Ingressos

Desafio técnico Elite Dev (Verzel). Plataforma onde um organizador publica eventos a partir de um catálogo externo, e clientes reservam, pagam (simulado) e recebem ingressos com QR code assinado, validados na portaria na entrada do evento.

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

### 4. Rodar o front-end

```bash
cd frontend
npm install
npm run dev
```

A aplicação sobe em `http://localhost:5173` e já consome a API em `http://localhost:8080`.

---

## Dados de teste (seed)

Criados automaticamente na primeira inicialização com o banco vazio:

| Papel | Email | Senha |
|---|---|---|
| Organizador | `organizador@verzel.com` | `123456` |
| Cliente 1 | `cliente1@verzel.com` | `123456` |
| Cliente 2 | `cliente2@verzel.com` | `123456` |
| Portaria | `portaria@verzel.com` | `123456` |

Também é criado um evento publicado ("Matrix Resurrections", Cinema, Sala 1 - Cine Verzel, R$ 35,00, 10 assentos A1-A10 livres) para permitir testar o fluxo completo sem precisar montar dados manualmente.

---

## Principais endpoints da API

### Autenticação
- `POST /auth/register` — cadastro público (nome, email, senha) — sempre criado com role CLIENTE
- `POST /auth/login` — login, retorna JWT
- `POST /auth/staff` — criação de usuários ORGANIZADOR ou PORTARIA (não permite role CLIENTE, que deve usar `/auth/register`)

### Eventos (públicos para leitura)
- `GET /eventos` — lista eventos publicados
- `GET /eventos/{id}` — detalhes de um evento
- `GET /eventos/{id}/assentos` — lista os assentos de um evento e seus status (LIVRE, RESERVADO, VENDIDO)

### Organizador (requer role ORGANIZADOR)
- `GET /organizador/eventos/catalogo?query=` — busca filmes no TMDb
- `POST /organizador/eventos` — cria evento a partir do catálogo (título, tipo, data/hora, local, capacidade, preço e URL do pôster do filme selecionado)
- `GET /organizador/eventos` — lista eventos do organizador logado

### Cliente (requer role CLIENTE)
- `POST /cliente/reservas` — reserva um assento (aceita uma chave de idempotência opcional para evitar reservas duplicadas em reenvios)
- `POST /cliente/reservas/{id}/pagamento` — processa pagamento simulado e emite o ingresso
- `GET /cliente/ingressos` — lista os ingressos do cliente logado

### Público (link compartilhado)
- `GET /tickets/share/{token}` — visualiza um ingresso compartilhado, sem necessidade de login

### Portaria (requer role PORTARIA)
- `POST /portaria/validar` — valida um ingresso (código no formato `reservaId:qrHash`), retorna `VALIDO`, `INVALIDO`, `JA_UTILIZADO` ou `EVENTO_ERRADO`
- `GET /portaria/eventos/{eventoId}/historico` — histórico de validações de um evento (data, resultado e portaria responsável)

---

## Front-end

O front-end foi construído em React + Vite, consumindo diretamente a API acima:

- **Autenticação**: `AuthContext` guarda o usuário logado e o token JWT; rotas protegidas por role (`ProtectedRoute`) redirecionam conforme o papel do usuário.
- **Home**: lista os eventos publicados, com busca por texto e filtro por tipo.
- **Detalhe do evento**: mostra o mapa de assentos e permite seleção múltipla; a reserva de cada assento selecionado é enviada em paralelo, tratando falhas parciais (ex.: assento que acabou de ser reservado por outro cliente).
- **Pagamento**: formulário simulado de cartão (um cartão terminado em `0000` simula recusa da operadora); ao concluir, o ingresso é emitido com QR code e link de compartilhamento.
- **Meus Ingressos**: lista os ingressos do cliente, com visual de e-ticket.
- **Portaria**: valida ingressos por código e mostra o histórico de validações do evento.
- **Painel do Organizador**: busca no catálogo do TMDb, criação de eventos e listagem dos eventos já publicados.
- **Design**: tema escuro ("Cinema Dark Premium"), com acento laranja, glassmorphism, mapa de assentos estilo sala de cinema e cards de ingresso no estilo e-ticket; transições de página com Framer Motion.

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

---

## Uso de IA no desenvolvimento

Usei IA para auxiliar na construção do projeto — principalmente na configuração do Docker Compose e em partes específicas onde eu tinha menos domínio prévio — e não para gerar o projeto como um todo. As decisões de arquitetura, modelagem e as escolhas documentadas na seção acima foram minhas.

---

## Limitações conhecidas

- Testes automatizados não implementados devido ao prazo do desafio.
- Painel avançado do organizador (métricas de vendas, disponibilidade de assentos em tempo real, cancelamento de evento) não implementado — apenas criação e listagem de eventos.
- Deploy (Render + Neon + Vercel) planejado mas não realizado até esta versão do README; instruções acima cobrem apenas execução local.