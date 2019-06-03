# PDFC-Samples

This project provides code samples for the API usage of the [i-net PDF Content Comparer](https://www.inetsoftware.de/products/pdf-content-comparer "i-net PDF Content Comparer"). 
The samples are all written in Java and contain numerous examples for 

* Basic comparison procedure calls
* Analysis of the comparison results
* Customization of the comparison parameters by use of configuration settings
* Use of filters to control the comparison input  
* Generation of comparison reports with different format and layout
* Usage of the PDF Parser to inspect PDF documents 

To run the example projects, you need to have a Java Runtime Version 8 or higher installed. 

# Installation

## 1. Download of source project

You can download the whole repository by using the **Git** version control system. If not already installed on your system, download and install the program from [here](https://git-scm.com/downloads). 

After successfully having installed **Git**, enter

     $ git clone https://github.com/i-net-software/pdfc-samples
     $ git checkout -b <PDFC VERSION> origin/<PDFC VERSION>

on the command line. This creates a directory `pdfc-samples` in your working directory containing the Java source files for the examples.
 
Alternatively, if you are using an IDE like eclipse or IntelliJ Idea, you can clone the repository by using one of the avaiable **Git**-plugins for these tools.    

## 2. Setting up the dependencies

Download the latest PDFC SDK at https://download.inetsoftware.de/pdfc-sdk-latest.zip and unzip the archive to a local directory. After that
 
1. copy the files `i-net PDFC/java/PDFC.jar` and `i-net PDFC/java/inetcore.jar` into the `lib` folder of the project and add these jar-files to the classpath.
2. copy the directory `i-net PDFC/lang` into the project folder (only for tesseract (OCR) usage)
3. copy at least the files `plugins/reporting.zip`, all `plugins/parser.*.zip` and all `plugins/filter.*.zip`into the
`plugins`-folder of the project

After that, you are able to compile the project and run the sample programs. 
 

