package com.eason.blog.req;

import javax.validation.constraints.NotNull;

public class CategorySaveReq {
    private Long id;

    private Long parent;

    @NotNull(message = "Category Name not be null")
    private String name;

    @NotNull(message = "Category Sort not be null")
    private Integer sort;

    private Long user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CategorySaveReq{" +
                "id=" + id +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", user=" + user +
                '}';
    }
}