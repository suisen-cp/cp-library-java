APP_NAME="dependency_expander"
SCRIPT_DIR="${APP_NAME}/scripts"
DATA_DIR="${APP_NAME}/dependency_data"
TMP_DIR="${APP_NAME}/tmp"
OUT_DIR="${APP_NAME}/out"

mkdir "${TMP_DIR}"
# shellcheck disable=SC1090
source "${SCRIPT_DIR}/config.sh"

LIB_OBJ_PATH="${DATA_DIR}/lib_dep.txt.object"
STD_OBJ_PATH="${DATA_DIR}/std_dep.txt.object"
LIB_TMP_OUT="${TMP_DIR}/lib_tmp"
STD_TMP_OUT="${TMP_DIR}/std_tmp"

target=$1
output=$2

class_name=$(basename "${target}" | sed -e 's/\.java$//')
class=$(basename "${target}" | sed -e 's/\.java$/\.class/')
javac -d "${TMP_DIR}" -cp "${lib_class_path}" "${target}"

lib_deps=$(jdeps -f java\\..* -v -cp "${lib_class_path}" "${TMP_DIR}/${class}" | \
  sed -e "s/${lib_name}$//" -e '/.*not found$/d' -e '/.*->.*\$.*/d' -e 's/  *//g' -e '1d' -e 's/^.*->//')
std_deps=$(jdeps -f lib\\..* -v -cp "${lib_class_path}" "${TMP_DIR}/${class}" | \
  sed -e 's/java\.base$//' -e '/.*->.*\$.*/d' -e 's/  *//g' -e '1d' -e 's/^.*->//' -e '/.*java\.lang\.[A-Z].*/d' -e '/.*java\.lang\.invoke\..*/d')

#echo "${lib_deps}" > "dependency_expander/out/dbg_lib_deps"
#echo "${std_deps}" > "dependency_expander/out/dbg_std_deps"

java -cp "${OUT_DIR}" Expander "${LIB_TMP_OUT}" "${STD_TMP_OUT}" "${lib_deps}" "${std_deps}" "${LIB_OBJ_PATH}" "${STD_OBJ_PATH}"

# shellcheck disable=SC2002
content=$(\
  cat "${target}" | \
  sed -e '/^package/d' -e '/^import/d' -e 's/^public //' -e '/^ *$/d' | \
  sed -e '1!s/^/    /')

{
  cat "${STD_TMP_OUT}"
  echo ""
  echo "class Main {"
  echo "    public static void main(String[] args) throws Exception {"
  echo "        ${class_name}.main(args);"
  echo "    }"
  echo "    static ${content}"
  echo "}"
} > "${output}"

while read -r line ; do
  if [ -n "${line}" ]; then
    # shellcheck disable=SC2002
    cat "src/${line}.java" | sed -e '/^package/d' -e '/^import/d' -e 's/^public //' >> "${output}"
  fi
done < "${LIB_TMP_OUT}"

rm -r "${TMP_DIR}"
