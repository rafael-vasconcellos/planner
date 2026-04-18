# Sobre

projeto do [NLW](https://www.figma.com/community/file/1392276515495389646) de 2024

> O projeto Journey tem como objetivo ajudar o usuário a organizar viagens à trabalho ou lazer. O usuário pode criar uma viagem com nome, data de início e fim. Dentro da viagem o usuário pode planejar sua viagem adicionando atividades para realizar em cada dia.
> 

## Requisitos

### Requisitos funcionais

1. O usuário cadastra uma viagem informando o local de destino, data de início, data de término, e-mails dos convidados e também seu nome completo e endereço de e-mail;
2. O criador da viagem recebe um e-mail para confirmar a nova viagem através de um link. Ao clicar no link, a viagem é confirmada, os convidados recebem e-mails de confirmação de presença e o criador é redirecionado para a página da viagem;
3. Os convidados, ao clicarem no link de confirmação de presença, são redirecionados para a aplicação onde devem inserir seu nome (além do e-mail que já estará preenchido) e então estarão confirmados na viagem;
4. Na página do evento, os participantes da viagem podem adicionar links importantes da viagem como reserva do AirBnB, locais para serem visitados, etc...
5. Ainda na página do evento, o criador e os convidados podem adicionar atividades que irão ocorrer durante a viagem com título, data e horário;
6. Novos participantes podem ser convidados dentro da página do evento através do e-mail e assim devem passar pelo fluxo de confirmação como qualquer outro convidado


## fluxo
**formulário 1:** destino e datas  
**formulário 2:** convidados (apenas email)  
**formulário 3:** seu nome e email  
**formulário 4:** confirmar viagem  

**1.** convidados recebem email e então confirmam sua presença escrevendo seu nome.  
**2.** é possível alterar a viagem, cadastrar atividades e adicionar links relevantes a viagem.  

## todo

SAST & SCA ✅  
DAST  
use docker ✅  
enhance error handling  
add API documentation ✅  
enhance tests ✅  
switch to postgresSQL❓  

## Documentação da API

A API do Planner segue o padrão RESTful. Todas as datas devem estar no formato ISO 8601 (ex: `2024-12-25T10:30:00`).

### Base URL
```
http://localhost:8080
```

---

## Endpoints

### 🚗 Trips (Viagens)

#### Criar uma nova viagem
**POST** `/trips`

Cria uma nova viagem com os detalhes fornecidos e registra os convidados.

**Request Body:**
```json
{
  "destination": "string",      // Local de destino (obrigatório)
  "ownerEmail": "string",       // Email do criador (obrigatório)
  "ownerName": "string",        // Nome do criador (obrigatório)
  "startsAt": "string",         // Data/hora de início (formato ISO 8601)
  "endsAt": "string",           // Data/hora de término (formato ISO 8601)
  "invites": ["string"]         // Array com emails dos convidados
}
```

**Response (201 - OK):**
```json
{
  "id": "uuid",
  "destination": "string",
  "ownerName": "string",
  "ownerEmail": "string",
  "startsAt": "2024-12-25T10:30:00",
  "endsAt": "2024-12-30T18:00:00",
  "isConfirmed": true
}
```

**Exemplo:**
```bash
curl -X POST http://localhost:8080/trips \
  -H "Content-Type: application/json" \
  -d '{
    "destination": "Rio de Janeiro",
    "ownerEmail": "joao@email.com",
    "ownerName": "João Silva",
    "startsAt": "2024-12-25T10:30:00",
    "endsAt": "2024-12-30T18:00:00",
    "invites": ["maria@email.com", "pedro@email.com"]
  }'
```

---

#### Obter detalhes de uma viagem
**GET** `/trips/{tripId}`

Recupera os detalhes completos de uma viagem específica.

**Parâmetros:**
- `tripId` (path) - UUID da viagem

**Response (200 - OK):**
```json
{
  "id": "uuid",
  "destination": "string",
  "ownerName": "string",
  "ownerEmail": "string",
  "startsAt": "2024-12-25T10:30:00",
  "endsAt": "2024-12-30T18:00:00",
  "isConfirmed": true
}
```

**Respostas possíveis:**
- `200 OK` - Viagem encontrada
- `404 Not Found` - Viagem não existe

**Exemplo:**
```bash
curl http://localhost:8080/trips/123e4567-e89b-12d3-a456-426614174000
```

---

#### Atualizar uma viagem
**PUT** `/trips/{tripId}`

Atualiza os detalhes de uma viagem existente. Todos os campos são opcionais.

**Parâmetros:**
- `tripId` (path) - UUID da viagem

**Request Body (todos opcionais):**
```json
{
  "destination": "string",
  "ownerName": "string",
  "ownerEmail": "string",
  "startsAt": "string",
  "endsAt": "string"
}
```

**Response (200 - OK):**
```json
{
  "id": "uuid",
  "destination": "string",
  "ownerName": "string",
  "ownerEmail": "string",
  "startsAt": "2024-12-25T10:30:00",
  "endsAt": "2024-12-30T18:00:00",
  "isConfirmed": true
}
```

**Respostas possíveis:**
- `200 OK` - Viagem atualizada
- `404 Not Found` - Viagem não existe

**Exemplo:**
```bash
curl -X PUT http://localhost:8080/trips/123e4567-e89b-12d3-a456-426614174000 \
  -H "Content-Type: application/json" \
  -d '{
    "destination": "São Paulo",
    "startsAt": "2024-12-20T08:00:00"
  }'
```

---

#### Listar viagens de um usuário
**GET** `/trips`

Lista todas as viagens criadas por um proprietário específico.

**Parâmetros:**
- `email` (query) - Email do proprietário da viagem (obrigatório)

**Response (200 - OK):**
```json
[
  {
    "id": "uuid",
    "destination": "string",
    "ownerName": "string",
    "ownerEmail": "string",
    "startsAt": "2024-12-25T10:30:00",
    "endsAt": "2024-12-30T18:00:00",
    "isConfirmed": true
  }
]
```

**Exemplo:**
```bash
curl "http://localhost:8080/trips?email=joao@email.com"
```

---

### 👥 Participants (Participantes)

#### Confirmar participação na viagem
**PUT** `/participants/{participantId}/confirm`

Confirma a presença de um participante e registra seu nome.

**Parâmetros:**
- `participantId` (path) - UUID do participante

**Request Body:**
```json
{
  "name": "string"  // Nome do participante (obrigatório)
}
```

**Response (200 - OK):**
```json
{
  "id": "uuid",
  "name": "string",
  "email": "string",
  "isConfirmed": true
}
```

**Exemplo:**
```bash
curl -X PUT http://localhost:8080/participants/123e4567-e89b-12d3-a456-426614174000/confirm \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Maria Silva"
  }'
```

---

#### Remover um participante
**DELETE** `/participants/{participantId}`

Remove um participante da viagem.

**Parâmetros:**
- `participantId` (path) - UUID do participante

**Response (200 - OK):**
```
(sem conteúdo)
```

**Exemplo:**
```bash
curl -X DELETE http://localhost:8080/participants/123e4567-e89b-12d3-a456-426614174000
```

---

### ✏️ Trip Participants (Gerenciar Participantes da Viagem)

#### Convidar novo participante
**POST** `/trips/{tripId}/participants/invite`

Envia um convite para um novo participante.

**Parâmetros:**
- `tripId` (path) - UUID da viagem

**Request Body:**
```json
{
  "email": "string"  // Email do novo participante (obrigatório)
}
```

**Response (200 - OK):**
```json
{
  "id": "uuid",
  "name": null,
  "email": "string",
  "isConfirmed": false
}
```

**Respostas possíveis:**
- `200 OK` - Participante convidado
- `404 Not Found` - Viagem não existe

**Exemplo:**
```bash
curl -X POST http://localhost:8080/trips/123e4567-e89b-12d3-a456-426614174000/participants/invite \
  -H "Content-Type: application/json" \
  -d '{
    "email": "carlos@email.com"
  }'
```

---

#### Listar participantes de uma viagem
**GET** `/trips/{tripId}/participants`

Retorna todos os participantes de uma viagem.

**Parâmetros:**
- `tripId` (path) - UUID da viagem

**Response (200 - OK):**
```json
[
  {
    "id": "uuid",
    "name": "string",
    "email": "string",
    "isConfirmed": true
  }
]
```

**Respostas possíveis:**
- `200 OK` - Lista de participantes retornada
- `404 Not Found` - Viagem não existe

**Exemplo:**
```bash
curl http://localhost:8080/trips/123e4567-e89b-12d3-a456-426614174000/participants
```

---

### 📅 Activities (Atividades)

#### Criar atividade
**POST** `/trips/{tripId}/activities`

Cria uma nova atividade para um dia específico da viagem.

**Parâmetros:**
- `tripId` (path) - UUID da viagem

**Request Body:**
```json
{
  "title": "string",     // Título da atividade (obrigatório)
  "occursAt": "string"   // Data/hora da atividade (ISO 8601, obrigatório)
}
```

**Response (200 - OK):**
```json
{
  "id": "uuid",
  "title": "string",
  "occursAt": "2024-12-25T14:30:00"
}
```

**Respostas possíveis:**
- `200 OK` - Atividade criada
- `404 Not Found` - Viagem não existe

**Exemplo:**
```bash
curl -X POST http://localhost:8080/trips/123e4567-e89b-12d3-a456-426614174000/activities \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Visitar Cristo Redentor",
    "occursAt": "2024-12-25T10:00:00"
  }'
```

---

#### Listar atividades de uma viagem
**GET** `/trips/{tripId}/activities`

Retorna todas as atividades planejadas para uma viagem.

**Parâmetros:**
- `tripId` (path) - UUID da viagem

**Response (200 - OK):**
```json
[
  {
    "id": "uuid",
    "title": "string",
    "occursAt": "2024-12-25T14:30:00"
  }
]
```

**Respostas possíveis:**
- `200 OK` - Lista de atividades retornada
- `404 Not Found` - Viagem não existe

**Exemplo:**
```bash
curl http://localhost:8080/trips/123e4567-e89b-12d3-a456-426614174000/activities
```

---

#### Remover atividade
**DELETE** `/activities/{activityId}`

Remove uma atividade planejada.

**Parâmetros:**
- `activityId` (path) - UUID da atividade

**Response (200 - OK):**
```
(sem conteúdo)
```

**Exemplo:**
```bash
curl -X DELETE http://localhost:8080/activities/123e4567-e89b-12d3-a456-426614174000
```

---

### 🔗 Links (Links)

#### Adicionar link
**POST** `/trips/{tripId}/links`

Adiciona um link importante relacionado à viagem (ex: AirBnB, locais de interesse).

**Parâmetros:**
- `tripId` (path) - UUID da viagem

**Request Body:**
```json
{
  "title": "string",  // Título/descrição do link (obrigatório)
  "url": "string"     // URL do link (obrigatório)
}
```

**Response (200 - OK):**
```json
{
  "id": "uuid",
  "title": "string",
  "url": "string"
}
```

**Respostas possíveis:**
- `200 OK` - Link adicionado
- `404 Not Found` - Viagem não existe

**Exemplo:**
```bash
curl -X POST http://localhost:8080/trips/123e4567-e89b-12d3-a456-426614174000/links \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Hospedagem no Airbnb",
    "url": "https://airbnb.com/rooms/123456"
  }'
```

---

#### Listar links de uma viagem
**GET** `/trips/{tripId}/links`

Retorna todos os links associados a uma viagem.

**Parâmetros:**
- `tripId` (path) - UUID da viagem

**Response (200 - OK):**
```json
[
  {
    "id": "uuid",
    "title": "string",
    "url": "string"
  }
]
```

**Respostas possíveis:**
- `200 OK` - Lista de links retornada
- `404 Not Found` - Viagem não existe

**Exemplo:**
```bash
curl http://localhost:8080/trips/123e4567-e89b-12d3-a456-426614174000/links
```

---

## Códigos de Status HTTP

| Código | Significado |
|--------|-------------|
| `200` | OK - Requisição bem-sucedida |
| `201` | Created - Recurso criado com sucesso |
| `204` | No Content - Sucesso sem conteúdo na resposta |
| `400` | Bad Request - Dados inválidos ou ausentes |
| `404` | Not Found - Recurso não encontrado |
| `500` | Internal Server Error - Erro no servidor |

---

## to study
flyway  
