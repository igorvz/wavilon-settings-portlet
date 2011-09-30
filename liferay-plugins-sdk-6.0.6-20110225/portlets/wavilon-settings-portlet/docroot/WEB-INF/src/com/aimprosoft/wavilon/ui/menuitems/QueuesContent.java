package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.QueuesDragAndDropAgents;
import com.aimprosoft.wavilon.ui.menuitems.forms.QueuesForm;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class QueuesContent extends VerticalLayout {
    private ResourceBundle bundle;
    private PortletRequest request;
    private IndexedContainer tableData;
    private List<String> tableFields;
    private List<String> hiddenFields;
    private Table queuesTable = new Table();
    private QueueDatabaseService service = ObjectFactory.getBean(QueueDatabaseService.class);
    private QueuesForm queuesForm;
    private VerticalLayout bottom;

    public QueuesContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        this.hiddenFields = fillHiddenFields();
        this.tableFields = fillFields();
        this.tableData = createTableData();

        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setSizeUndefined();
        initLayout();
        initQueuesTable();
    }

    private List<String> fillFields() {
        List<String> tableFields = new LinkedList<String>();

        tableFields.add("NAME");
        tableFields.add("EXTENSION ON MAX. TIME");
        tableFields.add("EXTENSION ON MAX. LENGTH");

        return tableFields;
    }

    private void initLayout() {
        VerticalLayout top = createTop();
        addComponent(top);

        bottom = new VerticalLayout();
        addComponent(bottom);
        bottom.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        bottom.setHeight(240, Sizeable.UNITS_PIXELS);

        HorizontalLayout head = createHead();
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        top.addComponent(head);

        this.queuesTable.setContainerDataSource(this.tableData);
        this.queuesTable.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        this.queuesTable.setHeight(150, Sizeable.UNITS_PIXELS);

        this.queuesTable.addStyleName("tableCustom");
        top.addComponent(this.queuesTable);


    }

    private VerticalLayout createTop() {
        VerticalLayout top = new VerticalLayout();
        top.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        top.setHeight(220, Sizeable.UNITS_PIXELS);
        return top;
    }

    private void initQueuesTable() {
        this.queuesTable.setContainerDataSource(this.tableData);
        this.queuesTable.setVisibleColumns(this.tableFields.toArray());
        this.queuesTable.setSelectable(true);
        this.queuesTable.setImmediate(true);


        this.queuesTable.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object id = queuesTable.getValue();
                if (null != id) {
                    String queueId = (String) queuesTable.getItem(id).getItemProperty("id").getValue();
                    getAgentsTwinColumns(queueId);
                } else {
                  bottom.removeAllComponents();
                }
            }
        });


        this.queuesTable.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        String queueId = (String) item.getItemProperty("id").getValue();
                        getAgentsTwinColumns(queueId);
                        getForm(queueId);
                    }
                }
            }
        });

    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();
        List<Queue> numbers = getQueues();

        for (String field : this.hiddenFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        for (Queue queue : numbers) {
            Object object = ic.addItem();
            ic.getContainerProperty(object, "NAME").setValue(queue.getName());
            ic.getContainerProperty(object, "EXTENSION ON MAX. TIME").setValue(queue.getExtensionOnMaxTime());
            ic.getContainerProperty(object, "EXTENSION ON MAX. LENGTH").setValue(queue.getExtensionOnMaxLength());
            ic.getContainerProperty(object, "id").setValue(queue.getId());
        }

        return ic;
    }

    private List<Queue> getQueues() {
        try {
            return service.getAllQueuesByUser(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception ignored) {
        }
        return Collections.emptyList();
    }

    public HorizontalLayout createHead() {
        HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        Label headLabel = new Label("Queues");
        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("headLine");
        headLabel.addStyleName("phoneHeader");
        headLabel.addStyleName("tableHeader");

        HorizontalLayout addRemoveButtons = createButtons();
        head.addComponent(addRemoveButtons);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addRemoveButtons, Alignment.MIDDLE_RIGHT);

        return head;
    }

    private HorizontalLayout createButtons() {
        HorizontalLayout addRemoveButtons = new HorizontalLayout();
        addRemoveButtons.addComponent(new Button("+", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                getForm("-1");
            }
        }));
        addRemoveButtons.addComponent(new Button("-", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object id = queuesTable.getValue();
                if (null != id) {
                    String phoneNumbersID = (String) queuesTable.getItem(id).getItemProperty("id").getValue();

                    ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                    getWindow().addWindow(confirmingRemove);
                    confirmingRemove.init(phoneNumbersID, queuesTable);
                    confirmingRemove.center();
                    confirmingRemove.setWidth("300px");
                    confirmingRemove.setHeight("180px");
                } else {
                    getWindow().showNotification("Select Queue");
                }
            }
        }));
        return addRemoveButtons;
    }

    private void getAgentsTwinColumns(String id) {
        QueuesDragAndDropAgents agentsLayout = new QueuesDragAndDropAgents(bundle);
        agentsLayout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        agentsLayout.setHeight(250, Sizeable.UNITS_PIXELS);

        this.bottom.removeAllComponents();
        this.bottom.addComponent(agentsLayout);
        agentsLayout.init(id);
    }

    private void getForm(String id) {
        queuesForm = new QueuesForm(this.bundle, this.queuesTable);
        queuesForm.setWidth("480px");
        queuesForm.setHeight("400px");
        queuesForm.center();
        queuesForm.setModal(true);

        getWindow().addWindow(queuesForm);
        queuesForm.init(id);
    }

    private List<String> fillHiddenFields() {
        List<String> hiddenFields = new LinkedList<String>();

        hiddenFields.add("NAME");
        hiddenFields.add("EXTENSION ON MAX. TIME");
        hiddenFields.add("EXTENSION ON MAX. LENGTH");
        hiddenFields.add("id");

        return hiddenFields;
    }


}
