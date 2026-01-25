#!/bin/sh

APP_HOME=$(cd "$(dirname "$0")" && pwd -P) || exit 1
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Determine Java
if [ -n "$JAVA_HOME" ] ; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD=java
fi

exec "$JAVACMD" -Dorg.gradle.appname=gradlew -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
