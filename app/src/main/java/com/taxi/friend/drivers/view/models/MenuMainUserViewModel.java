package com.taxi.friend.drivers.view.models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.taxi.friend.drivers.models.User;


public class MenuMainUserViewModel extends ViewModel {
    private MutableLiveData<User> user = new MutableLiveData<User>();

    public LiveData<User> getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user.setValue(user);
    }
}
