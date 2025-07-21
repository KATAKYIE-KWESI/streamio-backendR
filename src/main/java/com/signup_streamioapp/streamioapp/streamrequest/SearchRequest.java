package com.signup_streamioapp.streamioapp.streamrequest;

import lombok.Data;

@Data
public class SearchRequest {
    private String userId;
    private String searchTerm;
}
