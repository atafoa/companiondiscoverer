import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public final class WebAPI {
    public static JSONArray getQuery(String target) {
    try {
        int i           = target.indexOf('?');      // Find the last '?' in the api target.
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
        if (action.equals("inquiry")) {
            return DatabaseConnector.getInquiries();
        }
        if (action.equals("donation")) {
            return DatabaseConnector.getDonations();
        }
    }
    catch (Exception e) {
        SocketServer.timestamp(e.toString());
    }
        return null;
    }

    public static String postQuery(String target) {
    try {
        int i           = target.indexOf('?');      // Find the last '?' in the api target.
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
            return DatabaseConnector.adoptAnimal(params);
        }
        if (action.equals("inquiry")) {
            DatabaseConnector.addInquiry(params);
            return "/html/animal.html";
        }
        if (action.equals("inquiryAns")) {
            DatabaseConnector.updateInquiry(params);
            return "/html/viewinquiry.html";
        }
        if (action.equals("donation")) {
            DatabaseConnector.addDonation(params);
            return "/html/animal.html";
        }

    }
    catch (Exception e) {
        SocketServer.timestamp(e.toString());
    }
        return null;
    }

}
