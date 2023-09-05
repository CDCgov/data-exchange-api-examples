package cdc.gov.upload.client.model;

public class Field {
    private String fieldname;
    private Object allowed_values;
    private String required;
    private String description;

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public Object getAllowed_values() {
        return allowed_values;
    }

    public void setAllowed_values(Object allowed_values) {
        this.allowed_values = allowed_values;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
