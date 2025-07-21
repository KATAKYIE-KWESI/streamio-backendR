package com.signup_streamioapp.streamioapp.streamservices;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.signup_streamioapp.streamioapp.streammodels.SearchLog;
import com.signup_streamioapp.streamioapp.streamrepository.SearchLogRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchMonitorService {

    private final SearchLogRepository searchLogRepo;

    public boolean logSearchAndCheck(String userId, String searchTerm) {
        SearchLog log = new SearchLog();
        log.setUserId(userId);
        log.setSearchTerm(searchTerm.toLowerCase());
        log.setTimestamp(LocalDateTime.now());
        searchLogRepo.save(log);

        long count = searchLogRepo.countSensitiveSearches(userId);
        return count >= 3;
    }
}
