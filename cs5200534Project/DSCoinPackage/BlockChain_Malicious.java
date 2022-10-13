package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
CRF crf=new CRF(64);
boolean correct=false;

if(tB.dgst.substring(0,4).equals("0000"))
correct=true;
else correct=false;
if(correct==false)
return correct;

if(tB.previous!=null)
{
if(tB.dgst.equals(crf.Fn(tB.previous.dgst+"#"+tB.trsummary+"#"+tB.nonce)))
correct=true;
else correct=false;
}
else
{
if(tB.dgst.equals(crf.Fn(start_string+"#"+tB.trsummary+"#"+tB.nonce)))
correct=true;
else correct=false;
}
if(correct==false)
return correct;

//can i use the same building function for checking or i have to sefine here a tree building function and check it?????????
TransactionBlock trashtransactionbock=new TransactionBlock(tB.trarray);
String rootval=trashtransactionbock.trsummary;
if(rootval.equals(tB.trsummary))
correct=true;
else correct=false;
if(correct==false)
return correct;

if(!tB.trarray[0].Source.UID.equals("Moderator"))
for(int i=0;i<tB.trarray.length;i++)
{
boolean put=false;
TransactionBlock lastonedummy=tB;
lastonedummy=tB;
while(lastonedummy!=null)
{
if(lastonedummy==tB.trarray[i].coinsrc_block || tB.trarray[i].coinsrc_block==null)
{
put=true;
break;
}
lastonedummy=lastonedummy.previous;
}
if(put==false)
return false;
}

for(int i=0;i<tB.trarray.length;i++)
{
if(tB.previous!=null)
{
correct=tB.previous.checkTransaction(tB.trarray[i]);
if(correct==false)
return false;
}
else 
{
correct=true;
}
}

return true;
  }

  public TransactionBlock FindLongestValidChain () {
int lengths=0;
for(lengths=0;lengths<lastBlocksList.length;lengths++)
if(lastBlocksList[lengths]==null)
break;

TransactionBlock[] last=new TransactionBlock[lengths];
int[] sizeofeach=new int[lengths];

for(int i=0;i<lengths;i++)
{
TransactionBlock traverser=lastBlocksList[i];
last[i]=traverser;
int length=0;
while(traverser!=null)
{
if(this.checkTransactionBlock(traverser)==true)
{
length++;
}
else if(this.checkTransactionBlock(traverser)==false)
{
length=0;
last[i]=traverser.previous;
}
traverser=traverser.previous;
}
sizeofeach[i]=length;
}

int max=sizeofeach[0];
for(int i=0;i<sizeofeach.length;i++)
{
if(sizeofeach[i]>=max)
max=sizeofeach[i];
}


for(int i=0;i<sizeofeach.length;i++)
if(max==sizeofeach[i])
return last[i];

return null;
  }

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

  public void InsertBlock_Malicious (TransactionBlock newBlock) {


CRF crf=new CRF(64);
if(this.lastBlocksList==null)
{
newBlock.nonce=getnounce(newBlock);
newBlock.previous=null;
newBlock.dgst=crf.Fn(this.start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
TransactionBlock[] lastBlocks=new TransactionBlock[100];
lastBlocks[0]=newBlock;
lastBlocksList=lastBlocks;
}
else
{
int lengths=0;
for(lengths=0;lengths<lastBlocksList.length;lengths++)
if(lastBlocksList[lengths]==null)
break;
if(lengths==0)
{
newBlock.nonce=getnounce(newBlock);
newBlock.previous=null;
newBlock.dgst=crf.Fn(this.start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
lastBlocksList[0]=newBlock;
}
else
{
TransactionBlock correctblock=this.FindLongestValidChain();
newBlock.previous=correctblock;
newBlock.nonce=getnounce(newBlock);
newBlock.dgst=crf.Fn(newBlock.previous.dgst+"#"+newBlock.trsummary+"#"+newBlock.nonce);
boolean present=false;
int i=0;
for(i=0;i<lengths;i++)
if(correctblock==lastBlocksList[i]){
present=true;
break;
}
if(present==true)
{
lastBlocksList[i]=newBlock;
}
else{ lastBlocksList[lengths]=newBlock;}
}
}
   }
}
