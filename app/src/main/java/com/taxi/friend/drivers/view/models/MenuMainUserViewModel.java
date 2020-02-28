package com.taxi.friend.drivers.view.models;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
