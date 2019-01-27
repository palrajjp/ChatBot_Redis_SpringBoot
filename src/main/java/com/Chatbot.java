package com;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.utils.IOUtils;

import com.connector.NetClient;
 
public class Chatbot {
    private static final boolean TRACE_MODE = false;
    static String botName = "PalBot";
    public static String doGet(String value){
    	String apiVal = "";
    	
    	NetClient apiResponse = new NetClient();
        List<String> uriList = new ArrayList<String>();
        
    	uriList = Arrays.asList(value.split(","));
    	apiVal = apiResponse.doGetService(uriList.get(1));
    	
    	return apiVal;
    }
    public static void main(String[] args) {
        try {
            String resourcesPath = getResourcesPath();
            System.out.println(resourcesPath);
            MagicBooleans.trace_mode = TRACE_MODE;
            Bot bot = new Bot("PalBot", resourcesPath);
            Chat chatSession = new Chat(bot);
            bot.brain.nodeStats();
            String textLine = "";
 
            while(true) {
                System.out.print("Human : ");
                textLine = IOUtils.readInputTextLine();
                if ((textLine == null) || (textLine.length() < 1))
                    textLine = MagicStrings.null_input;
                if (textLine.equals("q")) {
                    System.exit(0);
                } else if (textLine.equals("wq")) {
                    bot.writeQuit();
                    System.exit(0);
                } else {
                    String request = textLine;
                    if (MagicBooleans.trace_mode)
                        System.out.println("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
                    String response = chatSession.multisentenceRespond(request);
                    while (response.contains("&lt;"))
                        response = response.replace("&lt;", "<");
                    while (response.contains("&gt;"))
                        response = response.replace("&gt;", ">");
                    while (response.contains("REDIS"))
                    	response = response.replace(response, doGet(response));
                    while (response.contains("SPRINGBOOTAPI"))
                    	response = response.replace(response, doGet(response));
                    System.out.println("Robot : " + response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        System.out.println(path);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        return resourcesPath;
    }
}
