package com.example.postServices.services;

import com.example.jwtSecurity.models.UserProfile;
import com.example.postServices.Repository.*;
import com.example.postServices.config.JwtService;
import com.example.postServices.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final UserPostRepository userPostRepository;

    private  final PostRepository postRepository;
    private  final CommentsRepository commentRepository;
    private  final ReactionRepository reactionRepository;

    public ResponseEntity<String> addPost(Posts post, String token){
        UserProfile user=findProfileByToken(token);
        AtomicBoolean postSaved= new AtomicBoolean(false);
        Posts newPost=new Posts(
                post.getType(),
                post.getTitle(),
                post.getAudiance(),
                new Date(System.currentTimeMillis()),
                post.getText(),
                (post.getType().equals(PostType.TEXT))? "" : post.getUrlMedia(),
                user.getId(),
                true,
                0,
                0,
                false,
                new ArrayList<>(),
                new ArrayList<>()
        );
        var postToAdd=postRepository.save(newPost);
        postRepository.delete(postToAdd);
        //System.out.println(testpost);

        userPostRepository.findByUserID(user.getId()).ifPresentOrElse(userPost -> {
            List<Posts> newList=userPost.getPosts();
            if (newList==null){
                newList=List.of(postToAdd);
            }else {
                newList.add(0,postToAdd);
            }
            userPost.setPosts(newList);
            userPostRepository.save(userPost);
            postSaved.set(true);
        },()->{
            UserPost userPost=new UserPost(user.getId(),List.of(postToAdd));
            userPostRepository.save(userPost);
            postSaved.set(true);
        });

        if(postSaved.get()){
            return ResponseEntity.ok().body("Post Added Successfully");
        }
        return ResponseEntity.badRequest().body("Something Went Wrong. Post Not Saved!");
    }

    public ResponseEntity<List<Posts>> getAllMyPosts(String id,String token){
        //UserProfile user=findProfileById(token);
        AtomicReference<List<Posts>> newList= new AtomicReference(new ArrayList());
        userPostRepository.findByUserID(id).ifPresent(userPost -> {
            newList.set(userPost.getPosts());
            if(newList.get() ==null){
                newList.set(new ArrayList());
            }
        });
        return ResponseEntity.ok().body(newList.get());
    }

    public ResponseEntity<String> deletePost(String postId, String token){
        UserProfile user=findProfileByToken(token);
        //String response="";
        AtomicBoolean check= new AtomicBoolean(false);
        userPostRepository.findByUserID(user.getId()).ifPresent(userPost -> {

            List<Posts> newList=userPost.getPosts();
            if(newList==null || newList.isEmpty()){

                return;
            }
            Iterator<Posts> iterator = newList.iterator();
            while (iterator.hasNext()) {
                Posts post = iterator.next();
                if (post.getId().equals(postId)) {
                    iterator.remove();
                    userPost.setPosts(newList);
                    userPostRepository.save(userPost);
                    check.set(true);
                    break;
                }
            }

        });
        System.out.println("55");
        if(check.get()){
            return ResponseEntity.ok().body("Post Deleted Successfully");
        }
        return ResponseEntity.badRequest().body("Post Not Found Or Already Deleted");
    }

    public ResponseEntity<String> addComment(String idPost , Comments newComment, String idUserPost){
        //UserProfile user=findProfileByToken(token);
        AtomicBoolean check= new AtomicBoolean(false);
        newComment.setCreatedAt(new Date(System.currentTimeMillis()));
        var comment=commentRepository.save(newComment);
        commentRepository.delete(comment);
        userPostRepository.findByUserID(idUserPost).ifPresent(userPost -> {
            var list=userPost.getPosts();
            //System.out.println("list: "+list);

            list.forEach(post -> {
                //System.out.println("check: "+post.getId());
                if (post.getId().equals(idPost)){
                    var commentList=post.getComments();
                    commentList.add(0,comment);
                    post.setComments(commentList);
                    post.setNbComments(post.getNbComments()+1);
                    var index=list.indexOf(post);
                    list.set(index,post);
                    System.out.println("post: "+post);
                    check.set(true);
                }
            });
            userPost.setPosts(list);
            userPostRepository.save(userPost);
        });
        if(check.get()){
            return ResponseEntity.ok().body("Comment added Successfully");
        }
        return ResponseEntity.badRequest().body("Something went wrong");
    }

    public ResponseEntity<List<Comments>> getComments(String idPost, String idUserPost){

        AtomicReference<List<Comments>> commentList= new AtomicReference<List<Comments>>(new ArrayList<>());
        userPostRepository.findByUserID(idUserPost).ifPresent(userPost -> {
            var list= userPost.getPosts();
            list.forEach(post -> {
                if (post.getId().equals(idPost)) {
                     commentList.set(post.getComments());
                }
            });
        });
        return ResponseEntity.ok().body(commentList.get());
    }

    public ResponseEntity<String> addReaction(String idPost , Reaction newReaction, String idUserPost){
        //UserProfile user=findProfileByToken(token);
        newReaction.setUserName(findProfileById(newReaction.getUserId()).getFullName());
        newReaction.setUserProfilePic(findProfileById(newReaction.getUserId()).getProfile_pic_url());
        var reaction=reactionRepository.save(newReaction);
        reactionRepository.delete(reaction);
        userPostRepository.findByUserID(idUserPost).ifPresent(userPost -> {
            var list=userPost.getPosts();
            //System.out.println("list: "+list);

            list.forEach(post -> {
                //System.out.println("check: "+post.getId());
                if (post.getId().equals(idPost)){
                    var reactionList=post.getReactions();
                    AtomicBoolean check= new AtomicBoolean(true);
                    reactionList.forEach(reaction1 -> {
                        if(reaction1.getUserId().equals(reaction.getUserId())){
                            reactionList.remove(reaction);
                            //post.setNbReactions(post.getNbReactions()-1);
                            check.set(false);
                        }
                    });
                    if(check.get()) {
                        reactionList.add(0,reaction);
                        post.setNbReactions(post.getNbReactions()+1);
                    }


                    var index=list.indexOf(post);
                    list.set(index,post);
                    System.out.println("post: "+post);

                }
            });
            userPost.setPosts(list);
            userPostRepository.save(userPost);
        });

            return ResponseEntity.ok().body("Reacted Successfully");
    }

    public ResponseEntity<List<Reaction>> getReactions(String idPost, String idUserPost){

        AtomicReference<List<Reaction>> commentList= new AtomicReference<List<Reaction>>(new ArrayList<>());
        userPostRepository.findByUserID(idUserPost).ifPresent(userPost -> {
            var list= userPost.getPosts();
            list.forEach(post -> {
                if (post.getId().equals(idPost)) {
                    commentList.set(post.getReactions());
                }
            });
        });
        return ResponseEntity.ok().body(commentList.get());
    }


    public ResponseEntity<Map> getAcceuilPost(String token){
        System.out.println("token: "+token);
        UserProfile user=findProfileByToken(token);
        Map<Posts,UserProfile> posts=new HashMap<>();
        userPostRepository.findAll().forEach(userPost -> {
            if(!userPost.getUserID().equals(user.getId())){
                userPost.getPosts().forEach(posts1 -> {
                    if(posts1.getUserId()!=null){
                        posts.put(posts1,findProfileById(posts1.getUserId()));
                    }

                });
            }
        });
        System.out.println("posts: "+posts);
        List<Posts> postKeys=new ArrayList<>();
        posts.forEach((posts1, userProfile) -> {
            postKeys.add(posts1);
            Collections.shuffle(postKeys);
        });
        Map<Posts,UserProfile> newMap=new HashMap<>();
        postKeys.forEach(o -> {
            newMap.put(o,posts.get(o));
        });
        System.out.println(newMap);
        return ResponseEntity.ok().body(newMap);
    }


    public ResponseEntity<List<AcceuilPost>> getAcceuilPost2(String idUser){
        System.out.println("id is: "+idUser);
        //UserProfile user=findProfileByToken(token);
        List<AcceuilPost> posts=new ArrayList<>();
        System.out.println("22");
        userPostRepository.findAll().forEach(userPost -> {
            if(!userPost.getUserID().equals(idUser)){
                System.out.println("22");
                userPost.getPosts().forEach(posts1 -> {
                    if(posts1.getUserId()!=null){
                        var tempUser=findProfileById(posts1.getUserId());
                        AcceuilPost newAcceuilPost=new AcceuilPost(
                                posts1.getId(),
                                posts1.getType(),
                                posts1.getTitle(),
                                posts1.getAudiance(),
                                posts1.getCreatedAt(),
                                posts1.getText(),
                                posts1.getUrlMedia(),
                                posts1.getUserId(),
                                tempUser.getFullName(),
                                tempUser.getProfile_pic_url(),
                                tempUser.getUserStatut().toString(),
                                posts1.isBlocked(),
                                posts1.getNbReactions(),
                                posts1.getNbComments(),
                                posts1.getReactions(),
                                posts1.getComments()
                        );
                        posts.add(newAcceuilPost);
                        //System.out.println(posts);
                    }

                });
            }
        });

        Collections.shuffle(posts);
        return ResponseEntity.ok().body(posts);
    }
    public UserProfile findProfileByToken(String token){
        var jwtToken=token.substring(7);
        var email=jwtService.extractUserName(jwtToken);
        return userRepository.findByEmail(email).orElseThrow();
    }

    public UserProfile findProfileById(String id){
        return userRepository.findById(id).orElseThrow();
    }
}
