package finalproject.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClientUtils {
    
    public static String jsonifySignIn(String email, String password) {
        return "";
	}

	public static String jsonifySignUp(String name, String email, String password) {
        User user = new User(name, email, password);
        Message message = new Message(Message.Type.SIGNIN, user);
		Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
		Gson gson = new Gson();
		String json = gson.toJson(message);
		return json;
	}
}