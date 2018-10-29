package com.xonestep.one.jcsd.sonaroneoauth;

import org.sonar.api.Plugin;

/**
 * created by jinxingjia on 2018/10/26 8:52
 **/
public class AuthOnePlugin implements Plugin {

    @Override
    public void define(Context context) {
        context.addExtensions(
                OneIdentityProvider.class,
                OneSettings.class,
                OneAuthApi.class);
        context.addExtensions(OneSettings.definitions());
    }
}
