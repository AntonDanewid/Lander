# super simple makefile
# call it using 'make NAME=name_of_code_file_without_extension'
# (assumes a .java extension)

NAME = "A3Basic"
all:
	# (a bit of a hack to compile everything each time ...)
	javac -cp vecmath.jar lander/*.java
	@echo "Compiling..."

	
run: all
# windows needs a semicolon
		@echo "Running ..."
		java -cp "vecmath.jar:." lander.A3Basic


clean:
	rm -rf drawing/*.class