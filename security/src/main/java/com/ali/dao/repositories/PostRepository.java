package com.ali.dao.repositories;

import com.ali.dao.entities.Certificate;
import com.ali.dao.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
