
2
package org.strongswan.android.utils;
3

4

5
import java.io.BufferedInputStream;
6
import java.io.ByteArrayOutputStream;
7
import java.io.FileInputStream;
8
import java.net.InetAddress;
9
import java.net.NetworkInterface;
10
import java.net.UnknownHostException;
11
import java.util.Collections;
12
import java.util.List;
13

14
public class Utils
15
{
16
	static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();
17

18
	public static String bytesToHex(byte[] bytes)
19
	{
20
		char[] hex = new char[bytes.length * 2];
21
		for (int i = 0; i < bytes.length; i++)
22
		{
23
			int value = bytes[i];
24
			hex[i*2]   = HEXDIGITS[(value & 0xf0) >> 4];
25
			hex[i*2+1] = HEXDIGITS[ value & 0x0f];
26
		}
27
		return new String(hex);
28
	}
29

30

31
	public native static boolean isProposalValid(boolean ike, String proposal);
32

33

34
	private native static byte[] parseInetAddressBytes(String address);
35

36

37
	public static InetAddress parseInetAddress(String address) throws UnknownHostException
38
	{
39
		byte[] bytes = parseInetAddressBytes(address);
40
		if (bytes == null)
41
		{
42
			throw new UnknownHostException();
43
		}
44
		return InetAddress.getByAddress(bytes);
45
	}
46

47

48
	public static byte[] getUTF8Bytes(String str) {
49
		try { return str.getBytes("UTF-8"); } catch (Exception ex) { return null; }
50
	}
51

52

53

54
	public static String getMACAddress(String interfaceName) {
55
		try {
56
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
57
			for (NetworkInterface intf : interfaces) {
58
				if (interfaceName != null) {
59
					if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
60
				}
61
				byte[] mac = intf.getHardwareAddress();
62
				if (mac==null) return "";
63
				StringBuilder buf = new StringBuilder();
64
				for (byte aMac : mac) buf.append(String.format("%02X:",aMac));
65
				if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
66
				return buf.toString();
67
			}
68
		} catch (Exception ignored) { }
69
		return "";
70

71
	}
72

73

74
	public static String getIPAddress(boolean useIPv4) {
75
		try {
76
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
77
			for (NetworkInterface intf : interfaces) {
78
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
79
				for (InetAddress addr : addrs) {
80
					if (!addr.isLoopbackAddress()) {
81
						String sAddr = addr.getHostAddress();
82
						boolean isIPv4 = sAddr.indexOf(':')<0;
83

84
						if (useIPv4) {
85
							if (isIPv4)
86
								return sAddr;
87
						} else {
88
							if (!isIPv4) {
89
								int delim = sAddr.indexOf('%');
90
								return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
91
							}
92
						}
93
					}
94
				}
95
			}
96
		} catch (Exception ignored) { }
97
		return "";
98
	}
99
}
