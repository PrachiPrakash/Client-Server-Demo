import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class Server
{
	public static void main(String ...args)throws IOException
	{
		ServerSocketChannel ss = ServerSocketChannel.open();
		ss.socket().bind(new InetSocketAddress(7890));
		//System.out.println("TCPServer started...");

		DatagramChannel udpServer = DatagramChannel.open();
		udpServer.socket().bind(new InetSocketAddress(7890));

		Selector selector = Selector.open();
		ss.configureBlocking(false);
		udpServer.configureBlocking(false);

		ss.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("registered TCP  with selector");

		udpServer.register(selector,SelectionKey.OP_READ);
		System.out.println("rtegistered udp with selector");
		ByteBuffer br = ByteBuffer.allocate(2048);
		String resp = "";

		while(true)
		{
			selector.select();
			Set<SelectionKey> requestSet = selector.selectedKeys();
			Iterator<SelectionKey> i = requestSet.iterator();

			while(i.hasNext()){

				SelectionKey key = i.next();

				if(key.isAcceptable()){
					SocketChannel c = ss.accept();
					new Thread(new TcpThread(c.socket())).start();
				}

				else if(key.isReadable()){
					System.out.println("Got a UDP Request");
					SocketAddress sa = udpServer.receive(br);
					resp = getResponse(br.array());
					System.out.println(resp);

					if(sa != null){
						ByteBuffer response = ByteBuffer.allocate(2048);
						response.clear();
						response.put(resp.getBytes());
						response.flip();
						br.clear();
						udpServer.send(response, sa);
					}

				}
				i.remove();
			}
		}
	}

	public static String getResponse(byte req[])
	{
		String resp = "";

		String input[] = new String(req).split("\r\n");

		if(input[0].equalsIgnoreCase("Subnet"))
			resp = subnetGame(input[1], input[2], input[3])+"";
		else if(input[0].equalsIgnoreCase("Sort"))
			resp = sortingGame(input[1])+"\r\n";

		return resp;
	}

	public static int subnetGame(String ip_1,String ip_2,String s_m)
	{

		int ip1 = getIntegerIP(ip_1);
		int ip2 = getIntegerIP(ip_2);
		int sm = getIntegerIP(s_m);

		System.out.printf("IP 1 %x\n",ip1);
		System.out.printf("IP 2 %x\n ",ip2);
		System.out.printf("mask %x\n",sm);


		if((ip1 & sm) == (ip2 & sm))
			return 1;
		else
			return 0;


	}

	public static String sortingGame(String input)
	{
		String res = "";
		String num[] = input.split(" ");
		int list[] = new int[num.length];

		for(int i=0; i<num.length; i++)
			list[i] = Integer.parseInt(num[i]);

		Arrays.sort(list);
		res = res+list[0];

		for(int i=1; i<list.length; i++)
			res = res + " "+list[i];

		return res;
	}

	public static int getIntegerIP(String ip)
	{
		System.out.println("here"+ip);
		int temp = 0;
		int res = 0;
		int i=3;
		StringTokenizer st = new StringTokenizer(ip, ".",false);

		while(st.hasMoreTokens()){
			temp = Integer.parseInt(st.nextToken()) << (8*i);
			res = res | temp;
			i--;
		}
		return res;
	}
}
