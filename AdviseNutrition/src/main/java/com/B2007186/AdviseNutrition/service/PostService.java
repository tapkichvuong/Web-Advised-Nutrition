package com.B2007186.AdviseNutrition.service;

import com.B2007186.AdviseNutrition.domain.Comment;
import com.B2007186.AdviseNutrition.domain.Post;
import com.B2007186.AdviseNutrition.domain.PostCategory;
import com.B2007186.AdviseNutrition.domain.Users.User;
import com.B2007186.AdviseNutrition.dto.PostReq;
import com.B2007186.AdviseNutrition.dto.PostRes;
import com.B2007186.AdviseNutrition.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final String FOLDER_PATH = "D:\\CTU\\NLNganh\\uploads\\";
    private final String BASE_IMAGE_URL = "http://localhost:8080/api/v1/image/";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCatRepository postCatRepository;

    private static String generateUniqueFilename() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStamp = dateFormat.format(new Date());
        return timeStamp + "_" + UUID.randomUUID().toString().substring(0, 4); // Append a portion of UUID
    }
    public PostRes addPost(PostReq postReq) throws IOException {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username);
        if(!user.get().getEnabled()){
            return PostRes.builder().message("This account is not activated").build();
        }

        if(postCatRepository.findByName(postReq.getCategory()).isEmpty()){
            var cat = PostCategory.builder().name(postReq.getCategory()).build();
            postCatRepository.save(cat);
        }
        var postCat = postCatRepository.findByName(postReq.getCategory());
        String originalFilename = postReq.getThumbnail().getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = generateUniqueFilename() + fileExtension;
        String filePath = FOLDER_PATH + newFilename;
        var post = Post.builder()
                .title(postReq.getTitle())
                .postCategory(postCat.get())
                .thumbnail(filePath)
                .body(postReq.getBody())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user.get())
                .build();
        postRepository.save(post);
        postReq.getThumbnail().transferTo(new File(filePath));
        return PostRes.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(postCat.get().getName())
                .thumbnail(filePath)
                .body(postReq.getBody())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .message("Post has been added successfully")
                .build();
    }

    public PostRes updatePost(Long postId, PostReq postReq) throws IOException {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username);
        if(!user.get().getEnabled()){
            return PostRes.builder().message("This account is not activated").build();
        }
        // Retrieve the post
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            return PostRes.builder()
                    .message("Post not found")
                    .statusCode(404)
                    .build();
        }
        Post post = optionalPost.get();

        // Check if the user is the owner of the post
        if (!post.getUser().getId().equals(user.get().getId())) {
            return PostRes.builder()
                    .message("You are not authorized to update this post")
                    .statusCode(401)
                    .build();
        }
        String originalFilename = postReq.getThumbnail().getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = generateUniqueFilename() + fileExtension;
        String filePath = FOLDER_PATH + newFilename;

        post.setTitle(postReq.getTitle());
        post.setThumbnail(filePath);
        post.setBody(postReq.getBody());;
        post.setUpdatedAt(LocalDateTime.now());
        postReq.getThumbnail().transferTo(new File(filePath));
        postRepository.save(post);
        return PostRes.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getPostCategory().getName())
                .thumbnail(filePath)
                .body(post.getBody())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .message("Post has been updated successfully")
                .statusCode(200)
                .build();
    }

    public String deletePost(Long postid) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username);
        if(!user.get().getEnabled()){
            return "This account is not activated";
        }
        var post = postRepository.findById(postid).get();
        postRepository.delete(post);
        return "Delete successfully";
    }
    public PostRes getPost(long postId) throws IOException {
        var post = postRepository.findById(postId).get();
        System.out.println(post.getThumbnail());
        String[] filePath=post.getThumbnail().split("\\\\");
        String filename = filePath[filePath.length - 1];
        String thumbnailLink = BASE_IMAGE_URL + filename;
        User user = post.getUser();
        String[] avatarPath=user.getAvatar().split("\\\\");
        String avatar = avatarPath[filePath.length - 1];
        String avatarLink = BASE_IMAGE_URL + avatar;
        return PostRes.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getPostCategory().getName())
                .thumbnail(thumbnailLink)
                .body(post.getBody())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .message("Fetch post successfully")
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatar(avatarLink)
                .build();
    }

    private PostRes mapToPostResponse(Post post) throws IOException {
        String[] filePath=post.getThumbnail().split("\\\\");
        String filename = filePath[filePath.length - 1];
        String thumbnailLink = BASE_IMAGE_URL + filename;
        User user = post.getUser();
        String[] avatarPath=user.getAvatar().split("\\\\");
        String avatar = avatarPath[filePath.length - 1];
        String avatarlLink = BASE_IMAGE_URL + avatar;
        return PostRes.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getPostCategory().getName())
                .thumbnail(thumbnailLink)
                .body(post.getBody())
                .createdAt(post.getUpdatedAt())
                .updatedAt(post.getUpdatedAt())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatar(avatarlLink)
                .build();
    }
    public List<PostRes> getMyPosts(int _start, int _end, String _sort, String _order) throws IOException {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            Sort.Direction direction = _order.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(_start / (_end - _start), _end - _start, direction, _sort);
            List<Post> postList = postRepository.findPostByUser(username, pageable).getContent();
            return postList.stream().map(post -> {
                try {
                    return mapToPostResponse(post);
                } catch (IOException e) {
                    throw new RuntimeException("Error mapping post to response: " + e.getMessage());
                }
            }).toList();
        } catch (Exception e) {
            // Handle JWT validation exception
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public List<PostRes> getAllPost() throws IOException {
        try {
            List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
            return postList.stream().map(post -> {
                        try {
                            return mapToPostResponse(post);
                        } catch (IOException e) {
                            throw new RuntimeException("Error mapping post to response: " + e.getMessage());
                        }
                    }).toList();
        } catch (Exception e) {
            // Handle JWT validation exception
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
