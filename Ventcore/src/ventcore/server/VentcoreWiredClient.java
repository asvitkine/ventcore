package ventcore.server;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.security.KeyStore;
import java.util.List;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.Icon;
import javax.swing.JFileChooser;

import wired.EventBasedWiredClient;
import wired.WiredEventHandler;
import wired.WiredUtils;
import wired.event.FileInfo;
import wired.event.WiredEvent;

public class VentcoreWiredClient extends EventBasedWiredClient {
	private SSLSocket socket;

	public VentcoreWiredClient(SSLSocket socket, final String user) throws IOException {
		super(socket.getInputStream(), socket.getOutputStream(),
			new WiredEventHandler() {
				public void handleEvent(WiredEvent event) {
					EventDispatcher.getInstance().dispatch(event, user);					
				}
			}
		);
		this.socket = socket;
	}

	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
		}
	}

	protected FileInfo readFile(List<String> params) {
		FileInfo file = super.readFile(params);
		file.setIcon(getIconAsString(params.get(0)));
		return file;
	}

	public static VentcoreWiredClient createClientFor(final String user, String host, int port) throws Exception {
		File certificatesFile = new File("/Users/shadowknight/Projects/ventcore/ssl_certs");
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
		return new VentcoreWiredClient(socket, user);
	}

	private static SoftReference<JFileChooser> iconFileChooserRef;
	private static Icon getFileIcon(File file) {
		JFileChooser fc = null;
		if (iconFileChooserRef != null) {
			fc = iconFileChooserRef.get();
		}
		if (fc == null) {
			fc = new JFileChooser();
			iconFileChooserRef = new SoftReference<JFileChooser>(fc);
		}
		return fc.getIcon(file);
	}

	private static String getIconAsString(String path) {
		File file = new File("/Library/Wired/files/" + path);
		if (file.exists()) {
			Icon icon = getFileIcon(file);
			if (icon != null) {
				BufferedImage image =
					new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = image.createGraphics();
				icon.paintIcon(null, g, 0, 0);
				g.dispose();
				image.flush();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					ImageIO.write(image, "png", out);
					return WiredUtils.bytesToBase64(out.toByteArray());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
