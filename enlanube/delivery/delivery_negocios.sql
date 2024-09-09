DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS categorias;

# tablas: categorias y productos
CREATE TABLE categorias (
    idcategoria INT NOT NULL AUTO_INCREMENT,
    categoria varchar(50) NOT NULL,
    descripcion text,
    fotopath varchar(100) DEFAULT '',

    PRIMARY KEY (idcategoria),
    UNIQUE KEY IDX_categorias_2 (categoria)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE productos (
    idproducto INT NOT NULL AUTO_INCREMENT,
    idcategoria INT NOT NULL,
    producto varchar(200) NOT NULL,
    precio FLOAT(13,2) NOT NULL,
    fotopath varchar(100) DEFAULT '',
    estado char(1) NOT NULL, -- 0==No habilitado / 1==Habilitado

    PRIMARY KEY (idproducto),
    UNIQUE KEY IDX_productos_2 (producto),
    KEY FK_productos_1 (idcategoria),
    CONSTRAINT FK_productos_1 FOREIGN KEY (idcategoria) REFERENCES categorias (idcategoria) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;


INSERT INTO categorias (categoria, descripcion, fotopath) VALUES ('Abarrotes', 'Tenemos arroz, frijoles, aceite, az�car, harina, y el resto de la canasta b�sica familiar. Son nuestros productos principales y en nuestra tienda los tenemos todos.', 'abarrotes.png');
INSERT INTO categorias (categoria, descripcion, fotopath) VALUES ('Bebidas', 'En nuestro establecimiento tenemos todo tipo de bebidas, como: gaseosas, agua mineral, jugos, cervezas, infusiones, vinos y muchos m�s, que puede apreciar cando guste.', 'bebidas.png');
INSERT INTO categorias (categoria, descripcion, fotopath) VALUES ('Cuidado personal', 'Si se trata trata de mantener el cuerpo limpio y sano, entonces visita nuestro local, donde tenemos: jabones, shampoo, pasta dental, perfumes, cremas y otros de las m�s variadas marcas.', 'cuidado.png');
INSERT INTO categorias (categoria, descripcion, fotopath) VALUES ('Limpieza', 'Para la limpieza de tu casa, de tu ropa y de tus muebles tenemos detergentes muy variados, aerosoles, ceras, escobillones y otros que lograran deslumbrar a tu familia e invitados.', 'limpieza.png');
INSERT INTO categorias (categoria, descripcion, fotopath) VALUES ('Panader�a y pasteler�a', 'Todo tipo de pan y variados pasteles para que deleites momentos sublimes. Solo tienes que hacer tu pedido y nosotros te lo enviamos de forma inmediata.', 'panes.png');
INSERT INTO categorias (categoria, descripcion, fotopath) VALUES ('Frutas y verduras', 'Frutas y verduras frescas, reci�n cosechadas, es lo que te enviaremos en bolsas herm�ticas y seguras para que lo disfrutes con tu familia.', 'frutas.png');

INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (1, 'Aceite Vegetal PRIMOR Premium Botella 900L', 12.70, 'Aceite Vegetal PRIMOR Premium Botella 900L.png',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (1, 'Arroz Familiar COSECHA NORTE�A Bolsa 5Kg', 17.50, 'Arroz Familiar COSECHA NORTE�A Bolsa 5Kg.png',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (1, 'Az�car DULFINA Ca�a de az�car rubia Bolsa 1Kg', 4.70, 'Az�car DULFINA Ca�a de az�car rubia Bolsa 1Kg.png',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (1, 'Ajo SIBARITA Pasta Doypack 250Gr', 10.00, 'Ajo SIBARITA Pasta Doypack 250Gr.png',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (1, 'Hongos y Laurel BELLS Sobre 10g', 2.00, 'Hongos y Laurel BELLS Sobre 10g.png',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (1, 'Sal Marina EMSAL Mesa Bolsa 1Kg', 1.90, 'Sal Marina EMSAL Mesa Bolsa 1Kg.png',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (2, 'Agua de Mesa Sin Gas San Luis Botella 2.5L', 2.40, 'Agua de Mesa Sin Gas San Luis Botella 2.5L.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (2, 'Cerveza CRISTAL 6 Pack Lata 473ml', 18.90, 'Cerveza CRISTAL 6 Pack Lata 473ml.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (2, 'Gaseosa Coca Cola Botella 2.5 L', 7.90, 'Gaseosa Coca Cola Botella 2.5 L.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (2, 'Gaseosa Inca Kola Botella 2.25 L', 7.90, 'Gaseosa Inca Kola Botella 2.25 L.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (2, 'N�ctar WATTS Durazno Caja 1L', 2.00, 'N�ctar WATTS Durazno Caja 1L.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (2, 'Pisco SANTIAGO QUEIROLO Quebranta Botella 750ml', 27.00, 'Pisco SANTIAGO QUEIROLO Quebranta Botella 750ml.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (3, 'Shampoo + Acondicionador Dove �leo Nutrici�n para Cabello Seco � Pack 400 Ml+ 400 Ml', 14.00, 'Shampoo Acondicionador Dove.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (3, 'Shampoo 2 en 1 Cuidado Cl�sico Pantene Pro-V Frasco 400 ml', 14.00, 'Shampoo Pantene.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (3, 'Jab�n Antibacterial Protex Omega3 Barra 110g Paquete 3un', 10.90, 'Jab�n Antibacterial Protex Omega3 Barra 110g Paquete 3un.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (3, 'Jab�n Antibacterial DOVE Cuida & Protege Barra 90g PACK 3un', 7.00, 'Jab�n Antibacterial DOVE Cuida & Protege Barra 90g PACK 3un.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (3, 'Crema Dental Colgate Pack Luminous White - Pack 2 UN', 16.20, 'Crema Dental Colgate.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (3, 'Crema Dental Vitis Aloe Vera - Tubo 100 ML', 4.50, 'Crema Dental Vitis.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (4, 'Detergente en Polvo ARIEL con Toque Downy Bolsa 800g', 13.90, 'Detergente en Polvo ARIEL con Toque Downy Bolsa 800g.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (4, 'Detergente en Polvo CARICIA Rosa Ropa Delicada Bolsa 1.4Kg', 43.60, 'Detergente en Polvo CARICIA Rosa Ropa Delicada Bolsa 1.4Kg.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (4, 'Jab�n en Barra BOL�VAR Floral Paquete 190g 2un', 6.20, 'Jab�n en Barra BOL�VAR Floral Paquete 190g 2un.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (4, 'Escoba Escob�n Azul � Hude', 16.80, 'Escoba Escob�n Azul   Hude.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (4, 'Lej�a Cloro Sapolio 5L', 9.80, 'Lej�a Cloro Sapolio 5L.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (4, 'Cera l�quida Roja 1 gl', 64.00, 'Cera l�quida Roja 1 gl.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (5, 'Torta Feliz Cumplea�os de Chocolate', 64.00, 'Torta Feliz Cumplea�os de Chocolate.jpg', '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (5, 'St Torta Carlota (Torta Helada)', 60.00, 'St Torta Carlota _Torta Helada_.jpg', '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (5, 'Mini Alfajores LA FLORENCIA Bandeja 40un', 18.00, 'Mini Alfajores LA FLORENCIA Bandeja 40un.jpg',  '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (5, 'Empanadas de Pollo', 6.00, 'empanadas de pollo.jpg', '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (5, 'Crema Volteada', 7.00, 'crema volteada.jpg', '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (5, 'Mazamorra Morada', 12.00, 'Mazamorra Morada.jpg', '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (6, 'Manzanas por unidad', 2.50, 'manzanas.jpg', '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (6, 'Papaya por Kg.', 8.00, 'papaya.jpg', '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (6, 'Uvas por Kg.', 12.00, 'uvas.jpg', '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (6, 'Cerezas por Kg.', 15.00, 'cerezas.jpg', '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (6, 'Coliflor por Kg', 12.00, 'Coliflor por Kg.jpg', '1');
INSERT INTO productos (idcategoria, producto, precio, fotopath, estado) VALUES (6, 'Betarraga por Kg', 7.00, 'betarraga.jpg', '1');
