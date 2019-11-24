package com.prography.progrpahy_pizza.src.addChallenge.fragments.interfaces;

import android.view.View;

public interface SelectorBotomSheetFragmentView extends View.OnClickListener {
    void onItemClick(int recyclerIndex, int position);

    void onItemSelected(int recyclerIndex, int position);
}
