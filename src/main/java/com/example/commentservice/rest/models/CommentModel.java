package com.example.commentservice.rest.models;


import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Document("comment")
public class CommentModel {

  @Id
  private String id;

  private String content;

  @CreatedBy
  private String createdBy;
  @CreatedDate
  private Date createdAt = new Date();
  private String post;

  public String getPost() {
    return post;
  }

  public void setPost(String post) {
    this.post = post;
  }

  private Set<String> likes = new HashSet<>();

  public Set<String> getLikes() {
    return likes;
  }

  public Date getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(Date createdAt){
    this.createdAt = createdAt;
  }

  public void setLikes(String like) {
    if (this.likes.contains(like)) {
      this.likes.remove(like);
    } else {
      this.likes.add(like);
    }
  }

  public CommentModel() {
  } // Needed when creating a new comment

  public CommentModel(String content) {
    this.content = content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getId() {
    return this.id;
  }

  public String getCreatedBy() {
    return this.createdBy;
  }

  public String getContent() {
    return this.content;
  }
}
