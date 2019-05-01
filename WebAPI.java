import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public final class WebAPI {
    public static JSONArray handleQuery(String method, String target) {
        try {
            return DatabaseConnector.readDatabase();
        }
        catch (Exception e) {
            SocketServer.timestamp(e.toString());
        }
        return null;
    }
}
