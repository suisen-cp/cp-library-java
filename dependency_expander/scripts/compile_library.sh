#!/bin/bash

source "$(cd $(dirname $0);pwd)/bash_template.sh"

mkdir -p "${TMP_DIR}"
mkdir -p "${CLASS_PATH}"

find "${SOURCE_PATH}" | grep -e ".*.java" > "${TMP_DIR}/lib_list"
javac -d "${CLASS_PATH}" @${TMP_DIR}/lib_list

rm -r "${TMP_DIR}"