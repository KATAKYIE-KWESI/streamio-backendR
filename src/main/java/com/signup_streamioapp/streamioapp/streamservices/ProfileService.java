package com.signup_streamioapp.streamioapp.streamservices;

import com.signup_streamioapp.streamioapp.ProfileRequest;
import com.signup_streamioapp.streamioapp.streammodels.Profile;
import com.signup_streamioapp.streamioapp.streammodels.User;
import com.signup_streamioapp.streamioapp.streamrepository.ProfileRepository;
import com.signup_streamioapp.streamioapp.streamrepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public List<Profile> getProfilesForUser(Integer userId) {
        return profileRepository.findByUserId(userId);
    }

    public Profile addProfile(Integer userId, ProfileRequest request, MultipartFile avatarFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        String avatarUrl = null;

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String uploadsDir = "uploads/";
                File uploadFolder = new File(uploadsDir);
                if (!uploadFolder.exists())
                    uploadFolder.mkdirs();

                String filename = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
                File destFile = new File(uploadFolder, filename);
                avatarFile.transferTo(destFile);

                avatarUrl = "/uploads/" + filename; // Web-accessible path
            } catch (IOException e) {
                throw new RuntimeException("Failed to save avatar image", e);
            }
        }

        Profile profile = Profile.builder()
                .name(request.getName())
                .isKids(request.isKids())
                .avatarUrl(avatarUrl)
                .user(user)
                .build();

        return profileRepository.save(profile);
    }

    public void deleteProfile(Integer profileId) {
        profileRepository.deleteById(profileId);
    }
}
