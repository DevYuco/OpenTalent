-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS open_talent_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE open_talent_db;

-- Configurar collation a nivel de sesión
SET NAMES utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- Crear usuario de aplicación si no existe
CREATE USER IF NOT EXISTS 'opentalent_user'@'%' IDENTIFIED BY 'opentalent*1234';

-- Otorgar privilegios sobre la base de datos
GRANT ALL PRIVILEGES ON open_talent_db.* TO 'opentalent_user'@'%';

-- Aplicar cambios
FLUSH PRIVILEGES;