DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
  idusuario int NOT NULL AUTO_INCREMENT,
  apellidos varchar(100) NOT NULL,
  nombres varchar(100) NOT NULL,
  usuario varchar(100) NOT NULL,
  password tinyblob NOT NULL,
  autorizacion varchar(50) NOT NULL,

  PRIMARY KEY (idusuario),
  UNIQUE KEY IDX_usuarios_1 (apellidos,nombres),
  UNIQUE KEY IDX_usuarios_2 (usuario)

) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;


INSERT INTO usuarios (apellidos, nombres, usuario, password, autorizacion) 
VALUES ('Balta Alva', 'Víctor Manuel', 'vitucho', AES_ENCRYPT('999999999', 'parainfo'), 'ADMIN');
INSERT INTO usuarios (apellidos, nombres, usuario, password, autorizacion) 
VALUES ('Balta Gaitán', 'Diego Alexander', 'dieguito', AES_ENCRYPT('999999999', 'parainfo'), 'ADMIN');
INSERT INTO usuarios (apellidos, nombres, usuario, password, autorizacion) 
VALUES ('Altamirano Andrade', 'Carla Patricia', 'carlita', AES_ENCRYPT('999999999', 'parainfo'), 'ADMIN');

