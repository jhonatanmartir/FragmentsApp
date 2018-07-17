package com.dev.jhonyrg.fragmentsapp.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.jhonyrg.fragmentsapp.R;
import com.dev.jhonyrg.fragmentsapp.fragments.EditFragment;
import com.dev.jhonyrg.fragmentsapp.fragments.ListFragment;

public class MainActivity extends AppCompatActivity implements ListFragment.OnItemToDoListener, EditFragment.OnItemToDoResultListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment listFragment = new ListFragment();
        replaceFragment(listFragment, true);
    }

    @Override
    public void onItemToDo(int operation, Integer idToDo) {
        Fragment editFragment = EditFragment.newInstance(operation, idToDo);
        replaceFragment(editFragment, false);
    }

    @Override
    public void onToDoItemResult(Integer resultCode) {

    }

    private void replaceFragment(Fragment fragment, Boolean defaultFragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.frmList, fragment);

        if(!defaultFragment)
        {
            // and add the transaction to the back stack so the user can navigate back
            fragmentTransaction.addToBackStack(null);
        }

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }
}
