package com.dlu.ghh.bean;

public class PostUser {
    private Integer id;

    private Post post;

    private User user;

    private Integer praised;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getPraised() {
        return praised;
    }

    public void setPraised(Integer praised) {
        this.praised = praised;
    }
}