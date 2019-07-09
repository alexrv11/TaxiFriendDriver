package com.taxi.friend.drivers.models;

import java.util.List;

public class ResponseWrapper<T> {
    private List<T> result;

    public ResponseWrapper() {
    }

    public ResponseWrapper(List<T> result) {
        this.result = result;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
