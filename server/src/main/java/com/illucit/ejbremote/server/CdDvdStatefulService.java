package com.illucit.ejbremote.server;

import java.util.List;

public interface CdDvdStatefulService {

        List<CD> listAvailableCds();

        List<CD> listBorrowedCds();

        void borrowCd(String cdId);

        void returnCd(String cdId);

        void addCd(CD cd);

        void updateCd(CD cd);

        void deleteCd(String cdId);
}
