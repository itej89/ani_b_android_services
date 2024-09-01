package fm.ani.fmlauncher;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fm.ani.fmlauncher.DataTypes.Delegates.AppSelectionConvey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppsDrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppsDrawerFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    AppSelectionConvey app_selected_handler;


    public AppsDrawerFragment(AppSelectionConvey _app_selected_handler) {
        app_selected_handler = _app_selected_handler;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_apps_drawer,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.appDrawer_recylerView);


        adapter = new AppsDrawerAdapter(getContext(), app_selected_handler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

}