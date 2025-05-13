#!/bin/bash

# Ubicación del proyecto - ajusta esta ruta
PROJECT_DIR="$(pwd)"
BACKEND_DIR="${PROJECT_DIR}"


# Iniciar los contenedores
echo "Iniciando contenedores con Docker Compose..."
cd $BACKEND_DIR
docker-compose down
docker-compose build
docker-compose up -d

echo "¡Despliegue completado!"
echo "Recuerda configurar Apache2 y Certbot manualmente en el servidor."