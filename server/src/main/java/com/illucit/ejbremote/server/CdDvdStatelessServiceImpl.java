package com.illucit.ejbremote.server;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class CdDvdStatelessServiceImpl implements CdDvdStatelessService {


    private List<CD> cds = new ArrayList<>();
    private List<CD> borrowedCds = new ArrayList<>();

    public CdDvdStatelessServiceImpl() {
        // Initialize with some CDs
        cds.add(new CD("1", "CD1"));
        cds.add(new CD("2", "CD2"));
    }

    @Override
    public List<CD> listAvailableCds() {
        return new ArrayList<>(cds);
    }

    @Override
    public List<CD> listBorrowedCds() {
        return new ArrayList<>(borrowedCds);
    }

    @Override
    public void borrowCd(String cdId) {
        CD cd = findCdById(cdId, cds);
        if (cd != null) {
            cds.remove(cd);
            borrowedCds.add(cd);
        }
    }

    @Override
    public void returnCd(String cdId) {
        CD cd = findCdById(cdId, borrowedCds);
        if (cd != null) {
            borrowedCds.remove(cd);
            cds.add(cd);
        }
    }
    private CD findCdById(String cdId, List<CD> cdList) {
        return cdList.stream().filter(cd -> cd.getId().equals(cdId)).findFirst().orElse(null);
    }

}