package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Event;
import it.unicam.cs.ids.filieraagricola.model.Participation;

import java.util.ArrayList;
import java.util.List;

public class EventService {

    private List<Event> eventList;
    private List<Participation> participationList;

    public EventService() {
        this.eventList = new ArrayList<>();
        this.participationList = new ArrayList<>();
    }

    public void organizeEvent(Event event) {
        this.eventList.add(event);
    }

    public void bookEvent(Participation participation) {
        this.participationList.add(participation);
    }


    public List<Event> getEventList() {
        return eventList;
    }


    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<Participation> getListaPartecipazioni() {
        return participationList;
    }


    public void setListaPartecipazioni(List<Participation> participationList) {
        this.participationList = participationList;
    }


}