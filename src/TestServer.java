import server.HttpTaskServer;
import util.Managers;

import java.io.IOException;

public class TestServer {
    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(Managers.getDefault());
        server.start();
        server.stop();
    }
}
