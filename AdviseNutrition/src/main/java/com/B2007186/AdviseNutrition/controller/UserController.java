package com.B2007186.AdviseNutrition.controller;

import com.B2007186.AdviseNutrition.dto.InfoReq;
import com.B2007186.AdviseNutrition.dto.InfoRes;
import com.B2007186.AdviseNutrition.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<InfoRes> getProfile() {
        return userService.getProfile()
                .map(info -> new ResponseEntity<>(info, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<InfoRes> updateProfile(InfoReq profile) throws IOException {
        return userService.updateProfile(profile)
                .map(info -> new ResponseEntity<>(info, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/search")
    public ResponseEntity<List<InfoRes>> searchProfiles(@RequestParam("query") String query) {
        List<InfoRes> searchResults = userService.searchProfiles(query);
        if (!searchResults.isEmpty()) {
            return new ResponseEntity<>(searchResults, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/description")
    public ResponseEntity<String> getUserDescription(@RequestParam("username") String username) {
        Optional<String> description = userService.getUserDescription(username);
        return description.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/description")
    public ResponseEntity<String> updateUserDescription(@RequestBody String description) {
        Optional<String> updatedDescription = userService.updateUserDescription(description);
        return updatedDescription.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
