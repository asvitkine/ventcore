import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;
import java.security.cert.*;
import javax.net.ssl.*;

public class WiredMain {
	public static WiredClient createClientFor(String host, int port) throws Exception {
		File certificatesFile = new File("ssl_certs");
		InputStream in = new FileInputStream(certificatesFile);
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(in, "changeit".toCharArray());
		in.close();

		SSLContext context = SSLContext.getInstance("TLS");
		String algorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
		tmf.init(ks);
		context.init(null, new TrustManager[] {tmf.getTrustManagers()[0]}, null);
		SSLSocketFactory factory = context.getSocketFactory();
		SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
		socket.setEnabledProtocols(new String[] {"TLSv1"});
		return new WiredClient(socket.getInputStream(), socket.getOutputStream());
	}

	public static void main(String[] args) throws Exception {
		/*
		System.out.print("Enter host to connect to : ");
		String host = console.readLine();
		System.out.print("Enter port to connect on : ");
		int port = Integer.parseInt(console.readLine());
		*/

		String host = "localhost";
		int port = 2000;
	
		WiredClient client = createClientFor(host, port);
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

		/*
		System.out.print("Enter nick to use : ");
		String nick = console.readLine();
		System.out.print("Enter username : ");
		String user = console.readLine();
		System.out.print("Enter password : ");
		String pass = console.readLine();
		int result = client.login(nick, user, (pass.length() > 0 ? pass : null));
		*/

		int result = client.login("Wired User", "guest", null);
		if (result == 0) {
			System.out.println("Connected.");
			client.requestUserList(1);
			String in = console.readLine();
			while (in != null) {
				client.sendChatMessage(1, in);
				in = console.readLine();
			}
		}
	}
}
