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
package io.entgra.device.mgt.core.device.mgt.core.operation.mgt;

import java.io.Serializable;
import java.util.List;

public class ProfileOperation extends ConfigOperation implements Serializable {

    private static final long serialVersionUID = -3322674908775087365L;
    private List<Integer> correctiveActionIds;

    private List<Integer> reactiveActionIds;

    public ProfileOperation() {
        super();
    }

    public Type getType() {
        return Type.PROFILE;
    }

    public List<Integer> getCorrectiveActionIds() {
        return correctiveActionIds;
    }

    public void setCorrectiveActionIds(List<Integer> correctiveActionIds) {
        this.correctiveActionIds = correctiveActionIds;
    }

    public List<Integer> getReactiveActionIds() {
        return reactiveActionIds;
    }

    public void setReactiveActionIds(List<Integer> reactiveActionIds) {
        this.reactiveActionIds = reactiveActionIds;
    }

}
