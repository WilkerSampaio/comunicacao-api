## Endpoints da API

### 1. Agendar Mensagem

- **Método HTTP**: `POST`
- **Caminho**: `/comunicacao/agendar`
- **Descrição**: Agenda uma mensagem para ser enviada a um destinatário no futuro.

### 2. Buscar Status da Mensagem

- **Método HTTP**: `GET`
- **Caminho**: `/comunicacao`
- **Descrição**: Retorna todos os dados sobre o status da comunicação enviada ao destinatário especificado.

### 3. Alterar Status para "Cancelado"

- **Método HTTP**: `PATCH`
- **Caminho**: `/comunicacao/cancelar`
- **Descrição**: Altera o status de uma comunicação para "cancelado", impedindo o envio da mensagem.

### 4. Enviar Mensagem Imediatamente

- **Método HTTP**: `POST`
- **Caminho**: `/comunicacao/mensagem`
- **Descrição**: Envia a mensagem imediatamente para o destinatário.
