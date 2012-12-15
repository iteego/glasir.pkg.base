Description
===========
This project is a base 'bootstrap' project for creating new 
glasir package management packages. 

glasir is a family of tools to improve productivity in Oracle ATG 
e-commerce products. 

glasir.pkg is part of the glasir product family and makes it possible
to perform automated on-the-fly installations of required products such as 
the Oracle ATG E-commerce platform, the JBoss application server, and 
various ATG products required to run an e-commerce application. 

This project is a 'bootstrap' project which can be used as a starting 
point when creating new auto-install glasir.pkg packages. 

Note, the instructions below assume that you want to version control the 
files used to create the glasir.pkg package and that you use git to do so. 
Both of these choices are optional, you can use another source control system
and/or choose not to version control you package creation. 

To Craete a New glasir.pkg Package
==================================
To create a glasir.pkg package for a new ATG artifact using this project (and git): 

  * clone this repository
  * delete the .git directory from the cloned repository
  * rename the cloned directory 'glasir.pkg.base' to correspond to 
    the artifact you want to create a glasir.pkg for. Example: for 
    ATG10.0.3 rename the directory to 'glasir.pkg.atg-10.0.3'
  * unzip your ATG artifact (such as the ATG platform installation, 
    CommerceReferenceStore, Search, etc) directly into the root of 
    the cloned repository. Example: <scm_root>/ATG10.0.2/
  * if you want to trim your git repository, see if you can live without
    some of the content in your artifact directory. An example would be 
    the 'patch' directory found in most ATG installations. This directory 
    is only needed when installing ATG products, is not used runtime, and 
    usually takes up a lot of space. Since product installation in glasir 
    is handled by the glasir.pkg system, the patch directory is unnecessary. 
  * edit gradle.properties file to correspond to your ATG product and version
  * Execute (from the root of the git repository): 
    git add .gitignore
    git add .gradle/.gitignore
    git add .
    git commit -a -m "your commit message"
  * execute 'gradlew clean jar' to build a glasir.pkg package at 
    build/libs. 
  * to deploy the package to a maven/artifactory/nexus server, please 
    use jenkins/ci-server artifactory/nexus/etc integrations (recommended)
    or look to the gradle documentation for how to publish artifacts directly 
    by modifying this build script. 

once you have deployed the glasir.pkg package to your maven/artifactory/nexus 
server, you can use the deployed package in a glasir-enabled e-commerce project by inserting
code like the following into the settings.gradle file in the root of your project 
structure: 

    new glasir.Build(this).with {
      setup {
        dependencies {
          glasirpackage("com.iteego.glasir.pkg:atg-10.0.2:1.0")
        }
    
        addAtgModules from: atgRoot
      }
    }

