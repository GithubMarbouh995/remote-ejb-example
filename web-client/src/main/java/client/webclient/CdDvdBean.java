package client.webclient;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;
import com.illucit.ejbremote.server.CD;
import com.illucit.ejbremote.server.CdDvdStatelessService;


@Named
@RequestScoped
public class CdDvdBean {

    @EJB
    private CdDvdStatelessService cdDvdService;

    private List<CD> availableCds;
    private List<CD> borrowedCds;
    private String cdId;

    public List<CD> getAvailableCds() {
        if (availableCds == null) {
            availableCds = cdDvdService.listAvailableCds();
        }
        return availableCds;
    }

    public List<CD> getBorrowedCds() {
        if (borrowedCds == null) {
            borrowedCds = cdDvdService.listBorrowedCds();
        }
        return borrowedCds;
    }

    public void borrowCd() {
        cdDvdService.borrowCd(cdId);
        availableCds = null; // Rafraîchir la liste
        borrowedCds = null; // Rafraîchir la liste
    }

    public void returnCd() {
        cdDvdService.returnCd(cdId);
        availableCds = null; // Rafraîchir la liste
        borrowedCds = null; // Rafraîchir la liste
    }

    public String getCdId() {
        return cdId;
    }

    public void setCdId(String cdId) {
        this.cdId = cdId;
    }
}