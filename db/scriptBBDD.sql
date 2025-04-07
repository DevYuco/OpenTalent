-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS open_talent_db;
USE open_talent_db;

-- Tabla de Direcciones
CREATE TABLE Direcciones (
    id_direccion INT AUTO_INCREMENT PRIMARY KEY,
    calle VARCHAR(255),
    pais VARCHAR(100),
    codigo_postal VARCHAR(10),
    provincia VARCHAR(100),
    poblacion VARCHAR(100)
);

-- Tabla de Roles
CREATE TABLE Roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT
);

-- Tabla de Empresas (cif como clave primaria)
CREATE TABLE Empresas (
    cif VARCHAR(20) PRIMARY KEY, 
    nombre_empresa VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    email VARCHAR(255) UNIQUE NOT NULL,
    foto VARCHAR(255),
    foto_contenido VARCHAR(255),
    id_direccion INT,
    destacado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_direccion) REFERENCES Direcciones(id_direccion) ON DELETE SET NULL
);

-- Tabla de Usuarios (id_empresa cambiado a cif)
CREATE TABLE Usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150),
    email VARCHAR(255) UNIQUE NOT NULL,
    estudios TEXT,
    experiencia TEXT,
    CV VARCHAR(255),
    foto_perfil VARCHAR(255),
    telefono VARCHAR(20),
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fecha_alta DATE,
    fecha_nacimiento DATE,
    activo BOOLEAN DEFAULT TRUE,
    id_direccion INT,
    cif VARCHAR(20) NULL, 
    id_rol INT,
    FOREIGN KEY (id_direccion) REFERENCES Direcciones(id_direccion),
    FOREIGN KEY (cif) REFERENCES Empresas(cif) ON DELETE SET NULL,
    FOREIGN KEY (id_rol) REFERENCES Roles(id_rol) ON DELETE SET NULL
);

-- Tabla de Sectores
CREATE TABLE Sectores (
    id_sector INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) UNIQUE NOT NULL,
    descripcion TEXT
);

-- Tabla intermedia Empresas - Sectores (N:M) 
CREATE TABLE Empresa_Sector (
    cif VARCHAR(20),
    id_sector INT,
    PRIMARY KEY (cif, id_sector),
    FOREIGN KEY (cif) REFERENCES Empresas(cif) ON DELETE CASCADE,
    FOREIGN KEY (id_sector) REFERENCES Sectores(id_sector) ON DELETE CASCADE
);

-- Tabla de Ofertas 
CREATE TABLE Ofertas (
    id_oferta INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    fecha_inicio DATE,
    fecha_fin DATE,
    tipo_oferta ENUM('PRACTICAS', 'EMPLEO') NOT NULL,
    estado ENUM('ACTIVA', 'CERRADA', 'PENDIENTE') DEFAULT 'PENDIENTE',
    numero_plazas INT,
    cif VARCHAR(20) NULL,
    id_sector INT,
    foto_contenido VARCHAR(255),
    modalidad ENUM('PRESENCIAL', 'HIBRIDO', 'REMOTO') NOT NULL,
    FOREIGN KEY (cif) REFERENCES Empresas(cif) ON DELETE SET NULL,
    FOREIGN KEY (id_sector) REFERENCES Sectores(id_sector) ON DELETE SET NULL
);

-- Tabla intermedia Usuarios - Ofertas (N:M) (Aplicaciones a Ofertas)
CREATE TABLE Usuario_Oferta (
    id_usuario INT,
    id_oferta INT,
    fecha_aplicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('PENDIENTE', 'ACEPTADO', 'RECHAZADO') DEFAULT 'PENDIENTE',
    favorito BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id_usuario, id_oferta),
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_oferta) REFERENCES Ofertas(id_oferta) ON DELETE CASCADE
);

-- Tabla de Proyectos
CREATE TABLE Proyectos (
    id_proyecto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    descripcion TEXT,
    fecha_inicio DATE,
    fecha_fin DATE,
    foto VARCHAR(255),
    foto_contenido VARCHAR(255),
    plazas INT
);

-- Tabla intermedia Usuarios - Proyectos (N:M)
CREATE TABLE Usuario_Proyecto (
    id_usuario INT,
    id_proyecto INT,
    estado ENUM('PENDIENTE', 'ACEPTADO', 'RECHAZADO') NOT NULL DEFAULT 'PENDIENTE',
    favorito BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id_usuario, id_proyecto),
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_proyecto) REFERENCES Proyectos(id_proyecto) ON DELETE CASCADE
);

-- Tabla de Reseñas (Unificada para Empresas y Proyectos) 
CREATE TABLE Resennas (
    id_reseña INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    comentario TEXT,
    valoracion ENUM('POSITIVA', 'NEUTRAL', 'NEGATIVA') NOT NULL,
    puntuacion DECIMAL(2,1) CHECK (puntuacion BETWEEN 1.0 AND 5.0),
    id_usuario INT NOT NULL,
    cif VARCHAR(20) NULL,
    id_proyecto INT NULL,
    CHECK ((cif IS NOT NULL AND id_proyecto IS NULL) OR (id_proyecto IS NOT NULL AND cif IS NULL)),
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (cif) REFERENCES Empresas(cif) ON DELETE CASCADE,
    FOREIGN KEY (id_proyecto) REFERENCES Proyectos(id_proyecto) ON DELETE CASCADE
);
