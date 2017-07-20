#!/bin/bash
#ESTE SCRIPT DEBE EJECUTARSE CON PERMISOS DE SUPER USUARIO



postgres="sudo -i -u postgres"
redes2="sudo -i -u redes2"

#Corro los scripts en la base de dato correspondiente
function correScripts {
	echo 'corriendo scripts en servidor central'
	echo "$1"
	cat "$1" | $redes2 psql -d servidorCentral #-f "$1"
	echo 'corriendo scripts en servidor secundario'
	echo "$2"
	cat "$2" | $redes2 psql -d servidorSecundario #-f "$2"
}

		#ESTA FUNCION NO ME HACE FALTA
			#ES MENOS COMPLEJO E INVASIVO PASAR EL CONTENIDO DE LOS ARCHIVOS
			#A TRAVES DE UN PIPE CON EL COMANDO CAT

#funcion de dar permisos a postgres sobre los archivos
#(postgres sera dueño de los archivos)
#function otorgaPermisos {
	#echo 'otorgo permisos'
#	chown redes2 "$1"
#	chown redes2 "$2"
#}


#funcion de crear la BD
#creo las bases de datos, con el usuario postgres
#que sera el dueño de las mismas
function crearBD {
	$redes2 createdb servidorCentral
	$redes2 createdb servidorSecundario
}

#function eliminarUsuario {
#	userdel -r redes2
#}

function eliminarBasesDeDatos {
	$redes2 dropdb servidorCentral
	$redes2 dropdb servidorSecundario
}

function crearUsuario {
	useradd -m -d /home/redes2 -s /bin/bash redes2
	echo redes2:redes2 | chpasswd
	otorgarPermisosCreacionBD
#https://stackoverflow.com/questions/2150882/how-to-automatically-add-user-account-and-password-with-a-bash-script
}

function otorgarPermisosCreacionBD {
	#otorgo permisos de creacion de base de datos
	echo "reassign owned by redes2 to postgres;DROP OWNED BY redes2;" | $redes2 psql -d servidorCentral
	echo "reassign owned by redes2 to postgres;DROP OWNED BY redes2;" | $redes2 psql -d servidorSecundario
	echo "DROP user redes2;create user redes2 with password 'redes2' superuser createdb;commit;" | $redes2 psql -d servidorCentral
	echo "GRANT ALL ON schema public TO redes2;commit;" | $redes2 psql -d servidorCentral
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
			#el usuario existe?
		res="$(cat /etc/passwd | grep redes2)"
			if [ -z "$res" ];
			then
				echo "Creando el usuario redes2"
				crearUsuario
			else
				echo 'el usario redes2 ya existe'
				otorgarPermisosCreacionBD
			fi

			#las bases de datos existen?
		bdCentral="$(echo '\l' | $postgres psql | cut -c 1-18 | grep Central)"
		bdSecundaria="$(echo '\l' | $postgres psql | cut -c 1-18 | grep Central)"
		echo "\n $bdCentral \n"
		echo "\n $bdSecundaria \n"
			if [[ -z "$bdCentral" && -z "$bdSecundaria" ]];
			then
				echo 'las bd no existen, las creare...'
				crearBD
			else
				echo 'Eliminando bases de datos...'
				eliminarBasesDeDatos
				echo 'Creando BD...'
				crearBD
			fi

		#ESTO YA NO LO UTILIZO
		#VER DEFINICION DE LA FUNCION PARA MAS INFO
			#otorgaPermisos "$1" "$2"
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
