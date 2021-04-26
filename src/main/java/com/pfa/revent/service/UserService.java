package com.pfa.revent.service;

import com.pfa.revent.entity.Event;
import com.pfa.revent.entity.User;
import com.pfa.revent.repository.UserRepository;
import com.pfa.revent.security.Guard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Guard guard;

    public Page<User> findChildUsersByParent(Principal principal, Pageable pageable){
        User principalUser = userRepository.findByUsername(principal.getName()).orElse(null);
        long parentId=principalUser.getUserId();
        return userRepository.findChildUsersByParentId(parentId,pageable);
    }

    public void update(long userId, User updateUser){
        User user = userRepository.findById(userId).orElse(null);
        if (user!=null){
            user.setFirstName(updateUser.getFirstName());
            user.setLastName(updateUser.getLastName());
            user.setPassword(updateUser.getPassword());
            user.setEmail(updateUser.getEmail());
            user.setBirthdate(updateUser.getBirthdate());
            user.setAvatarImage(updateUser.getAvatarImage());
            user.setUsername(updateUser.getUsername());
/*            if(updateUser.getRoles().contains("ROLE_VIEWER")) {
                user.setRoles(updateUser.getRoles());
            }*/
            user.setMaxEditors(updateUser.getMaxEditors());
            userRepository.save(user);
        }
        else {
            System.out.println("Couldn't find or update user :" + userId);
        }
    }

    public String userType(Authentication authentication){
        if (guard.checkViewer(authentication)){
            if (guard.checkEditor(authentication)){
                if (guard.checkModerator(authentication)){
                    if (guard.checkAdmin(authentication)){
                        return "ADMIN";
                    }
                    return "MODERATOR";
                }
                return "EDITOR";
            }
            return "VIEWER";
        }
        return "GUEST";
    }

    public void saveImage(MultipartFile imageFile, long userId) throws IOException {
        if(imageFile != null){
            String folder = "./src/main/resources/static/img/avatars/";
            byte[] bytes = imageFile.getBytes();
            User user = userRepository.getOne(userId);
            String ext = imageFile.getContentType();
            user.setAvatarName(userId + "." + ext.split("/")[1]);
            userRepository.save(user);
            Path path = Paths.get(folder + userId + "." + ext.split("/")[1]);
            Files.write(path,bytes);
        }
        else{
            throw new IOException();
        }
    }
}
