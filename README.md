# Continuous Perf Test agent

[![Build Status](https://travis-ci.org/continuousperftest/agent-java.svg?branch=master)](https://travis-ci.org/continuousperftest/agent-java)[![Maintainability](https://api.codeclimate.com/v1/badges/db073d11f43ce2552fe5/maintainability)](https://codeclimate.com/github/continuousperftest/agent-java/maintainability)

---
- [About the Continuous Perf Test](https://github.com/continuousperftest/agent-java#about-the-continuous-perf-test)
- [Support](https://github.com/continuousperftest/agent-java#support)
- [Maven](https://github.com/continuousperftest/agent-java#maven)
  - [Dependency](https://github.com/reportportal/agent-java-testNG#dependency)
  - [Surefire Plugin](https://github.com/reportportal/agent-java-testNG#surefire-plugin)
- [Launch parameters](https://github.com/continuousperftest/agent-java#launch-parameters)
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

| 	      **Options**      		|       **Default value**       |        						**Description**        		                   |
|-------------------------------|-------------------------------|------------------------------------------------------------------------------|
| perf-test.isEnabled    		|false - {true, false}          |Turns on metrics collection if true is specified							   |
| perf-test.exporter     		|local - {local, remote, opted} |Chooses a mode for reporting collected metric   							   |
| perf-test.results.host 		|http://127.0.0.1:8095          |Host where collected metrics are sent to if the exporter is set to remote     |                  
| perf-test.results.directory   |perf-test-results              |Folder name where collected metrics are stored if the exporter is set to local|  


Launch parameters can be set using the following ways depending upon how you launch your automated tests:
-	If you use Maven to run your automated tests you can utilize `Maven CLI option` such as `-D` to set values to parameters, for instance, `mvn clean test -Dperf-test.isEnabled=true -Dperf-test.exporter=local`
-	If you launch your automated tests as a java application, please make sure that launch parameters are defined as system properties


## Contributing

Please read [CONTRIBUTING.md](https://github.com/continuousperftest/agent-java/blob/master/CONTRIBUTING.md) for details of the process for submitting pull requests to us.


## License

This project is licensed under the GPL-3.0 License - see the [LICENSE.md](https://github.com/continuousperftest/agent-java/blob/master/LICENSE) file for details