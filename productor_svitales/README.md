# DCNI_SUM_KAFKA - PRODUCTOR SIGNOS VITALES
DESARROLLO CLOUD NATIVE I - SUMATIVA

# Iniciar Springboot
mvn spring-boot:run

# Limpia y recompila el proyecto
mvn clean install

# Levantar contenedor Docker
docker build -t kafka_productor_svitales .
docker run --name kafka_productor_svitales -p 8091:8091 kafka_productor_svitales

# DockerHub
1. Crear repo en https://hub.docker.com/
2. Primero, asegúrate de estar logueado en Docker Hub desde tu terminal
    docker login
3. Identifica tu imagen local. Puedes ver tus imágenes locales con:
    docker images
4. Etiqueta tu imagen local con el formato requerido por Docker Hub:
    Por ejemplo, si tu imagen local se llama "backend-app:1.0", los comandos serían:
    docker tag kafka_productor_svitales espanderlof/kafka_productor_svitales:latest
    docker push espanderlof/kafka_productor_svitales:latest