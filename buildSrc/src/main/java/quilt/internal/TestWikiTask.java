package quilt.internal;


import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class TestWikiTask extends DefaultTask {
	public TestWikiTask() {
		setGroup("wiki");

		dependsOn("generateWiki");
	}

	@TaskAction
	public void testWebpage() throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt((String) getProject().property("test_port"))), 0);

		server.createContext("/", exchange -> {
			String url = exchange.getRequestURI().getPath();
			System.out.println(url);
			if (url.endsWith("/")) {
				sendWebpageFile(exchange, url.substring(1) + "index.html", "text/html;charset=utf-8");
			} else if (url.endsWith(".css")) {
				sendWebpageFile(exchange, url.substring(1), "text/css;charset=utf-8");
			} else if (url.endsWith(".ico")) {
				sendWebpageFile(exchange, url.substring(1), "image/png");
			}
			System.out.println("Served");
		});

		server.start();

		while (true);
	}

	private void sendWebpageFile(HttpExchange httpExchange, String fileName,
										String contentType) throws IOException {
		httpExchange.getResponseHeaders().set("Content-Type", contentType);
		Path output = getProject().file(getProject().property("output_path")).toPath();
		InputStream index = Files.newInputStream(output.resolve(fileName));
		try {
			byte[] bytes = index.readAllBytes();
			httpExchange.sendResponseHeaders(200, bytes.length);
			httpExchange.getResponseBody().write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpExchange.close();
	}
}
