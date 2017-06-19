/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package udp;

import java.net.PortUnreachableException;
import java.net.UnknownHostException;

/**
 *
 * @author sorifiend
 */
public class UDP
{

  /**
   * @param args usage: Server_Host Server_Port_out Proxy_Port_in
   */
  public static void main(String[] args)
  {
      //default ports
      String serverHost = "127.0.0.1";
      int serverPort = 9988;
      int proxyPort = 9987;  
//    if (args.length == 0)
//    {
//      System.out.println("Proxy starting with dfault ports:");
//      startProxy(serverHost, serverPort, proxyPort);
//    }
    if (args.length == 3)
    {    
      boolean error = false;
      //
      serverHost = args[0];
      try
      {
        serverPort = Integer.parseInt(args[1]);
      }
      catch (NumberFormatException e)
      {
        error = true;
        System.err.println("Invalid Server Port: "+args[1]);
      }
      try
      {
        proxyPort = Integer.parseInt(args[2]);
      }
      catch (NumberFormatException e)
      {
        error = true;
        System.err.println("Invalid Proxy Port: "+args[2]);
      }
      
      if (error == false)
      {
        startProxy(serverHost, serverPort, proxyPort);
      }
    }
    else
    {
      System.out.println("Usage: Server_Host Server_Port Proxy_Port"
              + "\r\nTeamspeak Server Example: 127.0.0.1 9988 9987");
    }
 }
  
  public static void startProxy(String serverHost, int serverPort, int proxyPort)
  {
    try
    {
      new UDPproxy(serverHost, serverPort, proxyPort).runServer();
    }
    catch (UnknownHostException e)
    {
      System.err.println("Invalid Server_Host");
    }
    catch (PortUnreachableException e)
    {
      System.err.println("The supplid port was unreachable");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
