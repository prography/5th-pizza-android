package com.prography.prography_pizza.src.mypage.fragments.history.interfaces;

import com.prography.prography_pizza.src.main.models.MainResponse;

import java.util.ArrayList;

public interface HistoryFragmentView {
    void validateSuccess(ArrayList<MainResponse.Data> data);
    void validateFailure(String message);
}
