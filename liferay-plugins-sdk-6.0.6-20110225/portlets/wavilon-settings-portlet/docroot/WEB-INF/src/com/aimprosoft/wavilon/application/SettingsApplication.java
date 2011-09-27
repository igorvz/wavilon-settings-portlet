package com.aimprosoft.wavilon.application;

import com.aimprosoft.wavilon.ui.PleaseSignInPage;
import com.aimprosoft.wavilon.ui.SettingsPage;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.ui.Window;
import org.apache.log4j.Logger;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsApplication extends GenericPortletApplication {

    private Long userId;
    private Logger _logger = Logger.getLogger(getClass());

    @Override
    public void init() {
        //initialize listeners
        super.init();
    }

    @Override
    public void handleRenderRequest(RenderRequest renderRequest, RenderResponse renderResponse, final Window window) {
        super.handleRenderRequest(renderRequest, renderResponse, window);

        try {

            ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);


            //get liferay locale
            Locale portalLocale = themeDisplay.getLocale();
            //window locale will be used everywhere
            window.setLocale(portalLocale);
            ResourceBundle bundle = ResourceBundle.getBundle("language", window.getLocale());

            if (window.getComponentIterator().hasNext()) {
                window.removeComponent(window.getComponentIterator().next());
            }

            window.addStyleName("wrapperLayouts");
            window.getLayout().setMargin(false);
            window.getContent().setSizeFull();
            if (themeDisplay.isSignedIn()) {
                userId = PortalUtil.getUserId(renderRequest);
                window.setContent(new SettingsPage(bundle, userId));
            } else {
                window.setContent(new PleaseSignInPage(bundle));
            }

        } catch (Exception e) {
            _logger.error(e.getMessage());
        }
    }
}
