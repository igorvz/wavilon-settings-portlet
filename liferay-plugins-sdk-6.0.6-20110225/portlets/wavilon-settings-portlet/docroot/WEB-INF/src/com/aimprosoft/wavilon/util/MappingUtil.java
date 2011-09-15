package com.aimprosoft.wavilon.util;

import com.aimprosoft.wavilon.model.User;
import com.fourspaces.couchdb.Document;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class MappingUtil {

    public static Document toDocument(User user) {
        Document document = new Document();

        document.setId(String.valueOf(user.getLiferay_user_id()));
        document.put("liferay_organization_id", user.getLiferay_organization_id());
        document.put("liferay_portal_id", user.getLiferay_portal_id());

        if (StringUtils.isNotBlank(user.getRevision())) {
            document.setRev(user.getRevision());
        }

        return document;
    }

    public static User toUser(Document document) {
        Long liferay_user_id =  NumberUtils.toLong(document.getId());
        String revision = document.getRev();
        Long liferay_organization_id = NumberUtils.toLong(document.getString("liferay_organization_id"));
        Long liferay_portal_id = NumberUtils.toLong(document.getString("liferay_portal_id"));

        User user = new User();

        user.setLiferay_user_id(liferay_user_id);
        user.setRevision(revision);
        user.setLiferay_organization_id(liferay_organization_id);
        user.setLiferay_portal_id(liferay_portal_id);

        return user;
    }


}
