FROM node:alpine AS builder

WORKDIR /rooster

COPY . .

RUN npm install --legacy-peer-deps && npm run build


FROM nginx:alpine

ENV NGINX_PORT=${NGINX_PORT}
ENV API_URL=${API_URL}

COPY --from=builder /rooster/dist/frontend/browser /usr/share/nginx/html/
COPY nginx.template /etc/nginx/nginx.template

CMD export NGINX_PORT=${NGINX_PORT:-80} && \
    envsubst '${API_URL}' < /usr/share/nginx/html/assets/env.template.js > /usr/share/nginx/html/assets/env.js && \
    envsubst '${NGINX_PORT}' < /etc/nginx/nginx.template > /etc/nginx/conf.d/default.conf && \
    exec nginx -g 'daemon off;'
