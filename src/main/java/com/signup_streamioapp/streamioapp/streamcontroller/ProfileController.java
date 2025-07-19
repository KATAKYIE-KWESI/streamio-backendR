package com.signup_streamioapp.streamioapp.streamcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.signup_streamioapp.streamioapp.ProfileRequest;
import com.signup_streamioapp.streamioapp.streammodels.Profile;
import com.signup_streamioapp.streamioapp.streamservices.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/{userId}/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Profile> addProfile(
            @PathVariable Integer userId,
            @RequestPart("data") String jsonData,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile) throws IOException {

        // Parse JSON string to ProfileRequest
        ProfileRequest request = objectMapper.readValue(jsonData, ProfileRequest.class);

        // Save profile
        Profile profile = profileService.addProfile(userId, request, avatarFile);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Profile>> getProfiles(@PathVariable Integer userId) {
        return ResponseEntity.ok(profileService.getProfilesForUser(userId));
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Integer profileId) {
        profileService.deleteProfile(profileId);
        return ResponseEntity.noContent().build();
    }
}
