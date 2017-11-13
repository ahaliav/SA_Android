package com.fox.ahaliav.saapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NewsFragment extends Fragment implements ICallbackMethod {

    ListView listview = null;
    ArrayList<News> list = null;
    private ProgressBar spinner;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();

        return fragment;
    }

    private void loadnews() {
        WebSiteHelper helper = new WebSiteHelper(this);
        helper.getNewsTitles();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);

        listview = (ListView) v.findViewById(R.id.listviewNews);
        spinner = (ProgressBar) v.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        list = new ArrayList<News>();
        loadnews();

        return v;
    }

    @Override
    public void onTaskDone(List<Object> objs) {
        for (int i = 0; i < objs.size(); ++i) {
            Map<String, Object> mapPost = (Map<String, Object>) objs.get(i);
            Map<String, Object> mapTitle = (Map<String, Object>) mapPost.get("title");
            Float nid = Float.parseFloat(mapPost.get("id").toString());
            News n = new News(nid,"", (String) mapTitle.get("rendered"), new Date());
            n.setTitle(n.getTitle().replaceAll("\\<[^>]*>", "").replaceAll("\\&.*?\\;", ""));
            list.add(n);
        }

        final NewsAdapter adapter = new NewsAdapter(list, getActivity().getApplicationContext());
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News dataModel = list.get(position);

                Bundle bundle = new Bundle();
                bundle.putFloat("id", dataModel.getId());

                NewsDetailsFragment news = new NewsDetailsFragment();
                news.setArguments(bundle);

                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment_container, news, "NewsDetailsFragment");
                ft.addToBackStack("NewsDetailsFragment");
                ft.commit();
            }
        });

        spinner.setVisibility(View.GONE);
    }
}
