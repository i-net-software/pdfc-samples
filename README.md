# pdfc-samples

Provides Java-samples for the API-usage of the [i-net PDF Content Comparer](https://www.inetsoftware.de/products/pdf-content-comparer "i-net PDF Content Comparer"). 
The samples cover a range from simple examples for basic comparison process to examples of configuration usage and report generation. 

* Basic comparison procedure calls
* Configuration usages, Filter settings 
* Comparison report generation
* Parser usage

# Installation

## 1. Download of source project

You can download the whole repository by using the **Git** version control system. If not already installed on your system, download and install the program from [here](https://git-scm.com/downloads). 

After successfully having installed **Git**, enter

     $ git clone https://github.com/i-net-software/pdfc-samples

on the command line. This creates a directory `pdfc-samples` in your working directory containing the Java source files for the examples.
 
Alternatively, if you are using an IDE like eclipse or IntelliJ Idea, you can clone the repository by using one of the avaiable **Git**-plugins for these tools.    

## 2. Setting up the dependencies

Download the latest PDFC SDK at https://download.inetsoftware.de/pdfc-sdk-latest.zip and unzip the archive to a local directory. After that
 
1. copy the files `java/PDFC.jar` and `java/inetcore.jar` into the `lib` folder of the project and add these jar-files to the classpath. 
2. copy at least the files `plugins/reporting.zip` and `plugins/pdfparserplugin.zip` into the `plugins`-folder of the project

After that, you are able to compile the project and run the sample programs. 
 

