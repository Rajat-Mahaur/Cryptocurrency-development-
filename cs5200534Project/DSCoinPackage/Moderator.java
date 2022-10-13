package DSCoinPackage;

import HelperClasses.Pair;
import java.util.*;

public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
int membernumber=0;
for(membernumber=0;membernumber<DSObj.memberlist.length;membernumber++)
if(DSObj.memberlist[membernumber]==null)
break;

Members mod=new Members();
mod.UID="Moderator";

int trcount=DSObj.bChain.tr_count;
int coinval=100000;
Transaction[][] transactions=new Transaction[coinCount/trcount][trcount];
for(int i=0;i<coinCount/trcount;i++)
for(int j=0;j<trcount;j++)
{
Transaction dummytransaction=new Transaction();
dummytransaction.coinID=Integer.toString(coinval);
dummytransaction.Source=mod;
dummytransaction.Destination=DSObj.memberlist[((trcount*i)+j)%membernumber];
dummytransaction.coinsrc_block=null;
transactions[i][j]=dummytransaction;
coinval++;
}

for(int i=0;i<coinCount/trcount;i++)
{
Transaction[] transactionInsert=new Transaction[trcount];
for(int j=0;j<trcount;j++)
transactionInsert[j]=transactions[i][j];
TransactionBlock tnewblock=new TransactionBlock(transactionInsert);
DSObj.bChain.InsertBlock_Honest(tnewblock);

for(int g=0;g<membernumber;g++)
{
if(DSObj.memberlist[g].mycoins==null)
{
ArrayList<Pair<String, TransactionBlock>> dummy = new ArrayList<Pair<String, TransactionBlock>>(); 
for(int j=0;j<trcount;j++)
{
if(transactionInsert[j].Destination==DSObj.memberlist[g])
dummy.add(new Pair<String, TransactionBlock>(transactionInsert[j].coinID,tnewblock));
}
DSObj.memberlist[g].mycoins=dummy;
}
else
{
for(int j=0;j<trcount;j++)
{
if(transactionInsert[j].Destination==DSObj.memberlist[g])
DSObj.memberlist[g].mycoins.add(new Pair<String, TransactionBlock>(transactionInsert[j].coinID,tnewblock));
}
}
}

DSObj.latestCoinID=Integer.toString(coinval);
}

  }  
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
int membernumber=0;
for(membernumber=0;membernumber<DSObj.memberlist.length;membernumber++)
if(DSObj.memberlist[membernumber]==null)
break;

Members mod=new Members();
mod.UID="Moderator";

int trcount=DSObj.bChain.tr_count;
int coinval=100000;
Transaction[][] transactions=new Transaction[coinCount/trcount][trcount];
for(int i=0;i<coinCount/trcount;i++)
for(int j=0;j<trcount;j++)
{
Transaction dummytransaction=new Transaction();
dummytransaction.coinID=Integer.toString(coinval);
dummytransaction.Source=mod;
dummytransaction.Destination=DSObj.memberlist[((trcount*i)+j)%membernumber];
dummytransaction.coinsrc_block=null;
transactions[i][j]=dummytransaction;
coinval++;
}

for(int i=0;i<coinCount/trcount;i++)
{
Transaction[] transactionInsert=new Transaction[trcount];
for(int j=0;j<trcount;j++)
transactionInsert[j]=transactions[i][j];
TransactionBlock tnewblock=new TransactionBlock(transactionInsert);
DSObj.bChain.InsertBlock_Malicious(tnewblock);

for(int g=0;g<membernumber;g++)
{
if(DSObj.memberlist[g].mycoins==null)
{
ArrayList<Pair<String, TransactionBlock>> dummy = new ArrayList<Pair<String, TransactionBlock>>(); 
for(int j=0;j<trcount;j++)
{
if(transactionInsert[j].Destination==DSObj.memberlist[g])
dummy.add(new Pair<String, TransactionBlock>(transactionInsert[j].coinID,tnewblock));
}
DSObj.memberlist[g].mycoins=dummy;
}
else
{
for(int j=0;j<trcount;j++)
{
if(transactionInsert[j].Destination==DSObj.memberlist[g])
DSObj.memberlist[g].mycoins.add(new Pair<String, TransactionBlock>(transactionInsert[j].coinID,tnewblock));
}
}
}

DSObj.latestCoinID=Integer.toString(coinval);
}
}
}