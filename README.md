# SecSign ID Java Api


**Overview**

SecSign ID is a real two-factor authentication solution. It was designed to be simple, robust and fast. The reason for the simplicity is that it should be easely integrated
in many environments. The SecSign ID Java Api can be used for Java Swing Applications, Java Applets or on server side in application 
server e.g. Apache Tomcat, Oracle Glassfish in Java Server Pages or in Java Server faces.


This Java Api allows a secure login using a private key on a smart phone running SecSign ID by SecSign Technologies Inc.


**Usage**

* Include the jars `SecSignIDApi.jar` and `SecPKIApi.jar` in the classpath
* Import the API classes in the java project

* Request an authentication session
* Show the access pass to user
* Get the authentication session state 
* React to the state and have the user logged in

The example `SecSignIDApiJavaSwingExample` shows how to integrate and use the SecSign ID Java Api. A more complete tutorial is found at <https://www.secsign.com/java-api-tutorial/>
or just visit <https://www.secsign.com> for more information.

**Files**

* `SecSignIDApi.jar` - contains the SecSign ID Api itself and must be included in the classpath
* `SecPKIApi.jar` - contains the classes to communicate with the SecSign ID Server via https

* `src/com/secsign/secsignid/SecSignIDApi.java` - the Java Api class itself
* `src/com/secsign/secsignid/AuthenticationSession.java` - the AuthenticationSession class which contains information about the authentication session retrieved from ID Server
* `src/com/secsign/secsignid/AuthenticationSessionState.java` - the AuthenticationSessionState class which contains information about the session state
* `src/com/secsign/secsignid/SecSignIDException.java` - exception class.

* `src/com/secsign/secsignid/SecSignIDApiJavaSwingExample.java` - java swing example

**Examples**


Create an api object and ask the id server for a so called authentication session for a specific SecSign ID

```java
// create the SecSignID-API instance which can send requests to the SecSignID server
SecSignIDApi secSignIdApi = new SecSignIDApi();

AuthenticationSession session = null;
try
{
	session = secSignIdApi.requestAuthenticationSession(secSignIdTextField.getText(), "SecSignID Java integration example", "www.example.com", "127.0.0.1");
}
catch (SecSignIDException exception)
{
	exception.printStackTrace();
	JOptionPane.showMessageDialog(null, exception.getLocalizedMessage());
}
        
```


===============

SecSign Technologies Inc. official site: <https://www.secsign.com>
