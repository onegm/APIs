package com.todayInHistory.TodayInHistory;

public class Article {
    private Event article;

    public Article(Event article) {
        this.article = article;
    }

    public Event getEvent() {
        return article;
    }

    public void setEvent(Event article) {
        this.article = article;
    }
}
