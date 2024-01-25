package com.example.commentservice.rest.dtos;

import com.example.commentservice.rest.models.CommentModel;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

public class CommentResponseDTO {
  @NotBlank
  private String id;
  @NotBlank
  private String post;

  public CommentResponseDTO(CommentModel commentModel, UserDTO user) {
    this.content = commentModel.getContent();
    this.createdBy = user;
    this.id = commentModel.getId();
    this.post = commentModel.getPost();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  @NotBlank
  private String content;
  @NotBlank
  private UserDTO createdBy;



  @NotBlank
  private List<UserDTO> likes = new LinkedList<>();

  public List<UserDTO> getLikes() {
    return likes;
  }

  public void setLikes(List<UserDTO> likes) {
    this.likes = likes;
  }

  public CommentResponseDTO(CommentModel comment){
    this.content = comment.getContent();
    this.id = comment.getId();
    this.post = comment.getPost();
  }


  public UserDTO getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserDTO createdBy) {
    this.createdBy = createdBy;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
