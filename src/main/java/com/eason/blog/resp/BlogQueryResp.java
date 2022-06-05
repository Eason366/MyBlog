package com.eason.blog.resp;

public class BlogQueryResp {
    private Long id;

    private String name;

    private Long category;

    private String description;

    private String cover;

    private Integer viewCount;

    private Integer voteCount;

    private String user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "BlogQueryResp{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", cover='" + cover + '\'' +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", user='" + user + '\'' +
                '}';
    }
}