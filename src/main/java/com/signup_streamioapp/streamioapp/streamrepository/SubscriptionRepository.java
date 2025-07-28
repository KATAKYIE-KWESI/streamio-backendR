package com.signup_streamioapp.streamioapp.streamrepository;



import  com.signup_streamioapp.streamioapp.streammodels.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserEmail(String email);
}

