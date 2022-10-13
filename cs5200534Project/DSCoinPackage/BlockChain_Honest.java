package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

public String getnounce(TransactionBlock newBlock){
CRF crf=new CRF(64);
String nounce=null;
String temp=null;
int i=0;
for(;;){
int integerstr=1000000001+i;
String str=Integer.toString(integerstr);
if(newBlock.previous!=null){
temp=crf.Fn(newBlock.previous.dgst+"#"+newBlock.trsummary+"#"+str);
}
else{
temp=crf.Fn(start_string+"#"+newBlock.trsummary+"#"+str);
}
if(temp.substring(0,4).equals("0000"))
{nounce=str;
return nounce;
}
else{i++;}
}
}

  public void InsertBlock_Honest (TransactionBlock newBlock) {
CRF crf=new CRF(64);
if(lastBlock==null){
newBlock.nonce=getnounce(newBlock);
newBlock.previous=null;
newBlock.dgst=crf.Fn(this.start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
lastBlock=newBlock;
}
else
{
newBlock.previous=this.lastBlock;
lastBlock=newBlock;
lastBlock.nonce=getnounce(lastBlock);
lastBlock.dgst=crf.Fn(this.lastBlock.previous.dgst+"#"+lastBlock.trsummary+"#"+lastBlock.nonce);

  }
}
}