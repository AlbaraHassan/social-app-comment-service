package com.example.commentservice.core.repositories;

import com.example.commentservice.rest.models.CommentModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<CommentModel, String> {
  void deleteAllByCreatedBy(String id);
  List<CommentModel> findAllByPost(String id, Pageable pageable);
}