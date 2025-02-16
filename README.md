# DCNI_SUM_KAFKA
DCNI_SUM_KAFKA

# Ejecutar docker compose
docker-compose up -d

# Ejecutar docker compose por nombre
sudo docker compose -f docker-compose-kafka-pro-cons.yml up

# Limpiar todo de docker compose
docker-compose down
docker system prune -f

# URL kafka
http://172.200.161.74:8090/

# Traer imagenes desde Docker hub

# Docker hub repos
espanderlof/kafka_productor_alertas
espanderlof/kafka_consumidor_alertas
espanderlof/kafka_productor_svitales
espanderlof/kafka_consumidor_svitales
espanderlof/kafka_generador_svitales

1.1 en caso de actualizar el contenedor realizar un pull primero  
docker pull espanderlof/kafka_productor_alertas:latest
docker pull espanderlof/kafka_consumidor_alertas:latest
docker pull espanderlof/kafka_productor_svitales:latest
docker pull espanderlof/kafka_consumidor_svitales:latest
docker pull espanderlof/kafka_generador_svitales:latest

1.2 eliminar el contenedor actual
    docker rm kafka_productor_alertas
    docker rm kafka_consumidor_alertas
    docker rm kafka_productor_svitales
    docker rm kafka_consumidor_svitales
    docker rm kafka_generador_svitales

1.3 volver a levandar el contenedor
sudo docker run -d -p 8092:8092 --name kafka_productor_alertas espanderlof/kafka_productor_alertas:latest
sudo docker run -d -p 8094:8094 --name kafka_consumidor_alertas espanderlof/kafka_consumidor_alertas:latest
sudo docker run -d -p 8091:8091 --name kafka_productor_svitales espanderlof/kafka_productor_svitales:latest
sudo docker run -d -p 8093:8093 --name kafka_consumidor_svitales espanderlof/kafka_consumidor_svitales:latest
sudo docker run -d -p 8095:8095 --name kafka_generador_svitales espanderlof/kafka_generador_svitales:latest


# levantar contenedores
sudo docker start kafka_productor_alertas kafka_consumidor_alertas
sudo docker start kafka_productor_svitales kafka_consumidor_svitales
sudo docker start kafka_generador_svitales

# ver log tiempo real contenedor
docker logs -f kafka_productor_alertas
docker logs -f kafka_consumidor_alertas
docker logs -f kafka_productor_svitales
docker logs -f kafka_consumidor_svitales
docker logs -f kafka_generador_svitales

# detener todos los contenedores
docker stop $(docker ps -a -q)

# detener un contenedor
docker stop kafka_generador_svitales