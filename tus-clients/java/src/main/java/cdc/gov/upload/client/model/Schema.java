package cdc.gov.upload.client.model;

import java.util.List;

public class Schema {

    private String schema_version;
    private List<Field> fields;

    public String getSchema_version() {
        return schema_version;
    }

    public void setSchema_version(String schema_version) {
        this.schema_version = schema_version;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
