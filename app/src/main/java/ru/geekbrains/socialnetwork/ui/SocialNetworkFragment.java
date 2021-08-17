package ru.geekbrains.socialnetwork.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.geekbrains.socialnetwork.MainActivity;
import ru.geekbrains.socialnetwork.Navigation;
import ru.geekbrains.socialnetwork.R;
import ru.geekbrains.socialnetwork.data.CardData;
import ru.geekbrains.socialnetwork.data.CardsSource;
import ru.geekbrains.socialnetwork.data.CardsSourceLocalImpl;
import ru.geekbrains.socialnetwork.data.CardsSourceRemoteImpl;
import ru.geekbrains.socialnetwork.data.CardsSourceResponse;
import ru.geekbrains.socialnetwork.observe.Observer;
import ru.geekbrains.socialnetwork.observe.Publisher;

public class SocialNetworkFragment extends Fragment {

    private CardsSource data;
    private SocialNetworkAdapter adapter;
    private RecyclerView recyclerView;

    private Navigation navigation;
    private Publisher publisher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity)context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }
    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }


    public static SocialNetworkFragment newInstance() {
        return new SocialNetworkFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_socialnetwork, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        // Получим источник данных для списка
        initRecyclerView(recyclerView, data);
        if(false){
            data = new CardsSourceLocalImpl(getResources()).init(new CardsSourceResponse() {
                @Override
                public void initialized(CardsSource cardsSource) {

                }
            });
        }else{
            data = new CardsSourceRemoteImpl().init(new CardsSourceResponse() {
                @Override
                public void initialized(CardsSource cardsSource) {
                    adapter.notifyDataSetChanged();
                }
            });
        }
        adapter.setDataSource(data);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView, CardsSource data) {

        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        adapter = new SocialNetworkAdapter(this);
        recyclerView.setAdapter(adapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(2000);
        defaultItemAnimator.setChangeDuration(2000);
        defaultItemAnimator.setRemoveDuration(2000);
        recyclerView.setItemAnimator(defaultItemAnimator);


        // Добавим разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        // Установим слушателя
        adapter.SetOnItemClickListener(new SocialNetworkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), String.format("Позиция - %d", position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                navigation.addFragment(CardUpdateFragment.newInstance(), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateState(CardData cardData) {
                        Log.d("mylogs","updateState1 "+data.size());
                        data.addCardData(cardData);
                        Log.d("mylogs","updateState2 "+data.size());
                        adapter.notifyItemInserted(data.size() - 1);
                    }
                });
                return true;
            case R.id.action_clear:
                data.clearCardData();
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.card_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuContextClickPosition();
        switch (item.getItemId()) {
            case R.id.action_update:
               /* data.getCardData(position).setTitle("ОБНОВИЛИ "+position);
                adapter.notifyItemChanged(position); */

                navigation.addFragment(CardUpdateFragment.newInstance(data.getCardData(position)), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateState(CardData cardData) {
                        Log.d("mylogs","updateState1 "+data.size());
                        data.updateCardData(position,cardData);
                        adapter.notifyItemChanged(position);
                    }
                });
                return true;
            case R.id.action_delete:
                data.deleteCardData(position);
                adapter.notifyItemRemoved(position);
                return true;
        }
        return super.onContextItemSelected(item);
    }
}