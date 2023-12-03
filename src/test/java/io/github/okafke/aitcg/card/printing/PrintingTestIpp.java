package io.github.okafke.aitcg.card.printing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PrintingTestIpp {
    private static final String IPP =
            """
            {
             OPERATION Print-Job
             GROUP operation-attributes-tag
              ATTR charset attributes-charset utf-8
              ATTR language attributes-natural-language en
              ATTR uri printer-uri $uri
                    
              GROUP job-attributes-tag
              ATTR collection media-col {
              MEMBER integer media-top-margin 0
              MEMBER integer media-bottom-margin 0
              MEMBER integer media-left-margin 0
              MEMBER integer media-right-margin 0
             }
                    
             FILE $filename
            }""";

    public static void main(String[] args) throws Exception {
        String ippUrl = "http://printer-ip-address/ipp";
        //sendIPPRequest(ippUrl, IPP.replace("$uri", ippUrl).replace());
    }

    private static void sendIPPRequest(String ippUrl, String ippRequest) throws Exception {
        URL url = new URL(ippUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/ipp");
        connection.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(ippRequest);
            wr.flush();
        }

        int responseCode = connection.getResponseCode();
        try (BufferedReader in = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println("Response Code: " + responseCode);
            System.out.println("Response:\n" + response.toString());
        }

        connection.disconnect();
    }

}
