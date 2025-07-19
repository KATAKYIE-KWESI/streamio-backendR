package com.signup_streamioapp.streamioapp.streamrepository;

import com.signup_streamioapp.streamioapp.streammodels.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;




public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    List<Profile> findByUserId(Integer userId);
}
