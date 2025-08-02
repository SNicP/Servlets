package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final AtomicLong idCounter = new AtomicLong(1);
    private final ConcurrentHashMap<Long, Post> posts;

    public PostRepository() {
        this.posts = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = idCounter.get();
            post.setId(newId);
            posts.put(newId, post);
            idCounter.getAndIncrement();
        } else if (!posts.containsKey(post.getId()) && post.getId() != 0) {
            throw new NotFoundException("Сообщение с id " + post.getId() + " не найдено!");
        } else {
            posts.get(post.getId()).setContent(post.getContent());
        }
        return post;
    }

    public void removeById(long id) {
        if (posts.containsKey(id)) {
            posts.remove(id);
        } else {
            throw new NotFoundException("Сообщение с id" + id + " не найдено!");
        }
    }
}
