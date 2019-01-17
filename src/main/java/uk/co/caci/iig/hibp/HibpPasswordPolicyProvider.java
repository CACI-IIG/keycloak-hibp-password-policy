package uk.co.caci.iig.hibp;

import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakContext;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.policy.PasswordPolicyProvider;
import org.keycloak.policy.PolicyError;

import uk.co.caci.iig.hibp.HaveIBeenPwned.ApiException;

public class HibpPasswordPolicyProvider implements PasswordPolicyProvider {
    private static final Logger LOG = Logger.getLogger(HibpPasswordPolicyProvider.class);

    private KeycloakContext context;
    private HaveIBeenPwned haveIBeenPwned;

    public HibpPasswordPolicyProvider(KeycloakContext context) {
        this.context = context;
        this.haveIBeenPwned = new HaveIBeenPwned();
    }

    public PolicyError validate(String user, String password) {
        Integer allowedBreaches = context.getRealm().getPasswordPolicy()
                .getPolicyConfig(HibpPasswordPolicyProviderFactory.ID);

        try {
            int breachOccurrences = haveIBeenPwned.lookup(password);
            if (breachOccurrences > allowedBreaches) {
                return new PolicyError("The entered password is present in {0} data breaches and cannot be used",
                        breachOccurrences);
            }
        } catch (ApiException e) {
            LOG.error("Failed to lookup password with HIBP, allowing password", e);
        }
        return null;
    }

    public PolicyError validate(RealmModel realm, UserModel user, String password) {
        return validate(user.getUsername(), password);
    }


    public Object parseConfig(String value) {
        return Integer.valueOf(value);
    }

    public void close() {
    }

}
