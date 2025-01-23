FROM node:alpine AS builder

WORKDIR /rooster

COPY . .

RUN npm install --legacy-peer-deps && npm run build
    
FROM nginx:alpine

COPY --from=builder /rooster/dist/frontend/browser /usr/share/nginx/html/
COPY /nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80