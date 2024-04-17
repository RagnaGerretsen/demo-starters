package demo.library.oauth;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration //NOSONAR
@Data
@Slf4j
@EnableConfigurationProperties(AzureAdOAuth2Properties.class)
public class OAuth2Configuration {

    private static final String BEAN_NAME = "getConfidentialClientApplication";
    private static final String BEAN_NAME_LOCAL = "getLocalConfidentialClientApplication";

    private final AzureAdOAuth2Properties azureAdOauth2Properties;

    //TODO: where is this used??
    @Bean(name = BEAN_NAME)
    @Profile("!local")
    public ConfidentialClientApplication getConfidentialClientApplication() {
        try {
            final InputStream keyStoreResource = this.azureAdOauth2Properties.keystoreFileResource().getInputStream();
            final String keystorePassword = this.azureAdOauth2Properties.keystorePassword();
            final KeyStore keyStore = loadKeystore(keyStoreResource, keystorePassword);
            final PrivateKey privateKey = (PrivateKey) keyStore.getKey(this.azureAdOauth2Properties.certificateAlias(), keystorePassword.toCharArray());
            final X509Certificate cert = (X509Certificate) keyStore.getCertificate(this.azureAdOauth2Properties.certificateAlias());
            return ConfidentialClientApplication.builder(this.azureAdOauth2Properties.clientId(),
                    ClientCredentialFactory.createFromCertificate(privateKey, cert))
                    .authority(this.azureAdOauth2Properties.tokenUrl()).build();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Failed to read configuration properties, make sure azureAdOAuth2Properties are defined.");
            return ConfidentialClientApplication.builder(null, null).build();
        }
    }

    @Bean(name = BEAN_NAME_LOCAL)
    @Profile("local")
    public ConfidentialClientApplication getLocalConfidentialClientApplication() {
        return null;
    }

    private static KeyStore loadKeystore(InputStream keyStoreResource, String keystorePassword) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(keyStoreResource, keystorePassword.toCharArray());
        return keyStore;
    }
}
