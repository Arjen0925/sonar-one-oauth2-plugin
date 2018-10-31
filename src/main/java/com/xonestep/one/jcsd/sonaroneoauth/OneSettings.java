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

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.server.ServerSide;

import javax.annotation.CheckForNull;
import java.util.Arrays;
import java.util.List;

import static org.sonar.api.PropertyType.*;
import static java.lang.String.valueOf;


/**
 * created by jinxingjia on 2018/10/26 9:05
 **/
@ServerSide
public class OneSettings {

    private static final String CLIENT_ID = "sonar.auth.one.oauth2.clientId";
    private static final String CLIENT_SECRET = "sonar.auth.one.oauth2.clientSecret";
    private static final String ENABLED = "sonar.auth.one.oauth2.enabled";
    private static final String ALLOW_USERS_TO_SIGN_UP = "sonar.auth.one.oauth2.allowUsersToSignUp";
    private static final String GROUPS_SYNC = "sonar.auth.one.oauth2.groupsSync";
    private static final String ONE_USER_URL = "sonar.auth.one.oauth2.sso.url";
    private static final String ONE_SSO_URL = "sonar.auth.one.oauth2.user.url";
    private static final String SCOPE = "sonar.auth.one.scope";
    private static final String ORGANIZATIONS = "sonar.auth.one.oauth2.organizations";
    private static final String CATEGORY = "newtouch-one";
    private static final String SUBCATEGORY = "authentication";
    public static final String NONE_SCOPE = "none";
    public static final String READ_USER_SCOPE = "read_user";

    private final Settings settings;

    public OneSettings(Settings settings) {
        this.settings = settings;
    }

    @CheckForNull
    public String ssoUrl() {
        return settings.getString(ONE_SSO_URL);
    }

    @CheckForNull
    public String userUrl() {
        return settings.getString(ONE_USER_URL);
    }

    @CheckForNull
    public String clientId() {
        return settings.getString(CLIENT_ID);
    }

    @CheckForNull
    public String secret() {
        return settings.getString(CLIENT_SECRET);
    }

    public String scope() {
        return settings.getString(SCOPE);
    }

    public boolean isEnabled() {
        return settings.getBoolean(ENABLED) && clientId() != null && secret() != null;
    }

    public boolean allowUsersToSignUp() {
        return settings.getBoolean(ALLOW_USERS_TO_SIGN_UP);
    }

    public static List<PropertyDefinition> definitions() {
        int index = 1;
        return Arrays.asList(
                PropertyDefinition.builder(ENABLED)
                        .name("Enabled")
                        .description("Enable One users to login. Value is ignored if client ID and secret are not defined.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(BOOLEAN)
                        .defaultValue(valueOf(false))
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(CLIENT_ID)
                        .name("Client ID")
                        .description("Client ID provided by One when registering the application.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(CLIENT_SECRET)
                        .name("Client Secret")
                        .description("Client password provided by One when registering the application.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(ALLOW_USERS_TO_SIGN_UP)
                        .name("Allow users to sign-up")
                        .description("Allow new users to authenticate. When set to 'false', only existing users will be able to authenticate to the server.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(BOOLEAN)
                        .defaultValue(valueOf(true))
                        .index(index++)
                        .build(),
                /*PropertyDefinition.builder(GROUPS_SYNC)
                        .name("Synchronize teams as groups")
                        .description("For each team he belongs to, the user will be associated to a group named 'Organisation/Team' (if it exists) in SonarQube.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(BOOLEAN)
                        .defaultValue(valueOf(false))
                        .index(index++)
                        .build(),*/
                PropertyDefinition.builder(ONE_USER_URL)
                        .name("The WEB url for a One_user instance.")
                        .description("The WEB url for a One_user instance")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(STRING)
                        .defaultValue(valueOf("http://user-web.test.onetest.newtouch.com"))
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(SCOPE)
                        .name("One access scope.")
                        .description("Scope provided by One when access user info")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(SINGLE_SELECT_LIST)
                        .options(NONE_SCOPE, READ_USER_SCOPE)
                        .defaultValue(READ_USER_SCOPE)
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(ONE_SSO_URL)
                        .name("The WEB url for a ONE_SSO instance.")
                        .description("The WEB url for a ONE_SSO instance.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(STRING)
                        .defaultValue(valueOf("http://web.test.onetest.newtouch.com"))
                        .index(index++)
                        /* .build(),
                 PropertyDefinition.builder(ORGANIZATIONS)
                         .name("Organizations")
                         .description("Only members of these organizations will be able to authenticate to the server. " +
                                 "If a user is a member of any of the organizations listed they will be authenticated.")
                         .multiValues(true)
                         .category(CATEGORY)
                         .subCategory(SUBCATEGORY)
                         .index(index++)*/
                        .build());
    }
}
