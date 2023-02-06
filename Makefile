.POSIX:
.SUFFIXES:

BUILD = build
CLASSPATH = ".:$(BUILD):/usr/share/java/protobuf.jar"

PACKAGE = com.rapczak.taeber.protobuf
SOURCE = com/rapczak/taeber/protobuf
OUTPUT = $(BUILD)/$(SOURCE)

all: tests

tests: compile
	java -cp $(CLASSPATH) $(PACKAGE).Tests

compile: preprocessor $(OUTPUT)/Tests.class
$(OUTPUT)/Tests.class: $(SOURCE)/Tests.java $(OUTPUT)/Triples.java
	javac -cp $(CLASSPATH) -d $(BUILD) -processor $(PACKAGE).ProtobufTemplateProcessor $(SOURCE)/Tests.java

$(OUTPUT)/Triples.java: $(BUILD)/protoc $(SOURCE)/triples.proto
	protoc --java_out=$(BUILD) $(SOURCE)/triples.proto

preprocessor: $(OUTPUT)/ProtobufTemplateProcessor.class
$(OUTPUT)/ProtobufTemplateProcessor.class: $(SOURCE)/ProtobufTemplateProcessor.java $(SOURCE)/ProtobufTemplate.java
	javac -cp $(CLASSPATH) -d $(BUILD) $^

$(BUILD):
	mkdir -p $(BUILD)

$(BUILD)/protoc: $(BUILD)
	which protoc || sudo apt install protobuf-compiler libprotobuf-java
	touch $(BUILD)/protoc

clean:
	rm -rf $(BUILD)
	@echo ''
	@echo Do you need to uninstall protoc?
	@echo '  sudo apt remove protobuf-compiler'
