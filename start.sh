#!/bin/sh
set -e
cd "$(dirname "$0")"
javac model/Pessoa.java model/Funcionario.java Servidor.java Main.java
exec java Main
