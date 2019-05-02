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
            action = target;
        }
        //FIX
        //else {
        //    throw new Exception("Unable to handle API GET query!");
        //}

        if (action.equals("animals")) {
            return DatabaseConnector.getAnimals(params);
        }
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
        
        if (i >= 0) {
            action = target.substring(0, i);
            params = target.substring(i+1).split("&"); 
        }
        else {
            throw new Exception("Unable to handle API POST query!");
        }
        if (action.equals("register")) {
            DatabaseConnector.addAccount(params);
            return "/html/login.html";
        }   
        if (action.equals("login")) {
            return DatabaseConnector.authenticate(params);
        }
        if (action.equals("newanimal")) {
            DatabaseConnector.addAnimal(params);
            return "/html/admin.html";
        }
        if (action.equals("adopt")) {
            String ret = DatabaseConnector.adoptAnimal(params);
            SocketServer.timestamp(ret);
            return ret;
            //return DatabaseConnector.adoptAnimal(params);
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