package com.yur.contacts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by Yur on 07.12.2016.
 */

public class ContactsAdapter extends RealmBasedRecyclerViewAdapter<Contact, ContactsAdapter.ContactViewHolder> {

    private RealmResults<Contact> mContacts;
    private ClickListener mClickListener;
    public interface ClickListener{Void itemClicked(View clickedView,int position);}



    public ContactsAdapter(Context context, RealmResults<Contact> realmResults, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
        this.mContacts=realmResults;
    }

    @Override
    public ContactViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
       View view=inflater.inflate(R.layout.contact_row,viewGroup,false);
        ContactViewHolder viewHolder=new ContactViewHolder(view,viewType);
        return viewHolder;
    }

    @Override
    public void onBindRealmViewHolder(ContactViewHolder holder, final int position) {
        final Contact mContact=mContacts.get(position);
        holder.mContact_name.setText(mContact.getName());
        holder.mContact_icon.setImageResource(R.drawable.ic_contact);
        holder.mRow_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener!=null){
                    mClickListener.itemClicked(view, position);
                }
                notifyDataSetChanged();
            }
        });

        switch(holder.mViewType){
            case 1:
                holder.mRow_layout.setPadding(0, 1, 0, 0);
                holder.mContact_marker.setText(mContacts.get(position).getName().charAt(0) + "");
                holder.mContact_marker.setBackgroundResource(R.drawable.marker_background);
                break;
            case 2:
                holder.mContact_marker.setBackgroundResource(R.drawable.fav_star);
                break;
        }


    }

    @Override
    public int getItemViewType(int position) {
        int viewType=0;
        Contact contact=mContacts.get(position);
        if(position==0){
            if(contact.isFavourite()){viewType=2;}else viewType=1;}
        else{
            if(!contact.isFavourite() && mContacts.get(position - 1).getName().charAt(0) != contact.getName().charAt(0)) {
                viewType = 1;
            }
        }
        return viewType;
    }

    public class ContactViewHolder extends RealmViewHolder{

        public TextView mContact_name,mContact_marker;
        public ImageView mContact_icon;
        public LinearLayout mRow_layout;
        public int mViewType;

        public ContactViewHolder(View itemView,int viewType) {
            super(itemView);
            mContact_marker=(TextView)itemView.findViewById(R.id.contact_marker);
            mContact_name=(TextView)itemView.findViewById(R.id.contact_name);
            mContact_icon =(ImageView) itemView.findViewById(R.id.contact_icon);
            mRow_layout=(LinearLayout)itemView.findViewById(R.id.contact_row);
            this.mViewType =viewType;
        }
    }

    public void setClickListener(ClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

}
