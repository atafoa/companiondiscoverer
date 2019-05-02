import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public final class WebAPI {
    public static JSONArray getQuery(String target) {
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

        /*if (action.equals("profiles")) {
            return DatabaseConnector.getProfiles(params);
        }*/
    }
    catch (Exception e) {
        SocketServer.timestamp(e.toString());
    }
        return null;
    }

    public static String postQuery(String target) {
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
        if (action.equals("register")) {
            DatabaseConnector.addAccount(params);
            return "/html/login.html";
        }   
        if (action.equals("login")) {
            String auth = DatabaseConnector.authenticate(params);
            if (auth.equals("admin")) {
                return "/html/admin.html";
            }
            else if (auth.equals("profile")) {
                return "/html/user.html";
            }
            else {
                return "/html/invalidlogin.html";
            }
        }
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