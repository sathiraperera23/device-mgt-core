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

package io.entgra.device.mgt.core.policy.mgt.core.mgt.impl;

import io.entgra.device.mgt.core.device.mgt.common.Feature;
import io.entgra.device.mgt.core.device.mgt.common.policy.mgt.Profile;
import io.entgra.device.mgt.core.device.mgt.common.policy.mgt.ProfileFeature;
import io.entgra.device.mgt.core.policy.mgt.common.FeatureManagementException;
import io.entgra.device.mgt.core.policy.mgt.core.dao.*;
import io.entgra.device.mgt.core.policy.mgt.core.mgt.FeatureManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;
import java.util.List;

public class FeatureManagerImpl implements FeatureManager {

    private FeatureDAO featureDAO;
    private ProfileDAO profileDAO;
    private static Log log = LogFactory.getLog(FeatureManagerImpl.class);

    public FeatureManagerImpl() {
        featureDAO = PolicyManagementDAOFactory.getFeatureDAO();
        profileDAO = PolicyManagementDAOFactory.getProfileDAO();
    }

    @Override
    public ProfileFeature addProfileFeature(ProfileFeature feature, int profileId) throws FeatureManagementException {
        try {
            PolicyManagementDAOFactory.beginTransaction();
            Profile profile = profileDAO.getProfile(profileId);
            if (profile == null) {
                throw new FeatureManagementException(
                        "Could not find a profile with the profile id: " + profileId);
            }
            feature = featureDAO.addProfileFeature(feature, profileId);
            PolicyManagementDAOFactory.commitTransaction();
        } catch (ProfileManagerDAOException | PolicyManagerDAOException | FeatureManagerDAOException e) {
            throw new FeatureManagementException("Error occurred while adding profile feature (" +
                                                         feature.getFeatureCode() + " - " + profileId + ")", e);
        } finally {
            PolicyManagementDAOFactory.closeConnection();
        }
        return feature;
    }

    @Override
    public ProfileFeature updateProfileFeature(ProfileFeature feature, int profileId) throws
                                                                                      FeatureManagementException {
        try {
            PolicyManagementDAOFactory.beginTransaction();
            Profile profile = profileDAO.getProfile(profileId);
            if (profile == null) {
                throw new FeatureManagementException(
                        "Could not find a profile with the profile id: " + profileId);
            }
            feature = featureDAO.updateProfileFeature(feature, profileId);
            PolicyManagementDAOFactory.commitTransaction();

        } catch (ProfileManagerDAOException | FeatureManagerDAOException | PolicyManagerDAOException e) {
            PolicyManagementDAOFactory.rollbackTransaction();
            throw new FeatureManagementException("Error occurred while updating feature (" +
                                                         feature.getFeatureCode() + " - " + profileId +
                                                         ") in database.", e);
        } finally {
            PolicyManagementDAOFactory.closeConnection();
        }
        return feature;
    }

    @Override
    public List<ProfileFeature> addProfileFeatures(List<ProfileFeature> features, int profileId) throws
                                                                                                 FeatureManagementException {
        try {
            PolicyManagementDAOFactory.beginTransaction();
            Profile profile = profileDAO.getProfile(profileId);
            if (profile == null) {
                throw new FeatureManagementException(
                        "Could not find a profile with the profile id: " + profileId);
            }
            features = featureDAO.addProfileFeatures(features, profileId);
            PolicyManagementDAOFactory.commitTransaction();
        } catch (ProfileManagerDAOException | FeatureManagerDAOException e) {
            PolicyManagementDAOFactory.rollbackTransaction();
            throw new FeatureManagementException("Error occurred while adding the features to profile id (" +
                                                         profileId + ")", e);
        } catch (PolicyManagerDAOException e) {
            throw new FeatureManagementException("Error occurred while adding the features to profile id (" +
                                                         profileId + ") to the database", e);
        } finally {
            PolicyManagementDAOFactory.closeConnection();
        }
        return features;
    }

    @Override
    public List<ProfileFeature> updateProfileFeatures(List<ProfileFeature> features, int profileId) throws
                                                                                                    FeatureManagementException {
        try {
            PolicyManagementDAOFactory.beginTransaction();
            Profile profile = profileDAO.getProfile(profileId);
            if (profile == null) {
                throw new FeatureManagementException(
                        "Could not find a profile with the profile id: " + profileId);
            }
            features = featureDAO.updateProfileFeatures(features, profileId);
            PolicyManagementDAOFactory.commitTransaction();
        } catch (ProfileManagerDAOException | FeatureManagerDAOException e) {
            PolicyManagementDAOFactory.rollbackTransaction();
            throw new FeatureManagementException("Error occurred while updating the features to profile id (" +
                                                         profileId + ")", e);
        } catch (PolicyManagerDAOException e) {
            throw new FeatureManagementException("Error occurred while updating the features to profile id (" +
                                                         profileId + ") to the database", e);
        } finally {
            PolicyManagementDAOFactory.closeConnection();
        }
        return features;
    }

    @Override
    public List<Feature> getAllFeatures(String deviceType) throws FeatureManagementException {
        try {
            PolicyManagementDAOFactory.openConnection();
            return featureDAO.getAllFeatures(deviceType);
        } catch (FeatureManagerDAOException e) {
            throw new FeatureManagementException("Error occurred while retrieving the features", e);
        } catch (SQLException e) {
            throw new FeatureManagementException("Error occurred while opening a connection to the data source", e);
        } finally {
            PolicyManagementDAOFactory.closeConnection();
        }
    }

    @Override
    public List<ProfileFeature> getFeaturesForProfile(int profileId) throws FeatureManagementException {
        try {
            PolicyManagementDAOFactory.openConnection();
            Profile profile = profileDAO.getProfile(profileId);
            if (profile == null) {
                throw new FeatureManagementException(
                        "Could not find a profile with the profile id: " + profileId);
            }
            return featureDAO.getFeaturesForProfile(profileId);
        } catch (ProfileManagerDAOException | FeatureManagerDAOException e) {
            throw new FeatureManagementException("Error occurred while getting the features", e);
        } catch (SQLException e) {
            throw new FeatureManagementException("Error occurred while opening a connection to the data source", e);
        } finally {
            PolicyManagementDAOFactory.closeConnection();
        }
    }

    @Override
    public boolean deleteFeaturesOfProfile(Profile profile) throws FeatureManagementException {
        boolean bool;
        try {
            PolicyManagementDAOFactory.beginTransaction();
            bool = featureDAO.deleteFeaturesOfProfile(profile);
            PolicyManagementDAOFactory.commitTransaction();
        } catch (FeatureManagerDAOException e) {
            PolicyManagementDAOFactory.rollbackTransaction();
            throw new FeatureManagementException("Error occurred while deleting the feature of - profile (" +
                                                         profile.getProfileName() + ")", e);
        } catch (PolicyManagerDAOException e) {
            throw new FeatureManagementException("Error occurred while deleting the feature of - profile (" +
                                                         profile.getProfileName() + ") from database", e);
        } finally {
            PolicyManagementDAOFactory.closeConnection();
        }
        return bool;
    }
}
