package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;
import HelperClasses.*;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;


 TransactionBlock(Transaction[] t) {
if(t==null)
{
this.trarray=null;
this.Tree=null;
this.trsummary=null;
this.previous=null;
this.dgst=null;
this.nonce=null;
}
else 
{
Transaction[] dummytrarray=new Transaction[t.length];
for(int i=0;i<t.length;i++){
dummytrarray[i]=t[i];
}
this.trarray=dummytrarray;
MerkleTree dummytree=new MerkleTree();
dummytree.Build(trarray);
String rootval=dummytree.rootnode.val;
this.Tree=dummytree;
this.trsummary=rootval;
this.previous=null;
this.dgst=null;
}
  }


public boolean check(TransactionBlock node, String tocheck){
if(node==null)
return false; 

for(int i=0;i<node.trarray.length;i++)
{
if(node.trarray[i].coinID==tocheck)
return true;
}

return false;
}


public boolean check2(TransactionBlock node, Transaction tocheck){
if(node==null)
return true; 

for(int i=0;i<node.trarray.length;i++)
{
if(node.trarray[i].coinID==tocheck.coinID && node.trarray[i].Destination==tocheck.Source)
return true;
}

return false;
}

  public boolean checkTransaction (Transaction t) {
if(t.coinsrc_block==null)
return true;

TransactionBlock currtransactionblock=this;
String tocheck=t.coinID;
while(currtransactionblock!=t.coinsrc_block && !currtransactionblock.trarray[0].Source.UID.equals("Moderator"))
{
if(check(currtransactionblock,t.coinID)==true)
return false;
currtransactionblock=currtransactionblock.previous;
}

if(check2(t.coinsrc_block,t)==false)
return false;

return true;
  }
}
