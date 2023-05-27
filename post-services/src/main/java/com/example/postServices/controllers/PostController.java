package com.example.postServices.controllers;

import com.example.postServices.models.AcceuilPost;
import com.example.postServices.models.Comments;
import com.example.postServices.models.Posts;
import com.example.postServices.models.Reaction;
import com.example.postServices.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api6/v1/post-services")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/add-post")
    public ResponseEntity<String> addPost (@RequestBody Posts post, @RequestHeader("Authorization") String token){
        System.out.println("post work");
        return postService.addPost(post, token);
    }

    @PostMapping("/delete-post")
    public ResponseEntity<String> deletePost (@RequestParam String postId, @RequestHeader("Authorization") String token){
        return postService.deletePost(postId,token);
    }

    @PostMapping("/getMyPosts")
    public ResponseEntity<List<Posts>> getMyPosts (@RequestParam String id,@RequestHeader("Authorization") String token){
        return postService.getAllMyPosts(id,token);
    }

    @PostMapping("/add-comment")
    public ResponseEntity<String> addComment (@RequestBody Comments comment,
                                              @RequestParam String idPost,@RequestParam String idUserPost){
        //System.out.println("comment work");
         return postService.addComment(idPost,comment, idUserPost);
    }

    @PostMapping("/get-comments")
    public ResponseEntity<List<Comments>> getComments (@RequestParam String idPost, @RequestParam String idUserPost){
        //System.out.println("comment work");
        return postService.getComments(idPost, idUserPost);
    }

    @PostMapping("/add-reaction")
    public ResponseEntity<String> addReaction (@RequestBody Reaction newReaction,
                                              @RequestParam String idPost,@RequestParam String idUserPost){
        //System.out.println("comment work");
        return postService.addReaction(idPost,newReaction, idUserPost);
    }

    @PostMapping("/get-reactions")
    public ResponseEntity<List<Reaction>> getReaction (@RequestParam String idPost, @RequestParam String idUserPost){
        //System.out.println("comment work");
        return postService.getReactions(idPost, idUserPost);
    }

    @PostMapping("/get-acceuilPosts")
    public ResponseEntity<List<AcceuilPost>> getAcceuilPosts (@RequestParam String idUser){
        System.out.println("post work");
        return postService.getAcceuilPost2(idUser);
    }



    @PostMapping
    public ResponseEntity<String> demoTest(){
        return ResponseEntity.ok().body("Hello Post EndPoint");
    }
}
