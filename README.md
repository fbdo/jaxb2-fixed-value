jaxb2-fixed-value
=================

This is an XJC plugin to set fixed values in the XJC generated classes based on the fixed attribute to <xs:element>. The fixed attribute is documented [here](http://www.w3.org/TR/xmlschema-0/#OccurrenceConstraints). Note that JAXB handles fixed values for <xs:attribute> natively, so this plugin is not necessary for an attribute fixed.


The plugin is particularly useful while generating Value Objects for a user interface from an XML schema. User interface tags such as the Struts HTML tags use reflection on bean properties to render themselves, so it is often useful to have a set of sensible defaults set in the Java Beans mapping to the user interface themselves.

The XML schema below describes a person and their address. It also uses a boolean flag to indicate whether the user's mailing address is different from their residential address. Since a person's mailing address is typically the same as their residential address, it would be nice to default the boolean to true. The XML schema "default" attribute is used to achieve this.

        <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" elementFormDefault="qualified">
        <xs:element name="Person">
          <xs:complexType>
            <xs:sequence>
              <xs:element name="firstName" type="xs:string"/>
              <xs:element name="middleName" type="xs:string"/>
              <xs:element name="lastName" type="xs:string"/>
              
              <xs:element name="residentialAddress" type="Address" minOccurs="1" maxOccurs="1"/>
              <xs:element name="mailingAddressIdentical" type="xs:boolean" default="true"/>
              <xs:element name="mailingAddress" type="Address" minOccurs="0" maxOccurs="1"/>
            </xs:sequence>
          </xs:complexType>
        </xs:element>
        
        <xs:complexType name="Address">
          <xs:sequence>
            <xs:element name="careOf" type="xs:string"/>
            <xs:element name="street" type="xs:string"/>
            <xs:element name="apt" type="xs:string"/>
            <xs:element name="city" type="xs:string"/>
            <xs:element name="state" type="xs:string"/>
            <xs:element name="ZIP" type="xs:string"/>
          </xs:sequence>
        </xs:complexType>
        </xs:schema>

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

Recently on BuildHive
-----------------------------
[![Build Status](https://buildhive.cloudbees.com/job/fbdo/job/jaxb2-fixed-value/badge/icon)](https://buildhive.cloudbees.com/job/fbdo/job/jaxb2-fixed-value/)
