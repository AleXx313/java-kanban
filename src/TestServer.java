import server.HttpTaskServer;

import java.io.IOException;

public class TestServer {
    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
        server.stop();
    }
}
