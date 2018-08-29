package sa.israel.org;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import sa.israel.org.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class EventsFragment extends Fragment implements ICallbackMethod {
    ListView listview = null;
    ArrayList<News> list = null;
    private ProgressBar spinner;
    Menu menu;

    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();

        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        this.menu = menu;

        MenuItem action_add = menu.findItem(R.id.action_add);
        action_add.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                String message = getResources().getString(R.string.mynameis) + ": \n";
                message += getResources().getString(R.string.myphoneis) + ": \n";
                message += getResources().getString(R.string.event_details) + ": \n";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + getResources().getString(R.string.new_event_subject) + "&body=" + message + "&to=news@sa-israel.org");
                intent.setData(data);

                startActivity(Intent.createChooser(intent, ""));
                return false;
            default:
                break;
        }

        return false;
    }

    private void loadevents() {
        WebSiteHelper helper = new WebSiteHelper(this, getContext());
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
        View v = inflater.inflate(R.layout.fragment_events, container, false);

        spinner = (ProgressBar)v.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        listview = (ListView)v.findViewById(R.id.listviewEvents);
        list = new ArrayList<News>();

        FloatingActionButton floatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = getResources().getString(R.string.mynameis) + ": \n";
                message += getResources().getString(R.string.myphoneis) + ": \n";
                message += getResources().getString(R.string.event_details) + ": \n";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + getResources().getString(R.string.new_event_subject) + "&body=" + message + "&to=news@sa-israel.org");
                intent.setData(data);

                startActivity(Intent.createChooser(intent, ""));
            }
        });

        loadevents();

        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onTaskDone(List<Object> objs) {

        if(objs != null){
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

}
