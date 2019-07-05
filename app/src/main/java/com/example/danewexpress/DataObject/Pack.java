package com.example.danewexpress.DataObject;


import com.example.danewexpress.R;

/*  没写完！    */
public class Pack {
    private String packageId;//运单号
    private String expenses;//金额
    private String SenderPhone;//发件人电话
    private Address SenderAddress;//发件人地址
    private String ReceiverPhone;//收件人电话
    private Address ReceiverAddress;//收件人地址
    private String PayerPhone;//支付人电话
    private String PaymentStatus="0";//支付状态，默认0

    private int ImageId= R.drawable.ic_md_package;//默认



    private String caculateExpenses(double weight,Address SenderAddress,Address ReceiverAddress){
        String 发件省=SenderAddress.省;
        String 收件省=ReceiverAddress.省;
        if(发件省.equals(收件省)) return String.valueOf((int)Math.round(12*weight));
        //其它规则待定
        return String.valueOf((int)Math.round(13*weight));
    }
    public Pack(String packageId,
                   double weight,
                   String SenderPhone,
                   Address SenderAddress,
                   String ReceiverPhone,
                   Address ReceiverAddress,
                   String PayerPhone){
        this.packageId=packageId;
        this.expenses=caculateExpenses(weight,SenderAddress,ReceiverAddress);
        this.SenderPhone=SenderPhone;
        this.SenderAddress=SenderAddress;
        this.ReceiverPhone=ReceiverPhone;
        this.ReceiverAddress=ReceiverAddress;
        this.PayerPhone=PayerPhone;
    }
    public Pack(String packageId,
                String expenses,
                String SenderPhone,
                Address SenderAddress,
                String ReceiverPhone,
                Address ReceiverAddress,
                String PayerPhone){
        this.packageId=packageId;
        this.expenses=expenses;
        this.SenderPhone=SenderPhone;
        this.SenderAddress=SenderAddress;
        this.ReceiverPhone=ReceiverPhone;
        this.ReceiverAddress=ReceiverAddress;
        this.PayerPhone=PayerPhone;
    }

    /*  Getter  */
    public String getPackageId(){return packageId; }

    public String getExpenses() {return expenses;}

    public String getSenderPhone(){return SenderPhone;}

    public String getSenderAddress(){return SenderAddress.getString();}

    public String getReceiverPhone(){return ReceiverPhone;}

    public String getReceiverAddress(){return ReceiverAddress.getString();}

    public String getPayerPhone(){return PayerPhone;}

    public String getPaymentStatus(){return PaymentStatus;}

    public int getImageId(){return ImageId;}

    public String getMessage(){
        String ret=getPackageId()+"\t"
                +String.valueOf(getExpenses())+"\t"
                +getSenderPhone() +"\t"
                +getSenderAddress()+"\t"
                +getReceiverPhone()+"\t"
                +getReceiverAddress()+"\t"
                +getPayerPhone()+"\t"
                +getPaymentStatus();
        return ret;
    }


    /*  Setter  */
    public void setPackageId(String expressId){this.packageId=expressId;}

    public void setExpenses(double weight,Address SenderAddress,Address ReceiverAddress) {this.expenses=caculateExpenses(weight,SenderAddress,ReceiverAddress); }

    public void setSenderPhone(String SenderPhone){this.SenderPhone=SenderPhone; }

    public void setSenderAddress(Address SenderAddress){this.SenderAddress=SenderAddress; }

    public void setReceiverPhone(String ReceiverPhone){this.ReceiverPhone=ReceiverPhone; }

    public void setReceiverAddress(Address ReceiverAddress){this.ReceiverAddress=ReceiverAddress; }

    public void setPayerPhone(String PayerPhone){ this.PayerPhone=PayerPhone;}

    public void setPaymentStatus(int PaymentStatus){ this.PaymentStatus=String.valueOf(PaymentStatus);}

    public void setImageId(int ImageId){
        this.ImageId=ImageId;
    }
}
