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

package io.entgra.device.mgt.core.device.mgt.common.general;

import java.util.Map;

public class QREnrollmentDetails {
    String ownershipType;
    String username;
    String enrollmentMode;
    Map<String, String> customValues;
    int tokenExpiry;

    public Map<String, String> getCustomValues() {
        return customValues;
    }

    public void setCustomValues(Map<String, String> customValues) {
        this.customValues = customValues;
    }

    public String getOwnershipType() { return ownershipType; }

    public void setOwnershipType(String ownershipType) { this.ownershipType = ownershipType; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEnrollmentMode() { return enrollmentMode; }

    public void setEnrollmentMode(String enrollmentMode) { this.enrollmentMode = enrollmentMode; }

    public int getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(int tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }
}
