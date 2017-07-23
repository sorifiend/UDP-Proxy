# UDP-Proxy
Simple java proxy for redirectring UDP traffic from an incoming port to an outgoing port.


**Update June 19, 2017:**

The latest artifact can be found here: https://github.com/sorifiend/UDP-Proxy/tree/master/compiled-jar

---
Usage Instructions:
---

```
  Usage:  java -jar UDP_Proxy-1.1.jar  Destination_Server_IP  Destination_Server_Port  Incoming_Proxy_Port
```

  Examples:
  
  Scenario 1:
  
     Use case:
  
     Forward teamspeak server traffic to local port 9988.
     (Handy for multiple child VM servers all on one IP address by using a proxy for different external ports):
  
     Specifics:
     
     Incoming traffic on port 9987 will be redirected to port 9988 on the localhost (port 127.0.0.1)

     Command to run:

     java -jar UDP_Proxy-1.1.jar 127.0.0.1 9988 9987

     
  Scenario 2:
  
     Use case:
  
     Forward another computers UDP gaming traffic to a server via your IP address.
     (Handy for working around a firewall that IP blocked an external address)
     
     Specifics:
     
     Incoming traffic on port 23010 will be redirected to port 23010 on the server at mygame.example.com
     
     The connecting PC can then connect to mygame.example.com via your connection by using your ip address and port 23010.
     For example, if your IP was "203.0.113.5" then they would use the following address to bayass the blocked IP: 203.0.113.5:23010 (Note that in some games/apps that you need to enter the port seperate from the IP)

     Command to run:

     java -jar UDP_Proxy-1.1.jar mygame.example.com 23010 23010

     
  Scenario 3:
  
     Use case:
  
     Forward incoming UDP server traffic to a different local port without conflicting with another server on the local network.
    
     Specifics:
     
     Incoming traffic on port 32100 will be redirected to local port 2017
     
     Command to run:
     
     java -jar UDP_Proxy-1.1.jar 127.0.0.1 2017 32100

---
Building
---
Make sure you have Maven in your OS class path, and then run the following from the Git Bash console

```
  git clone https://github.com/sorifiend/UDP-Proxy
  cd UDP-Proxy
  mvn package
```

The resulting JAR for Bukkit can be found in the compiled-jar folder

