public final class WebAPI {
    public static int handleQuery(String method, String target) {
        try {
            DatabaseConnector.readDatabase();
        }
        catch (Exception e) {
            SocketServer.timestamp(e.toString());
        }
        return 600;
    }
}
