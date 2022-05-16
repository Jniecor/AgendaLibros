CREATE TABLE EDITORIAL (
    ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    CODIGO CHAR(3),
    NOMBRE VARCHAR(20) NOT NULL,
    CONSTRAINT ID_EDITORIAL_PK PRIMARY KEY (ID)
);

CREATE TABLE LIBRO (
    ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY, -- Id autonumérico
    TITULO VARCHAR(40) NOT NULL,
    AUTOR VARCHAR(20),
    EDITORIAL INTEGER,
    ISBN CHAR(13),
    FECHA_PUBLICACION DATE,
    PRECIO INTEGER,
    FOTO VARCHAR(30),
    CONSTRAINT ID_LIBRO_PK PRIMARY KEY (ID),
    CONSTRAINT PROV_EDITORIAL_FK FOREIGN KEY (EDITORIAL) REFERENCES EDITORIAL (ID)
);