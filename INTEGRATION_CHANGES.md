# Mudanças de Integração Front-end e Back-end

Este documento descreve as principais mudanças implementadas para integrar o front-end (React) com o back-end (Spring Boot) da aplicação NutriTrack.

## 1. Gerenciamento Dinâmico de ID de Usuário

Para garantir que cada usuário interaja apenas com seus próprios dados, o gerenciamento de ID de usuário foi tornado dinâmico.

- **Decodificação de Token JWT:** A biblioteca `jwt-decode` foi adicionada ao front-end para decodificar o token JWT recebido após o login.
- **Armazenamento Local:** Após o login, o ID e o nome do usuário são extraídos do token e armazenados no `localStorage` do navegador.
- **Uso Dinâmico do ID:** O ID do usuário armazenado é agora utilizado em todas as chamadas de API que requerem identificação do usuário (por exemplo, ao buscar, criar, atualizar ou deletar refeições), eliminando a necessidade de IDs codificados.

## 2. Implementação do CRUD de Refeições

As funcionalidades de criar, ler, atualizar e deletar (CRUD) refeições foram totalmente integradas com o back-end.

- **Criação (`Create`):** A função `addSelection` em `Foods.tsx` foi atualizada para enviar uma requisição `POST` para o back-end, criando uma nova refeição no banco de dados.
- **Leitura (`Read`):** Ao carregar a página `Foods`, uma requisição `GET` é feita para buscar as refeições do usuário para o dia atual.
- **Atualização (`Update`):**
  - Um novo endpoint `PUT /api/v1/refeicoes/{idUser}/{id}` foi adicionado ao `RefeicaoController` no back-end.
  - O `RefeicaoService` e o `RefeicaoMapper` foram atualizados para incluir a lógica de atualização.
  - A função `saveMeal` no front-end agora envia uma requisição `PUT` para salvar as alterações feitas em uma refeição.
- **Deleção (`Delete`):** A função `removeItem` no front-end foi implementada para enviar uma requisição `DELETE` ao back-end, removendo um item de uma refeição.

## 3. Tratamento de Carregamento e Erros

Para melhorar a experiência do usuário, a interface agora fornece feedback visual durante as chamadas de API.

- **Estados de Carregamento e Erro:** Novos estados (`loading` e `error`) foram adicionados ao componente `Foods.tsx`.
- **Feedback Visual:**
  - Um indicador de "Carregando..." é exibido durante as chamadas de API.
  - Mensagens de erro são mostradas na tela caso ocorra algum problema na comunicação com o back-end.

## 4. Pesquisa de Alimentos no Back-end

A funcionalidade de pesquisa de alimentos foi otimizada para ser executada no back-end.

- **Busca por Parâmetro:** O front-end agora envia o termo de pesquisa como um parâmetro na chamada `GET /api/v1/alimentos`. Isso permite que o back-end filtre os resultados e retorne apenas os alimentos relevantes, em vez de enviar a lista completa para o cliente, melhorando a eficiência da aplicação.

## 5. Configuração de CORS

Para permitir a comunicação entre o front-end e o back-end (que rodam em portas diferentes), o Cross-Origin Resource Sharing (CORS) foi configurado no back-end.

- **`WebConfig`:** Uma classe `WebConfig` foi adicionada ao back-end para permitir requisições do endereço do front-end (`http://localhost:5173`), garantindo que o navegador não bloqueie as chamadas de API.
