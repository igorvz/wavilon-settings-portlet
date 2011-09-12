package com.aimprosoft.wavilon.application;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2;
import com.vaadin.ui.Window;

import javax.portlet.*;

public abstract class GenericPortletApplication extends Application implements PortletApplicationContext2.PortletListener {

    @Override
    public void init() {
        if (getContext() instanceof PortletApplicationContext2) {
            PortletApplicationContext2 ctx =
                    (PortletApplicationContext2) getContext();
            // Add a custom listener to handle action and
            // render requests.
            ctx.addPortletListener(this, this);
        }
    }

    public void handleRenderRequest(RenderRequest renderRequest, RenderResponse renderResponse, Window window) {
    }

    public void handleActionRequest(ActionRequest actionRequest, ActionResponse actionResponse, Window window) {
    }

    public void handleEventRequest(EventRequest eventRequest, EventResponse eventResponse, Window window) {
    }

    public void handleResourceRequest(ResourceRequest resourceRequest, ResourceResponse resourceResponse, Window window) {
    }

}
