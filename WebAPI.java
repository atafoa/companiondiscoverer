import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public final class WebAPI {
    public static JSONArray handleQuery(String method, String target) {
    try {
        int i           = target.lastIndexOf('?');      // Find the last '?' in the api target.
        String action   = null;
        String params[] = null;
        
        if (i > 0) {
            action = target.substring(0, i);
            params = target.substring(i+1).split("&"); 
        }
        else {
            throw new Exception("Unable to handle API query!");
        }

        //username=sbeve&password=fee
        if (action.equals("register")) {
            DatabaseConnector.addAccount(params);
        }   
        if (action.equals("login")) {
            DatabaseConnector.authenticate(params);
        }   
            
        return DatabaseConnector.readDatabase();
    }
    catch (Exception e) {
        SocketServer.timestamp(e.toString());
    }
        return null;
    }
}
/*
        String targetExtension = null;  // Extension of filename.
        String type = null;             // MIME type.

        int i = filename.lastIndexOf('.');      // Find the last '.' in the filename.
        if (i > 0) {                // Avoid any weird errors.
            targetExtension = filename.substring(i+1);  // Set targetExtension to the extension, with no dot.
        }
        else {
            targetExtension = "";   // If we do have weird errors, avoid a null pointer.
        }
        */