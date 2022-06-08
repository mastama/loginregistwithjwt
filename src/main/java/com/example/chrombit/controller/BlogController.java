package com.example.chrombit.controller;


import com.example.chrombit.model.Blog;
import com.example.chrombit.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @GetMapping("/blogs")
    public List<Blog> index() {
        return blogRepository.findAll();
    }

    @GetMapping("blogs/{id}")
    public Blog show(@PathVariable UUID id) {
        return blogRepository.findById(id).orElse(new Blog());
    }

    @PostMapping("/blogs")
    public Blog create(@RequestBody Blog blog) {
        return blogRepository.save(blog);
    }

    @PutMapping("/blogs/{id}")
    public Blog update(@PathVariable UUID id, @RequestBody Blog blog) {
        blog.setId(id);
        return blogRepository.save(blog);
    }

    @DeleteMapping("/blogs/{id}")
    public Boolean delete(@PathVariable UUID id) {
        blogRepository.deleteById(id);
        return true;
    }
}
