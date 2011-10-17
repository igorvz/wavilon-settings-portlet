package com.aimprosoft.wavilon.util;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;

import javax.portlet.PortletRequest;
import java.util.*;

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

    public static CouchModelLite getCouchModelLite(String id, ResourceBundle bundle) {
        try {
            return modelLiteService.getCouchLiteModel(id);
        } catch (Exception e) {
            CouchModelLite modelLite = new CouchModelLite();
            modelLite.setId(id);
            modelLite.setName(bundle.getString("wavilon.error.massage.entity.removed"));
            return modelLite;
        }
    }

    public static List<CouchModelLite> getForwards(Long organizationId) {
        List<CouchModelLite> modelLiteList = new LinkedList<CouchModelLite>();

        try {
            modelLiteList.addAll(modelLiteService.getAllCouchModelsLite(organizationId, CouchTypes.queue));
            modelLiteList.addAll(modelLiteService.getAllCouchModelsLite(organizationId, CouchTypes.agent));
            modelLiteList.addAll(modelLiteService.getAllCouchModelsLite(organizationId, CouchTypes.extension));
            modelLiteList.addAll(modelLiteService.getAllCouchModelsLite(organizationId, CouchTypes.recording));
        } catch (Exception e) {
            Collections.emptyList();
        }

        return modelLiteList;
    }

    public static Map<String, String> extensionTypeMapPut(ResourceBundle bundle) {
        Map<String, String> extensionTypeMap = new LinkedHashMap<String, String>();

        extensionTypeMap.put(bundle.getString("wavilon.form.extensions.type.phone.number"), "phone");
        extensionTypeMap.put(bundle.getString("wavilon.form.extensions.type.gtalk"), "gtalk");
        extensionTypeMap.put(bundle.getString("wavilon.form.extensions.type.sip"), "sip");


        return extensionTypeMap;
    }

    public static Map<String, String> extensionTypeMapEject(ResourceBundle bundle) {
        Map<String, String> extensionTypeMap = new LinkedHashMap<String, String>();

        extensionTypeMap.put("phone", bundle.getString("wavilon.form.extensions.type.phone.number"));
        extensionTypeMap.put("gtalk",bundle.getString("wavilon.form.extensions.type.gtalk"));
        extensionTypeMap.put("sip", bundle.getString("wavilon.form.extensions.type.sip"));


        return extensionTypeMap;
    }
}
