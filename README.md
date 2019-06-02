# Continuous Perf Test agent

[![Build Status](https://travis-ci.org/continuousperftest/agent-java.svg?branch=master)](https://travis-ci.org/continuousperftest/agent-java)

---
- [About the Continuous Perf Test](https://github.com/continuousperftest/agent-java#about-the-continuous-perf-test)
- [Support](https://github.com/continuousperftest/agent-java#support)
- [Maven](https://github.com/continuousperftest/agent-java#maven)
  - [Dependency](https://github.com/continuousperftest/agent-java#dependency)
  - [Surefire Plugin](https://github.com/continuousperftest/agent-java#surefire-plugin)
- [Launch parameters](https://github.com/continuousperftest/agent-java#launch-parameters)
  - [Exporter](https://github.com/continuousperftest/agent-java#exporter)
  	- [Local](https://github.com/continuousperftest/agent-java#local)
  	- [Remote](https://github.com/continuousperftest/agent-java#remote)
  	- [Opted](https://github.com/continuousperftest/agent-java#opted)
- [UI application](https://github.com/continuousperftest/agent-java#ui-application)
- [Released versions](https://github.com/continuousperftest/agent-java/blob/master/CHANGES.md)
---


## About the Continuous Perf Test

Continuous Perf Test is about measuring performance of HTTP calls handling that are emitted from test frameworks made up of HTTP clients and xUnit frameworks to give Quality Assurance Engineers an idea of how a system under test (web service) operates from performance standpoint from a release to a release while running a suite of functional automated tests against the system under test. The detailed information about Continuous Perf Test could be found [here](https://www.linkedin.com/pulse/continuous-perf-test-aleh-struneuski).


## Support

As of now, Continuous Perf Test agent supports the following HTTP clients and xUnit frameworks:

-	HTTP clients:
	-	[Apache HTTP client, version 4.X.X](https://hc.apache.org/httpcomponents-client-ga)
-	xUnit frameworks:
	- 	[TestNG, version 6.X.X](https://testng.org/doc/index.html)

If you would like to utilize the library and something needed is not supported yet, feel free to request it using [github issues tracker](https://github.com/continuousperftest/agent-java/issues)


## Maven:

### Dependency

Add to `POM.xml`

```xml
<dependency>
    <groupId>com.github.continuousperftest</groupId>
    <artifactId>agent-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Surefire Plugin

If you are running automated tests using `maven surefire plugin`, it is required to update `maven-surefire-plugin` section in `POM.xml`

```xml
<properties>
	<maven-surefire-plugin>2.9</maven-surefire-plugin>
	<aspectj.version>1.8.13</aspectj.version>
</properties>
	
<plugins>
    [...]
	<plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-surefire-plugin</artifactId>
		<version>${surefire.plugin.version}</version>
		<configuration>
			<argLine>-XX:-UseSplitVerifier</argLine>
			<argLine>-javaagent:${user.home}/.m2/repository/org/aspectj/aspectjweaver/${aspectj.version}/aspectjweaver-${aspectj.version}.jar</argLine>
			<systemPropertyVariables>
				<project.build.directory>${project.build.directory}</project.build.directory>
			</systemPropertyVariables>
		</configuration>
		<dependencies>
			<dependency>
				<groupId>org.aspectj</groupId>
				<artifactId>aspectjweaver</artifactId>
				<version>${aspectj.version}</version>
			</dependency>
		</dependencies>
	</plugin>
    [...]
</plugins>
```


## Launch parameters

| 	      **Parameters**      	|       **Default value**       |        						**Description**        		                   |
|-------------------------------|-------------------------------|------------------------------------------------------------------------------|
| perf-test.isEnabled    		|false - {true, false}          |Turns on metrics collection if true is specified							   |
| perf-test.exporter     		|local - {local, remote, opted} |Chooses a mode for reporting collected metric   							   |
| perf-test.results.host 		|http://127.0.0.1:8095          |Host where collected metrics are sent to if the exporter is set to remote     |                  
| perf-test.results.directory   |perf-test-results              |Folder name where collected metrics are stored if the exporter is set to local|  


Launch parameters can be set using the following ways depending upon how you launch your automated tests:
-	If you use Maven to run your automated tests you can utilize `Maven CLI option` such as `-D` to set values to parameters, for instance, `mvn clean test -Dperf-test.isEnabled=true -Dperf-test.exporter=local`
-	If you launch your automated tests as a java application, please make sure that launch parameters are defined as system properties

### Exporter

Exporter parameter is used to set an exporting strategy that will be used to send collected performance metrics to its final store. There are three options available.

#### Local

Specifying the following Maven argument `-Dperf-test.exporter=local`, collected performance metrics will be stored to a folder from which tests are launched.
This folder, by default, is `perf-test-results`, but this folder can be changed using the following Maven argument `perf-test.results.directory`.

#### Remote

- Using this option, namely `-Dperf-test.exporter=remote`, collected performance metrics will be sent to a remote storage so that metrics can be analyzed after several test launches to observer performance trends later on.

- Continuous Perf Test is delivered with both `the remote storage and UI application` for demonstration purpose.

- [The UI application](http://52.202.21.1) serves as a dashboard where performance trends can be seen based on collected performance metrics by Continuous Perf Test agent.

Note: All the data (performance metrics) saved in the remote storage is cleaned once per week.

#### Opted

If you would like to use your own exporter for collecting performance metrics, it is required to specify the following Maven argument `-Dperf-test.exporter=opted`.
Besides, you are expected to implement the following interface in your project `com.github.continuousperftest.service.MetricExporterService` and register the implemented exported using ServiceLoader.

##### Code example of how to implement your own exporter

```java
import com.github.continuousperftest.entity.domain.Perfomance;
import com.github.continuousperftest.service.MetricExporterService;
import java.util.List;

public class YourOwnExporterServiceImpl implements MetricExporterService {

  @Override
  public void export(List<Perfomance> metrics) {

    // save a list of Performance object

  }

}
```

##### Using ServiceLoader to register the implemented exporter

You can use service loaded mechanism for adding your exporter to the Continuous Perf Test agent. JDK offers a very elegant mechanism to specify implementations of interfaces on the class path via the ServiceLoader class. With ServiceLoader, all you need to do to register implemented exporter is the implementation of your exporter in your project and a configuration file. When you run tests, the Continuous Perf Test agent will automatically find your implementation of your exporter. 

To register the implemented exported, the following steps are to be done:
- Create the folder `META-INF/services` in the `src/main/resources` folder;
- Create a file that is called `com.github.continuousperftest.service.MetricExporterService` inside the `META-INF/services` folder;
- Write full name of the implemented exporter in the first line, for instance:  packge.where.your.exporter.is.placed.YourOwnExporterServiceImpl.


## UI application

- The application is hosted using the following [host](http://52.202.21.1)

- To use the dashboard that the Continuous Perf Test agent is delivered with, it is required to run tests using TestNG groups since charts are built by selecting data for charts based on TestNG groups and a number of threads that tests were launched in.


## Contributing

Please read [CONTRIBUTING.md](https://github.com/continuousperftest/agent-java/blob/master/CONTRIBUTING.md) for details of the process for submitting pull requests to us.


## License

This project is licensed under the GPL-3.0 License - see the [LICENSE.md](https://github.com/continuousperftest/agent-java/blob/master/LICENSE) file for details