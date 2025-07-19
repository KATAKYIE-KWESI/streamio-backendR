package com.signup_streamioapp.streamioapp;

public class ProfileRequest {

    private String name;
    private boolean isKids;
    private String avatarUri; // ✅ new field to support image URL

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isKids() {
        return isKids;
    }

    public void setKids(boolean isKids) {
        this.isKids = isKids;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}
