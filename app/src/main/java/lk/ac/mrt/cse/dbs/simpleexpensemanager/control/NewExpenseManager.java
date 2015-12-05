package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.NewAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.NewTransactionDAO;

/**
 * Created by Jayan on 12/5/2015.
 */
public class NewExpenseManager extends ExpenseManager {


    private Context context = null;
    public NewExpenseManager( Context context) throws ExpenseManagerException {
        this.context = context;
        setup();

    }
    @Override
    public void setup() throws ExpenseManagerException {
        TransactionDAO transactionDAO = new NewTransactionDAO(context);
        setTransactionsDAO(transactionDAO);



        AccountDAO persistentAccountDAO = new NewAccountDAO(context);
        setAccountsDAO(persistentAccountDAO);

    }
}
