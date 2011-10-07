package com.aimprosoft.wavilon.util;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.liferay.portal.util.PortalUtil;

import javax.portlet.PortletRequest;
import java.util.UUID;

public class CouchModelUtil {
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
}
