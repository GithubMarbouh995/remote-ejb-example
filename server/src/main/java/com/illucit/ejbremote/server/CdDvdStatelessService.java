package com.illucit.ejbremote.server;

import java.util.List;

public interface CdDvdStatelessService {
    List<CD> listAvailableCds();
    List<CD> listBorrowedCds();
    void borrowCd(String cdId);
    void returnCd(String cdId);
}
