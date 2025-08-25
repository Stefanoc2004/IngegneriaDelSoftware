package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Content;

import java.util.ArrayList;
import java.util.List;

public class ContentService {


    private List<Content> contentList;


    public ContentService() {

        this.contentList = new ArrayList<>();
    }


    public boolean createContent(Content content) {
        return this.contentList.add(content);
    }


    public boolean approveContent(Content content) {
        // TODO: Aggiungi logica
        return true;
    }


    public void showSocialContent() {
        // TODO: Aggiungi logica

    }


    public List<Content> getContentList() {
        return contentList;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }


}