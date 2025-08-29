package it.unicam.cs.ids.filieraagricola.service;

import it.unicam.cs.ids.filieraagricola.model.Content;
import it.unicam.cs.ids.filieraagricola.service.exception.ContentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContentService {


    private List<Content> contentList;


    public ContentService() {

        this.contentList = new ArrayList<>();
    }


    public boolean createContent(Content content) {
        return this.contentList.add(content);
    }


    public boolean approveContent(int id) throws ContentNotFoundException {
        // Searching for the content
        Content content = null;
        for (Content c: contentList) {
            if (c.getId() == id) {
                content = c;
            }
        }
        if (content == null) {
            throw new ContentNotFoundException();
        }
        // Approve the content
        if (content.isState()) {
            return false;
        }
        // find the content and approve it
        content.setState(true);
        return true;
    }


    public List<Content> showSocialContent() {
        List<Content> approvedContent = this.contentList.stream()
                .filter(Content::isState)
                .collect(Collectors.toList());

        if (approvedContent.isEmpty()) {
            return null;
        }
        return approvedContent;
    }


    public List<Content> getContentList() {
        return contentList;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }


}