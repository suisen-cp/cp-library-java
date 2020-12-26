#!/bin/bash -eu

function usage() {
    echo "Usage: gen_templates.sh [options] <template_file> <output_directory>"
    echo "           (Main.java)"
    echo "   or  gen_templates.sh [options] <template_file> <output_directory> <class_name>"
    echo "           (<class_name>.java)"
    echo "   or  gen_templates.sh [options] <template_file> <output_directory> <class_name_1> <class_name_2>"
    echo "           (<class_name_1>.java, ..., <class_name_2>.java (i.e. A.java, B.java, ..., D.java))"
    echo ""
    echo " where options include:"
    echo ""
    echo " -p <prefix>         Adding the specified prefix."
    echo "                     (i.e. 'gen_templates.sh -p Task A C' generates 'TaskA.java', 'TaskB.java', 'TaskC.java'.)"
    echo " -v <variables file> Expanding the variables defined at <variables file>, using 'source <variables file>'"
    echo "                     Some variables are reserved:"
    echo "                        - 'CLASS_NAME'  (same as '<class_name>' or <prefix><class_name>)"
    echo "                        - 'ABS_DIR' (absolute path of '<output_directory>')"
    echo "                        - 'DIR'     (same as '<output_directory>')"
}

PREFIX=""
VARS_FILE=""
while getopts "p:v:h" OPT
do
  case $OPT in
    h) usage; exit 0;;
    p) PREFIX=$OPTARG;;
    v) VARS_FILE=$OPTARG;;
  esac
done

shift $(($OPTIND - 1))

if [ $# -lt 2 ]; then
    usage
    exit 1
fi

TEMPLATE=$(cat "$1")
ABS_DIR=$(cd "$2";pwd)
DIR="$2"

function output() {
    CLASS_NAME="$1"
    source "${VARS_FILE}"
    content=$(eval "echo \"${TEMPLATE}\"")
    mkdir -p "${DIR}"
    echo "${content}" > "${DIR}/${CLASS_NAME}.java"
}

if [ $# -eq 2 ]; then
    output "${PREFIX}Main"
elif [ $# -eq 3 ]; then
    CLASS_NAME="$3"
    output "${PREFIX}${CLASS_NAME}"
else
    brace=$(eval echo {$3..$4})
    if [ "${brace}" = "{$3..$4}" ]; then
        echo_colored "Brace Expansion Failed (Invalid Range): '$3..$4'." $YELLOW
        exit 1
    fi
    for CLASS_NAME in ${brace}; do
        output "${PREFIX}${CLASS_NAME}"
    done
fi
