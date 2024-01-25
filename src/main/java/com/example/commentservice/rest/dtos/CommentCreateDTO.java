package com.example.commentservice.rest.dtos;


import com.example.commentservice.rest.models.CommentModel;

public class CommentCreateDTO {
  private String content;

  private String createdBy;

  private String post;

  public String getPost() {
    return post;
  }

  public void setPost(String post) {
    this.post = post;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public CommentModel toEntity(){
    CommentModel comment = new CommentModel();
    comment.setContent(this.content);
    comment.setCreatedBy(this.createdBy);
    comment.setPost(this.post);
    return comment;
  }

}
