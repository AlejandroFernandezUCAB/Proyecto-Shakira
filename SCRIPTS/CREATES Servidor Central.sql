CREATE USER redes2 WITH PASSWORD 'redes2';

CREATE TABLE CLIENTE(

	ipCliente varchar(15) not null,
	--puertoEscucha int not null,
	puertoCmd int not null,
	puertoData int not null,	
	CONSTRAINT pk_ipCliente PRIMARY KEY ( ipCliente )

);

CREATE TABLE VIDEO(
	id_video int not null,
	nombre text not null,
	CONSTRAINT PK_idVideo PRIMARY KEY( id_video )
);

CREATE TABLE SERVIDOR(

	ipServidor varchar(15) not null,
	puertoCmd int not null,
	puertoData int not null,
	CONSTRAINT PK_ipServidor PRIMARY KEY ( ipServidor )

);

CREATE TABLE VIDEOS_SERVIDOR(
	
	parte1 boolean not null,
	parte2 boolean not null,
	parte3 boolean not null,
	videoFk int not null,
	servidor varchar(15) not null,
	CONSTRAINT fk_video FOREIGN KEY ( videoFk ) REFERENCES VIDEO ( id_video ),
	CONSTRAINT fk_servidor FOREIGN KEY ( servidor ) REFERENCES SERVIDOR ( ipServidor )

);

CREATE TABLE VIDEOS_CLIENTE(

	descargaExitosa boolean not null,
	videoFk int not null,
	clienteFk varchar(15) not null,
	CONSTRAINT fk_video_cliente FOREIGN KEY ( videoFk ) REFERENCES VIDEO ( id_video ),
	CONSTRAINT fk_cliente FOREIGN KEY ( clienteFk ) REFERENCES CLIENTE ( ipCliente )
);

commit;
