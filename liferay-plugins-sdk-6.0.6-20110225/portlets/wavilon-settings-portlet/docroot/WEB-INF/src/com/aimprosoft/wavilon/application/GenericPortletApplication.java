package com.aimprosoft.wavilon.application;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2;
import com.vaadin.ui.Window;

import javax.portlet.*;

public abstract class GenericPortletApplication extends Application implements PortletApplicationContext2.PortletListener {

    private PortletRequest portletRequest;

    @Override
    public void init() {
        if (getContext() instanceof PortletApplicationContext2) {
            PortletApplicationContext2 ctx =
                    (PortletApplicationContext2) getContext();
            // Add a custom listener to handle action and
            // render requests.
            ctx.addPortletListener(this, this);
        }

        Window window = new Window();
        setMainWindow(window);
    }

    public void handleRenderRequest(RenderRequest renderRequest, RenderResponse renderResponse, Window window) {
        setPortletRequest(renderRequest);
    }

    public void handleActionRequest(ActionRequest actionRequest, ActionResponse actionResponse, Window window) {
        setPortletRequest(actionRequest);
    }

    public void handleEventRequest(EventRequest eventRequest, EventResponse eventResponse, Window window) {
        setPortletRequest(eventRequest);
    }

    public void handleResourceRequest(ResourceRequest resourceRequest, ResourceResponse resourceResponse, Window window) {
        setPortletRequest(resourceRequest);
    }

    public PortletRequest getPortletRequest() {
        return portletRequest;
    }

    public void setPortletRequest(PortletRequest portletRequest) {
        this.portletRequest = portletRequest;
    }
}
