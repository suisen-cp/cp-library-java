#!/bin/bash

source "$(cd $(dirname $0);pwd)/bash_template.sh"

mkdir -p "${DEP_DIR}"

tmp_dep="${DEP_DIR}/tmp_dep.txt"
lib_dep="${DEP_DIR}/lib_dep.txt"
std_dep="${DEP_DIR}/std_dep.txt"

echo -n "getting all dependencies..."
jdeps -v "${CLASS_PATH}" | sed -e '/^[^ ]/d' | sed -E 's/\$[^ ]+ //g' > "${tmp_dep}"
echo_ok

echo -n "extracting std dependencies..."
grep    "java\.base" "${tmp_dep}" | sed -E 's/[^ ]+$//' | sed -e 's/ *//g' | sort | uniq > "${std_dep}"
echo_ok

echo -n "extracting lib dependencies..."
grep -v "java\.base" "${tmp_dep}" | sed -E 's/[^ ]+$//' | sed -e 's/ *//g' | sort | uniq > "${lib_dep}"
echo_ok

echo -n "getting dependencies recursively..."
java -cp "${OUT_DIR}" LibExpander "${lib_dep}" "${std_dep}"
echo_ok

rm "${tmp_dep}"
