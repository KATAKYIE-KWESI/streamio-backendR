package com.signup_streamioapp.streamioapp.streamcontroller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "videos";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file provided");
            }

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            File dest = new File(uploadDir, file.getOriginalFilename());
            file.transferTo(dest);

            String videoUrl = "/videos/" + file.getOriginalFilename();
            return ResponseEntity.ok("Upload successful: " + videoUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    // ✅ This method serves GET /api/videos
    @GetMapping
    public ResponseEntity<List<String>> getAllVideos() {
        File folder = new File(UPLOAD_DIR);
        File[] files = folder.listFiles();

        List<String> videoUrls = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    videoUrls.add("/videos/" + file.getName());
                }
            }
        }

        return ResponseEntity.ok(videoUrls);
    }
}
