#!/bin/bash

source "$(cd $(dirname $0);pwd)/bash_template.sh"

mkdir -p "${OUT_DIR}"

javac -d "${OUT_DIR}" "${SRC_DIR}/*.java"