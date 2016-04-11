1- What is this?
    * This is example code for a Java Server Faces component allowing a secure 
      login using a private key on a smart phone running SecSignID by SecSign 
      Technologies Inc.
    * SecSign Technologies Inc. official site: https://www.secsign.com

2- How to use it?
    * You can use and alter the example code to integrate a SecSignID login
      into your own projects.
    * Please install for example Java 1.6 EE and the contained GlassFish server 
      as described in Oracle's "The Java EE 6 Tutorial".
    * In order to re-compile the SecSignIDBean example you will need to put 
      C:\glassfish3\glassfish\modules\javax.servlet-api.jar and 
      C:\glassfish3\glassfish\modules\javax.faces.jar 
      into your class path.
    * In order re-build the web module secsignid.war of the example type:
      cd target
      "c:\Program Files (x86)\Java\jdk1.6.0_27"\bin\jar cvf ../secsignid.war .
    * Deploy secsignid.war in the GlassFish server at:
      http://localhost:4848/
    * Enjoy the running example at:
      http://localhost:8080/secsignid/

Copyright:
    * Copyright (C) 2012 SecSign Technologies Inc. All rights reserved.
