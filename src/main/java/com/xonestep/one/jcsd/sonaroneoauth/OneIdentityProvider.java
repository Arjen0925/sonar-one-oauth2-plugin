package com.xonestep.one.jcsd.sonaroneoauth;

import org.sonar.api.server.ServerSide;
import org.sonar.api.server.authentication.Display;
import org.sonar.api.server.authentication.OAuth2IdentityProvider;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * created by jinxingjia on 2018/10/26 8:55
 **/
@ServerSide
public class OneIdentityProvider implements OAuth2IdentityProvider {


    static final String KEY = "one";

    private static final Logger LOGGER = Loggers.get(OneIdentityProvider.class);

    public void init(InitContext initContext) {

    }

    public void callback(CallbackContext callbackContext) {

    }

    public String getKey() {
        return null;
    }

    public String getName() {
        return null;
    }

    public Display getDisplay() {
        return null;
    }

    public boolean isEnabled() {
        return false;
    }

    public boolean allowsUsersToSignUp() {
        return false;
    }
}
