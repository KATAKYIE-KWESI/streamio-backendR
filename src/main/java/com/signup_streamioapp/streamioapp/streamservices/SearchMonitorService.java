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
    String term = searchTerm.toLowerCase();

    // 1. Save the current search into the database
    SearchLog log = new SearchLog();
    log.setUserId(userId);
    log.setSearchTerm(term);
    log.setTimestamp(LocalDateTime.now());
    searchLogRepo.save(log);

    // 2. Check if the current search term is one of the sensitive keywords
    boolean isCurrentTermSensitive = term.contains("death") ||
                                     term.contains("suicide") ||
                                     term.contains("kill") ||
                                     term.contains("die") ||
                                     term.contains("alone");

    // 3. If the current term is not sensitive, no need to show the chatbot message
    if (!isCurrentTermSensitive) {
        return false;
    }

    // 4. If the current term is sensitive, count how many sensitive terms this user has searched so far
    long count = searchLogRepo.countSensitiveSearches(userId);

    // 5. Only show chatbot message if the user has searched 3 or more sensitive terms total
    return count >= 3;
}
}
