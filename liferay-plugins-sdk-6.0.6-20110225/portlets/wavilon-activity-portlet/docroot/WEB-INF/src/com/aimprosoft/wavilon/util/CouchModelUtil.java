package com.aimprosoft.wavilon.util;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

import javax.portlet.PortletRequest;
import java.util.UUID;

public class CouchModelUtil {

    public static CouchModel newCouchModel(PortletRequest request, String type) {
        CouchModel newCouchModel = new CouchModel();
        try {
            newCouchModel.setId(UUID.randomUUID().toString());
            newCouchModel.setLiferayUserId(PortalUtil.getUserId(request));
            newCouchModel.setLiferayOrganizationId(getOrganizationId(request));
            newCouchModel.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
        } catch (Exception ignored) {
        }
        newCouchModel.setType(type);

        return newCouchModel;
    }

    public static Long getOrganizationId(PortletRequest request) {
        try {
            Long userId = PortalUtil.getUserId(request);
            Long companyId = PortalUtil.getDefaultCompanyId();
            User currentUser = UserLocalServiceUtil.getUserById(companyId, userId);
            long organizationIds[] = currentUser.getOrganizationIds();

            if (organizationIds.length != 0) {
                return organizationIds[0];
            } else {
                return -1l;
            }
        } catch (Exception ignored) {
            return -1l;
        }
    }

}
