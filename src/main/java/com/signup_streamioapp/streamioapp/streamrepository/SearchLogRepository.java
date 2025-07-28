package com.signup_streamioapp.streamioapp.streamrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.signup_streamioapp.streamioapp.streammodels.SearchLog;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

    @Query(value = """
                SELECT COUNT(*) FROM search_log
                WHERE user_id = :userId
                AND (
                    LOWER(search_term) LIKE '%death%' OR
                    LOWER(search_term) LIKE '%suicide%' OR
                    LOWER(search_term) LIKE '%kill%' OR
                    LOWER(search_term) LIKE '%die%' OR
                    LOWER(search_term) LIKE '%depression%' OR
                    LOWER(search_term) LIKE '%alone%'
                )
            """, nativeQuery = true)
    long countSensitiveSearches(@Param("userId") String userId);
}
