/**
  This is a glasir.pkg package install script. It gets called to install 
  a glasir package represented as a jar file in a maven repo, into the local 
  project directory of a calling project. 
  
  TODO: See http://someconfluencepage describing glasir packages
  
  The contract for this install script is that it returns a map with three keys:
  
  [install: {}, update: {}, uninstall: {}]
  
  where the values are groovy closures representing the actions we should take 
  for that specific operation. 

  An optional fourth 'license' key can be specified: 

  [install: {}, update: {}, uninstall: {}, license: {}]

  if the package requires license validation. 

  Implicit variables available for this script:
  
  settingsDir - the project structure root dir for the calling project. Type File.
  ant -         a groovy AntBuilder instance. 
  gradle -      http://gradle.org/current/docs/javadoc/org/gradle/api/invocation/Gradle.html
  pkg -         a GlasirPackage object representing the meta data about the package in question
  settings -    the settings object from the calling gradle settings file
                http://gradle.org/docs/current/dsl/org.gradle.api.initialization.Settings.html
  fileUtils -   static instance of org.apache.commons.io.FileUtils
  
  all properties defined in a pkg.properties file get injected into this groovy file 
  via a groovy binding. Note that two properties are special in the pkg.properties file: 
  
  extractDir - The directory where we should extract the package jar file
  packageDir - The main package directory once the jar file has been extracted
  
  example for glasir.db: extractDir=packages/modules, packageDir=/packages/modules/glasir/db
  both variables have type File.
  
  Note that when accessing these properties in this install file, they have been 'resolved' 
  into absolute File objects using the settingsDir of the calling project as the root dir. 

  Also, all exported properties from packages resolved earlier (example would be atgRoot or 
  jBossRoot) will be available for this script.
      
*/

[
  install: {
    ant.unzip(src: pkg.artifactFile, dest: pkg.extractDir) {
      patternset {
        exclude name: "META-INF/**"
      }
    }      
  },
  
  update: {
    //the deleteDirectory method is symlink safe, i.e. deletes the link but doesn't follow it
    fileUtils.deleteDirectory(pkg.packageDir)
    ant.unzip(src: pkg.artifactFile, dest: pkg.extractDir, overwrite: true) {
      patternset {
        exclude name: "META-INF/**"
      }
    }              
  }, 
  
  uninstall: {
    //the deleteDirectory method is symlink safe, i.e. deletes the link but doesn't follow it
    fileUtils.deleteDirectory(pkg.packageDir)
  },
  
  license: {
    [
      [prefix: """\
  ===============================================================================
  License Agreement
  -----------------
  
  Installation and use of @@atgProduct@@ @@atgVersion@@ requires acceptance of the following License
  Agreement:
  
  <hit return to continue>
""",
       licenseFile: "license_atg.txt", 
       prompt: "DO YOU ACCEPT THE TERMS OF THIS LICENSE AGREEMENT? (Y/N): ", 
       requiredAnswer: "Y"]
    ]
  }  
]

  
  
  
