package uk.co.caci.iig.hibp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

public class PrefixResponseHandler implements ResponseHandler<String> {

    private String prefix;

    public PrefixResponseHandler(String prefix) {
        this.prefix = prefix.toUpperCase();
    }

    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            HttpEntity entity = response.getEntity();
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
            try {
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    if (inputLine.startsWith(prefix)) {
                        br.close();
                        return inputLine;
                    }
                }
                br.close();
                throw new ClientProtocolException("No entry found for: " + prefix);
            } catch (IOException e) {
                throw new ClientProtocolException("Exception thrown reading response", e);
            }
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }

    }
}
