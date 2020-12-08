source "dependency_expander/scripts/config.sh"

lib_obj_path="dependency_expander/dependency_data/lib_dep.txt.object"
std_obj_path="dependency_expander/dependency_data/std_dep.txt.object"
lib_tmp_out="dependency_expander/out/lib_tmp"
std_tmp_out="dependency_expander/out/std_tmp"

target=$1
output=$2

class_name=$(basename "${target}" | sed -e 's/\.java$//')
class=$(basename "${target}" | sed -e 's/\.java$/\.class/')
javac -d "dependency_expander/out" -cp "${lib_class_path}" "${target}"

lib_deps=$(jdeps -f java\\..* -v -cp "${lib_class_path}" "dependency_expander/out/${class}" | \
  sed -e "s/${lib_name}$//" -e '/.*not found$/d' -e '/.*->.*\$.*/d' -e 's/  *//g' -e '1d' -e 's/^.*->//')
std_deps=$(jdeps -f lib\\..* -v -cp "${lib_class_path}" "dependency_expander/out/${class}" | \
  sed -e 's/java\.base$//' -e '/.*->.*\$.*/d' -e 's/  *//g' -e '1d' -e 's/^.*->//' -e '/.*java\.lang\.[A-Z].*/d' -e '/.*java\.lang\.invoke\..*/d')

#echo "${lib_deps}" > "dependency_expander/out/dbg_lib_deps"
#echo "${std_deps}" > "dependency_expander/out/dbg_std_deps"

java -cp "dependency_expander/out" Expander "${lib_tmp_out}" "${std_tmp_out}" "${lib_deps}" "${std_deps}" "${lib_obj_path}" "${std_obj_path}"

# shellcheck disable=SC2002
content=$(\
  cat "${target}" | \
  sed -e '/^package/d' -e '/^import/d' -e 's/^public //' -e '/^ *$/d' | \
  sed -e '1!s/^/    /')

{
  cat "${std_tmp_out}"
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
done < "${lib_tmp_out}"

rm "dependency_expander/out/${class}"
rm "${lib_tmp_out}"
rm "${std_tmp_out}"
