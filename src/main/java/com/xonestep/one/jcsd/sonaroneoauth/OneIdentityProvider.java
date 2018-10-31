/*
 * One OAuth 2.0 Authentication for SonarQube
 * Copyright (C) 2009-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
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

        OAuthRequest userRequest = new OAuthRequest(Verb.GET, oneSettings.userUrl() +"/one/sso/user", scribe);
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
        return "newtouchone";
    }

    public String getName() {
        return "NewtouchOne";
    }

    public Display getDisplay() {
        return Display.builder()
                .setIconPath("/static/oneoauth2/newtouch-one.png").setBackgroundColor("#333c47").build();
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
                .provider(new OneAuthApi(oneSettings.ssoUrl()))
                .apiKey(oneSettings.clientId())
                .apiSecret(oneSettings.secret())
                .grantType(OAuthConstants.AUTHORIZATION_CODE)
                .callback(context.getCallbackUrl());
        LOGGER.info("call back url:"+context.getCallbackUrl());

        if (oneSettings.scope() != null && !oneSettings.NONE_SCOPE.equals(oneSettings.scope())) {
            serviceBuilder.scope(oneSettings.scope());
        }
        return serviceBuilder;
    }
}
