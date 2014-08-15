JAXB 2 Fixed Value Plugin [![Build Status](https://buildhive.cloudbees.com/job/fbdo/job/jaxb2-fixed-value/badge/icon)](https://buildhive.cloudbees.com/job/fbdo/job/jaxb2-fixed-value/)
=================

# Introduction

This plugin is highly inspired by the [JAXB 2 Default Value Plugin](https://java.net/projects/jaxb2-commons/pages/Default-Value), and share a lot of common code. This is an XJC plugin to set fixed values in the XJC generated classes based on the fixed attribute to <xs:element>. The fixed attribute is documented [here](http://www.w3.org/TR/xmlschema-0/#OccurrenceConstraints). Note that JAXB handles fixed values for <xs:attribute> natively, so this plugin is not necessary for an attribute fixed.


The plugin is particularly useful while generating Value Objects for a user interface from an XML schema. User interface tags such as the Struts HTML tags use reflection on bean properties to render themselves, so it is often useful to have a set of sensible fixed values set in the Java Beans mapping to the user interface themselves.

The XML schema below describes a generic product and some subclasses, like books, clothes and groceries. It also uses a string to indicate the product group, and it's unit type. Since we already know the product classification and unit type for the subclasses, it will great to enforce the correct value. The XML schema "fixed" attribute is used to achieve this.

```xml
        <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" elementFormDefault="qualified">
        <xs:element name="product" type="Product" />
        
        <xs:complexType name="Product" abstract="true">
            <xs:sequence>
                <xs:element name="price" type="Money"/>
            </xs:sequence>
        </xs:complexType>
        
        <xs:complexType name="Book">
            <xs:complexContent>
                <xs:extension base="Product">
                    <xs:sequence>
                        <xs:element name="unit" type="UnitOfMeasurement" fixed="unit" />
                        <xs:element name="group" type="ProductGroup" fixed="Books"/>
                        <xs:element name="isbn" type="xs:string" />
                    </xs:sequence>
                </xs:extension>
            </xs:complexContent>
        </xs:complexType>
                
        </xs:schema>
```

The XJC generated code for the schema (comments removed for brevity):


```java
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "Book", propOrder = {
        "unit",
        "group",
        "isbn"
    })
    public class Book
        extends Product
    {
    
        @XmlElement(required = true)
        protected UnitOfMeasurement unit;
        @XmlElement(required = true)
        protected ProductGroup group;
        @XmlElement(required = true)
        protected String isbn;
    
        /**
         * Gets the value of the unit property.
         * 
         * @return
         *     possible object is
         *     {@link UnitOfMeasurement }
         *     
         */
        public UnitOfMeasurement getUnit() {
            return unit;
        }
    
        /**
         * Sets the value of the unit property.
         * 
         * @param value
         *     allowed object is
         *     {@link UnitOfMeasurement }
         *     
         */
        public void setUnit(UnitOfMeasurement value) {
            this.unit = value;
        }
    
        /**
         * Gets the value of the group property.
         * 
         * @return
         *     possible object is
         *     {@link ProductGroup }
         *     
         */
        public ProductGroup getGroup() {
            return group;
        }
    
        /**
         * Sets the value of the group property.
         * 
         * @param value
         *     allowed object is
         *     {@link ProductGroup }
         *     
         */
        public void setGroup(ProductGroup value) {
            this.group = value;
        }
    
        /**
         * Gets the value of the isbn property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIsbn() {
            return isbn;
        }
    
        /**
         * Sets the value of the isbn property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIsbn(String value) {
            this.isbn = value;
        }
    
    }
```

Note that the group property's does not map to the variable declaration - which, being a ProductGroup, defaults to null. Wouldn't it be nice to have the declaration reflect this as well? Enter the fixed value plugin. Here's the declaration section of Book.java with the fixed value plugin enabled:

```java
    @XmlElement(required = true)
    protected UnitOfMeasurement unit = UnitOfMeasurement.UNIT;
    @XmlElement(required = true)
    protected ProductGroup group = ProductGroup.BOOKS;
    @XmlElement(required = true)
    protected String isbn;
```

# Usage

For information about how to use XJC plugins in general, see [here](http://weblogs.java.net/blog/kohsuke/archive/2005/06/writing_a_plugi.html).

This is a sample build.xml that shows how the plugin is used. It's used to build the sample classes in the samples/ directory:

```xml
    <?xml version="1.0" ?>
    
    <project name="samples" default="compile" basedir=".">
      <description>Builds the default value plugin sample</description>
    
      <property file="../build.properties"/>
    
      <!-- Remove an earlier build by deleting the "build" directory -->
      <target name="clean">
        <delete dir="gen-src"/>
      </target>
    
      <!-- Compile classes generated by XJC compiler -->
      <target name="compile" depends="gen-src">
        <javac compiler="javac1.5" srcdir="gen-src/generated" includes="*.java">
          <classpath>
    	<fileset dir="${jaxb.lib.dir}" includes="*.jar"/>
          </classpath>
        </javac>
      </target>
    
      <!-- Use XJC task to compile schema to Java code -->
      <target name="gen-src">
        <mkdir dir="gen-src"/>
        <xjc removeOldOutput="yes" schema="sample.xsd" target="gen-src">
          <arg value="-extension"/>
          <arg value="-Xfixed-value"/>
        </xjc>
      </target>
      
      <!-- Defines XJC task -->
      <taskdef name="xjc" classname="com.sun.tools.xjc.XJCTask">
        <classpath>
          <fileset dir="${jaxb.lib.dir}" includes="*.jar"/>
          <pathelement location="../build/jaxb2-fixed-value-1.2.jar"/>
        </classpath>
      </taskdef>
    
    </project>
```

A sample for using this plugin with Maven can be found on the source code (see this [pom.xml](https://github.com/fbdo/jaxb2-fixed-value/blob/master/src/it/fixedvalue-it/pom.xml)). Here a small snippet:

```xml
    <build>
          <plugins>
              <plugin>
                  <groupId>org.jvnet.jaxb2.maven2</groupId>
                  <artifactId>maven-jaxb2-plugin</artifactId>
                  <executions>
                      <execution>
                          <goals>
                              <goal>generate</goal>
                          </goals>
                      </execution>
                  </executions>
                  <configuration>
                      <args>
                          <arg>-Xfixed-value</arg>
                      </args>
                      <plugins>
                          <plugin>
                              <groupId>com.github.fbdo</groupId>
                              <artifactId>jaxb2-fixed-value</artifactId>
                              <version>1.2</version>
                          </plugin>
                      </plugins>
                  </configuration>
              </plugin>
              <plugin>
                  <inherited>true</inherited>
                  <groupId>org.apache.maven.plugins</groupId>
                  <artifactId>maven-compiler-plugin</artifactId>
                  <configuration>
                      <source>1.5</source>
                      <target>1.5</target>
                  </configuration>
              </plugin>
          </plugins>
    </build>
```