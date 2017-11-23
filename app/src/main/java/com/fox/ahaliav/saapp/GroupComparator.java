package com.fox.ahaliav.saapp;

import java.util.Comparator;

/**
 * Created by ahaliav_fox on 23 נובמבר 2017.
 */

public class GroupComparator implements Comparator<Group> {
    @Override
    public int compare(Group o1, Group o2) {
        return Float.compare(o1.getKm(),o2.getKm());
    }
}
