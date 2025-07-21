package com.signup_streamioapp.streamioapp.streamcontroller;

import java.util.HashMap;
import java.util.Map;
import com.signup_streamioapp.streamioapp.streamservices.SearchMonitorService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchMonitorService searchMonitorService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> handleSearch(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String term = request.get("searchTerm");

        boolean showHelpMessage = searchMonitorService.logSearchAndCheck(userId, term);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Search logged");
        response.put("showSupportMessage", showHelpMessage);
        return ResponseEntity.ok(response);
    }
}
