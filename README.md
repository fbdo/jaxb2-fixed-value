JAXB 2 Fixed Value Plugin
=================

# Introduction

This plugin is highly inspired by the [JAXB 2 Default Value Plugin](https://java.net/projects/jaxb2-commons/pages/Default-Value), and share a lot of common code. This is an XJC plugin to set fixed values in the XJC generated classes based on the fixed attribute to <xs:element>. The fixed attribute is documented [here](http://www.w3.org/TR/xmlschema-0/#OccurrenceConstraints). Note that JAXB handles fixed values for <xs:attribute> natively, so this plugin is not necessary for an attribute fixed.


The plugin is particularly useful while generating Value Objects for a user interface from an XML schema. User interface tags such as the Struts HTML tags use reflection on bean properties to render themselves, so it is often useful to have a set of sensible fixed values set in the Java Beans mapping to the user interface themselves.

The XML schema below describes a generic product and some subclasses, like books, clothes and groceries. It also uses a string to indicate the product group, and it's unit type. Since we already know the product classification and unit type for the subclasses, it will great to enforce the correct value. The XML schema "fixed" attribute is used to achieve this.

```xml
        <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" elementFormDefault="qualified">
        <xs:element name="product" type="Product" />
        
        <xs:complexType name="Product">
          <xs:sequence>
            <xs:element name="price" type="xs:string"/>
            <xs:element name="street" type="xs:string"/>
            <xs:element name="apt" type="xs:string"/>
            <xs:element name="city" type="xs:string"/>
            <xs:element name="state" type="xs:string"/>
            <xs:element name="ZIP" type="xs:string"/>
          </xs:sequence>
        </xs:complexType>
        
        
        
        </xs:schema>
```

The XJC generated code for the schema (comments removed for brevity):


```java
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "firstName",
        "middleName",
        "lastName",
        "residentialAddress",
        "mailingAddressIdentical",
        "mailingAddress"
    })
    @XmlRootElement(name = "Person")
    public class Person {
    
        @XmlElement(required = true)
        protected String firstName;
        @XmlElement(required = true)
        protected String middleName;
        @XmlElement(required = true)
        protected String lastName;
        @XmlElement(required = true)
        protected Address residentialAddress;
        @XmlElement(defaultValue = "true")
        protected boolean mailingAddressIdentical;
        protected Address mailingAddress;
    
        public String getFirstName() {
            return firstName;
        }
    
        public void setFirstName(String value) {
            this.firstName = value;
        }
    
        public String getMiddleName() {
            return middleName;
        }
    
        public void setMiddleName(String value) {
            this.middleName = value;
        }
    
        public String getLastName() {
            return lastName;
        }
    
        public void setLastName(String value) {
            this.lastName = value;
        }
    
        public Address getResidentialAddress() {
            return residentialAddress;
        }
    
        public void setResidentialAddress(Address value) {
            this.residentialAddress = value;
        }
    
        public boolean isMailingAddressIdentical() {
            return mailingAddressIdentical;
        }
    
        public void setMailingAddressIdentical(boolean value) {
            this.mailingAddressIdentical = value;
        }
    
        public Address getMailingAddress() {
            return mailingAddress;
        }
    
        public void setMailingAddress(Address value) {
            this.mailingAddress = value;
        }
    
    }
```

# Usage

For information about how to use XJC plugins in general, see [here](http://weblogs.java.net/blog/kohsuke/archive/2005/06/writing_a_plugi.html).




Recently on BuildHive
-----------------------------
[![Build Status](https://buildhive.cloudbees.com/job/fbdo/job/jaxb2-fixed-value/badge/icon)](https://buildhive.cloudbees.com/job/fbdo/job/jaxb2-fixed-value/)
