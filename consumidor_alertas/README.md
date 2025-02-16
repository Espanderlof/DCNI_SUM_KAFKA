# DCNI_SUM_KAFKA - CONSUMIDOR ALERTAS
DESARROLLO CLOUD NATIVE I - SUMATIVA

# Iniciar Springboot
mvn spring-boot:run

# Limpia y recompila el proyecto
mvn clean install

# Levantar contenedor Docker
docker build -t kafka_consumidor_alertas .
docker run --name kafka_consumidor_alertas -p 8094:8094 kafka_consumidor_alertas

# DockerHub
1. Crear repo en https://hub.docker.com/
2. Primero, asegúrate de estar logueado en Docker Hub desde tu terminal
    docker login
3. Identifica tu imagen local. Puedes ver tus imágenes locales con:
    docker images
4. Etiqueta tu imagen local con el formato requerido por Docker Hub:
    Por ejemplo, si tu imagen local se llama "backend-app:1.0", los comandos serían:
    docker tag kafka_consumidor_alertas espanderlof/kafka_consumidor_alertas:latest
    docker push espanderlof/kafka_consumidor_alertas:latest