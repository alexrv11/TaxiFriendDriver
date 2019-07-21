package com.taxi.friend.drivers.view.models;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.taxi.friend.drivers.models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuMainUserViewModel extends ViewModel {
    private MutableLiveData<User> user;
}
