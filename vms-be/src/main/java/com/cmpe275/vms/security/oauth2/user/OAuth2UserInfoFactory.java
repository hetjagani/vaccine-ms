package com.cmpe275.vms.security.oauth2.user;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return new GoogleOAuth2UserInfo(attributes);
    }
}
