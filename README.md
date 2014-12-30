Spring JDBC Placeholder Configurer
==================================

This implementation of [PropertyPlaceholderConfigurer](http://docs.spring.io/spring/docs/3.2.x/javadoc-api/org/springframework/beans/factory/config/PropertyPlaceholderConfigurer.html) resolve placeholders using as JDBC DataSource and a SQL statement.

Much of the test code has been taken from the [Spring Framwork code base](https://github.com/spring-projects/spring-framework/).

Travis CI : [![Build Status](https://secure.travis-ci.org/Redpill-Linpro/spring-config-jdbc.png)](http://travis-ci.org/Redpill-Linpro/spring-config-jdbc)

Usage example
-------------

1. Build and install into your local maven repo
```shell
 ./gradlew publishToMavenLocal
```
2. Include a dependecy in your project

3. Add a placehoder into your spring configuration 
```xml
    <bean class="com.redpill_linpro.springframework.beans.factory.config.JdbcPlaceholderConfigurer">
            <property name="dataSource" ref="dataSource" />
            <property name="selectStatement" value="SELECT value FROM properties WHERE key = ?" />
    </bean>
```
4. Use ${propertyPlaceholders} as normal in your spring configuration. The value will be looked up in the database using the SQL statment you specified. 
