## Настройка базы данных

Для работы приложения необходимо иметь установленный PostgreSQL. После установки выполните следующие шаги:

1. Откройте терминал или командную строку и войдите в интерактивный терминал PostgreSQL:

   bash psql -U имя_пользователя

2. Создайте базу данных для проекта:

   sql CREATE DATABASE crypto_exchange;

3. Настройте файл `application.properties` в вашем проекте, указав параметры подключения к базе данных:

   # application.properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/crypto_exchange
    spring.datasource.username=имя_пользователя
    spring.datasource.password=пароль
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true

   Замените `ваше_имя_пользователя` и `ваш_пароль` на ваши реальные данные доступа к базе данных.
