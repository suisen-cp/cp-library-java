#!/bin/bash

source "$(cd $(dirname $0);pwd)/paths.sh"

set -o pipefail
set -eu

RED=31
GREEN=32
YELLOW=33
BLUE=34
MAGENTA=35
CYAN=36
WHITE=37

function echo_colored() {
    echo -e "\033[0;$2m$1\033[0;39m"
}
function echo_ok() {
    echo_colored "OK." $GREEN
}
function catch() {
    echo_colored "Failed." $RED
}
trap catch ERR
