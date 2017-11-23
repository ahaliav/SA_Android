package com.fox.ahaliav.saapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NewsDetailsFragment extends Fragment implements ICallbackMethod {

    private static final String ID = "id";
    private ProgressBar spinner;
    ArrayList<News> list = null;
    WebView webview = null;
    private Float id;
    TextView txtTitle = null;
    Menu menu;
    public NewsDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getFloat(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_details, container, false);
        webview = (WebView) v.findViewById(R.id.webview);
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        spinner = (ProgressBar) v.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        list = new ArrayList<News>();

        loadnews();

        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        this.menu = menu;

        MenuItem action_add = menu.findItem(R.id.action_edit);
        action_add.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:

                return false;
            default:
                break;
        }

        return false;
    }

    private void loadnews() {
        WebSiteHelper helper = new WebSiteHelper(this);
        helper.getNewsDetails(id);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onTaskDone(List<Object> objs) {
        if (objs != null) {
            for (int i = 0; i < objs.size(); ++i) {
                Map<String, Object> mapPost = (Map<String, Object>) objs.get(i);
                Map<String, Object> mapTitle = (Map<String, Object>) mapPost.get("title");
                Map<String, Object> mapContent = (Map<String, Object>) mapPost.get("content");
                Float nid = Float.parseFloat(mapPost.get("id").toString());
                News n = new News(nid, (String) mapContent.get("rendered"), (String) mapTitle.get("rendered"), new Date());
                String title = n.getTitle().replaceAll("\\<[^>]*>", "").replaceAll("\\&.*?\\;", "");
                txtTitle.setText(title);


                webview.loadData("<html><body dir=\"rtl\">" + n.getContent() + "</body></html>", "text/html; charset=UTF-8", null);

                break;
            }
        }


        spinner.setVisibility(View.GONE);
    }
}
