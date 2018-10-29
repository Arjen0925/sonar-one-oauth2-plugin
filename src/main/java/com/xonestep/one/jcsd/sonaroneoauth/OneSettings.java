package com.xonestep.one.jcsd.sonaroneoauth;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.server.ServerSide;

import javax.annotation.CheckForNull;
import java.util.Arrays;
import java.util.List;
import static java.lang.String.valueOf;
import static org.sonar.api.PropertyType.BOOLEAN;
import static org.sonar.api.PropertyType.STRING;

/**
 * created by jinxingjia on 2018/10/26 9:05
 **/
@ServerSide
public class OneSettings {

    private static final String CLIENT_ID = "sonar.auth.github.clientId.secured";
    private static final String CLIENT_SECRET = "sonar.auth.github.clientSecret.secured";
    private static final String ENABLED = "sonar.auth.github.enabled";
    private static final String ALLOW_USERS_TO_SIGN_UP = "sonar.auth.github.allowUsersToSignUp";
    private static final String GROUPS_SYNC = "sonar.auth.github.groupsSync";
    private static final String WEB_URL = "sonar.auth.github.webUrl";
    private static final String SCOPE = "sonar.auth.github.webUrl";
    private static final String ORGANIZATIONS = "sonar.auth.github.organizations";
    private static final String CATEGORY = "github";
    private static final String SUBCATEGORY = "authentication";
    public static final String NONE_SCOPE = "none";

    private final Settings settings;

    public OneSettings(Settings settings) {
        this.settings = settings;
    }

    @CheckForNull
    public String url() {
        return settings.getString(WEB_URL);
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
                        .description("Enable GitHub users to login. Value is ignored if client ID and secret are not defined.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(BOOLEAN)
                        .defaultValue(valueOf(false))
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(CLIENT_ID)
                        .name("Client ID")
                        .description("Client ID provided by GitHub when registering the application.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(CLIENT_SECRET)
                        .name("Client Secret")
                        .description("Client password provided by GitHub when registering the application.")
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
                PropertyDefinition.builder(GROUPS_SYNC)
                        .name("Synchronize teams as groups")
                        .description("For each team he belongs to, the user will be associated to a group named 'Organisation/Team' (if it exists) in SonarQube.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(BOOLEAN)
                        .defaultValue(valueOf(false))
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(SCOPE)
                        .name("The API url for a GitHub instance.")
                        .description("The API url for a GitHub instance. https://api.github.com/ for github.com, https://github.company.com/api/v3/ when using Github Enterprise")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(STRING)
                        .defaultValue(valueOf("https://api.github.com/"))
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(WEB_URL)
                        .name("The WEB url for a GitHub instance.")
                        .description("The WEB url for a GitHub instance. " +
                                "https://github.com/ for github.com, https://github.company.com/ when using GitHub Enterprise.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(STRING)
                        .defaultValue(valueOf("https://github.com/"))
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(ORGANIZATIONS)
                        .name("Organizations")
                        .description("Only members of these organizations will be able to authenticate to the server. " +
                                "If a user is a member of any of the organizations listed they will be authenticated.")
                        .multiValues(true)
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .index(index++)
                        .build());
    }
}
