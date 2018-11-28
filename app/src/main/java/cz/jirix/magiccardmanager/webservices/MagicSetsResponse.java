package cz.jirix.magiccardmanager.webservices;

import java.util.List;

import cz.jirix.magiccardmanager.model.MagicSet;

/*
 * A simple class to help Retrofit parse the response correctly
 */
public class MagicSetsResponse {

    private List<MagicSet> sets;

    public MagicSetsResponse(List<MagicSet> sets) {
        this.sets = sets;
    }

    public List<MagicSet> getSets() {
        return sets;
    }

    public void setSets(List<MagicSet> sets) {
        this.sets = sets;
    }
}
