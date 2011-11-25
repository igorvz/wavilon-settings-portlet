package com.aimprosoft.wavilon.service;

import com.vaadin.ui.Table;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ResourceBundle;

public interface ExportCSVService {
    void exportTableData(Table table,  Writer writer, ResourceBundle bundle) throws IOException;
}
