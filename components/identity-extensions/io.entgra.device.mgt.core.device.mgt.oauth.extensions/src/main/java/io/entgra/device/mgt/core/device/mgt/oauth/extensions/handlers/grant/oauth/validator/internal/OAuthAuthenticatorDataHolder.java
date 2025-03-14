/*
 * Copyright (c) 2018 - 2023, Entgra (Pvt) Ltd. (http://www.entgra.io) All Rights Reserved.
 *
 * Entgra (Pvt) Ltd. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.entgra.device.mgt.core.device.mgt.oauth.extensions.handlers.grant.oauth.validator.internal;

import org.wso2.carbon.identity.oauth2.OAuth2TokenValidationService;

/**
 * DataHolder of Backend OAuth Authenticator component.
 */
public class OAuthAuthenticatorDataHolder {

    private OAuth2TokenValidationService oAuth2TokenValidationService;

    private static OAuthAuthenticatorDataHolder thisInstance = new OAuthAuthenticatorDataHolder();

    private OAuthAuthenticatorDataHolder() {}

    public static OAuthAuthenticatorDataHolder getInstance() {
        return thisInstance;
    }

    public OAuth2TokenValidationService getOAuth2TokenValidationService() {
        if (oAuth2TokenValidationService == null) {
            throw new IllegalStateException("OAuth2TokenValidation service is not initialized properly");
        }
        return oAuth2TokenValidationService;
    }

    public void setOAuth2TokenValidationService(
            OAuth2TokenValidationService oAuth2TokenValidationService) {
        this.oAuth2TokenValidationService = oAuth2TokenValidationService;
    }
}