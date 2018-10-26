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
        Preconditions.checkValidUrl(oAuthConfig.getCallback(), "Must provide a valid url as callback. GitLab does not support OOB");
        String authUrl = String.format("%s/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                this.url, oAuthConfig.getApiKey(), OAuthEncoder.encode(oAuthConfig.getCallback()));
        if (oAuthConfig.hasScope()) {
            authUrl += "&scope=" + OAuthEncoder.encode(oAuthConfig.getScope());
        }
        return authUrl;
    }
}
