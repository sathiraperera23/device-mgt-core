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

package io.entgra.device.mgt.core.apimgt.webapp.publisher;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.entgra.device.mgt.core.apimgt.annotations.Scope;
import io.entgra.device.mgt.core.apimgt.extension.rest.api.constants.Constants;
import io.entgra.device.mgt.core.apimgt.webapp.publisher.config.APIResource;
import io.entgra.device.mgt.core.apimgt.webapp.publisher.config.APIResourceConfiguration;
import io.entgra.device.mgt.core.apimgt.webapp.publisher.config.WebappPublisherConfig;
import io.entgra.device.mgt.core.apimgt.webapp.publisher.dto.ApiScope;
import io.entgra.device.mgt.core.apimgt.webapp.publisher.dto.ApiUriTemplate;
import io.entgra.device.mgt.core.apimgt.webapp.publisher.internal.APIPublisherDataHolder;
import io.entgra.device.mgt.core.apimgt.webapp.publisher.lifecycle.util.AnnotationProcessor;
import io.entgra.device.mgt.core.device.mgt.common.exceptions.MetadataManagementException;
import io.entgra.device.mgt.core.device.mgt.common.metadata.mgt.Metadata;
import org.apache.catalina.core.StandardContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.core.util.Utils;
import org.wso2.carbon.user.api.TenantManager;
import org.wso2.carbon.user.api.UserStoreException;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.*;

public class APIPublisherUtil {

    public static final String API_VERSION_PARAM = "{version}";
    public static final String PROPERTY_PROFILE = "profile";
    private static final Log log = LogFactory.getLog(APIPublisherUtil.class);
    private static final String DEFAULT_API_VERSION = "1.0.0";
    private static final String API_CONFIG_DEFAULT_VERSION = "1.0.0";
    private static final String PARAM_MANAGED_API_ENDPOINT = "managed-api-endpoint";
    private static final String PARAM_MANAGED_API_ENDPOINT_TYPE = "managed-api-endpoint-type";
    private static final String PARAM_MANAGED_API_TRANSPORTS = "managed-api-transports";
    private static final String PARAM_MANAGED_API_POLICY = "managed-api-policy";
    private static final String PARAM_MANAGED_API_IS_SECURED = "managed-api-isSecured";
    private static final String PARAM_SHARED_WITH_ALL_TENANTS = "isSharedWithAllTenants";
    private static final String PARAM_PROVIDER_TENANT_DOMAIN = "providerTenantDomain";
    private static final String NON_SECURED_RESOURCES = "nonSecuredEndPoints";
    private static final String AUTH_TYPE_NON_SECURED = "None";
    private static final String PARAM_IS_DEFAULT = "isDefault";
    private static final Gson gson = new Gson();

    public static String getServerBaseUrl() {
        WebappPublisherConfig webappPublisherConfig = WebappPublisherConfig.getInstance();
        return Utils.replaceSystemProperty(webappPublisherConfig.getHost());
    }

    public static String getWsServerBaseUrl() {
        return getServerBaseUrl().replace("https", "wss");
    }

    public static String getApiEndpointUrl(String context) {
        return getServerBaseUrl() + context;
    }

    public static String getWsApiEndpointUrl(String context) {
        return getWsServerBaseUrl() + context;
    }

    /**
     * Build the API Configuration to be passed to APIM, from a given list of URL templates
     *
     * @param servletContext
     * @param apiDef
     * @return
     */
    public static APIConfig buildApiConfig(ServletContext servletContext, APIResourceConfiguration apiDef)
            throws UserStoreException {
        APIConfig apiConfig = new APIConfig();

        String name = apiDef.getName();
        if (name == null || name.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("API Name not set in @SwaggerDefinition Annotation");
            }
            name = servletContext.getServletContextName();
        }
        apiConfig.setName(name);

        String version = apiDef.getVersion();
        if (version == null || version.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("'API Version not set in @SwaggerDefinition Annotation'");
            }
            version = API_CONFIG_DEFAULT_VERSION;
        }
        apiConfig.setVersion(version);

        String apiDocumentationName = apiDef.getApiDocumentationName();
        if (apiDocumentationName == null || apiDocumentationName.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("'API Documentation not set in @SwaggerDefinition Annotation'");
            }
        } else {
            apiConfig.setApiDocumentationName(apiDef.getApiDocumentationName());
        }

        String apiDocumentationSummary = apiDef.getApiDocumentationSummary();
        if (apiDocumentationSummary == null || apiDocumentationSummary.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("'API Documentation summary not set in @SwaggerDefinition Annotation'");
            }
        } else {
            apiConfig.setApiDocumentationSummary(apiDef.getApiDocumentationSummary());
        }

        String apiDocumentationSourceFile = apiDef.getApiDocumentationSourceFile();
        if (apiDocumentationSourceFile == null || apiDocumentationSourceFile.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("'API Documentation source file not set in @SwaggerDefinition Annotation'");
            }
        } else {
            apiConfig.setApiDocumentationSourceFile(apiDef.getApiDocumentationSourceFile());
        }
        String context = apiDef.getContext();
        if (context == null || context.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("'API Context not set in @SwaggerDefinition Annotation'");
            }
            context = servletContext.getContextPath();
        }
        apiConfig.setContext(context);

        apiConfig.setEndpointType(apiDef.getEndpointType());

        apiConfig.setInSequenceName(apiDef.getInSequenceName());

        apiConfig.setInSequenceConfig(apiDef.getInSequenceConfig());

        apiConfig.setAsyncApiDefinition(apiDef.getAsyncApiDefinition());

        String[] tags = apiDef.getTags();
        if (tags == null || tags.length == 0) {
            if (log.isDebugEnabled()) {
                log.debug("'API tag not set in @SwaggerDefinition Annotation'");
            }
        } else {
            apiConfig.setTags(tags);
        }

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        servletContext.setAttribute(PARAM_PROVIDER_TENANT_DOMAIN, tenantDomain);
        tenantDomain = (tenantDomain != null && !tenantDomain.isEmpty()) ? tenantDomain :
                MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
        apiConfig.setTenantDomain(tenantDomain);

        String endpoint = servletContext.getInitParameter(PARAM_MANAGED_API_ENDPOINT);
        if (endpoint == null || endpoint.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("'managed-api-endpoint' attribute is not configured");
            }
            String endpointContext = apiDef.getContext();
            endpoint = APIPublisherUtil.getApiEndpointUrl(endpointContext);

            if ("WS".equals(apiDef.getEndpointType())) {
                endpoint = APIPublisherUtil.getWsApiEndpointUrl(endpointContext);
            }
        }
        apiConfig.setEndpoint(endpoint);

        String owner = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUserRealm().getRealmConfiguration()
                .getAdminUserName();
        if (!MultitenantConstants.SUPER_TENANT_DOMAIN_NAME.equals(tenantDomain)) {
            owner = owner + "@" + tenantDomain;
        }
        if (owner == null || owner.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("'managed-api-owner' attribute is not configured");
            }
        }
        apiConfig.setOwner(owner);

        apiConfig.setSecured(false);

        boolean isDefault = true;
        String isDefaultParam = servletContext.getInitParameter(PARAM_IS_DEFAULT);
        if (isDefaultParam != null && !isDefaultParam.isEmpty()) {
            isDefault = Boolean.parseBoolean(isDefaultParam);
        }
        apiConfig.setDefault(isDefault);

        String transports = servletContext.getInitParameter(PARAM_MANAGED_API_TRANSPORTS);
        if (transports == null || transports.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("'managed-api-transports' attribute is not configured. Therefore using the default, " +
                        "which is 'https'");
            }
            transports = "https,http";
            if ("WS".equals(apiDef.getEndpointType())) {
                transports = "wss,ws";
            }
        }
        apiConfig.setTransports(transports);

        String sharingValueParam = servletContext.getInitParameter(PARAM_SHARED_WITH_ALL_TENANTS);
        boolean isSharedWithAllTenants = Boolean.parseBoolean(sharingValueParam);
        if (isSharedWithAllTenants && !MultitenantConstants.SUPER_TENANT_DOMAIN_NAME.equals(tenantDomain)) {
            isSharedWithAllTenants = false;
        }
        apiConfig.setSharedWithAllTenants(isSharedWithAllTenants);

        Set<ApiUriTemplate> uriTemplates = new LinkedHashSet<>();
        for (APIResource apiResource : apiDef.getResources()) {
            ApiUriTemplate template = new ApiUriTemplate();
            template.setAuthType(apiResource.getAuthType());
            template.setHttpVerb(apiResource.getHttpVerb());
            template.setResourceURI(apiResource.getUri());
            template.setUriTemplate(apiResource.getUriTemplate());
            template.setScope(apiResource.getScope());
            template.setUriMapping(apiResource.getUriMapping());
            uriTemplates.add(template);
        }
        apiConfig.setUriTemplates(uriTemplates);
        // adding scopes to the api
        Map<String, ApiScope> apiScopes = new HashMap<>();
        if (uriTemplates != null) {
            // this creates distinct scopes list
            for (ApiUriTemplate template : uriTemplates) {
                ApiScope scope = template.getScope();
                if (scope != null) {
                    if (apiScopes.get(scope.getKey()) == null) {
                        apiScopes.put(scope.getKey(), scope);
                    }
                }
            }
            Set<ApiScope> scopes = new HashSet<>(apiScopes.values());
            // set current scopes to API
            apiConfig.setScopes(scopes);
        }

        String policy = servletContext.getInitParameter(PARAM_MANAGED_API_POLICY);
        if (policy == null || policy.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("'managed-api-policy' attribute is not configured. Therefore using the default, " +
                        "which is 'null'");
            }
            policy = null;
        }
        apiConfig.setPolicy(policy);
        setResourceAuthTypes(servletContext, apiConfig);
        return apiConfig;
    }

    public static String getSwaggerDefinition(APIConfig apiConfig) {
        Map<String, JsonObject> httpVerbsMap = new HashMap<>();
        List<ApiScope> scopes = new ArrayList<>();

        for (ApiUriTemplate uriTemplate : apiConfig.getUriTemplates()) {
            JsonObject response = new JsonObject();
            response.addProperty("200", "");

            JsonObject responses = new JsonObject();
            responses.add("responses", response);
            JsonObject httpVerbs = httpVerbsMap.get(uriTemplate.getUriTemplate());
            if (httpVerbs == null) {
                httpVerbs = new JsonObject();
            }
            JsonObject httpVerb = new JsonObject();
            httpVerb.add("responses", response);

            httpVerb.addProperty("x-auth-type", uriTemplate.getAuthType());
            httpVerb.addProperty("x-throttling-tier", "Unlimited");
            if (uriTemplate.getScope() != null) {
                httpVerb.addProperty("x-scope", uriTemplate.getScope().getKey());
                scopes.add(uriTemplate.getScope());
            }
            httpVerbs.add(uriTemplate.getHttpVerb().toLowerCase(), httpVerb);
            httpVerbsMap.put(uriTemplate.getUriTemplate(), httpVerbs);
        }

        Iterator it = httpVerbsMap.entrySet().iterator();
        JsonObject paths = new JsonObject();
        while (it.hasNext()) {
            Map.Entry<String, JsonObject> pair = (Map.Entry) it.next();
            paths.add(pair.getKey(), pair.getValue());
            it.remove();
        }

        JsonObject info = new JsonObject();
        info.addProperty("title", apiConfig.getName());
        info.addProperty("version", apiConfig.getVersion());

        JsonObject swaggerDefinition = new JsonObject();
        swaggerDefinition.add("paths", paths);
        swaggerDefinition.addProperty("swagger", "2.0");
        swaggerDefinition.add("info", info);

        // adding scopes to swagger definition
        if (!apiConfig.getScopes().isEmpty()) {
            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(apiConfig.getScopes(), new TypeToken<Set<Scope>>() {
            }.getType());
            if (element != null) {
                JsonArray apiScopes = element.getAsJsonArray();
                JsonObject apim = new JsonObject();
                apim.add("x-wso2-scopes", apiScopes);
                JsonObject wso2Security = new JsonObject();
                wso2Security.add("apim", apim);
                swaggerDefinition.add("x-wso2-security", wso2Security);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("API swagger definition: " + swaggerDefinition);
        }
        return swaggerDefinition.toString();
    }


    public static void setResourceAuthTypes(ServletContext servletContext, APIConfig apiConfig) {
        List<String> resourcesList = null;
        String nonSecuredResources = servletContext.getInitParameter(NON_SECURED_RESOURCES);
        if (null != nonSecuredResources) {
            resourcesList = Arrays.asList(nonSecuredResources.split(","));
        }
        Set<ApiUriTemplate> templates = apiConfig.getUriTemplates();
        if (null != resourcesList) {
            for (ApiUriTemplate template : templates) {
                String fullPaath = "";
                if (!template.getUriTemplate().equals(AnnotationProcessor.WILD_CARD)) {
                    fullPaath = apiConfig.getContext() + template.getUriTemplate();
                } else {
                    fullPaath = apiConfig.getContext();
                }
                for (String context : resourcesList) {
                    if (context.trim().equals(fullPaath)) {
                        template.setAuthType(AUTH_TYPE_NON_SECURED);
                    }
                }
            }
        }
        apiConfig.setUriTemplates(templates);
    }

    /**
     * This method will extract and retrieve the API resource configuration by processing the API resources
     * @param standardContext {@link StandardContext}
     * @param servletContext {@link ServletContext}
     * @return Extracted {@link APIResourceConfiguration} list describing from the servlet context
     * @throws IOException Throws when error occurred while processing the swagger annotations
     * @throws ClassNotFoundException Throws when error occurred while extracting api configurations
     */
    public static List<APIResourceConfiguration> getAPIResourceConfiguration(StandardContext standardContext, ServletContext servletContext)
            throws IOException, ClassNotFoundException {
        List<APIResourceConfiguration> apiResourceConfigurations = new ArrayList<>();
        String profile = System.getProperty(PROPERTY_PROFILE);
        if (WebappPublisherConfig.getInstance().getProfiles().getProfile().contains(profile.toLowerCase())) {
            AnnotationProcessor annotationProcessor = new AnnotationProcessor(standardContext);
            Set<String> annotatedSwaggerAPIClasses = annotationProcessor.
                    scanStandardContext(io.swagger.annotations.SwaggerDefinition.class.getName());
            apiResourceConfigurations = annotationProcessor.extractAPIInfo(servletContext,
                    annotatedSwaggerAPIClasses);
        }
        return apiResourceConfigurations;
    }

    /**
     * This method can use to publish the apis after the server startup complete.
     *
     * @param apiConfig {@link APIConfig} Contains API definition
     */
    public static void publishAPIAfterServerStartup(APIConfig apiConfig) {
        APIPublisherDataHolder apiPublisherDataHolder = APIPublisherDataHolder.getInstance();
        if (!apiPublisherDataHolder.isServerStarted()) {
            if (log.isDebugEnabled()) {
                log.debug("Abort publishing the API [" + apiConfig.getName() + "]. Server still starting");
            }
            throw new IllegalStateException("Server starting procedure is still not completed");
        }

        TenantManager tenantManager = apiPublisherDataHolder.getTenantManager();
        if (tenantManager == null) {
            throw new IllegalStateException("Tenant manager service not initialized properly");
        }
        try {
            if (tenantManager.isTenantActive(tenantManager.getTenantId(apiConfig.getTenantDomain()))) {
                APIPublisherService apiPublisherService = apiPublisherDataHolder.getApiPublisherService();
                if (apiPublisherService == null) {
                    throw new IllegalStateException("API Publisher service is not initialized properly");
                }
                apiPublisherService.publishAPI(apiConfig);
                for (ApiScope scope : apiConfig.getScopes()) {
                    apiPublisherDataHolder.getPermScopeMapping().putIfAbsent(scope.getPermissions(), scope.getKey());
                }

                Metadata permScopeMapping = new Metadata();
                permScopeMapping.setMetaKey(Constants.PERM_SCOPE_MAPPING_META_KEY);
                permScopeMapping.setMetaValue(gson.toJson(apiPublisherDataHolder.getPermScopeMapping()));

                try {
                    apiPublisherDataHolder.getMetadataManagementService().updateMetadata(permScopeMapping);
                } catch (MetadataManagementException e) {
                    log.error("Error encountered while updating the " + Constants.PERM_SCOPE_MAPPING_META_KEY + "entry");
                }
            } else {
                log.error("Can't find an active tenant under tenant domain " + apiConfig.getTenantDomain());
            }
        } catch (Throwable e) {
            log.error("Error occurred while publishing API '" + apiConfig.getName() + "' with the context '" +
                    apiConfig.getContext() + "' and version '" + apiConfig.getVersion() + "'", e);
        }
    }
}
