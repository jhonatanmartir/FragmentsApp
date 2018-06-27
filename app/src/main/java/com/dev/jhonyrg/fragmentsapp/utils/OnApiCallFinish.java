package com.dev.jhonyrg.fragmentsapp.utils;

public interface OnApiCallFinish {
    void onSuccess(Integer id, String content);
    void onError(Integer id, Integer code);
}
