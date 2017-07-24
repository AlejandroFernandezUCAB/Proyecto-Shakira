--CREATE USER redes2 WITH PASSWORD 'redes2';

CREATE TABLE CLIENTE(

	ipCliente varchar(15) not null,
	puerto int not null,
	CONSTRAINT pk_ipCliente PRIMARY KEY ( ipCliente )

);

CREATE TABLE VIDEO(
	id_video int not null,
	nombre text not null,
	rutaEnDisco text not null,
	parteAsignada int not null,
	CONSTRAINT PK_idVideo PRIMARY KEY( id_video )
);

CREATE TABLE VIDEOS_SERVIDOR(
	
	parte1 boolean not null,
	parte2 boolean not null,
	parte3 boolean not null,
	videoFk int not null,
	CONSTRAINT fk_video FOREIGN KEY ( videoFk ) REFERENCES VIDEO ( id_video )

);

CREATE TABLE VIDEOS_CLIENTE(

	statusDescarga int not null,
	videoFk int not null,
	clienteFk varchar(15) not null,
	CONSTRAINT fk_video_cliente FOREIGN KEY ( videoFk ) REFERENCES VIDEO ( id_video ),
	CONSTRAINT fk_cliente FOREIGN KEY ( clienteFk ) REFERENCES CLIENTE ( ipCliente )
);

CREATE SEQUENCE sec_id_video 
	start with 1
	increment by 1
	cycle;
commit;
