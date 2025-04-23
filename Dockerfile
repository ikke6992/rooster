FROM alpine:latest

# Install dependencies
RUN apk add --no-cache \
    openjdk21-jdk \
    nodejs \
    npm \
    nginx \
    supervisor \
    bash

# Setup working directory
WORKDIR /app

# Copy frontend and build it
COPY frontend/ ./frontend/
WORKDIR /app/frontend
RUN npm install --legacy-peer-deps && npm run build

# Copy built frontend to Nginx
COPY --from=builder /rooster/dist/frontend/browser /usr/share/nginx/html/

# Copy backend
WORKDIR /app
COPY rooster-0.0.1-SNAPSHOT.jar /app/rooster.jar

# Copy Nginx and supervisord config
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY supervisord.conf /etc/supervisord.conf

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]