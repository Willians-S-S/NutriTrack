# ---------- STAGE 1: Build ----------
FROM node:22.12-alpine AS build
WORKDIR /app

# Cache de deps
COPY package*.json ./
RUN npm ci

# Código e build
COPY . .
RUN npm run build

# ---------- STAGE 2: Static server ----------
FROM nginx:alpine
# (opcional) remove config default de exemplo
RUN rm -f /etc/nginx/conf.d/default.conf

# Config Nginx simples p/ SPA (redireciona 404 para index.html)
# se você NÃO precisa de fallback de SPA, pode omitir isso.
RUN printf 'server {\n\
  listen 80;\n\
  server_name _;\n\
  root /usr/share/nginx/html;\n\
  index index.html;\n\
  location / {\n\
    try_files $uri $uri/ /index.html;\n\
  }\n\
  gzip on;\n\
  gzip_types text/plain text/css application/javascript application/json image/svg+xml;\n\
}\n' > /etc/nginx/conf.d/app.conf

# Copia artefatos do Vite
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
