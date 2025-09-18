package it.unicam.cs.ids.filieraagricola.controllers.dto;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.SupplyChainPoint;

import java.util.List;

public class CreateSupplyChainDto {



    private String supplyChainName;

    private List<SupplyChainPoint> points;


    public CreateSupplyChainDto() {
    }

    public String getSupplyChainName() {
        return supplyChainName;
    }

    public void setSupplyChainName(String supplyChainName) {
        this.supplyChainName = supplyChainName;
    }

    public List<SupplyChainPoint> getPoints() {
        return points;
    }

    public void setPoints(List<SupplyChainPoint> points) {
        this.points = points;
    }
}
