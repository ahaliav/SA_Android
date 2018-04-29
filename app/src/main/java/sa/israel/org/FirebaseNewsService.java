package sa.israel.org;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Ahaliav on 26/02/2018.
 */

public class FirebaseNewsService extends FirebaseInstanceIdService {

    final static String REG_TOKEN = "REG_TOKEN";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String recent_token = FirebaseInstanceId.getInstance().getToken();
    }
}
