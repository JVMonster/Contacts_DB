package com.yur.contacts;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.ClickListener {
    private Realm mRealm;
    private RealmResults<Contact> mContacts;
    private RealmRecyclerView mRecyclerView;
    private ContactsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRealm=Realm.getDefaultInstance();

        ActionBar mActionBar=getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.app_bar_view);

        addToRealm();
        mAdapter =new ContactsAdapter(this, mContacts,true,true);
        mRecyclerView=(RealmRecyclerView)findViewById(R.id.recycler);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);

    }

    private void addToRealm() {
        mContacts =mRealm.where(Contact.class).findAllSortedAsync("isFavourite",Sort.DESCENDING,"name",Sort.ASCENDING);
        Contact contact;

        if(mRealm.isEmpty()){
            for(int i = 0; i< getContacts().size(); i++) {

            if (i < getContacts().size() - 10) {
                contact=new Contact(getContacts().get(i), false);
            } else{
                contact=new Contact(getContacts().get(i), true);}

            mRealm.beginTransaction();
            mRealm.insert(contact);
            mRealm.commitTransaction();

        }}
    }

    private ArrayList<String> getContacts() {
        ArrayList<String> lists=new ArrayList<>();
        int i=0;
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while(cursor.moveToNext()&& i<100){
            try{
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                lists.add(name);
                i++;
            }catch (Exception e){cursor.close();}
        }cursor.close();
        return lists;
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRealm!=null)
        mRealm.close();
    }

    @Override
    public Void itemClicked(View clickedView, int position) {
        Contact clickedContact;
        mRealm.beginTransaction();

        clickedContact= mContacts.get(position);
        clickedContact.setFavourite(!clickedContact.isFavourite());

        mRealm.insertOrUpdate(clickedContact);
        mRealm.commitTransaction();

        return null;
    }
}
