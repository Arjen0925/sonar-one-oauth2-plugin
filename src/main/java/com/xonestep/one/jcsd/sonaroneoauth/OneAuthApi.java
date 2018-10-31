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

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.extractors.JsonTokenExtractor;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;
import org.sonar.api.server.ServerSide;

/**
 * created by jinxingjia on 2018/10/26 9:41
 **/
@ServerSide
public class OneAuthApi extends DefaultApi20 {


    private final String url;

    public OneAuthApi(String url) {
        super();
        this.url = url;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public String getAccessTokenEndpoint() {
        return url + "/oauth/token";
    }

    public String getAuthorizationUrl(OAuthConfig oAuthConfig) {
        Preconditions.checkValidUrl(oAuthConfig.getCallback(), "Must provide a valid url as callback. One does not support OOB");
        String authUrl = String.format("%s/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                this.url, oAuthConfig.getApiKey(), OAuthEncoder.encode(oAuthConfig.getCallback()));
        if (oAuthConfig.hasScope()) {
            authUrl += "&scope=" + OAuthEncoder.encode(oAuthConfig.getScope());
        }
        return authUrl;
    }

    public String getAuthorizationUrl(OAuthConfig config,String callback) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. GitLab does not support OOB");
        String authUrl = String.format("%s/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code", this.url, config.getApiKey(), OAuthEncoder.encode(callback));
        if (config.hasScope()) {
            authUrl += "&scope=" + OAuthEncoder.encode(config.getScope());
        }
        return authUrl;
    }
}
