ForceFlow is a simple library to automate build and deployment-related tasks on the Force.com platform. 
It is built on top of [Apache Ant](http://ant.apache.org/), so it can be used in combination with the [Force.com migration tool](https://developer.salesforce.com/page/Force.com_Migration_Tool) and with any continuous integration or build solutions, such as [Atlassian Bamboo](https://www.atlassian.com/software/bamboo) or [Jenkins CI](https://jenkins-ci.org/).

## Features so far
* Schedule and abort Apex jobs
* Execute Apex code
* Run Apex tests (with XML reports)
* Insert and delete records

## Usage
### Create a build file
```XML
<?xml version="1.0" encoding="UTF-8"?>
<project name="forceflow-sample" xmlns:forceflow="antlib:com.spaceheroes">

	<!-- 
	This assumes forceflow.jar is copied 
	in a folder named "lib"
	====================================== -->
	<path id="ant.additions.classpath">
		<fileset dir="./lib/" includes="*.jar"/>
	</path>
	<taskdef resource="com/spaceheroes/task/antlib.xml" uri="antlib:com.spaceheroes" classpathref="ant.additions.classpath" />
	
	<!--
	You can now define Ant targets and 
	use any ForceFlow tasks within.
	====================================== -->

</project>
```

### Execute Apex code
```XML
<forceflow:apex username="..." password="..." serverurl="...">
	<![CDATA[		
		List<Contact> cts = [SELECT id,Email FROM Contact WHERE (Email<>null AND (NOT Email LIKE '%.nospam'))];
		for (Contact ct : cts) { 
			ct.Email = ct.Email + '.nospam'; 
		}
		update cts;
	]]>
</forceflow:apex>
```

### Clear scheduled jobs
Aborts all the jobs currently scheduled. 
```XML
<forceflow:clearschedule username="..." password="..." serverurl="..." /> 	
```

### Run all tests (with XML test report)
Generates a full test report in the JUnit XML format (output file: _test-report.xml_). This report can be read by Atlassian Bamboo to [display test results of a build](https://confluence.atlassian.com/bamboo/viewing-test-results-for-a-build-289276936.html).
```XML
<forceflow:runtests username="..." password="..." serverurl="..." />
```

### Schedule an Apex job
You can use this to schedule the execution of a batch class.
```XML
<forceflow:scheduleapex username="..." password="..." serverurl="..." className="MySchedulableClass" cron="0 0 12 1/1 * ? *" />	
```

### Insert a record
Works with both objects and custom settings.
```XML
<forceflow:insert object="Contact" username="..." password="..." serverurl="...">
	<property name="FirstName" value="Some"/>
	<property name="LastName" value="Dude"/>
	<property name="Email" value="somedude@salesforce.com"/>
</forceflow:insert>
```

### Delete all records
Deletes all the records from an object or a custom settings. It's bulkified, so it works also with very large tables.
```XML
<forceflow:deleteall object="Contact" username="..." password="..." serverurl="..."/>
```
