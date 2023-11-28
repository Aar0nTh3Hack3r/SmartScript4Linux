DIST = wagofix-1.0.jar
SRC_DIR = src/
APP_DIR = ../app/app/

space :=
space +=

sources = $(shell find $(SRC_DIR) -type f -name '*.java')
classes = $(sources:.java=.class)
classpath = $(wildcard $(APP_DIR)*.jar)

all: $(classes) jar

jar:
	jar --create --verbose --main-class=com.wagofix.Main --file $(DIST) $(foreach class,$(patsubst $(SRC_DIR)%,%,$(classes)),-C $(SRC_DIR) $(class))

%.class: %.java
	javac -classpath "$(SRC_DIR):$(subst $(space),:,$(wildcard $(APP_DIR)*.jar))" $<

clean:
	find $(SRC_DIR) -name "*.class" -type f -delete
	rm -f $(DIST)