/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.nznetwork.udp;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UDPproxy
{
  static final int CONNECTION_TIMEOUT = 60000;
  HashMap<InetSocketAddress, ChannelInfo> channelMap = new HashMap<InetSocketAddress, ChannelInfo>();
  InetSocketAddress serverAddress;
  int proxyPort;

  public UDPproxy(String serverHost, int serverPort, int proxyPort) throws PortUnreachableException, UnknownHostException
  {
    InetAddress resolvedAddress = InetAddress.getByName(serverHost);
    this.serverAddress = new InetSocketAddress(resolvedAddress, serverPort);
    this.proxyPort = proxyPort;
  }

  public void runServer() throws Exception
  {
    System.out.println("Listening on " + proxyPort + ", forwarding to " + serverAddress);

    ByteBuffer buff = ByteBuffer.allocate(1024 * 256);
    Selector selector = Selector.open();
    DatagramChannel proxyChannel = addChannel(selector, proxyPort);
    long connectionTestTime = System.currentTimeMillis();

    while (true)
    {
      try
      {
        selector.selectNow();
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

        while (keyIterator.hasNext())
        {
          SelectionKey key = keyIterator.next();
          keyIterator.remove();

          if (key.isReadable())
          {
            DatagramChannel currentChannel = (DatagramChannel) key.channel();
            InetSocketAddress localAddress = (InetSocketAddress) currentChannel.socket().getLocalSocketAddress();
            buff.clear();
            InetSocketAddress remoteAddress = (InetSocketAddress) currentChannel.receive(buff);

            buff.flip();

            if (!fromServer(remoteAddress))
            {
              ChannelInfo info = channelMap.get(remoteAddress);
              if (info == null)
              {
//                if (IpChecker.checkip(remoteAddress.getAddress().getHostAddress()))
//                {

                    DatagramChannel tempChannel = addChannel(selector, serverAddress);
                    InetSocketAddress tempAddress = (InetSocketAddress) tempChannel.socket().getLocalSocketAddress();
                    info = new ChannelInfo(tempChannel, remoteAddress);
                    channelMap.put(remoteAddress, info);
                    System.out.println("Added key = " + remoteAddress.toString());
                    channelMap.put(tempAddress, info);
                    System.out.println("Added key = " + tempAddress.toString());
//                }
//                else
//                {
//                  System.out.println("Not from New Zealand: " + remoteAddress.getAddress().getHostAddress());
//                }
              }

              info.rxTime = System.currentTimeMillis();
              info.channel.send(buff, serverAddress);
            }
            else
            {
              ChannelInfo info = channelMap.get(localAddress);
              if (info != null)
              {
                proxyChannel.send(buff, info.remoteAddress);
              }
            }
          }
        }

        //Test & remove old connections
        if (System.currentTimeMillis() - connectionTestTime >= CONNECTION_TIMEOUT)
        {
          connectionTestTime = System.currentTimeMillis();
          Iterator<Map.Entry<InetSocketAddress, ChannelInfo>> entryIterator = channelMap.entrySet().iterator();
          while (entryIterator.hasNext())
          {
            Map.Entry<InetSocketAddress, ChannelInfo> entry = entryIterator.next();
            InetSocketAddress address = entry.getKey();
            ChannelInfo info = entry.getValue();
            if (connectionTestTime - info.rxTime >= CONNECTION_TIMEOUT)
            {
              info.channel.close();
              entryIterator.remove();
              System.out.println("Removed key = " + address.toString());
            }
          }
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
  }

  public boolean fromServer(InetSocketAddress address)
  {
    return address.getAddress().equals(serverAddress.getAddress());
  }

  public DatagramChannel addChannel(Selector selector, int bindPort) throws Exception
  {
    DatagramChannel channel = DatagramChannel.open();
    channel.configureBlocking(false);
    channel.socket().bind(new InetSocketAddress(bindPort));
    channel.register(selector, SelectionKey.OP_READ);
    return channel;
  }

  public DatagramChannel addChannel(Selector selector, InetSocketAddress address) throws Exception
  {
    DatagramChannel channel = DatagramChannel.open();
    channel.configureBlocking(false);
    channel.connect(address);
    channel.register(selector, SelectionKey.OP_READ);
    return channel;
  }

  public static class ChannelInfo
  {
    public ChannelInfo()
    {
    }

    public ChannelInfo(DatagramChannel channel, InetSocketAddress remoteAddress)
    {
      this.channel = channel;
      this.localAddress = (InetSocketAddress) channel.socket().getLocalSocketAddress();
      this.remoteAddress = remoteAddress;
    }

    DatagramChannel channel;
    InetSocketAddress localAddress;
    InetSocketAddress remoteAddress;
    long rxTime;
  }
}
