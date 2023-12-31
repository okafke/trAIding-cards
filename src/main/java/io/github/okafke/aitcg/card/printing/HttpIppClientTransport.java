package io.github.okafke.aitcg.card.printing;

import com.hp.jipp.encoding.IppInputStream;
import com.hp.jipp.encoding.IppOutputStream;
import com.hp.jipp.trans.IppClientTransport;
import com.hp.jipp.trans.IppPacketData;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * A simple HTTP/HTTPS transport for IPP.
 * It is assumed that the remote server will not deliver additional data (just an IPP packet).
 * This sample is from the JIPP repository:
 * <a href=https://github.com/HPInc/jipp/blob/5f1592b0a6dce7635e682e6de0daf5d5a5d7e051/sample/jprint/src/main/java/sample/HttpIppClientTransport.java>JIPP Sample</a>
 */
class HttpIppClientTransport implements IppClientTransport {
    private static final String SSL_PROTOCOL = "TLSv1.2";

    private static final TrustManager[] TRUST_ALL_CERTS;
    private static final HostnameVerifier ALL_HOSTS_VALID;
    static {
        TRUST_ALL_CERTS = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        };
        ALL_HOSTS_VALID = (hostname, session) -> true;
    }

    private final SSLContext sslContext;
    private final boolean acceptSelfSignedCerts;

    /**
     * @param acceptSelfSignedCerts If true, auto-accept self-signed HTTPS certificates. Real implementations should
     * implement a Trust-On-First-Use approach to minimize the potential for MITM attacks.
     */
    HttpIppClientTransport(boolean acceptSelfSignedCerts) {
        this.acceptSelfSignedCerts = acceptSelfSignedCerts;
        this.sslContext = createSSLContext();
    }

    @Override
    @NotNull
    public IppPacketData sendData(@NotNull URI uri, @NotNull IppPacketData request) throws IOException {
        HttpURLConnection connection = createURLConnection(uri);
        connection.setConnectTimeout(6 * 1000);
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Content-type", "application/ipp");
        connection.setChunkedStreamingMode(0);
        connection.setDoOutput(true);

        try (IppOutputStream output = new IppOutputStream(connection.getOutputStream())) {
            output.write(request.getPacket());
            InputStream extraData = request.getData();
            if (extraData != null) {
                copy(extraData, output);
                extraData.close();
            }
        }

        ByteArrayOutputStream responseBytes = new ByteArrayOutputStream();
        try (InputStream response = connection.getInputStream()) {
            copy(response, responseBytes);
        }

        IppInputStream responseInput = new IppInputStream(new ByteArrayInputStream(responseBytes.toByteArray()));
        return new IppPacketData(responseInput.readPacket(), responseInput);
    }

    private void copy(InputStream data, OutputStream output) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        int readAmount = data.read(buffer);
        while (readAmount != -1) {
            output.write(buffer, 0, readAmount);
            readAmount = data.read(buffer);
        }
    }

    private HttpURLConnection createURLConnection(URI uri) throws IOException {
        URL url = new URL(uri.toString().replaceAll("^ipp", "http"));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (acceptSelfSignedCerts && connection instanceof HttpsURLConnection httpsConnection) {
            httpsConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            httpsConnection.setHostnameVerifier(ALL_HOSTS_VALID);
        }

        return connection;
    }

    private SSLContext createSSLContext() {
        try {
            if (!acceptSelfSignedCerts) {
                return SSLContext.getDefault();
            }

            SSLContext sslContext = SSLContext.getInstance(SSL_PROTOCOL);
            sslContext.init(null, TRUST_ALL_CERTS, new java.security.SecureRandom());
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
