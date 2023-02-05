.POSIX:
.SUFFIXES:

BUILD = build
CLASSPATH = ".:$(BUILD):/usr/share/java/protobuf.jar"

PACKAGE = com.rapczak.taeber.protobuf
SOURCE = com/rapczak/taeber/protobuf
OUTPUT = $(BUILD)/$(SOURCE)

all: run

run: compile
	java -cp $(CLASSPATH) $(PACKAGE).Tests

compile: preprocessor $(OUTPUT)/Tests.class
$(OUTPUT)/Tests.class: $(SOURCE)/Tests.java $(OUTPUT)/Triples.java
	javac -cp $(CLASSPATH) -d $(BUILD) -processor $(PACKAGE).TemplatedProcessor $(SOURCE)/Tests.java

$(OUTPUT)/Triples.java: $(BUILD)/protoc $(SOURCE)/triples.proto
	protoc --java_out=$(BUILD) $(SOURCE)/triples.proto

$(BUILD):
	mkdir -p $(BUILD)

$(BUILD)/protoc: $(BUILD)
	which protoc || sudo apt install protobuf-compiler libprotobuf-java
	touch $(BUILD)/protoc

# preprocessor: $(BUILD)/preprocessor.bash
# $(BUILD)/preprocessor.bash: $(OUTPUT)/TemplatedProcessor.class
# $(OUTPUT)/TemplatedProcessor.class: $(SOURCE)/TemplatedProcessor.java $(SOURCE)/Templated.java
# 	javac -cp $(CLASSPATH) -d $(BUILD) $^
# 	echo "#!/bin/bash" > $(BUILD)/preprocessor.bash
# 	echo javac -cp $(CLASSPATH) -processor $(PACKAGE).TemplatedProcessor $$\* >> $(BUILD)/preprocessor.bash
# 	chmod +x $(BUILD)/preprocessor.bash

preprocessor: $(OUTPUT)/TemplatedProcessor.class
$(OUTPUT)/TemplatedProcessor.class: $(SOURCE)/TemplatedProcessor.java $(SOURCE)/Templated.java
	javac -cp $(CLASSPATH) -d $(BUILD) $^

clean:
	rm -rf $(BUILD)
	@echo ''
	@echo Do you need to uninstall protoc?
	@echo '  sudo apt remove protobuf-compiler'
