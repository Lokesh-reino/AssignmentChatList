package com.example.assignment.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.models.Contact;
import com.example.assignment.viewmodel.ContactView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Callback callback;
    private List<Contact> contacts;

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        // Inflate the ContactView and set the click listener
        ContactView contactView = (ContactView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        contactView.setOnClickListener(v -> {
            if (callback != null) {
                Contact contact = ((ContactView) v).getContact();
                callback.onClick(contact);
            }
        });
        // Create and return the view holder
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, int position) {
        // Get the contact at the given position
        Contact contact = getContact(position);
        // Get the view from the holder and bind the contact
        ContactView view = holder.getContactView();
        view.bind(contact);
    }

    @Override
    public long getItemId (int position) {
        Contact contact = getContact(position);
        return contact.hashCode();
    }

    @Override
    public int getItemCount () {
        return getContacts().size();
    }

    /**
     * Returns the contact at the given position.
     * @param position The position of the contact.
     * @return The contact at the given position.
     */
    public Contact getContact (int position) {
        return getContacts().get(position);
    }

    /**
     * Returns the contacts represented in the recycler view.
     */
    public List<Contact> getContacts () {
        if (contacts == null) {
            contacts = new ArrayList<>(0);
        }
        return contacts;
    }

    /**
     * Sets the contacts to represent in the recycler view.
     * @param contacts The contacts.
     */

    /**
     * Sets the Callback to be notified on user interactions with contacts.
     * @param callback The callback.
     */
    public void setCallback (Callback callback) {
        this.callback = callback;
    }

    public void setContacts(List<ir.mirrajabi.rxcontacts.Contact> contacts) {
    }

    /**
     * TODO Write javadoc
     */
    public interface Callback {

        /**
         * This method is called as soon as the user clicks on a contact.
         * @param contact The clicked contact.
         */
        void onClick (Contact contact);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ContactView contactView;

        public ViewHolder (ContactView contactView) {
            super(contactView);
            this.contactView = contactView;
        }

        public ContactView getContactView () {
            return contactView;
        }
    }
}
