package sa.israel.org;

import java.util.Comparator;

/**
 * Created by ahaliav_fox on 23 נובמבר 2017.
 */

public class GroupComparator implements Comparator<Group> {
    @Override
    public int compare(Group o1, Group o2) {
       return Float.compare(((float)o1.getDayNum() + o1.getKm()),((float)o2.getDayNum() + o2.getKm()));

    }
}
