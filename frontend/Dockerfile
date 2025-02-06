FROM node:alpine AS builder

WORKDIR /rooster

COPY . .

RUN npm install --legacy-peer-deps && npm run build --prod
    
FROM nginx:alpine

ENV NGINX_PORT=${NGINX_PORT}

COPY --from=builder /rooster/dist/frontend/browser /usr/share/nginx/html/
COPY nginx.template /etc/nginx/nginx.template

CMD export NGINX_PORT=${NGINX_PORT:-80} && \
    envsubst '${NGINX_PORT}' < /etc/nginx/nginx.template > /etc/nginx/conf.d/default.conf && \
    exec nginx -g 'daemon off;'
