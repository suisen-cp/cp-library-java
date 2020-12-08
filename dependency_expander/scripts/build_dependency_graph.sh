source "dependency_expander/scripts/config.sh"

lib_dep="dependency_expander/dependency_data/lib_dep.txt"
std_dep="dependency_expander/dependency_data/std_dep.txt"
jdeps -e lib -e lib\\..* -v "${lib_class_path}" | \
  sed -e "s/${lib_name}$//" -e 's/  *//g' -e '/.*->.*\$.*/d' -e '1d' > \
  "${lib_dep}"
jdeps -f lib\\..* -v "${lib_class_path}/lib" | \
  sed -e 's/java.base$//' -e 's/  *//g' -e '/.*->.*\$.*/d' -e '/.*java\.lang\.[A-Z].*/d' -e '/.*java\.lang\.invoke\..*/d' -e '1d' > \
  "${std_dep}"
java -cp "dependency_expander/out" LibExpander "${lib_dep}" "${std_dep}"