package com.hyetaekon.hyetaekon.comment.service;

import com.hyetaekon.hyetaekon.comment.dto.CommentDto;
import com.hyetaekon.hyetaekon.comment.entity.Comment;
import com.hyetaekon.hyetaekon.comment.mapper.CommentMapper;
import com.hyetaekon.hyetaekon.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public Page<CommentDto> getComments(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findByPostId(postId, pageable)
                .map(commentMapper::toDto);
    }

    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setPostId(postId);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    public Page<CommentDto> getReplies(Long postId, Long commentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findByPostIdAndParentId(postId, commentId, pageable)
                .map(commentMapper::toDto);
    }

    public CommentDto createReply(Long postId, Long commentId, CommentDto commentDto) {
        Comment reply = commentMapper.toEntity(commentDto);
        reply.setPostId(postId);
        reply.setParentId(commentId);
        reply = commentRepository.save(reply);
        return commentMapper.toDto(reply);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
