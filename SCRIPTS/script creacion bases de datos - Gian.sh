#!/bin/bash
#ESTE SCRIPT DEBE EJECUTARSE CON PERMISOS DE SUPER USUARIO



postgres="sudo -i -u postgres"

#Corro los scripts en la base de dato correspondiente
function correScripts {
	echo 'corriendo scripts en servidor central'
	echo "$1"
	$postgres psql -d servidorCentral -f "$1"
	echo 'corriendo scripts en servidor secundario'
	echo "$2"
	$postgres psql -d servidorSecundario -f "$2"
}


#funcion de dar permisos a postgres sobre los archivos
#(postgres sera dueño de los archivos)
function otorgaPermisos {
	#echo 'otorgo permisos'
	chown postgres "$1"
	chown postgres "$2"
}


#funcion de crear la BD
#creo las bases de datos, con el usuario postgres
#que sera el dueño de las mismas
function crearBD {
	$postgres createdb servidorCentral
	$postgres createdb servidorSecundario
}

#verifico que el usuario haya proporcionado dos argumentos no vacios
if [[ -z "$1" || -z "$2" ]]; 
then
	echo 'faltan argumentos'
else
#verifico que el archivo existe
	if [[ -f $1 && -f $2 ]];
	then
		echo 'archivos existen'
		crearBD
		otorgaPermisos "$1" "$2"
		correScripts "$1" "$2"
	else
		echo 'no se pudo encontrar alguno de los archivos'
	fi
fi


#operators and questions:
#http://tldp.org/LDP/abs/html/comparison-ops.html
#https://stackoverflow.com/questions/6482377/check-existence-of-input-argument-in-a-bash-shell-script
#check if file exists:
#https://blog.desdelinux.net/comprobar-si-un-archivo-o-carpeta-existe-o-no-y-mas-con-ciclo-if/
