ProxyServer
===========

IDE : Netbeans
Browser : Mozilla Firefox

Requirement :

Java JRE 7

How to compile it :

- go to JavaProxyServer/src
- run command : javac  -sourcepath . server/main/Proxy.java 

How to run it :

- go to JavaProxyServer/src
- run command : java -cp . server.main.Proxy port

port is optionnal, default port is 1111

Example :

java -cp . server.main.Proxy
java -cp . server.main.Proxy 1234


Tested with :

- http://www.perdu.com
- http://www.aloispaulus.be
- http://www.w3.org/Protocols/rfc2616/rfc2616.html
- http://www.google.be/
