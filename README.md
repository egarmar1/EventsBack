# Proyecto de Microservicios para Gestión de Eventos

## Tabla de Contenidos

- [Proyecto de Microservicios para Gestión de Eventos](#proyecto-de-microservicios-para-gestión-de-eventos)
  - [Descripción del Proyecto](#descripción-del-proyecto)
  - [Arquitectura](#arquitectura)
  - [Estructura de los Microservicios](#estructura-de-los-microservicios)

---

## Descripción del Proyecto

Este proyecto presenta una aplicación de eventos, permite a vendedores crear eventos , y por otra parte da la posiblidad a clientes de realizar reservas en estos eventos. La aplicación está desarrollada con Spring Boot y sigue un diseño de microservicios para garantizar escalabilidad y modularidad.

Las tecnologías utilizadas se encuentran en la [Arquitectura](#arquitectura)

---

## Arquitectura


- **[Gateway API](https://github.com/egarmar1/EventsBack/tree/main/gatewayserver)**: Controla el enrutamiento y la entrada de tráfico mediante Spring Cloud Gateway.
- **[Eureka Service Discovery](https://github.com/egarmar1/EventsBack/tree/main/eurekaserver)**: Permite el descubrimiento dinámico de microservicios.
- **[OpenFeign](https://github.com/egarmar1/EventsBack/tree/24862a68a488de658a99fe1643f4fb1f5d6ee857/bookings/src/main/java/com/kike/events/bookings/service/client)**: Utilizado para la comunicación síncrona entre microservicios. Usado en varios microservicios: `Suscriptinons, Events, EventsHistory, notifications ...` 

- **[Kafka/ Spring Cloud Stream](https://github.com/egarmar1/EventsBack/blob/24862a68a488de658a99fe1643f4fb1f5d6ee857/message/src/main/java/com/kike/message/functions/MessageFunctions.java)**: Comunicación asíncrona usada para enviar emails al usuario con el qr, emails de sus suscripciones... Esto es gracias a Spring Cloud Stream, que permite producir mensajes y subirlos a un topic, para después desde el consumidor obtenerlos con un Consumer<T>

- **Keycloak**: IAM utilizado para la gestión de usuarios, contraseñas, roles ... y toda la autorización y autenticación
  
- **OAuth 2.0 y JWT**: Toda la seguridad ha sido cubierta con Oauth2.0, usando el **Authorization Code Grant Type**, y con el uso de JWTs proporcionados por Keycloak para la autorización.
  
- **[Docker y Docker Compose](https://github.com/egarmar1/EventsBack/tree/main/docker-compose)**: También se ha hace uso de docker y docker Compose para la contenerización de los microservicios
- **[Grafana, Loki, prometheus y tempo](https://github.com/egarmar1/EventsBack/tree/main/docker-compose/observability)**: Estas herramientas han proporcionado monitoreo de métricas(prometheus), gestión de logs(loki), y tracing (Tempo). Y todo esto es visible y monitorizado gracias a grafana.
- **[OpenAPI](https://egarmar1.github.io/Documentar-APIs-en-Spring-Boot-con-OpenAPI/#)**: Estas herramientas han proporcionado monitoreo de métricas(prometheus), gestión de logs(loki), y tracing (Tempo). Y todo esto es visible y monitorizado gracias a grafana.
  


---

## Estructura de los Microservicios

Cada microservicio está diseñado para manejar una funcionalidad específica:

1. **User Service**: Contendrá cierta información adicional, ya que la autenticación e información relevante se encuentra en keycloak, aquí se almacena también el tipo de usuario (cliente o vendedor).
2. **Events Service**: Creación y gestión de eventos.
3. **Bookings Service**: Gestión de reservas para los eventos.
4. **Subscriptions Service**: Permite a los usuarios suscribirse a vendedores específicos para recibir notificaciones sobre sus nuevos eventos.
5. **EventsHistory Service**: Información sobre el historial de los eventos, a los clientes se les mostrará el historial de los eventos acudidos, y a los vendedores el historial de eventos creados
6. **Messaging Service**: Envío de correos electrónicos.
7. **Notifications Service**: Actúa como intermediario entre los microservicios y el servicio de mensajería, gestionando qué mensajes deben enviarse y cuándo.
8. **Gateway Server**: Actúa como el API gateway de la aplicación
9. **Eureka Server**: Utilizado para el descubrimiento de microservicios

