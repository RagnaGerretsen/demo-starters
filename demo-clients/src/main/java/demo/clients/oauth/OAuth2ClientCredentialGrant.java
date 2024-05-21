package demo.clients.oauth;

import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!local")
public class OAuth2ClientCredentialGrant {

    private final ConfidentialClientApplication confidentialClientApplication;

    public IAuthenticationResult getAccessTokenByClientCredentialGrant(final String scope) {
        final ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(Collections.singleton(scope)).build();
        final IAuthenticationResult result = confidentialClientApplication.acquireToken(clientCredentialParam).join();
        log.info("token obtained from azure ad");
        return result;
    }
}