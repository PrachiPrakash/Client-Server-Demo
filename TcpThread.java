
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringTokenizer;

public class TcpThread implements Runnable
{
	private Socket s;

	public TcpThread(Socket s)
	{
		this.s = s;
	}

	@Override
	public void run()
	{
		System.out.println("acepted connection from"+s.getInetAddress());
		try{
			BufferedReader br = new BufferedReader(new
					InputStreamReader(s.getInputStream()));
			String operation  = br.readLine();
			System.out.println(operation);
			Writer w = new OutputStreamWriter(s.getOutputStream());


			if(operation.equalsIgnoreCase("Subnet"))
				w.write(subnetGame(br)+"\r\n");
			else if(operation.equalsIgnoreCase("Sort"))
				w.write(sortingGame(br.readLine())+"\r\n");

			w.flush();
			s.close();

		}
		catch(Exception ex)
		{
			System.out.println(ex);
		}



	}

	public int subnetGame(BufferedReader br)throws Exception
	{

		int ip1 = getIntegerIP(br.readLine());
		int ip2 = getIntegerIP(br.readLine());
		int sm = getIntegerIP(br.readLine());

		System.out.printf("IP 1 %x ",ip1);
		System.out.printf("IP 2 %x ",ip2);
		System.out.printf("mask %x ",sm);


		if((ip1 & sm) == (ip2 & sm))
			return 1;
		else
			return 0;


	}

	public String sortingGame(String input)throws Exception
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
