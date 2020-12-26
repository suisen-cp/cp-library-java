#!/bin/bash

source "$(cd $(dirname $0);pwd)/bash_template.sh"

mkdir -p "${TMP_DIR}"

LIB_OBJ_PATH="${DEP_DIR}/lib_dep.txt.object"
STD_OBJ_PATH="${DEP_DIR}/std_dep.txt.object"
ALL_TMP_OUT="${TMP_DIR}/all_tmp"
LIB_TMP_OUT="${TMP_DIR}/lib_tmp"
STD_TMP_OUT="${TMP_DIR}/std_tmp"

if [ $# -lt 1 ]; then
    echo_colored "usage : expander.sh <source file> [destination file]" RED
    exit 1
fi

SRC_FILE=$1
DST_FILE="${2:-$(cd $(dirname $1);pwd)/Main.java}"
echo_colored "${DST_FILE}" $RED

class_name=$(basename "${SRC_FILE}" | sed -e 's/\.java$//')

echo -n "compiling the source file..."
javac -d "${TMP_DIR}" -cp "${CLASS_PATH}" "${SRC_FILE}"
echo_ok

echo -n "getting all dependencies..."
jdeps -v -cp "${CLASS_PATH}" "${TMP_DIR}" | sed '/^[^ ]/d' | sed -E 's/\$[^ ]+ //g' > "${ALL_TMP_OUT}"
echo_ok

echo -n "getting lib dependencies..."
lib_deps=$(grep -v "java\.base" "${ALL_TMP_OUT}" | sed -E 's/[^ ]+$//' | sed -e 's/ *//g' -e '/^\(.*\)->\1$/d' -e 's/^.*->//' | sort | uniq)
echo_ok

echo "lib dependencies:"
echo_colored "${lib_deps}" $BLUE

echo -n "getting std dependencies..."
std_deps=$(grep    "java\.base" "${ALL_TMP_OUT}" | sed -E 's/[^ ]+$//' | sed -e 's/ *//g' -e 's/^.*->//' | sort | uniq)
echo_ok

echo "std dependencies:"
echo_colored "${std_deps}" $CYAN

echo -n "getting dependencies recursively..."
java -cp "${OUT_DIR}" Expander "${LIB_TMP_OUT}" "${STD_TMP_OUT}" "${lib_deps}" "${std_deps}" "${LIB_OBJ_PATH}" "${STD_OBJ_PATH}"
echo_ok

lib_deps_rec=$(cat ${LIB_TMP_OUT} | sed 's;/;\.;g')
std_deps_rec=$(cat ${STD_TMP_OUT} | sed -e 's/import //' -e 's/;//')
echo "lib dependencies (recursively):"
echo_colored "${lib_deps_rec}" $BLUE
echo "std dependencies (recursively):"
echo_colored "${std_deps_rec}" $CYAN

echo -n "processing the source file..."
#   - remove lines that starts with "package", "import"
# ? - remove empty line & replace the class name with "Main" in the first line
content=$(\
    cat -s "${SRC_FILE}" | \
    sed -e '/^package/d' -e '/^import/d' -e "s/class ${class_name}/class Main/")
echo_ok

echo -n "inserting import statements..."
{
    cat "${STD_TMP_OUT}"
    echo ""
    echo "${content}"
} > "${DST_FILE}"
echo_ok

echo -n "expanding libraries..."
while read -r line ; do
    if [ -n "${line}" ]; then
        cat "${SOURCE_PATH}/${line}.java" | sed -e '/^package/d' -e '/^import/d' -e 's/^public //' >> "${DST_FILE}"
    fi
done < "${LIB_TMP_OUT}"
echo_ok

echo -n "cleaning..."
rm -r "${TMP_DIR}"
echo_ok
