package com.aimprosoft.wavilon.service.impl;

import au.com.bytecode.opencsv.CSVWriter;
import com.aimprosoft.wavilon.service.ExportCSVService;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class ExportCSVServiceImpl implements ExportCSVService {
    @Override
    public void exportTableData(Table table, Writer writer, ResourceBundle bundle) throws IOException {
        table.getVisibleItemIds();
        CSVWriter csvWriter = new CSVWriter(writer);
        List<String[]> rows = new LinkedList<String[]>();

        for (Object itemObj : table.getVisibleItemIds()) {
            Item item = table.getItem(itemObj);
            StringBuilder row = new StringBuilder();

            for (Object propObj : table.getContainerDataSource().getContainerPropertyIds()) {
                if (propObj.equals("") || propObj.equals("id")) continue;
                String propertyId = propObj.toString();

                Property itemProperty = item.getItemProperty(propertyId);
                if (null != itemProperty && !"".equals(itemProperty.toString())) {
                    row.append(itemProperty);
                } else {
                    row.append(bundle.getString("wavilon.error.massage.empty"));
                }

                row.append("#");
            }

            rows.add(row.toString().split("#"));
        }

        csvWriter.writeAll(rows);
        csvWriter.close();
    }
}
