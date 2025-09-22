package it.unicam.cs.ids.filieraagricola.controllers.dto;

import it.unicam.cs.ids.filieraagricola.model.ContentType;

public class CreateContentDto {

    private String name;
    private String description;
    private ContentType type;
    public String idSupplyChainPoint;


    public CreateContentDto() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }


    public String getIdSupplyChainPoint() {
        return idSupplyChainPoint;
    }

    public void setIdSupplyChainPoint(String idSupplyChainPoint) {
        this.idSupplyChainPoint = idSupplyChainPoint;
    }
}

