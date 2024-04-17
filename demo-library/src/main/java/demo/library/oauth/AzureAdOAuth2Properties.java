package demo.library.oauth;

import jakarta.annotation.Nonnull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * @param authority            URL of Azure Active Directory (AAD).  Will be used for setting "aud" in the JwtClaimsSet
 * @param clientId             ApplicationId in Azure
 * @param certificateAlias     The alias of the certificate in the JWK file
 * @param keystoreFileResource The OAUTH JKS file
 * @param keystorePassword     The password for the OAUTH JKS file
 */

@ConfigurationProperties("azure.security.oauth")
public record AzureAdOAuth2Properties(@Nonnull String authority, @Nonnull String clientId, @Nonnull String certificateAlias,
                                      @Nonnull Resource keystoreFileResource, @Nonnull String keystorePassword,
                                      @Nonnull String tokenUrl) {
}
