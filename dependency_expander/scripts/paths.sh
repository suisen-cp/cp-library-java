#!/bin/bash

EXPANDER_DIR=$(cd $(dirname $0);cd ../;pwd)

DEP_DIR="${EXPANDER_DIR}/dependency_data"
TMP_DIR="${EXPANDER_DIR}/tmp"
OUT_DIR="${EXPANDER_DIR}/out"
SRC_DIR="${EXPANDER_DIR}/src"

# --------------- edit here ------------------ #
CLASS_PATH="out/production/cp-library-java"     
SOURCE_PATH="src"                               
# --------------- edit here ------------------ #
