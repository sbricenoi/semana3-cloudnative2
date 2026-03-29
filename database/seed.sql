-- Datos de prueba para Sistema de Biblioteca

-- Insertar usuarios
INSERT INTO usuarios (id, nombre, apellido, email, rut, telefono, estado) 
VALUES (seq_usuarios.NEXTVAL, 'Carlos', 'González', 'carlos.gonzalez@email.com', '18456789-2', '+56912345678', 'ACTIVO');

INSERT INTO usuarios (id, nombre, apellido, email, rut, telefono, estado) 
VALUES (seq_usuarios.NEXTVAL, 'María', 'Rodríguez', 'maria.rodriguez@email.com', '19876543-K', '+56923456789', 'ACTIVO');

INSERT INTO usuarios (id, nombre, apellido, email, rut, telefono, estado) 
VALUES (seq_usuarios.NEXTVAL, 'Pedro', 'Silva', 'pedro.silva@email.com', '17234567-8', '+56934567890', 'ACTIVO');

INSERT INTO usuarios (id, nombre, apellido, email, rut, telefono, estado) 
VALUES (seq_usuarios.NEXTVAL, 'Ana', 'Martínez', 'ana.martinez@email.com', '20123456-5', '+56945678901', 'ACTIVO');

INSERT INTO usuarios (id, nombre, apellido, email, rut, telefono, estado) 
VALUES (seq_usuarios.NEXTVAL, 'Luis', 'Fernández', 'luis.fernandez@email.com', '16789012-3', '+56956789012', 'SUSPENDIDO');

-- Insertar libros
INSERT INTO libros (id, titulo, autor, isbn, categoria, cantidad_disponible, cantidad_total) 
VALUES (seq_libros.NEXTVAL, 'Clean Code', 'Robert C. Martin', '978-0132350884', 'Programación', 3, 5);

INSERT INTO libros (id, titulo, autor, isbn, categoria, cantidad_disponible, cantidad_total) 
VALUES (seq_libros.NEXTVAL, 'Design Patterns', 'Gang of Four', '978-0201633610', 'Programación', 2, 3);

INSERT INTO libros (id, titulo, autor, isbn, categoria, cantidad_disponible, cantidad_total) 
VALUES (seq_libros.NEXTVAL, 'The Pragmatic Programmer', 'Andrew Hunt', '978-0135957059', 'Programación', 4, 4);

INSERT INTO libros (id, titulo, autor, isbn, categoria, cantidad_disponible, cantidad_total) 
VALUES (seq_libros.NEXTVAL, 'Introducción a la Arquitectura de Software', 'Jorge Villalobos', '978-9587755060', 'Arquitectura', 1, 2);

INSERT INTO libros (id, titulo, autor, isbn, categoria, cantidad_disponible, cantidad_total) 
VALUES (seq_libros.NEXTVAL, 'Cloud Native Java', 'Josh Long', '978-1449374648', 'Cloud Computing', 2, 3);

INSERT INTO libros (id, titulo, autor, isbn, categoria, cantidad_disponible, cantidad_total) 
VALUES (seq_libros.NEXTVAL, 'Microservices Patterns', 'Chris Richardson', '978-1617294549', 'Arquitectura', 3, 3);

INSERT INTO libros (id, titulo, autor, isbn, categoria, cantidad_disponible, cantidad_total) 
VALUES (seq_libros.NEXTVAL, 'Domain-Driven Design', 'Eric Evans', '978-0321125217', 'Arquitectura', 1, 2);

INSERT INTO libros (id, titulo, autor, isbn, categoria, cantidad_disponible, cantidad_total) 
VALUES (seq_libros.NEXTVAL, 'Building Microservices', 'Sam Newman', '978-1492034025', 'Arquitectura', 2, 2);

INSERT INTO libros (id, titulo, autor, isbn, categoria, cantidad_disponible, cantidad_total) 
VALUES (seq_libros.NEXTVAL, 'Refactoring', 'Martin Fowler', '978-0134757599', 'Programación', 0, 3);

INSERT INTO libros (id, titulo, autor, isbn, categoria, cantidad_disponible, cantidad_total) 
VALUES (seq_libros.NEXTVAL, 'Continuous Delivery', 'Jez Humble', '978-0321601919', 'DevOps', 2, 2);

-- Insertar préstamos
INSERT INTO prestamos (id, id_usuario, id_libro, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado) 
VALUES (seq_prestamos.NEXTVAL, 1, 1, CURRENT_TIMESTAMP, SYSDATE + 14, NULL, 'PRESTADO');

INSERT INTO prestamos (id, id_usuario, id_libro, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado) 
VALUES (seq_prestamos.NEXTVAL, 2, 3, CURRENT_TIMESTAMP - 5, SYSDATE + 9, NULL, 'PRESTADO');

INSERT INTO prestamos (id, id_usuario, id_libro, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado) 
VALUES (seq_prestamos.NEXTVAL, 3, 5, CURRENT_TIMESTAMP - 20, SYSDATE - 6, NULL, 'RETRASADO');

INSERT INTO prestamos (id, id_usuario, id_libro, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado) 
VALUES (seq_prestamos.NEXTVAL, 1, 7, CURRENT_TIMESTAMP - 30, SYSDATE - 16, CURRENT_TIMESTAMP - 2, 'DEVUELTO');

INSERT INTO prestamos (id, id_usuario, id_libro, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado) 
VALUES (seq_prestamos.NEXTVAL, 4, 9, CURRENT_TIMESTAMP - 10, SYSDATE + 4, NULL, 'PRESTADO');

INSERT INTO prestamos (id, id_usuario, id_libro, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado) 
VALUES (seq_prestamos.NEXTVAL, 2, 9, CURRENT_TIMESTAMP - 15, SYSDATE - 1, CURRENT_TIMESTAMP - 1, 'DEVUELTO');

INSERT INTO prestamos (id, id_usuario, id_libro, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado) 
VALUES (seq_prestamos.NEXTVAL, 3, 9, CURRENT_TIMESTAMP - 8, SYSDATE + 6, NULL, 'PRESTADO');

COMMIT;
