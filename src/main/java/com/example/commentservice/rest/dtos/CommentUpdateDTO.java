package com.example.commentservice.rest.dtos;


import com.example.commentservice.rest.models.CommentModel;

public class CommentUpdateDTO {
  private String content;


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public CommentModel toEntity(){
    CommentModel comment = new CommentModel();
    comment.setContent(this.content);
    return comment;
  }


}
