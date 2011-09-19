package com.aimprosoft.wavilon.util;


import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.model.BaseModel;
import com.aimprosoft.wavilon.model.Queue;
import com.fourspaces.couchdb.Document;
import com.liferay.portal.util.PortalUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import javax.portlet.RenderRequest;
import javax.print.Doc;
import java.util.UUID;

public class MappingUtil {
    private static Logger _logger = Logger.getLogger(MappingUtil.class);

    public static Document toDocument(Queue queue) {
        Document document = new Document();

        if (StringUtils.isNotBlank(queue.getRevision())) {
            fillDocument(queue, document);
        } else {
            fillDocument((RenderRequest) null, document);
        }

        return document;
    }

    public static Document toDocument(Agent agent) {
        Document document = new Document();

        if (StringUtils.isNotBlank(agent.getRevision())) {
            fillDocument(agent, document);
        } else {
            fillDocument((RenderRequest) null, document);
        }

        document.put("firstName", agent.getFirstName());


        return document;
    }

    public static Agent toAgent(Document document) {
        Agent agent = new Agent();
        fillBaseModel(agent, document);

        String firstName = document.getString("firstName");

        agent.setFirstName(firstName);

        return agent;
    }

    public static Queue toQueue(Document document) {
        Queue queue = new Queue();
        fillBaseModel(queue, document);

        return queue;
    }



    private static void fillBaseModel(BaseModel model, Document document) {
        String id = document.getId();
        String revision = document.getRev();
        Long liferayUserId = NumberUtils.toLong(document.getString("liferay_user_id"));
        Long liferayOrganizationId = NumberUtils.toLong(document.getString("liferay_organization_id"));
        String liferayPortalId = document.getString("liferay_portal_id");

        model.setId(id);
        model.setRevision(revision);
        model.setLiferayUserId(liferayUserId);
        model.setLiferayOrganizationId(liferayOrganizationId);
        model.setLiferayPortalId(liferayPortalId);
    }

    private static void fillDocument(BaseModel model, Document document) {
        Long liferayUserId = model.getLiferayUserId();
        Long liferayOrganizationId = model.getLiferayOrganizationId();
        String liferayPortalId = model.getLiferayPortalId();
        String uuid = model.getId();
        String revision = model.getRevision();

        document.put("liferay_user_id", String.valueOf(liferayUserId));
        document.put("liferay_organization_id", String.valueOf(liferayOrganizationId));
        document.put("liferay_portal_id", String.valueOf(liferayPortalId));
        document.setId(uuid);
        document.setRev(revision);
    }

    private static void fillDocument(RenderRequest request, Document document) {
        String uuid = UUID.randomUUID().toString();
        Long liferayUserId = PortalUtil.getUserId(request);
        Long liferayOrganizationId = null;
        String liferayPortalId = null;
        try {
            liferayOrganizationId = PortalUtil.getScopeGroupId(request);
            liferayPortalId = PortalUtil.getCompany(request).getWebId();
        } catch (Exception e) {
           _logger.error(e.getMessage());
        }

        document.setId(uuid);
        document.put("liferay_user_id", String.valueOf(liferayUserId));
        document.put("liferay_organization_id", String.valueOf(liferayOrganizationId));
        document.put("liferay_portal_id", liferayPortalId);
    }

}