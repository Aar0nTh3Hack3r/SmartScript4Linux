cd `dirname "$0"`

javafx=($"../downloads/javafx-sdk-*/lib/")
agent=($"../app/byte-buddy-agent-*.jar")

java \
--module-path "$(echo ${javafx[0]})" --add-modules javafx.controls,javafx.fxml \
-classpath "$(printf %s: ../app/app/*.jar)wagofix-1.0.jar" \
-javaagent:$(echo ${agent[0]}) \
-Dlog4j.configurationFile="./log4j.properties" \
-Dlog4j.debug \
-Djpackage.app-version="1.0" \
--add-opens="javafx.controls/javafx.scene.control.skin=ALL-UNNAMED" \
--add-opens="javafx.base/javafx.beans.property=ALL-UNNAMED" \
--add-opens="javafx.graphics/javafx.scene.text=ALL-UNNAMED" \
--add-exports="javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED" \
--add-exports="javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED" \
--add-exports="javafx.base/com.sun.javafx.event=ALL-UNNAMED" \
--add-exports="javafx.graphics/com.sun.javafx.css=ALL-UNNAMED" \
--add-exports="javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED" \
--add-exports="javafx.graphics/com.sun.javafx.scene.traversal=ALL-UNNAMED" \
--add-exports="javafx.graphics/com.sun.javafx.util=ALL-UNNAMED" \
-Xmx4096m \
-Djpackage.app-path=SmartScript.exe \
com.wagofix.Main \
"$@"

#com.wago.cleverscript.WagoMainApplication
