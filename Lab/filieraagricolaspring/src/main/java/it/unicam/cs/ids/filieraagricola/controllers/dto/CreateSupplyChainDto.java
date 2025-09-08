package it.unicam.cs.ids.filieraagricola.controllers.dto;

import it.unicam.cs.ids.filieraagricola.model.Product;

import java.util.List;

public class CreateSupplyChainDto {



    private String supplyChainName;


    public CreateSupplyChainDto() {
    }

    public String getSupplyChainName() {
        return supplyChainName;
    }

    public void setSupplyChainName(String supplyChainName) {
        this.supplyChainName = supplyChainName;
    }
}
