#!/bin/bash
mvn install:install-file -DgroupId=android -DartifactId=android -Dversion=1.5_r2 -Dpackaging=jar -Dfile=$ANDROID_SDK_15/platforms/android-1.5/android.jar
mvn install:install-file -DgroupId=db4o -DartifactId=db4o -Dversion=7.4.88.12908-java1.2 -Dpackaging=jar -Dfile=db4o-7.4.88.12908-java1.2.jar
mvn install:install-file -DgroupId=org.neodatis -DartifactId=neodatis-odb -Dversion=1.9.3.601 -Dpackaging=jar -Dfile=neodatis-odb-1.9.3.601.jar
