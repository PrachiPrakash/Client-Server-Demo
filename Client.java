/**
  *this is the cient program for both 1)a 1)b
  *java SubnetClient protocol(TCP/UDP) Operation(Subnet/Sort) data
  *ex java SubnetClient TCP Sort "5 4 3 2 1"
  *ex java SubnetClient UDP Subnet 192.168.3.15 192.168.3.27 255.255.255.0
  *@author Prachi Prakash
  */
import java.net.*;
import java.io.*;

public class Client
{
	public static void main(String args[])throws Exception
	{

			if(args[0].equalsIgnoreCase("tcp"))
				tcpRequest(args);
			else if(args[0].equalsIgnoreCase("udp"))
				udpRequest(args);

	}

	public static void tcpRequest(String args[])throws Exception
	{
			Socket s = new Socket("localhost",7890);
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

			Writer w  = new OutputStreamWriter(s.getOutputStream());

			if(args[1].equalsIgnoreCase("Subnet")){
				w.write("Subnet\r\n");
				w.flush();

				w.write(args[2]+"\r\n");
				w.flush();
				w.write(args[3]+"\r\n");
				w.flush();
				w.write(args[4]+"\r\n");
				w.flush();

				String resp = br.readLine();
				if(resp.equals("1"))
					System.out.println("the two ips are in the same network");
				else
					System.out.println("they are not in the same network");
		}

		else if(args[1].equalsIgnoreCase("Sort")){
			w.write("Sort"+"\r\n");
			w.flush();
			w.write(args[2]+"\r\n");
			w.flush();

			String resp = br.readLine();
			System.out.println(resp);
		}
	}

	public static void udpRequest(String args[])throws Exception
	{

			DatagramSocket req = new DatagramSocket();
			byte r[] = new byte[2048];


			if(args[1].equalsIgnoreCase("Sort")){
				String ss = "Sort\r\n"+args[2]+"\r\n";
				DatagramPacket request = new DatagramPacket(ss.getBytes(),ss.getBytes().length,
								InetAddress.getByName("localhost"),7890);
				req.send(request);



				DatagramPacket res = new DatagramPacket(r,2048);
				req.receive(res);

				System.out.println(new String(res.getData()));
			}

			else if(args[1].equalsIgnoreCase("Subnet")){
				String ss = "Subnet\r\n"+args[2]+"\r\n"+args[3]+"\r\n"+args[4]+"\r\n";
				DatagramPacket request = new DatagramPacket(ss.getBytes(),ss.getBytes().length,
								InetAddress.getByName("localhost"),7890);
				req.send(request);



				DatagramPacket res = new DatagramPacket(r,2048);
				req.receive(res);

				String rs = new String(res.getData());

				System.out.println(rs);

				if(Integer.parseInt(rs.trim()) == 1)
					System.out.println("Same network");

				else
					System.out.println("Different Network");
			}
	}
}
