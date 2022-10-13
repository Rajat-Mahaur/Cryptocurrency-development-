package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
this.numTransactions++;
if(firstTransaction==null)
{
this.firstTransaction=transaction;
this.lastTransaction=transaction;
}
else 
{
this.lastTransaction.next=transaction;
this.lastTransaction=transaction;
}
return;
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
if(this.firstTransaction==null)
throw new EmptyQueueException();
else
{
this.numTransactions--;
Transaction dummytransaction= new Transaction();
dummytransaction=this.firstTransaction;
this.firstTransaction=this.firstTransaction.next;
return dummytransaction;
}
  }

  public int size() {
    return this.numTransactions;
  }
}
