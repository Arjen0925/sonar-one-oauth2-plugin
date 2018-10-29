package com.xonestep.one.jcsd.sonaroneoauth;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import org.sonar.api.server.ServerSide;
import org.sonar.api.server.authentication.Display;
import org.sonar.api.server.authentication.OAuth2IdentityProvider;
import org.sonar.api.server.authentication.UserIdentity;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

import static com.github.scribejava.core.model.OAuthConstants.EMPTY_TOKEN;
import static java.lang.String.format;

/**
 * created by jinxingjia on 2018/10/26 8:55
 **/
@ServerSide
public class OneIdentityProvider implements OAuth2IdentityProvider {



    private static final Logger LOGGER = Loggers.get(OneIdentityProvider.class);

    private final OneSettings oneSettings;
    public OneIdentityProvider(OneSettings oneSettings) {
        this.oneSettings = oneSettings;
    }

    public void init(InitContext initContext) {


        OAuthService scribe = prepareScribe(initContext).build();
        String url = scribe.getAuthorizationUrl(EMPTY_TOKEN);
        initContext.redirectTo(url);

    }

    public void callback(CallbackContext callbackContext) {
        HttpServletRequest request = callbackContext.getRequest();
        OAuthService scribe = prepareScribe(callbackContext).build();
        String oAuthVerifier = request.getParameter("code");

        Token accessToken = scribe.getAccessToken(EMPTY_TOKEN, new Verifier(oAuthVerifier));

        OAuthRequest userRequest = new OAuthRequest(Verb.GET, oneSettings.url() +
                "/api/" + "/user", scribe);
        scribe.signRequest(accessToken, userRequest);

        com.github.scribejava.core.model.Response userResponse = userRequest.send();
        if (!userResponse.isSuccessful()) {
            throw new IllegalStateException(format("Fail to authenticate the user. Error code is %s, Body of the response is %s", userResponse.getCode(), userResponse.getBody()));
        }
        String userResponseBody = userResponse.getBody();
        LOGGER.trace("User response received : %s", userResponseBody);
        GsonUser gsonUser = GsonUser.parse(userResponseBody);

        UserIdentity.Builder builder = UserIdentity.builder().setProviderLogin(gsonUser.getUsername()).setLogin(gsonUser.getUsername()).setName(gsonUser.getName()).setEmail(gsonUser.getEmail());
       /* if (!gitLabConfiguration.userExceptions().contains(gsonUser.getUsername())) {
            Set<String> groups = getUserGroups(accessToken);
            if (!groups.isEmpty()) {
                builder.setGroups(groups);
            }
        }*/

        callbackContext.authenticate(builder.build());
        callbackContext.redirectToRequestedPage();
    }

    public String getKey() {
        return "one";
    }

    public String getName() {
        return "one";
    }

    public Display getDisplay() {
        return Display.builder()
                .setIconPath("/static/authgitlab/gitlab.svg").setBackgroundColor("#333c47").build();
    }

    public boolean isEnabled() {
        return oneSettings.isEnabled();
    }

    public boolean allowsUsersToSignUp() {
        return oneSettings.allowUsersToSignUp();
    }





    private ServiceBuilder prepareScribe(OAuth2IdentityProvider.OAuth2Context context) {
        if (!isEnabled()) {
            throw new IllegalStateException("One Authentication is disabled");
        }
        ServiceBuilder serviceBuilder = new ServiceBuilder()
                .provider(new OneAuthApi(oneSettings.url()))
                .apiKey(oneSettings.clientId())
                .apiSecret(oneSettings.secret())
                .grantType(OAuthConstants.AUTHORIZATION_CODE)
                .callback(context.getCallbackUrl());

        if (oneSettings.scope() != null && !oneSettings.NONE_SCOPE.equals(oneSettings.scope())) {
            serviceBuilder.scope(oneSettings.scope());
        }
        return serviceBuilder;
    }
}
