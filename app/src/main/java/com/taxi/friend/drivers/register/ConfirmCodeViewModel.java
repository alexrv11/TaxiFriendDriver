package com.taxi.friend.drivers.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfirmCodeViewModel extends ViewModel {
    private MutableLiveData<ConfirmCode> code;

    public LiveData<ConfirmCode> getCode() {
        if (code == null) {
            code = new MutableLiveData<>();
            code.postValue(new ConfirmCode(""));
        }

        return code;
    }

    public void setCode(ConfirmCode newCode) {
        code.setValue(newCode);
    }
}
