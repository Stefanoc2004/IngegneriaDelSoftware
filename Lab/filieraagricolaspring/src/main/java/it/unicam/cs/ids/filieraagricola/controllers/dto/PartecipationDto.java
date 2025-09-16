package it.unicam.cs.ids.filieraagricola.controllers.dto;

import it.unicam.cs.ids.filieraagricola.model.ParticipationRole;

public class PartecipationDto {

    private String actorId;

    private ParticipationRole role;

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public ParticipationRole getRole() {
        return role;
    }

    public void setRole(ParticipationRole role) {
        this.role = role;
    }
}
