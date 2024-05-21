package demo.clients.client.config.drink;

import demo.clients.oauth.OAuth2ClientCredentialGrant;
import demo.clients.oauth.OAuthClientRequestInterceptor;
import feign.RequestInterceptor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Slf4j
@Getter
@SuppressWarnings("pmd:ImplementEqualsHashCodeOnValueObjects")
public class ClientConfiguration {

    private static final String BEAN_NAME = "drinksOAuthClientRequestInterceptor";
    private static final String BEAN_NAME_LOCAL = "drinksLocalClientRequestInterceptor";

    @Value("${drink.name.azure-application-id-scope}")
    @Getter
    private String scope;

    @Bean(name = BEAN_NAME)
    @Profile("!local")
    public OAuthClientRequestInterceptor getOAuthClientRequestInterceptor(final OAuth2ClientCredentialGrant oAuth2ClientCredentialGrant) {
        return new OAuthClientRequestInterceptor(oAuth2ClientCredentialGrant, getScope());
    }

    @Bean(name = BEAN_NAME_LOCAL)
    @Profile("local")
    public RequestInterceptor requestInterceptorLocal() {
        return requestTemplate -> {};
    }
}
