package com.doctorfinderapp.doctorfinder.NewSocialShare;

import android.widget.TextView;

/**
 * A simple utility to bind a {@link TextView} with a {@link Contact}.
 */
public class ContactViewBinder {

    /**
     * Binds the {@code textView} with the specified {@code contact}.
     *
     * @param contact  The contact.
     * @param textView The TextView.
     */
    public static void bind(Contact contact, TextView textView) {
        textView.setText(contact.getName());
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(contact.getIcon(), 0, 0, 0);
    }

}
