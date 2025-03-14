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

package io.entgra.device.mgt.core.device.mgt.core.status.task;

import io.entgra.device.mgt.core.device.mgt.common.DeviceStatusTaskPluginConfig;
import io.entgra.device.mgt.core.device.mgt.core.dto.DeviceType;

/**
 * This interface defines the methods that should be implemented by the management service of
 * DeviceStatusMonitoringTask.
 */
public interface DeviceStatusTaskManagerService {

    /**
     * This method will start the task.
     *
     * @param deviceType - DeviceType
     * @param deviceStatusTaskConfig - DeviceStatusTaskConfig
     * @throws DeviceStatusTaskException
     */
    void startTask(DeviceType deviceType, DeviceStatusTaskPluginConfig deviceStatusTaskConfig)
            throws DeviceStatusTaskException;

    /**
     * This method will stop the task.
     *
     * @param deviceType - DeviceType
     * @param deviceStatusTaskConfig - DeviceStatusTaskConfig
     * @throws DeviceStatusTaskException
     */
    void stopTask(DeviceType deviceType, DeviceStatusTaskPluginConfig deviceStatusTaskConfig)
            throws DeviceStatusTaskException;

    /**
     * This will update the task frequency which it runs.
     *
     * @param deviceType
     * @param deviceStatusTaskConfig - DeviceStatusTaskConfig
     * @throws DeviceStatusTaskException
     */
    void updateTask(DeviceType deviceType, DeviceStatusTaskPluginConfig deviceStatusTaskConfig)
            throws DeviceStatusTaskException;

    /**
     * This will check weather the task is scheduled.
     * @param deviceType - Device Type
     * @throws DeviceStatusTaskException
     */
    boolean isTaskScheduled(DeviceType deviceType) throws DeviceStatusTaskException;
}