This piece of software uses a simple protocol to solve the problem of both
the problems.
--------------------------------------------------------------------------------
Protocol
--------------------------------------------------------------------------------
<operation><crlf>
<data>
operation can be Sort or Subnet

ex of Sort message:
Sort<crlf>
5 4 3 2 1<crlf>(note the numbers should be space separated)

ex of Subnet Message:
Subnet <crlf>
192.168.3.12<crlf>
192.168.3.36<crlf>
255.255.255.0<crlf>

note <crlf>: Carraige return linefeed("\r\n")
-------------------------------------------------------------------------------
The Server side of the software uses the Channels(nio) of java to accept both tcp and udp packets on the fly
listening to the same port.

to compile the server side of the software use
javac TcpThread.java Server.java

to run the server
java Server

the client side of the program uses commmand line args to take inputs ex are below
java SubnetClient protocol(TCP/UDP) Operation(Subnet/Sort) data
ex java SubnetClient TCP Sort "5 4 3 2 1"
ex java SubnetClient UDP Subnet 192.168.3.15 192.168.3.27 255.255.255.0
