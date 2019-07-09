package com.taxi.friend.drivers.register;

public class RegisterPhotoInfo {
    private String messageForm;
    private int photoPreview;

    public RegisterPhotoInfo(String messageForm, int preview) {
        this.messageForm = messageForm;
        this.photoPreview = preview;
    }

    public String getMessageForm() {
        return messageForm;
    }

    public void setMessageForm(String messageForm) {
        this.messageForm = messageForm;
    }

    public int getPhotoPreview() {
        return photoPreview;
    }

    public void setPhotoPreview(int photoPreview) {
        this.photoPreview = photoPreview;
    }
}
