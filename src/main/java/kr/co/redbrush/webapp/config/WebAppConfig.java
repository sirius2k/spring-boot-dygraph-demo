package kr.co.redbrush.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import javax.validation.constraints.NotNull;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Configuration
public class WebAppConfig {

    @Value("${restTemplate.connectionTimoutInMillis:15000}")
    private int connectionTimeoutInMillis;

    @Value("${restTemplate.readTimeoutInMillis:10000}")
    private int readTimeoutInMillis;

    static {
        disableSslVerification();

        // This setting is for avoiding following exception.
        // javax.net.ssl.SSLHandshakeException: server certificate change is restrictedduring renegotiation
        // Please, refer to https://stackoverflow.com/questions/27105004/what-means-javax-net-ssl-sslhandshakeexception-server-certificate-change-is-re
        System.setProperty("https.protocols", "TLSv1");
    }

    private static void disableSslVerification() {
        try {
            TrustManager[] trustAllCerts = createInsecureTrustManagers();
            installAllTrustingTrustManager(trustAllCerts);

            HostnameVerifier allHostsValid = createAllTrustingHostNameVerifier();
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private static HostnameVerifier createAllTrustingHostNameVerifier() {
        return new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
    }

    private static void installAllTrustingTrustManager(TrustManager[] trustAllCerts) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    @NotNull
    private static TrustManager[] createInsecureTrustManagers() {
        return new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
    }

    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(connectionTimeoutInMillis)
                .setReadTimeout(readTimeoutInMillis)
                .build();
    }
}
