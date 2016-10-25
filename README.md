ForceFlow is a simple library to automate build and deployment-related tasks on the Force.com platform. 
It is built on top of [Apache Ant](http://ant.apache.org/), so it can be used in combination with the [Force.com migration tool](https://developer.salesforce.com/page/Force.com_Migration_Tool) and with any continuous integration or build solutions, such as [Atlassian Bamboo](https://www.atlassian.com/software/bamboo) or [Jenkins CI](https://jenkins-ci.org/).

## Features so far
* Schedule and abort Apex jobs
* Manage remote site settings
* Execute Apex code
* Run Apex tests (with XML reports)
* Insert and delete records
* Support for flow deactivation and deletion

## Usage
### Create a build file
If you are not familiar with Ant and you need a template for your script you can copy the one below.
```XML
<?xml version="1.0" encoding="UTF-8"?>
<project name="forceflow-sample" xmlns:ff="antlib:com.spaceheroes">

	<!-- 
	This assumes forceflow.jar is copied 
	in a folder named "lib"
	====================================== -->
	<path id="ant.additions.classpath">
		<fileset dir="./lib/" includes="*.jar"/>
	</path>
	<taskdef resource="antlib.xml" uri="antlib:com.spaceheroes" classpathref="ant.additions.classpath" />
	
	<!--
	You can now define Ant targets and 
	use any ForceFlow tasks within.
	====================================== -->

</project>
```

### Execute Apex code
You can execute an Apex script as follows.
```XML
<ff:apex username="..." password="..." serverurl="...">
	<![CDATA[		
		List<Contact> cts = [SELECT id,Email FROM Contact WHERE (Email<>null AND (NOT Email LIKE '%.nospam'))];
		for (Contact ct : cts) { 
			ct.Email = ct.Email + '.nospam'; 
		}
		update cts;
	]]>
</ff:apex>
```

### Remote Site Settings
Use this to create or update remote site settings.
```XML
<ff:remotesite name="Google_APIs" url="https://www.googleapis.com" disableProtocolSecurity="true" username="..." password="..." serverurl="...">
	Allows queries to the Google Maps API
</ff:remotesite>
```

### Clear scheduled jobs
Use this tag to abort all the jobs that are currently scheduled. 
```XML
<ff:clearschedule username="..." password="..." serverurl="..." /> 	
```

### Schedule an Apex job
You can use this task to schedule the execution of a batch class.
```XML
<ff:scheduleapex username="..." password="..." serverurl="..." className="MySchedulableClass" cron="0 0 12 1/1 * ? *" />	
```

### Insert a record
Works with both objects and custom settings.
```XML
<ff:insert object="Contact" username="..." password="..." serverurl="...">
	<property name="FirstName" value="Some"/>
	<property name="LastName" value="Dude"/>
	<property name="Email" value="somedude@salesforce.com"/>
</ff:insert>
```

### Delete all records
You can delete all the records from an object or a custom settings. It's bulkified, so it works also with very large tables.
```XML
<ff:deleteall object="Contact" username="..." password="..." serverurl="..."/>
```

### Run all tests (with XML test report)
Generates a full test report in the JUnit XML format (output file: _test-report.xml_). This report can be read by Atlassian Bamboo to [display test results of a build](https://confluence.atlassian.com/bamboo/viewing-test-results-for-a-build-289276936.html).
```XML
<ff:runtests username="..." password="..." serverurl="..." />
```

### Deactivate flows
It's possible to use ForceFlow in composition with the Force Migration Tool to deactivate all flows in a Salesforce organisation.

```XML
<target name="deactivateFlows">
       
    <delete dir="temp"/>

    <!-- retrieve existing flows -->
    <ff:createFlowRetrieveAllManifest destinationPath="temp/existing" />
    <sf:retrieve 
        username="${sf.username}" 
        password="${sf.password}" 
        serverurl="${sf.serverurl}" 
        retrieveTarget="temp/existing" 
        unpackaged="temp/existing/package.xml"/>

    <!-- create a manifest to inactivate all the flows, then deploy it -->
    <ff:createFlowInactivateManifest 
        sourcePath="temp/existing"  
        destinationPath="temp/inactivation" />
    <sf:deploy 
        username="${sf.username}" 
        password="${sf.password}" 
        serverurl="${sf.serverurl}" 
        deployRoot="temp/inactivation" />

    <delete dir="temp"/>
</target>
```

## License
ForceFlow is available under the MIT license. Please see the [LICENSE](LICENSE.md) for details.
