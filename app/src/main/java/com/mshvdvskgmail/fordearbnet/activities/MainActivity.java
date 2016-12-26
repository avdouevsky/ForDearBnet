package com.mshvdvskgmail.fordearbnet.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mshvdvskgmail.fordearbnet.R;
import com.mshvdvskgmail.fordearbnet.fragments.NoteListFragment;


public class MainActivity extends AppCompatActivity {

   /* @Override
    public void onTaskCompleted(String values) {
        a.setText(values);
    }*/

    private com.mikepenz.materialdrawer.Drawer result = null;

    private Toolbar mToolbar;
    NoteListFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        fragment = new NoteListFragment();
        //promptForDelete(fragment.toString());
        openFragment(fragment, "Notes");

        /*
        //Now build the navigation drawer and pass the AccountHeader
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.title_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.title_editor).withIcon(FontAwesome.Icon.faw_edit).withIdentifier(2)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem drawerItem) {
                        if (drawerItem != null && drawerItem instanceof Nameable){
                            String name = ((Nameable)drawerItem).getName().getText(MainActivity.this);
                            mToolbar.setTitle(name);
                        }

                        if (drawerItem != null){
                            int selectedScren = drawerItem.getIdentifier();
                            switch (selectedScren){
                                case 1:
                                    fragment =  (NoteListFragment)getSupportFragmentManager().findFragmentByTag(String.valueOf(1));
                                    if (fragment == null){
                                        fragment = new NoteListFragment();
                                        promptForDelete(fragment.toString());
                                        openFragment(fragment, "Notes");
                                    }
                                    //do nothing
                                    break;
                                case 2:
                                    startActivity(new Intent(MainActivity.this, NoteEditorActivity.class));
                            }
                            if (fragment != null) {
                                FragmentManager fm = getSupportFragmentManager();
                                fm.beginTransaction().replace(R.id.container, fragment, String.valueOf(1)).commitAllowingStateLoss();
                            }

                        }
                        return false;
                    }


                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {
                        KeyboardUtil.hideKeyboard(MainActivity.this);

                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }

                    @Override
                    public void onDrawerSlide(View view, float v) {

                    }
                })
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();

        if (savedInstanceState == null){
            result.setSelection(1);
        }*/

    }

    private void openFragment(final Fragment fragment, String title){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle(title);
    }

    public void promptForDelete(String result){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(this.toString());
        alertDialog.setMessage(result);
        alertDialog.setPositiveButton("Check Wi-Fi Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // NoteManager.newInstance(getActivity()).delete(mCurrentNote);
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                //startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        alertDialog.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mRecyclerView.notifyDataSetChanged();
                //mLayoutManager.noti
                //mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();

    }


}
