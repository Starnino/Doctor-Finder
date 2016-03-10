package com.doctorfinderapp.doctorfinder.NewSocialShare;


import com.doctorfinderapp.doctorfinder.R;

public class Contact {

    /**
     * The list of dummy contacts.
     */
    public static final Contact[] CONTACTS = {
            new Contact("Tereasa"),
            new Contact("Chang"),
            new Contact("Kory"),
            new Contact("Clare"),
            new Contact("Landon"),
            new Contact("Kyle"),
            new Contact("Deana"),
            new Contact("Daria"),
            new Contact("Melisa"),
            new Contact("Sammie"),
    };

    /**
     * The contact ID.
     */
    public static final String ID = "contact_id";

    /**
     * Representative invalid contact ID.
     */
    public static final int INVALID_ID = -1;

    /**
     * The name of this contact.
     */
    private final String mName;

    /**
     * Instantiates a new {@link Contact}.
     *
     * @param name The name of the contact.
     */
    public Contact(String name) {
        mName = name;
    }

    /**
     * Finds a {@link Contact} specified by a contact ID.
     *
     * @param id The contact ID. This needs to be a valid ID.
     * @return A {@link Contact}
     */
    public static Contact byId(int id) {
        return CONTACTS[id];
    }

    /**
     * Gets the name of this contact.
     *
     * @return The name of this contact.
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets the icon of this contact.
     *
     * @return The icon.
     */
    public int getIcon() {
        return R.drawable.logo_avatar;
    }

}
