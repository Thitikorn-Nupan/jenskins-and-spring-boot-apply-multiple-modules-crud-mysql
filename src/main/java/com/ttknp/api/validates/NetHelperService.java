package com.ttknp.api.validates;

import java.net.InetAddress;

public class NetHelperService {

    public static String getHostName() {
        try {
            String hostName = null;
            hostName = InetAddress.getLocalHost().getHostName(); // get User of OS
            return hostName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHostAddress() {
        try {
            String hostAddress = null;
            hostAddress = InetAddress.getLocalHost().getHostAddress(); // get Ip User of OS
            return hostAddress;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
