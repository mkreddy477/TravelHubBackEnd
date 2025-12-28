package com.example.travelhub.flightbooking.models.bookingmodels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Traveller information for booking
 * ti - Title of the Traveller (Adult: Mr,Mrs,Ms | Child: Ms, Master | Infant: Ms, Master)
 * pt - PaxType - ADULT, CHILD, INFANT (Values should be Caps)
 * fN - First Name of the Traveller
 * lN - Last Name of the Traveller
 * dob - Traveller Date Of Birth (YYYY-MM-DD Format)
 * pNum - Passport Number
 * eD - Expiry Date of the passport (YYYY-MM-DD Format)
 * pNat - Passport Nationality (Passport Issued Country) - IATA Country Code(2-Letter)
 * pid - Passport Issue Date (YYYY-MM-DD Format)
 * di - Document ID when required
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TravellerInfo {
    
    private String ti;  // Title
    private String pt;  // Pax Type (ADULT, CHILD, INFANT)
    @JsonProperty("fN")
    private String fN;  // First Name
    @JsonProperty("lN")
    private String lN;  // Last Name
    private String dob; // Date of Birth (YYYY-MM-DD)
    @JsonProperty("pNum")
    private String pNum; // Passport Number
    @JsonProperty("eD")
    private ExpiryDate eD;  // Passport Expiry Date (month/year object)
    @JsonProperty("pNat")
    private String pNat; // Passport Nationality (2-letter IATA code)
    @JsonProperty("pid")
    private String pid; // Passport Issue Date (YYYY-MM-DD)
    @JsonProperty("di")
    private String di;  // Document ID
    
    private List<SsrInfo> ssrBaggageInfos;
    private List<SsrInfo> ssrMealInfos;
    private List<SsrInfo> ssrSeatInfos;
    private List<SsrInfo> ssrExtraServiceInfos;

    public TravellerInfo() {
    }

    public String getTi() {
        return ti;
    }

    public void setTi(String ti) {
        this.ti = ti;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getfN() {
        return fN;
    }

    public void setfN(String fN) {
        this.fN = fN;
    }

    public String getlN() {
        return lN;
    }

    public void setlN(String lN) {
        this.lN = lN;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getpNum() {
        return pNum;
    }

    public void setpNum(String pNum) {
        this.pNum = pNum;
    }

    public ExpiryDate geteD() {
        return eD;
    }

    public void seteD(ExpiryDate eD) {
        this.eD = eD;
    }

    public String getpNat() {
        return pNat;
    }

    public void setpNat(String pNat) {
        this.pNat = pNat;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public List<SsrInfo> getSsrBaggageInfos() {
        return ssrBaggageInfos;
    }

    public void setSsrBaggageInfos(List<SsrInfo> ssrBaggageInfos) {
        this.ssrBaggageInfos = ssrBaggageInfos;
    }

    public List<SsrInfo> getSsrMealInfos() {
        return ssrMealInfos;
    }

    public void setSsrMealInfos(List<SsrInfo> ssrMealInfos) {
        this.ssrMealInfos = ssrMealInfos;
    }

    public List<SsrInfo> getSsrSeatInfos() {
        return ssrSeatInfos;
    }

    public void setSsrSeatInfos(List<SsrInfo> ssrSeatInfos) {
        this.ssrSeatInfos = ssrSeatInfos;
    }

    public List<SsrInfo> getSsrExtraServiceInfos() {
        return ssrExtraServiceInfos;
    }

    public void setSsrExtraServiceInfos(List<SsrInfo> ssrExtraServiceInfos) {
        this.ssrExtraServiceInfos = ssrExtraServiceInfos;
    }

    @Override
    public String toString() {
        return "TravellerInfo{ti='" + ti + "', pt='" + pt + "', fN='" + fN + "', lN='" + lN + "', dob='" + dob + "'}";
    }
}
