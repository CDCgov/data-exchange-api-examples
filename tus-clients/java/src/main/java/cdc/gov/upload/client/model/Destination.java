package cdc.gov.upload.client.model;

import java.util.List;

public class Destination {
    private String destination_id;
    private List<ExtEvent> ext_events;

    public String getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(String destination_id) {
        this.destination_id = destination_id;
    }

    public List<ExtEvent> getExt_events() {
        return ext_events;
    }

    public void setExt_events(List<ExtEvent> ext_events) {
        this.ext_events = ext_events;
    }
}
