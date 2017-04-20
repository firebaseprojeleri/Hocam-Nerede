package com.gelecegiyazanlar.hocamnerede;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.gelecegiyazanlar.hocamnerede.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity{

    Toolbar mToolbar;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    CustomTextView mUserFullName;
    CustomTextView mUserUniversity;

    TimelineViewPagerAdapter mViewPagerAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;;

    public static Intent newIntent(Activity callerActivity){
        return new Intent(callerActivity, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        if(fbUser == null) {
            Intent intent = SignActivity.newIntent(MainActivity.this);
            startActivity(intent);
            finish();
        } else {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            mUserFullName.setText(user.getFullname());
                            mUserUniversity.setText(user.getUniversity());
                            if (user.getAvatar() != null) {
                                //TODO: Avatar resmini aktar
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar)findViewById(R.id.toolbar_layout);
        mTabLayout = (TabLayout)findViewById(R.id.tabLayout);
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mUserFullName = (CustomTextView)findViewById(R.id.userFullName);
        mUserUniversity = (CustomTextView)findViewById(R.id.userUniversity);
        setSupportActionBar(mToolbar);

        mViewPagerAdapter = new TimelineViewPagerAdapter(getSupportFragmentManager());

        mViewPagerAdapter.addFragments(new Timeline(),"Timeline");
        mViewPagerAdapter.addFragments(new UserProfile(),"Profile");
        mViewPagerAdapter.addFragments(new Search(),"Search");

        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
