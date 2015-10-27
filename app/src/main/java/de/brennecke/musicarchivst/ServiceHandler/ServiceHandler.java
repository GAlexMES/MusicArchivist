package de.brennecke.musicarchivst.ServiceHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Alexander on 27.10.2015.
 */
public abstract class ServiceHandler {

    protected static String getContentFromURL(String url){
        String retval = "";
        try {
            URL requestURL = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            requestURL.openStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                retval = retval + inputLine;
            }

            in.close();
            return retval;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return retval;
    }
}
