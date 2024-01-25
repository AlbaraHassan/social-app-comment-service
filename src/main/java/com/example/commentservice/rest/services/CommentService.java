package com.example.commentservice.rest.services;



import com.example.commentservice.core.context.UserContext;
import com.example.commentservice.core.exceptions.auth.ForbiddenException;
import com.example.commentservice.core.exceptions.general.NotFoundException;
import com.example.commentservice.core.repositories.CommentRepository;
import com.example.commentservice.rest.dtos.CommentCreateDTO;
import com.example.commentservice.rest.dtos.CommentResponseDTO;
import com.example.commentservice.rest.dtos.CommentUpdateDTO;
import com.example.commentservice.rest.dtos.UserDTO;
import com.example.commentservice.rest.enums.Role;
import com.example.commentservice.rest.feign.UserService;
import com.example.commentservice.rest.models.CommentModel;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserService userService;
  private final UserContext userContext;

  public CommentService(
    CommentRepository commentRepository, UserService userService,
    UserContext userContext) {
    this.commentRepository = commentRepository;
    this.userService = userService;
    this.userContext = userContext;
  }

  public PageImpl<CommentResponseDTO> getAll(String id, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));

    List<CommentResponseDTO> commentResponseDTOs = this.commentRepository.findAllByPost(id, pageable)
      .stream()
      .map(post -> {
        UserDTO userDTO = this.userService.get(post.getCreatedBy())
          .orElse(null);

        CommentResponseDTO commentResponseDTO = new CommentResponseDTO(post, userDTO);
        commentResponseDTO.setLikes(this.getLikes(post.getId()));

        return commentResponseDTO;
      })
      .collect(Collectors.toList());

    long total = this.commentRepository.count();

    return new PageImpl<>(commentResponseDTOs, pageable, total);
  }

  public CommentResponseDTO get(String id) {
    return this.commentRepository.findById(id).map(commentModel -> {

      UserDTO userDTO = this.userService.get(commentModel.getCreatedBy())
        .orElse(null);
      CommentResponseDTO commentResponseDTO = new CommentResponseDTO(commentModel, userDTO);
      commentResponseDTO.setLikes(this.getLikes(id));
      return commentResponseDTO;
    }).orElse(null);
  }

  public CommentResponseDTO create(CommentCreateDTO data) {
    data.setCreatedBy(this.userContext.getId());
    data.setContent(data.getContent().trim());
    UserDTO user = this.userService.get(data.getCreatedBy())
      .orElse(null);
    CommentModel commentModel = this.commentRepository.save(data.toEntity());
    return new CommentResponseDTO(commentModel, user);
  }

  public CommentResponseDTO update(String id, CommentUpdateDTO updatedComment) {
    Optional<CommentModel> comment = this.commentRepository.findById(id);
    updatedComment.setContent(updatedComment.getContent().trim());
    comment.ifPresent(commentModel -> {
      if (!this.userContext.getId().equals(commentModel.getCreatedBy())) {
        throw new ForbiddenException("This comment can only be updated by its original creator");
      }
      CommentModel foundComment = comment.get();
      foundComment.setContent(updatedComment.getContent());
      this.commentRepository.save(foundComment);
    });
    return comment.map(commentModel -> {
      UserDTO user = this.userService.get(commentModel.getCreatedBy()).orElse(null);
      CommentResponseDTO commentResponseDTO = new CommentResponseDTO(commentModel, user);
      commentResponseDTO.setLikes(this.getLikes(id));
      return commentResponseDTO;
    }).orElseThrow(() -> new NotFoundException("Post does not exist"));


  }

  public int like(String id) {
    Optional<CommentModel> comment = this.commentRepository.findById(id);
    comment.ifPresent(commentModel -> {
      commentModel.setLikes(this.userContext.getId());
      this.commentRepository.save(commentModel);
    });
    return comment.orElseThrow(() -> new NotFoundException("Post does not exist")).getLikes().size();
  }

  public List<UserDTO> getLikes(String id) {
    CommentModel commentModel = this.commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Post does not exist"));
    CommentResponseDTO comment = new CommentResponseDTO(commentModel);
    List<UserDTO> users = commentModel.getLikes().stream().map(userId -> this.userService.get(String.valueOf(userId)).orElseThrow(() -> new NotFoundException("User not found"))).toList();
    comment.setLikes(users);
    return comment.getLikes();
  }

  public boolean delete(String id) {
    Optional<CommentModel> comment = this.commentRepository.findById(id);
    if (comment.isPresent()) {
      CommentModel commentModel = comment.get();
      if (!this.userContext.getId().equals(commentModel.getCreatedBy()) && !this.userContext.getRole().equals(Role.ADMIN)) {
        throw new ForbiddenException("This comment can only be deleted by its original creator or an admin");
      }
      this.commentRepository.delete(commentModel);
    } else {
      throw new NotFoundException("Comment does not exist");
    }
    return true;
  }

}
