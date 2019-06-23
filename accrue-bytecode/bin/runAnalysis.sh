#!/bin/sh

export MYACCRUE="."

vmargs="-Xmx25G -Xms25G -Xss30m"
dir=`dirname "$0"`/..
timeout=-1

if [ -z "$MYACCRUE" ]; then
  # check if we are already in the right folder
  MYACCRUE=$dir/../walaAnalysis
fi
if [ ! -d "$MYACCRUE" ]; then
  echo "Accrue bytecode path not found: try defining the MYACCRUE environment variable as the top-level directory for accrue-bytecode (this could be the walaAnalysis folder)."
fi

run() {
    if [ "$assert" = 1 ]; then
        cmmd="-classpath '$classpath' -ea main.AccrueAnalysisMain"
    else
        cmmd="-classpath '$classpath' main.AccrueAnalysisMain"
    fi

    if [ ! "$outputDirSet" ]; then
        # Use the default output directory
        args="$args '-out' '$MYACCRUE/tests'"
    fi

    if [ "$verbose" = 1 ]; then
        echo java "$vmargs" "$cmmd" "$args"
    fi
    
    if [ $timeout -gt 0 ]; then
        eval bin/timeout3.sh -t $timeout java "$vmargs" "$cmmd" "$args"
    else
        eval java "$vmargs" "$cmmd" "$args"
    fi
}

clean() {
    for file in tests/*.dot
    do
    rm "$file"
    done
    cleaned=true
    echo removed *.dot files from tests directory
}

while true; do
  case "$1" in
    "")
      break
      ;;
    clean)
      clean
      shift
      ;;
    -ea)
      assert=1
      shift
      ;;
    -V)
      verbose=1
      shift
      ;;
    -cp)
      # make sure the signatures make it onto the analysis classpath
      args="$args '$1'"
      shift
      args="$args '$1:$MYACCRUE/classes/signatures'"
      shift
      ;;
    -analysisClassPath)
      # make sure the signatures make it onto the analysis classpath
      args="$args '$1'"
      shift
      args="$args '$1:$MYACCRUE/classes/signatures'"
      shift
      ;;
    -out)
      # flag output directory as having been set
      outputDirSet=1
      args="$args '$1'"
      shift
      ;;
    -to)
      # set a timeout in seconds
      shift
      timeout=$1
      shift
      ;;
    *)
      args="$args '$1'"
      shift
      ;;
  esac
done

classpath="$MYACCRUE/classes"
classpath="$classpath:$MYACCRUE/data"
classpath="$classpath:$MYACCRUE/lib/JSON-java.jar"
classpath="$classpath:$MYACCRUE/lib/jcommander-1.35.jar"
classpath="$classpath:$MYACCRUE/lib/JFlex.jar"
classpath="$classpath:$MYACCRUE/lib/java-cup-11a.jar"
classpath="$classpath:$MYACCRUE/lib/apktool-cli.jar"
classpath="$classpath:$MYACCRUE/lib/wala-fork.jar"

run "$args"


