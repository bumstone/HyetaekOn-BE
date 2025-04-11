package com.hyetaekon.hyetaekon.recommend.service;

import com.hyetaekon.hyetaekon.post.repository.PostRepository;
import com.hyetaekon.hyetaekon.recommend.repository.RecommendRepository;
import com.hyetaekon.hyetaekon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {
    private final RecommendRepository recommendRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

}
