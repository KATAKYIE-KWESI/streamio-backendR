package com.signup_streamioapp.streamioapp.streamservices;

import com.signup_streamioapp.streamioapp.streammodels.Subscription;
import com.signup_streamioapp.streamioapp.streamrepository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepo;

    public Subscription startFreeTrial(String email) {
        Optional<Subscription> existing = subscriptionRepo.findByUserEmail(email);
        if (existing.isPresent() && existing.get().getEndDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("You already started a trial or subscription.");
        }

        Subscription sub = new Subscription();
        sub.setUserEmail(email);
        sub.setPlanType("free-trial");
        sub.setStartDate(LocalDate.now());
        sub.setEndDate(LocalDate.now().plusDays(1));
        sub.setIsActive(true);
        return subscriptionRepo.save(sub);
    }

    public Subscription simulatePremiumPayment(String email) {
        Subscription sub = new Subscription();
        sub.setUserEmail(email);
        sub.setPlanType("premium");
        sub.setStartDate(LocalDate.now());
        sub.setEndDate(LocalDate.now().plusDays(30));
        sub.setIsActive(true);
        return subscriptionRepo.save(sub);
    }

    public boolean isSubscriptionActive(String email) {
        Optional<Subscription> subscription = subscriptionRepo.findByUserEmail(email);
        return subscription.map(sub ->
                sub.isActive() && sub.getEndDate().isAfter(LocalDate.now()))
                .orElse(false);
    }

    public String[] getPartnerCinemas(String email) {
        if (!isSubscriptionActive(email)) {
            throw new RuntimeException("Subscription inactive");
        }

        return new String[] {
                "🎬 Silverbird Cinemas – Accra Mall",
                "🎥 Watch & Chill – Kumasi",
                "🍿 Tinsel Theatre – Takoradi",
                "🎞️ Osu Drive-In Experience – Osu",
                "📽️ Golden Movie Theatre – East Legon"
        };
    }

    public String getDiscountTicketCode(String email)
 {
        if (!isSubscriptionActive(email)) {
            throw new RuntimeException("Subscription inactive");
        }

        int random = (int)(Math.random() * 100000);
        return "STREAMIO-TICKET-" + random;
    }
}
