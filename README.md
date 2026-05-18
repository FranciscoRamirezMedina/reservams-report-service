\# ReservaMS - Report Service



\## Descripcion



Este microservicio administra reportes simples del sistema ReservaMS.



En esta version guarda un historial de reportes generados por usuarios administradores.



\## Responsabilidades



\- Crear registros de reportes.

\- Listar reportes generados.

\- Buscar reportes por ID.

\- Buscar reportes por tipo.

\- Buscar reportes por usuario.

\- Generar reportes basicos de reservas, pagos y ocupacion.



\## Puerto



8090



\## Base de datos



reservams\_report\_db



\## Endpoints principales



\- GET /api/v1/reports/logs

\- GET /api/v1/reports/logs/{id}

\- GET /api/v1/reports/logs/type/{reportType}

\- GET /api/v1/reports/logs/user/{userId}

\- POST /api/v1/reports/logs

\- POST /api/v1/reports/reservations-by-hotel/{hotelId}/user/{userId}

\- POST /api/v1/reports/payments-by-date/{date}/user/{userId}

\- POST /api/v1/reports/hotel-occupancy/{hotelId}/user/{userId}



\## Ejecucion



1\. Crear la base de datos reservams\_report\_db.

2\. Ejecutar el script SQL ubicado en la carpeta database.

3\. Levantar Eureka Server.

4\. Ejecutar el report-service.

5\. Probar los endpoints desde Postman o desde el API Gateway.



