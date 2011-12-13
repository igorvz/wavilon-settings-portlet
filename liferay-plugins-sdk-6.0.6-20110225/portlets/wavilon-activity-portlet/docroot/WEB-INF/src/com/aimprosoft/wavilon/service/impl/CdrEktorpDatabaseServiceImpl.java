package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.CdrModel;
import com.aimprosoft.wavilon.service.CdrEktorpDatabaseService;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class CdrEktorpDatabaseServiceImpl extends AbstractViewEntityService implements CdrEktorpDatabaseService {

    @Autowired
    @Qualifier("pushtestDatabaseConnector")
    private CouchDbConnector connector;

    @Override
    public CdrModel getModel(String id) throws IOException {
        return connector.get(CdrModel.class, id);
    }

    @Override
    public List<CdrModel> getAllCdrModels() throws IOException {
        ViewQuery query = new ViewQuery()
                .designDocId(functions.getPushTestDesignDocument())
                .viewName(functions.getFunctionPushTestAllModels());

        return getCdrModelList(query);
    }

    @Override
    public void addCdrModel(CdrModel model) throws IOException {
        connector.create(model);
    }

    @Override
    public void removeCdrModel(CdrModel model) throws IOException {
        connector.delete(model);
    }

    @Override
    public void removeCdrModel(String id) throws IOException {
        connector.delete(getModel(id));
    }

    private List<CdrModel> getCdrModelList(ViewQuery query) {
        List<CdrModel> cdrModelList = connector.queryView(query, CdrModel.class);

        if (0 == cdrModelList.size()) {
            return new LinkedList<CdrModel>();
        } else {
            return cdrModelList;
        }
    }

    @Override
    public List<String> getModelsId() {
        List<String> idList = new ArrayList<String>();

        ViewQuery query = new ViewQuery()
                .designDocId(functions.getPushTestDesignDocument())
                .viewName(functions.getFunctionPushTestLiteModels());

        ViewResult result = connector.queryView(query);

        for (int i = 0; i < result.getRows().size(); i++) {
            idList.add(result.getRows().get(i).getValue());
        }

        return idList;
    }

    @Override
    public ChangesFeed getChangesFeed() {

        ChangesCommand changesCommand = new ChangesCommand.Builder().build();
        return connector.changesFeed(changesCommand);
    }
}
