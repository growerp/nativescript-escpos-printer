all: lib

java_build:
	cd IChiPrinter && gradle build

lib: java_build
	cp IChiPrinter/library/build/outputs/aar/library-release.aar platforms/android/libs/IChiPrinter.aar
