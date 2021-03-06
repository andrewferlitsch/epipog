"# epipog v1.2" 

EpiPog is an “open source” program for developing design models for NoSQL databases. It’s target audience includes both academic 
researchers/students as well as those looking to deploy a NoSQL database for commercial uses.

# This Release

This release (v1.2) is a demonstration of the second phase of creating IP (1st stage), turning IP into Technology (2nd stage), and
Technology into Product (3rd stage). In this phase, we have developed a complete NoSQL data pipeline that supports an RDBMS, Document (JSON) and
columnar (CSV) data stores.

The database is designed using OOP principles with each stage in the pipeline uses an abstract factory design pattern. Using this pattern, any
stage in the pipeline can be replaced with another algorithm or process injection at run-time, allowing the pipeline to reconfigure and adapt to
changing conditions and input, while running in memory.

The database in this release has a complete command line interface. There is a partial micro-services interface. This **first release**, combined
with our blogs and tutorials to be an effective resource in learning and expanding your understanding, skills and development of data pipelines,
databases and extract transform load processes (ETL).

[Process Methodology](https://github.com/andrewferlitsch/epipog/blob/master/sprints/Methodology.pptx)

Epipog is developed following **Agile/Scrum** methodologies using **Test-Driven Development (TDD)**.

See our weekly to track our development and planning across sprints.

[Sprints] (https://github.com/andrewferlitsch/epipog/wiki)

# Design

This release is designed using an OOD (Object Oriented Design) using an Abstract Factory Design model. The data pipeline is partitioned into
components which are dynamically instantiated and injected from an abstract or interface definition (the "factory"). Each
component is additionally supported with a micro-service interface.

[ e2e Design](https://github.com/andrewferlitsch/epipog/blob/master/specs/stages/Data%20Pipeline.pptx)

Each module (package/library) has a detailed specification.

[Collection](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Collection.docx)

[Data](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Data.docx)

[Data Store](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/DataStore.docx)

[Ejector](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Ejector.docx)

[Index](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Index.docx)

[Parse](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Parse.docx)

[Reader](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Reader.docx)

[Schema](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Schema.docx)

[Storage](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Storage.docx)

# Build

Epipog is setup to build using Gradle. If you have not installed gradle, it is recommended
you use version 2.14.1 or later, which can be found at: http://www.gradle.org .

## Development PC

To build with Gradle pre-installed on your PC.

	> gradle build 
	
## Portable Build

To build on a PC without Gradle pre-installed, use the Gradle wrapper.

	> .\gradlew
	
## Local Build

Each package can be built separately, and it's automated unit tests ran, without a build management system, by
executing the build script in each package source directory. The build script will compile all sources in
the package, run the unit tests (_Test<N>), display any failures and report the total number of tests ran.

	> cd src\main\java\<package>	// e.g., Data, Parse, Schema, Collection, etc
	> .\build
	
# Executing

This project has a command line interface and a builtin web server has a builtin web server interface using Spring.io RESTful web service. If you are not
familiar with spring.io or using it to build a RESTful web service, a very good tutorial can be found
at: http://spring.io/guides/gs/rest-service/ .

## Command Line Interface

### Invocation

The command line version of this release can be invoked by, where %PACKAGE% is the path to the compiled packages (class libraries):

	>java -cp %PACKAGE% epipog
	
Below are the command line options:

	Usage: epipog <options>
		-c collection	# collection name
		-D datastore	# datastore (binary,csv,psv,tsv,json)
		-e				# extend schema
		-i insert		# insert
		-I file			# insert from file
		-l				# list collections
		-L				# list schema in collection
		-k link[:u]		# Index (link)
		-n				# no header (csv)
		-o orderby		# order by (sort)
		-O sort type	# sort type (quick,insertion)
		-R reader		# reader type (mem,line,mapped)
		-s select		# select fields from collection
		-S schema		# schema
		-t type			# input file type
		-T storage		# storage (single, multi)
		-v volume		# storage volume
		-V				# validate
		-w where(s)		# where clause
		-x				# delete a collection

### Examples

__Create a (empty) collection and assign a schema__

	> java -cp %PACKAGE% epipog -c mycollection -S city:string32,state:string32,postal:integer
	
__Import data from a CSV file (default) into a collection and store in JSON data store__

	> java -cp %PACKAGE% epipog -c mycollection -i data.csv -D json
	
__Query all rows in the collection__

	> java -cp %PACKAGE% epipog -c mycollection -s "*"
	
__Query for all rows where city > H and output only city and postal__

	> java -cp %PACKAGE% epipog -c mycollection -s "city,postal" -w "city>H"
	
__Query for all rows and sort by state in ascending order__

	> java -cp %PACKAGE% epipog -c mycollection -s "*" -o state
	
__Delete a collection__
	
	> java -cp %PACKAGE% epipog -x -c mycollection

## Web Service Interface

The endpoints on the service act as a micro-service, where the action has persistence across micro-service invocations.

### Starting the web server

	> java -jar build\libs\epipog-v1.1.jar
	
### Making REST api calls to the packages

Each package has its own URL endpoint:

	http://localhost:8080/data			# Data (Model)
	http://localhost:8080/parse			# Parse
	http://localhost:8080/schema		# Schema
	
Each API takes a set of paramters (explained below) and returns a JSON response object in
the form:

	[ "id": number, "status": code, "content": string ]
	
Each invocation will set a unique ID number, incrementing as an ordinal starting at 1. The
status field is the HTTP status code. Either 200 will be returned (OK), or 500 indicating an
error occurred.

The content field holds a JSON object as the response, when the status code is 200, or an
error string when the status code is 500.

Each endpoint can take the following parameters:
	
	method=method-name							# Required
	type=data-type								# Optional
	arg=value-to-pass-as-an-argument-to-method	# Optional

### Data (Model)

The Data (Model) package handles the representation of data types within the data pipeline. This package can be imported
as a Java library with the following import statement:

	import epipog.data.*;
	
[Specification](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Data.docx)
	
When using the epipog web server appication, it is accessed as a micro-service.

[Micro-Service](https://github.com/andrewferlitsch/epipog/blob/master/src/main/java/webserver/README.data.md)


### Parse

The Parse package handles parsing of input data sets. This package can be imported as a Java library with the following
import statement:

	import epipog.parse.*;
	
[Specification - Parse](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Parse.docx)
	
[Specification - Reader](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Reader.docx)
	
[Specification - Ejector](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Ejector.docx)
	
When using the epipog web server appication, it is accessed as a micro-service.

[Micro-Service](https://github.com/andrewferlitsch/epipog/blob/master/src/main/java/webserver/README.parse.md)


### Schema

The Schema package applying a schema to a dataset. This package can be imported as a Java library with the following
import statement:

	import epipog.schema.*;
	
[Specification](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Schema.docx)

[Micro-Service](https://github.com/andrewferlitsch/epipog/blob/master/src/main/java/webserver/README.schema.md)


### Collection

The Collection package handles representing related data as a collection. This package can be imported as a Java library with the following
import statement:

	import epipog.collection.*;
	
[Specification](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Collection.docx)

[Micro-Service](https://github.com/andrewferlitsch/epipog/blob/master/src/main/java/webserver/README.collection.md)


### Index

The Index package handles indexing data fields within a collection. This package can be imported as a Java library with the following
import statement:

	import epipog.index.*;
	
[Specification](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Index.docx)

[Micro-Service](https://github.com/andrewferlitsch/epipog/blob/master/src/main/java/webserver/README.index.md)

### Data Store

The Data Store package handles the data store representation of a collection. This package can be imported as a Java library with the following
import statement:

	import epipog.datastore.*;
	
[Specification](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/DataStore.docx)

[Micro-Service](https://github.com/andrewferlitsch/epipog/blob/master/src/main/java/webserver/README.datastore.md)

### Storage

The Storage package handles physically store and I/O of a collection. This package can be imported as a Java library with the following
import statement:

	import epipog.storage.*;
	
[Specification](https://github.com/andrewferlitsch/epipog/blob/master/specs/modules/Storage.docx)

[Micro-Service](https://github.com/andrewferlitsch/epipog/blob/master/src/main/java/webserver/README.storage.md)