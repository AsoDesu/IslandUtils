package net.asodev.islandutils.modules.scavenging;

import java.util.HashMap;
import java.util.Map;

public class ScavengingTotalList {

    Map<ScavengingItemHandler, ScavengingTotal> totals = new HashMap<>();

    public void apply(ScavengingTotal newTotal) {
        ScavengingTotal currentTotal = totals.get(newTotal.handler());

        ScavengingTotal addedTotal = newTotal;
        if (currentTotal != null) {
            addedTotal = currentTotal.apply(newTotal);
        }


        totals.put(newTotal.handler(), addedTotal);
    }

}
