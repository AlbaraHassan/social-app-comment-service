package com.example.commentservice.rest.controllers;


import com.example.commentservice.core.auth.Auth;
import com.example.commentservice.rest.dtos.CommentCreateDTO;
import com.example.commentservice.rest.dtos.CommentResponseDTO;
import com.example.commentservice.rest.dtos.CommentUpdateDTO;
import com.example.commentservice.rest.dtos.UserDTO;
import com.example.commentservice.rest.services.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/comment")
@Tag(name = "Comment")
public class CommentController {

  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @Auth
  @GetMapping("/all")
  public PageImpl<CommentResponseDTO> getAll(@RequestParam String id, @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
    return this.commentService.getAll(id, page, size);
  }

  @Auth
  @GetMapping
  public CommentResponseDTO get(@RequestParam String id) {
    return this.commentService.get(id);
  }

  @Auth
  @PostMapping
  public CommentResponseDTO create(@RequestBody CommentCreateDTO body) {
    return this.commentService.create(body);
  }


  @Auth
  @PatchMapping
  public CommentResponseDTO update(@RequestParam String id, @RequestBody CommentUpdateDTO body) {
    return this.commentService.update(id, body);
  }

  @Auth
  @PatchMapping("/like")
  public int like(@RequestParam String id){
    return this.commentService.like(id);
  }

  @Auth
  @GetMapping("/likes")
  public List<UserDTO> getLikes(@RequestParam String id){
    return this.commentService.getLikes(id);
  }

  @Auth
  @DeleteMapping
  public boolean delete(@RequestParam String id) {
    return this.commentService.delete(id);
  }

}
