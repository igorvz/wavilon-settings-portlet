package com.aimprosoft.wavilon.application;

import com.aimprosoft.wavilon.ui.SettingsPage;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.ui.Window;
import org.apache.log4j.Logger;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsApplication extends GenericPortletApplication {

    private Logger _logger = Logger.getLogger(getClass());

    @Override
    public void init() {
        //initialize listeners
        super.init();

        Window window = new Window();
        setMainWindow(window);
    }

    @Override
    public void handleRenderRequest(RenderRequest renderRequest, RenderResponse renderResponse, Window window) {
        try {

            //get liferay locale
            Locale portalLocale = PortalUtil.getLocale(renderRequest);

            //window locale will be used everywhere
            window.setLocale(portalLocale);

            ResourceBundle bundle = ResourceBundle.getBundle("language", window.getLocale());

            if (window.getComponentIterator().hasNext()) {

                window.removeComponent(window.getComponentIterator().next());
            }

            window.addComponent(new SettingsPage(bundle));

            long companyId = PortalUtil.getCompanyId(renderRequest);
            User liferayUser = PortalUtil.getUser(renderRequest);
            if (liferayUser != null) {
                window.setCaption(liferayUser.getFullName());
            }
        } catch (Exception e) {
            _logger.error(e.getMessage());
        }
    }
}
