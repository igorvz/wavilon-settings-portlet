package com.aimprosoft.wavilon.util;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;

import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class CouchModelUtil {
    private static CouchModelLiteDatabaseService modelLiteService = ObjectFactory.getBean(CouchModelLiteDatabaseService.class);


    public static CouchModel newCouchModel(PortletRequest request, CouchTypes couchTypes) {
        CouchModel newCouchModel = new CouchModel();

        try {
            newCouchModel.setId(UUID.randomUUID().toString());
            newCouchModel.setLiferayUserId(PortalUtil.getUserId(request));
            newCouchModel.setLiferayOrganizationId(PortalUtil.getScopeGroupId(request));
            newCouchModel.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
        } catch (Exception ignored) {
        }
        newCouchModel.setType(couchTypes);

        return newCouchModel;
    }

    public static CouchModelLite getCouchModelLite(String id) {
        try {
            return modelLiteService.getCouchLiteModel(id);
        } catch (Exception e) {
            CouchModelLite modelLite = new CouchModelLite();
            modelLite.setId(id);
            modelLite.setName("This entity has been removed!");
            return modelLite;
        }
    }

    public static List<CouchModelLite> getForwards(Long userId, Long organizationId) {
        List<CouchModelLite> modelLiteList = new LinkedList<CouchModelLite>();

        try {
            modelLiteList.addAll(modelLiteService.getAllCouchModelsLite(userId, organizationId, CouchTypes.queue));
            modelLiteList.addAll(modelLiteService.getAllCouchModelsLite(userId, organizationId, CouchTypes.agent));
            modelLiteList.addAll(modelLiteService.getAllCouchModelsLite(userId, organizationId, CouchTypes.extension));
            modelLiteList.addAll(modelLiteService.getAllCouchModelsLite(userId, organizationId, CouchTypes.recording));
        } catch (Exception e) {
            Collections.emptyList();
        }

        return modelLiteList;
    }

}
