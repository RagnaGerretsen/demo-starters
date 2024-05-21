package demo.clients.oauth;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
@RequiredArgsConstructor
public class OAuthClientRequestInterceptor implements RequestInterceptor {

    private static final String OAUTH2_AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    private final OAuth2ClientCredentialGrant oAuth2ClientCredentialGrant;
    private final String scope;


    @Override
    public void apply(final RequestTemplate template) {
        final IAuthenticationResult accessTokenByClientCredentialGrant = this.oAuth2ClientCredentialGrant.getAccessTokenByClientCredentialGrant(this.scope);
        final String token = accessTokenByClientCredentialGrant.accessToken();
        template.header(OAUTH2_AUTHORIZATION_HEADER, BEARER + token);

        logMdcValues(template);
    }

    private void logMdcValues(final RequestTemplate requestTemplate) {
        try {
            MDC.put("clientUrl", requestTemplate.url());
            log.info("Request to endpoint: {}", requestTemplate.url());

        } finally {
            MDC.remove("clientUrl");
        }
    }

}