package DSCoinPackage;

import java.util.*;
import HelperClasses.Pair;
import HelperClasses.TreeNode;

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
Transaction tobj=new Transaction();
tobj.coinID=this.mycoins.get(0).get_first();
tobj.Source=this;
tobj.coinsrc_block=this.mycoins.get(0).get_second();
for(int i=0;i<DSobj.memberlist.length;i++)
{
if(DSobj.memberlist[i].UID.equals(destUID))
{
tobj.Destination=DSobj.memberlist[i];
break;
}
}

//are we going to add to the array "in_process_trans" of how much length????? how to decide the index where this transaction is added????
if(this.in_process_trans==null)
{
Transaction[] inprocess=new Transaction[1000];
inprocess[0]=tobj;
this.in_process_trans=inprocess;
}
else
for(int i=0;i<this.in_process_trans.length;i++)
if(in_process_trans[i]==null)
{
in_process_trans[i]=tobj;
break;
}
mycoins.remove(0);
DSobj.pendingTransactions.AddTransactions(tobj);

  }

  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
Transaction tobj=new Transaction();
tobj.coinID=this.mycoins.get(0).get_first();
tobj.Source=this;
tobj.coinsrc_block=this.mycoins.get(0).get_second();
for(int i=0;i<DSobj.memberlist.length;i++)
{
if(DSobj.memberlist[i].UID.equals(destUID))
{
tobj.Destination=DSobj.memberlist[i];
break;
}
}

//are we going to add to the array "in_process_trans" of how much length????? how to decide the index where this transaction is added????
if(this.in_process_trans==null)
{
Transaction[] inprocess=new Transaction[1000];
inprocess[0]=tobj;
this.in_process_trans=inprocess;
}
else
for(int i=0;i<this.in_process_trans.length;i++)
if(in_process_trans[i]==null)
{
in_process_trans[i]=tobj;
break;
}
mycoins.remove(0);
DSobj.pendingTransactions.AddTransactions(tobj);

  }

  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
TransactionBlock tB=DSObj.bChain.lastBlock;
int found=0;
int index=0;
while(tB!=null)
{
for(int i=0;i<tB.trarray.length;i++){
if(tB.trarray[i].coinID.equals(tobj.coinID) && tB.trarray[i].Source.equals(tobj.Source) && tB.trarray[i].Destination.equals(tobj.Destination))
{
found=1;
index=i+1;
}}
if(found==1)
break;
else tB=tB.previous;
}

if(found==0)
throw new MissingTransactionException();

//sibling coupled path
ArrayList<Pair<String,String>> rraylist = new ArrayList<Pair<String,String>>();  
TreeNode node=new TreeNode();
node=tB.Tree.rootnode;
int tra=DSObj.bChain.tr_count;
int doc_idx=index;
while(node.left!=null && node.right!=null)
{
if(doc_idx>(tra/2))
{
node=node.right;
doc_idx=doc_idx-(tra/2);
}
else if(doc_idx<=(tra/2))
{
node=node.left;
}
tra=tra/2;
}
while(node.parent!=null)
{
rraylist.add(new Pair<String,String>(node.parent.left.val,node.parent.right.val));
node=node.parent;
}
rraylist.add(new Pair<String,String>(node.val,null));

//transaction block ki list
ArrayList<Pair<String,String>> transactionrraylist = new ArrayList<Pair<String,String>>();  
TransactionBlock dummy=DSObj.bChain.lastBlock;
while(dummy!=tB.previous)
{
transactionrraylist.add(new Pair<String,String>(dummy.dgst,dummy.previous.dgst+"#"+dummy.trsummary+"#"+dummy.nonce));
dummy=dummy.previous;
}
transactionrraylist.add(new Pair<String,String>(tB.previous.dgst,null));
ArrayList<Pair<String,String>> finaltransactionrraylist = new ArrayList<Pair<String,String>>();
for(int i=(transactionrraylist.size()-1);i>=0;i--)
finaltransactionrraylist.add(transactionrraylist.get(i));

int i=0;
for(i=0;i<in_process_trans.length;i++)
if(in_process_trans[i]==tobj)
break;
for(int h=i;h<(in_process_trans.length-1);h++)
in_process_trans[h]=in_process_trans[h+1];
in_process_trans[in_process_trans.length-1]=null;

return new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(rraylist,finaltransactionrraylist);
  }


    public List<Pair<String, TransactionBlock>> sort(List<Pair<String, TransactionBlock>> toBeSorted){
if(toBeSorted==null)
return toBeSorted;
        String tempfirst =null;
TransactionBlock tempsecond=null;  
         for(int i=0; i < toBeSorted.size(); i++){  
                 for(int j=1; j < (toBeSorted.size()-i); j++){ 
                          if(Integer.valueOf(toBeSorted.get(j-1).get_first()) > Integer.valueOf(toBeSorted.get(j).get_first())){   
                                 tempfirst = toBeSorted.get(j-1).first;  
                                 toBeSorted.get(j-1).first = toBeSorted.get(j).first;  
                                 toBeSorted.get(j).first = tempfirst;  
				tempsecond = toBeSorted.get(j-1).second;  
                                 toBeSorted.get(j-1).second = toBeSorted.get(j).second;  
                                 toBeSorted.get(j).second = tempsecond;
                         }  
                          
                 }  }
        return toBeSorted;
    }

  public void MineCoin(DSCoin_Honest DSObj) {
Transaction[] transact=new Transaction[DSObj.bChain.tr_count];
int i=0;
int found=0;
while(i<(DSObj.bChain.tr_count-1))
{
Transaction transactiondummy=DSObj.pendingTransactions.firstTransaction;

for(int j=0;j<i;j++)
if(transact[j].coinID.equals(transactiondummy.coinID))
{found=1;}

if(DSObj.bChain.lastBlock.checkTransaction(transactiondummy)==true && found==0)
{
transact[i]=transactiondummy;
i++;
}

try{
DSObj.pendingTransactions.RemoveTransaction();
}
catch(EmptyQueueException e)
{
System.out.println(e);
}
found=0;
}

Transaction minerRewardTransaction=new Transaction();
minerRewardTransaction.coinID=DSObj.latestCoinID;
minerRewardTransaction.Source=null;
minerRewardTransaction.Destination=this;
minerRewardTransaction.coinsrc_block=null;
transact[DSObj.bChain.tr_count-1]=minerRewardTransaction;

TransactionBlock tB=new TransactionBlock(transact);
DSObj.bChain.InsertBlock_Honest(tB);
this.mycoins.add(new Pair<String, TransactionBlock>(DSObj.latestCoinID,tB));
int latest=Integer.valueOf(DSObj.latestCoinID);
latest++;
DSObj.latestCoinID=Integer.toString(latest);


for(int distribute=0;distribute<transact.length-1;distribute++)
{
for(int ch=0;ch<transact[distribute].Source.mycoins.size();ch++)
{
if(transact[distribute].Source.mycoins!=null && transact[distribute].coinID==transact[distribute].Source.mycoins.get(ch).get_first())
transact[distribute].Source.mycoins.remove(ch);
}
if(transact[distribute].Destination.mycoins!=null)
transact[distribute].Destination.mycoins.add(new Pair<String, TransactionBlock>(transact[distribute].coinID,tB)); 
else{
ArrayList<Pair<String, TransactionBlock>> dummy = new ArrayList<Pair<String, TransactionBlock>>(); 
dummy.add(new Pair<String, TransactionBlock>(transact[distribute].coinID,tB)); 
transact[distribute].Destination.mycoins=dummy;
}
transact[distribute].Destination.mycoins=sort(transact[distribute].Destination.mycoins);
transact[distribute].Source.mycoins=sort(transact[distribute].Source.mycoins);
}
  }  

  public void MineCoin(DSCoin_Malicious DSObj) {
Transaction[] transact=new Transaction[DSObj.bChain.tr_count];
int i=0;
int found=0;
while(i<(DSObj.bChain.tr_count-1))
{
Transaction transactiondummy=DSObj.pendingTransactions.firstTransaction;

for(int j=0;j<i;j++)
if(transact[j].coinID==transactiondummy.coinID)
found=1;

if(found==0)
{
boolean put=false;
TransactionBlock lastone=DSObj.bChain.FindLongestValidChain();
TransactionBlock lastonedummy=lastone;
while(lastonedummy!=null)
{
if(lastonedummy==transactiondummy.coinsrc_block)
{put=true;
break;}
lastonedummy=lastonedummy.previous;
}
if(put==true)
if(lastone.checkTransaction(transactiondummy)==true)
{
transact[i]=transactiondummy;
i++;
}
}


try{
DSObj.pendingTransactions.RemoveTransaction();
}
catch(EmptyQueueException e)
{
System.out.println(e);
}
found=0;
}

Transaction minerRewardTransaction=new Transaction();
minerRewardTransaction.coinID=DSObj.latestCoinID;
minerRewardTransaction.Source=null;
minerRewardTransaction.Destination=this;
minerRewardTransaction.coinsrc_block=null;
transact[DSObj.bChain.tr_count-1]=minerRewardTransaction;

TransactionBlock tB=new TransactionBlock(transact);
DSObj.bChain.InsertBlock_Malicious(tB);
this.mycoins.add(new Pair<String, TransactionBlock>(DSObj.latestCoinID,tB));
int latest=Integer.valueOf(DSObj.latestCoinID);
latest++;
DSObj.latestCoinID=Integer.toString(latest);
/*
for(int distribute=0;distribute<transact.length-1;distribute++)
{
for(int ch=0;ch<transact[distribute].Source.mycoins.size();ch++)
{
if(transact[distribute].Source.mycoins!=null && transact[distribute].coinID==transact[distribute].Source.mycoins.get(ch).get_first())
transact[distribute].Source.mycoins.remove(ch);
}
if(transact[distribute].Destination.mycoins!=null)
transact[distribute].Destination.mycoins.add(new Pair<String, TransactionBlock>(transact[distribute].coinID,tB)); 
else{
ArrayList<Pair<String, TransactionBlock>> dummy = new ArrayList<Pair<String, TransactionBlock>>(); 
dummy.add(new Pair<String, TransactionBlock>(transact[distribute].coinID,tB)); 
transact[distribute].Destination.mycoins=dummy;
}
transact[distribute].Destination.mycoins=sort(transact[distribute].Destination.mycoins);
transact[distribute].Source.mycoins=sort(transact[distribute].Source.mycoins);
}
*/
  }  
}
