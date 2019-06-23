###############################################################################
# WALA property file
# This file defines the default settings for the core WALA Engine
###############################################################################

################# Mandatory settings without default value ####################

##### 
# Identify the directory where Java Runtime libraries are located.
# For instance, on a windows OS it's typically C:/Progra~1/Java/j2reYourVersion/lib
# On MAC OS, typically /System/Library/Frameworks/JavaVM.framework/Classes
#
# N.B. On Windows or Linux, this directory must contain a valid core.jar (or rt.jar 
# for older VMs).  On Mac, it should contain the classes.jar file.
# On IBM 1.4.x SDKs, this means you need to specify Java14x/jre/lib and not
# Java14x/lib!
#
# Info: Location must be absolute.
#####
#java_runtime_dir = /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/
java_runtime_dir = /Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes
#java_runtime_dir = /Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib
#java_runtime_dir = /mnt/experiments/accrue-bytecode/jdk

################### Mandatory settings with default value ######################

##### Default output dir
# Identify directory where all generated files without absolute path will be located.
# Note that this directory must be created manually; WALA may fail if it does not exist.
# Default value: results [Non absolute path are relative to WALA home location]
# Info: Can be absolute or relative.
#####
output_dir = /Users/mu/Documents/workspace/WALA/out

############################ Optional settings ################################

##### 
# Identify the directory where J2EE standard libraries are located.
# Required only if you ask to analyze J2EE code. 
# No default value.
# Info: Location must be absolute.
#####
#j2ee_runtime_dir = Your location

##### 
# Identify the directory where Eclipse plugins are installed
# Required only if you ask to analyze Eclipse plugins. 
# No default value.
# Info: Location must be absolute.
#####
#eclipse_plugins_dir = Your location

##### Report file
# Identify file name where to output WALA trace file.
# Default value: wala_report.txt [Non absolute path are relative to 'output.dir' variable value]
# Info: Can be absolute or relative.
#####
#WALA_report = Your file name