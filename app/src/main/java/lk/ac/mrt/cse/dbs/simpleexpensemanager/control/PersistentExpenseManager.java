package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

/**
 * Created by Jayan on 12/5/2015.
 */
public class PersistentExpenseManager extends ExpenseManager {


    private Context context = null;
    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.context = context;
        setup();

    }
    @Override
    public void setup() throws ExpenseManagerException {
        TransactionDAO transactionDAO = new PersistentTransactionDAO(context);
        setTransactionsDAO(transactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(context);
        setAccountsDAO(persistentAccountDAO);

    }
}
