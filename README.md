# UDP-Proxy
Simple java proxy for redirectring UDP traffic from an incoming port to an outgoing port.


**Update June 19, 2017:**

The latest artifact can be found here: https://github.com/sorifiend/UDP-Proxy/compiled-jar

---
Usage Instructions:
---

  Usage:  java -jar UDP_Proxy.jar  Server_IP_Outgoing  Proxy_Port_Outgoing  Proxy_Port_Incoming
  
```
  For example:
  1) Scenario:
     Forward teamspeak server traffic to local port 9988.
     (Handy for multiple child VM servers all on one IP address by using a proxy for different external ports):
  
     Specifics:
     Incoming traffic on port 9987
     We want traffic to be redirected to port 9988 on the localhost (port 127.0.0.1)
     
     Run:
     java -jar UDP_Proxy.jar 127.0.0.1 9988 9987
     
  2) Scenario:
     Forward another computers UDP gaming traffic to a server via your IP address.
     (Handy for working around a firewall that IP blocked an external address)
     
     Specifics:
     Incoming traffic on port 23010
     We want traffic to be redirected to port 23010 on the server at mygame.example.com with IP 1.2.3.4 (Just an example)
     
     Run:
     java -jar UDP_Proxy.jar 1.2.3.4 23010 23010
     
     Then they can connect to mygame.example.com via your connection by using your ip address and port 23010.
     If your IP was "203.0.113.5" then they would use: 203.0.113.5:23010
     
  3) Scenario:
     Forward incoming UDP server traffic to a different local port without conflicting with another server on the local network.
    
     Specifics:
     Incoming traffic on port 32100
     We want traffic to be redirected to local port 2017
     
     Run:
     java -jar UDP_Proxy.jar 127.0.0.1 2017 32100
```

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

