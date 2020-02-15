package com.mycodefu.werekitten.network;

import java.io.IOException;
import java.net.*;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.Enumeration;

public class NetworkUtils {
    public static String getInternetIpAddress() {
        try {
            // iterate over the network interfaces known to java
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface interface_ : Collections.list(interfaces)) {
                // we shouldn't care about loopback addresses
                if (interface_.isLoopback())
                    continue;

                // if you don't expect the interface to be up you can skip this
                // though it would question the usability of the rest of the code
                if (!interface_.isUp())
                    continue;

                // iterate over the addresses associated with the interface
                Enumeration<InetAddress> addresses = interface_.getInetAddresses();
                for (InetAddress address : Collections.list(addresses)) {
                    // look only for ipv4 addresses
                    if (address instanceof Inet6Address)
                        continue;

                    // use a timeout big enough for your needs
                    if (!address.isReachable(3000))
                        continue;

                    // java 7's try-with-resources statement, so that
                    // we close the socket immediately after use
                    try (SocketChannel socket = SocketChannel.open()) {
                        // again, use a big enough timeout
                        socket.socket().setSoTimeout(3000);

                        // bind the socket to your local interface
                        socket.bind(new InetSocketAddress(address, 8080));

                        // try to connect to *somewhere*
                        socket.connect(new InetSocketAddress("google.com", 80));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        continue;
                    }

                    System.out.format("ni: %s, ia: %s\n", interface_, address);

                    return address.getHostAddress();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }
}
