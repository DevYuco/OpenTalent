USE open_talent_db;

-- 1. Insertar roles
INSERT INTO Roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('USUARIO', 'Usuario registrado'),
('EMPRESA', 'Empresa registrada');

-- 2. Insertar dirección genérica
INSERT INTO Direcciones (calle, pais, codigo_postal, provincia, poblacion) VALUES
('Calle Falsa 123', 'España', '28080', 'Madrid', 'Madrid');

-- 3. Insertar empresa dummy
INSERT INTO Empresas (cif, nombre_empresa, email, id_direccion) VALUES
('CIFEMPRESA1', 'Empresa Ejemplo S.L.', 'empresa@example.com', 1);

-- 4. Insertar usuarios

-- Usuarios con roles predefinidos
INSERT INTO Usuarios (nombre, apellidos, email, username, password, activo, id_direccion, cif, id_rol, foto_perfil) VALUES
('Admin', 'Root', 'admin@demo.com', 'admin', '{noop}1234', TRUE, 1, NULL, 1, 'https://images.unsplash.com/photo-1599566150163-29194dcaad36'),
('Usuario', 'Normal', 'usuario@demo.com', 'usuario', '{noop}1234', TRUE, 1, NULL, 2, 'https://images.unsplash.com/photo-1544723795-3fb6469f5b39'),
('Empresa', 'S.L.', 'empresauser@demo.com', 'empresa', '{noop}1234', TRUE, 1, 'CIFEMPRESA1', 3, 'https://images.unsplash.com/photo-1552374196-c4e7ffc6e126');

-- Direcciones adicionales
INSERT INTO Direcciones (calle, pais, codigo_postal, provincia, poblacion) VALUES
('Avenida Siempre Viva 742', 'España', '46001', 'Valencia', 'Valencia'),
('Calle Luna 55', 'España', '08001', 'Barcelona', 'Barcelona'),
('Calle Sol 45', 'España', '29001', 'Málaga', 'Málaga'),
('Calle Mar 88', 'España', '03001', 'Alicante', 'Alicante');

-- Empresas
INSERT INTO Empresas (cif, nombre_empresa, email, id_direccion, foto, foto_contenido, destacado, descripcion) VALUES
('A12345678', 'Tech Solutions', 'contacto@techsolutions.com', 1, 'https://images.unsplash.com/photo-1591696205602-2f950c417cb9', 'https://images.unsplash.com/photo-1581090700227-1e8d92875aa0', TRUE, 'Empresa dedicada a soluciones tecnológicas innovadoras.'),
('B87654321', 'InnovateX', 'info@innovatex.es', 2, 'https://images.unsplash.com/photo-1605902711622-cfb43c4437d1', 'https://images.unsplash.com/photo-1573164713988-8665fc963095', FALSE, 'Innovación y desarrollo de software a medida.'),
('C11223344', 'DataCorp', 'contacto@datacorp.com', 4, 'https://images.unsplash.com/photo-1629904853716-f0bc54eea481', 'https://images.unsplash.com/photo-1519389950473-47ba0277781c', TRUE, 'Servicios de análisis de datos y consultoría tecnológica.'),
('D55667788', 'GreenFuture', 'info@greenfuture.es', 5, 'https://images.unsplash.com/photo-1504198453319-5ce911bafcde', 'https://images.unsplash.com/photo-1469474968028-56623f02e42e', FALSE, 'Soluciones sostenibles y energías renovables.');
-- Sectores
INSERT INTO Sectores (nombre, descripcion) VALUES
('Tecnología', 'Sector de tecnología e informática'),
('Marketing', 'Publicidad y estrategia de marca'),
('Educación', 'Servicios educativos y formación'),
('Medio Ambiente', 'Ecología, sostenibilidad y energías renovables'),
('Recursos Humanos', 'Selección de personal y gestión del talento');

-- Empresa - Sector
INSERT INTO Empresa_Sector (cif, id_sector) VALUES
('A12345678', 1),
('A12345678', 2),
('B87654321', 2),
('B87654321', 3),
('C11223344', 1),
('C11223344', 4),
('D55667788', 4),
('D55667788', 5);

-- Usuarios con {noop} en passwords
INSERT INTO Usuarios (nombre, apellidos, email, estudios, experiencia, CV, foto_perfil, telefono, username, password, fecha_alta, fecha_nacimiento, activo, id_direccion, cif, id_rol) VALUES
('Laura', 'Gómez', 'laura.gomez@email.com', 'Grado en Informática', '2 años como desarrolladora', 'cv_laura.pdf', 'https://images.unsplash.com/photo-1531123897727-8f129e1688ce', '600111222', 'laurag', '{noop}1234pass', CURDATE(), '1995-04-15', TRUE, 1, 'A12345678', 2),
('Pedro', 'López', 'pedro.lopez@email.com', 'Máster en Marketing', '3 años en agencias', 'cv_pedro.pdf', 'https://images.unsplash.com/photo-1502767089025-6572583495b0', '600333444', 'pedrol', '{noop}securepass', CURDATE(), '1990-09-30', TRUE, 2, 'B87654321', 2),
('Marta', 'Sánchez', 'marta.sanchez@email.com', 'Ingeniería Ambiental', 'Voluntariados y proyectos verdes', 'cv_marta.pdf', 'https://images.unsplash.com/photo-1531891437562-4301cf35b7f4', '600444555', 'martas', '{noop}pass123', CURDATE(), '1993-07-20', TRUE, 4, 'D55667788', 2),
('Carlos', 'Ruiz', 'carlos.ruiz@email.com', 'Recursos Humanos', '5 años como recruiter', 'cv_carlos.pdf', 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde', '600777888', 'carlosr', '{noop}pass456', CURDATE(), '1988-01-10', TRUE, 5, 'C11223344', 2);

-- Ofertas
INSERT INTO Ofertas (titulo, descripcion, fecha_inicio, fecha_fin, tipo_oferta, estado, numero_plazas, cif, id_sector, foto_contenido, modalidad) VALUES
('Desarrollador Backend', 'Se busca perfil con experiencia en Node.js', '2025-04-01', '2025-06-01', 'EMPLEO', 'ACTIVA', 2, 'A12345678', 1, 'https://images.unsplash.com/photo-1555066931-4365d14bab8c', 'REMOTO'),
('Becario Marketing Digital', 'Puesto de prácticas para estudiantes de marketing', '2025-04-15', '2025-07-15', 'PRACTICAS', 'ACTIVA', 1, 'B87654321', 2, 'https://images.unsplash.com/photo-1590608897129-79da98d159d3', 'HIBRIDO'),
('Analista de Datos', 'Buscamos perfil junior en análisis de datos', '2025-05-01', '2025-08-01', 'EMPLEO', 'PENDIENTE', 1, 'C11223344', 1, 'https://images.unsplash.com/photo-1611974789855-9c2a0a6d03d1', 'PRESENCIAL'),
('Técnico Medio Ambiente', 'Colaboración en proyectos sostenibles', '2025-03-15', '2025-07-15', 'EMPLEO', 'ACTIVA', 2, 'D55667788', 4, 'https://images.unsplash.com/photo-1504198453319-5ce911bafcde', 'HIBRIDO');

-- Usuario - Oferta
INSERT INTO Usuario_Oferta (id_usuario, id_oferta, propietario, estado, favorito) VALUES
(1, 1, FALSE, 'PENDIENTE', TRUE),
(2, 2, FALSE, 'ACEPTADO', FALSE),
(3, 4, FALSE, 'PENDIENTE', TRUE),
(4, 3, FALSE, 'RECHAZADO', FALSE);

-- Proyectos
INSERT INTO Proyectos (nombre, activo, descripcion, fecha_inicio, fecha_fin, foto, foto_contenido, plazas) VALUES
('Proyecto IA', TRUE, 'Desarrollo de un modelo de inteligencia artificial', '2025-03-01', '2025-08-01', 'https://images.unsplash.com/photo-1581093588401-945c9fe6332f', 'https://images.unsplash.com/photo-1581093588233-0185b882f816', 5),
('Campaña Redes Sociales', TRUE, 'Planificación de estrategia en redes sociales', '2025-04-01', '2025-07-01', 'https://images.unsplash.com/photo-1504384308090-c894fdcc538d', 'https://images.unsplash.com/photo-1498050108023-c5249f4df085', 3),
('EcoInnovación', TRUE, 'Iniciativa de sostenibilidad ambiental', '2025-02-01', '2025-06-30', 'https://images.unsplash.com/photo-1503437313881-503a91226402', 'https://images.unsplash.com/photo-1558981285-6f0c94958bb6', 4);

-- Usuario - Proyecto
INSERT INTO Usuario_Proyecto (id_usuario, id_proyecto, propietario, estado, favorito) VALUES
(1, 1, TRUE, 'ACEPTADO', TRUE),
(2, 2, FALSE, 'PENDIENTE', FALSE),
(3, 3, FALSE, 'ACEPTADO', TRUE),
(4, 1, FALSE, 'RECHAZADO', FALSE);

-- Reseñas
INSERT INTO Resennas (titulo, comentario, valoracion, puntuacion, id_usuario, cif) VALUES
('Excelente empresa', 'Me trataron muy bien durante mi beca', 'POSITIVA', 4.5, 1, 'A12345678'),
('Buena experiencia', 'Gran cultura laboral', 'POSITIVA', 4.0, 2, 'B87654321');

INSERT INTO Resennas (titulo, comentario, valoracion, puntuacion, id_usuario, id_proyecto) VALUES
('Gran proyecto de marketing', 'Muy enriquecedor', 'POSITIVA', 5.0, 2, 2),
('Mejoraría la organización', 'Desordenado al principio', 'NEUTRAL', 3.0, 3, 3);
