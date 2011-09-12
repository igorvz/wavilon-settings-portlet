package com.aimprosoft.wavilon.application;

import com.aimprosoft.wavilon.ui.SimplePage;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.ui.Window;
import org.apache.log4j.Logger;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class SettingsApplication extends GenericPortletApplication {

    private Logger _logger = Logger.getLogger(getClass());

    @Override
    public void init() {
        //initialize listeners
        super.init();

        Window window = new Window();
        window.addComponent(new SimplePage());
        setMainWindow(window);
    }

    @Override
    //todo test code, probably remove it
    public void handleRenderRequest(RenderRequest renderRequest, RenderResponse renderResponse, Window window) {
        try {
            long companyId = PortalUtil.getCompanyId(renderRequest);
            User liferayUser = PortalUtil.getUser(renderRequest);
            if (liferayUser != null){
                window.setCaption(liferayUser.getFullName());
            }
        } catch (Exception e) {
            _logger.error(e.getMessage());
        }
    }
}
