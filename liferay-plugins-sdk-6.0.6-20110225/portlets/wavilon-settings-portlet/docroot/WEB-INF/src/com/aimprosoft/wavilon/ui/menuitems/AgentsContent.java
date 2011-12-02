package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.AgentsForm;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.ui.HorizontalLayout;

import java.util.ResourceBundle;

public class AgentsContent extends GenerelContent {
    private AgentDatabaseService service = ObjectFactory.getBean(AgentDatabaseService.class);

    public AgentsContent(ResourceBundle bundle) {
        super(bundle);
    }

    public void init() {
        super.init();
        fillVisibleFields();
        fillHiddenFields();
        createTableData(service, CouchTypes.agent);
        fillTable();
        initLayout(CouchTypes.agent, ratioMap(1,2));
    }

    private void fillVisibleFields() {
        visibleFields.add(bundle.getString("wavilon.table.agents.column.name"));
        visibleFields.add(bundle.getString("wavilon.table.agents.column.current.extension"));
    }

    private void fillTable() {
        if (!couchModels.isEmpty()) {
            for (final CouchModel couchModel : couchModels) {
                Object object = tableData.addItem();
                Agent agent = getModel(couchModel, service, Agent.class);
                CouchModelLite extension = CouchModelUtil.getCouchModelLite((String) couchModel.getOutputs().get("extension"), bundle);

                tableData.getContainerProperty(object, "id").setValue(couchModel.getId());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.name")).setValue(agent.getName());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.current.extension")).setValue(extension);
                tableData.getContainerProperty(object, "").setValue(createTablesEditRemoveButtons(object, couchModel, null));
            }
        }
    }

}
