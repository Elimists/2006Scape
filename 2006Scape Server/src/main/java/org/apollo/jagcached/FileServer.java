package org.apollo.jagcached;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.rs2.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apollo.game.session.ApolloHandler;
import org.apollo.net.HttpChannelInitializer;
import org.apollo.net.JagGrabChannelInitializer;
import org.apollo.net.ServiceChannelInitializer;

@Slf4j
public final class FileServer {

	private final ServerBootstrap fileServerBootstrap = new ServerBootstrap();
	private final ServerBootstrap httpServerBootstrap = new ServerBootstrap();
	private final ServerBootstrap jaggrabServerBootstrap = new ServerBootstrap();

	private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

	private RequestWorkerPool workerPool;

	public void start(int httpPort, int jaggrabPort, int fileServerPort) throws Exception {
		validateWorkingDirectory();

		if (!Constants.FILE_SERVER) {
			log.info("File server disabled. Skipping startup.");
			return;
		}

		workerPool = new RequestWorkerPool();
		log.info("Starting request workers...");
		workerPool.start();

		log.info("Initializing Netty services...");
		initializeBootstraps();

		SocketAddress fileAddr = new InetSocketAddress(fileServerPort);
		SocketAddress httpAddr = new InetSocketAddress(httpPort);
		SocketAddress jaggrabAddr = new InetSocketAddress(jaggrabPort);

		log.info("Binding to ports: file={}, http={}, jaggrab={}", fileServerPort, httpPort, jaggrabPort);
		bind(fileServerBootstrap, fileAddr);
		bindSafely(httpServerBootstrap, httpAddr, "HTTP");
		bind(jaggrabServerBootstrap, jaggrabAddr);

		log.info("File server is ready for connections.");
	}

	private void initializeBootstraps() {
		ApolloHandler handler = new ApolloHandler();

		initializeBootstrap(fileServerBootstrap, new ServiceChannelInitializer(handler));
		initializeBootstrap(httpServerBootstrap, new HttpChannelInitializer(handler));
		initializeBootstrap(jaggrabServerBootstrap, new JagGrabChannelInitializer(handler));
	}

	private void initializeBootstrap(ServerBootstrap bootstrap, ChannelInitializer<SocketChannel> initializer) {
		bootstrap.group(eventLoopGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(initializer);
	}

	private void bind(ServerBootstrap bootstrap, SocketAddress address) throws IOException {
		try {
			bootstrap.bind(address).sync();
		} catch (Exception e) {
			throw new IOException("Failed to bind to " + address, e);
		}
	}

	private void bindSafely(ServerBootstrap bootstrap, SocketAddress address, String name) {
		try {
			bind(bootstrap, address);
		} catch (IOException e) {
			log.warn("Unable to bind {} listener to {}. Skipping.", name, address, e);
		}
	}

	private void validateWorkingDirectory() {
		if (!new File(Constants.FILE_SYSTEM_DIR).exists()) {
			String workingDir = System.getProperty("user.dir");
			log.error("""
				Working Directory = {}
				**************************************************************
				WARNING: Could not find the data/cache folder.
				You're likely running from the wrong working directory!

				In IntelliJ:
				  - Go to "Run > Edit Configurations"
				  - Set "Working Directory" to: "2006Scape/2006Scape Server"
				**************************************************************
				Exiting...
				""", workingDir);
			System.exit(1);
		}
	}
}
