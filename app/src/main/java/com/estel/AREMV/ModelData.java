package com.estel.AREMV;

public class ModelData {
    boolean AgreementFlag;
    String AuthCode;
    String BNO;
    String CRD;
    boolean ClientActivateFlag;
    String EmvStatus;
    String ExpDate;
    String InvNo;
    String MID;
    String MerchName;
    String PAN;
    String PAN4;
    String RRN;
    boolean ReadStatus;
    String ResCode;
    String RfndAmt;
    String Sign;
    String TXNDT;
    String Total;
    String Track2;
    String TxnDate;
    String TxnID;
    String TxnMessage;
    String TxnTyp;
    String VoidAmt;
    String amount;
    String cardHolderName;
    String cardNumber;
    String cardType;
    boolean connect;
    String expirayDate;
    String invoiceNo;
    String mode;
    boolean pin;
    String rrnNumber;
    String serviceTax;
    String tip;

    public ModelData() {
        this.AgreementFlag = false;
        this.ClientActivateFlag = false;
        this.ReadStatus = false;
        this.connect = false;
    }

    public boolean getBTConn() {
        return this.connect;
    }

    public void setBTConn(boolean connect) {
        this.connect = connect;
    }

    public boolean getReadCard() {
        return this.ReadStatus;
    }

    public void setReadCard(boolean ReadStatus) {
        this.ReadStatus = ReadStatus;
    }

    public void setTxnMode(String mode) {
        this.mode = mode;
    }

    public String getTxnMode() {
        return this.mode;
    }

    public void setEMVStatus(String EmvStatus) {
        this.EmvStatus = EmvStatus;
    }

    public String getEMVStatus() {
        return this.EmvStatus;
    }

    public void setTXNDT(String TXNDT) {
        this.TXNDT = TXNDT;
    }

    public String getTXNDT() {
        return this.TXNDT;
    }

    public void setAuthCode(String AuthCode) {
        this.AuthCode = AuthCode;
    }

    public String getAuthCode() {
        return this.AuthCode;
    }

    public void setBatchNo(String BNO) {
        this.BNO = BNO;
    }

    public String getBatchNo() {
        return this.BNO;
    }

    public void setMID(String MID) {
        this.MID = MID;
    }

    public String getMID() {
        return this.MID;
    }

    public void setSignData(String Sign) {
        this.Sign = Sign;
    }

    public String getSignData() {
        return this.Sign;
    }

    public boolean getCrdPin() {
        return this.pin;
    }

    public void setCrdPin(boolean pin) {
        this.pin = pin;
    }

    public String getMName() {
        return this.MerchName;
    }

    public void setMName(String MerchName) {
        this.MerchName = MerchName;
    }

    public String getTrack2() {
        return this.Track2;
    }

    public void setTrack2(String Track2) {
        this.Track2 = Track2;
    }

    public String getTxnID() {
        return this.TxnID;
    }

    public void setTxnID(String TxnID) {
        this.TxnID = TxnID;
    }

    public String getTxnDate() {
        return this.TxnDate;
    }

    public void setTxnDate(String TxnDate) {
        this.TxnDate = TxnDate;
    }

    public String getPAN4() {
        return this.PAN4;
    }

    public void setPAN4(String PAN4) {
        this.PAN4 = PAN4;
    }

    public String getTxnTyp() {
        return this.TxnTyp;
    }

    public void setTxnTyp(String TxnTyp) {
        this.TxnTyp = TxnTyp;
    }

    public String getResCode() {
        return this.ResCode;
    }

    public void setResCode(String ResCode) {
        this.ResCode = ResCode;
    }

    public String getVoidAmount() {
        return this.VoidAmt;
    }

    public void setVoidAmount(String VoidAmt) {
        this.VoidAmt = VoidAmt;
    }

    public String getVoidInv() {
        return this.InvNo;
    }

    public void setVoidInv(String InvNo) {
        this.InvNo = InvNo;
    }

    public String getRefundAmount() {
        return this.RfndAmt;
    }

    public void setRefundAmount(String RfndAmt) {
        this.RfndAmt = RfndAmt;
    }

    public String getRefundRRN() {
        return this.RRN;
    }

    public void setRefundRRN(String RRN) {
        this.RRN = RRN;
    }

    public String getPAN() {
        return this.PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getCrdData() {
        return this.CRD;
    }

    public void setCrdData(String CRD) {
        this.CRD = CRD;
    }

    public String getExpData() {
        return this.ExpDate;
    }

    public void setExpData(String ExpDate) {
        this.ExpDate = ExpDate;
    }

    public String getTxnMessage() {
        return this.TxnMessage;
    }

    public void setTxnMessage(String TxnMessage) {
        this.TxnMessage = TxnMessage;
    }

    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardType() {
        return this.cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirayDate() {
        return this.expirayDate;
    }

    public void setExpirayDate(String expirayDate) {
        this.expirayDate = expirayDate;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTip() {
        return this.tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getServiceTax() {
        return this.serviceTax;
    }

    public void setServiceTax(String serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getTotal() {
        return this.Total;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }

    public String getRRN() {
        return this.rrnNumber;
    }

    public void setRRN(String RRN) {
        this.rrnNumber = RRN;
    }

    public String getInvoice() {
        return this.invoiceNo;
    }

    public void setInvoice(String InvNo) {
        this.invoiceNo = InvNo;
    }

    public boolean getAgreement() {
        return this.AgreementFlag;
    }

    public void setAgreement(boolean flag) {
        this.AgreementFlag = flag;
    }

    public boolean getClientActivated() {
        return this.ClientActivateFlag;
    }

    public void setClientActivated(boolean flag) {
        this.ClientActivateFlag = flag;
    }
}
