package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Jayan on 12/4/2015.
 */
public class NewAccountDAO implements AccountDAO {

    private Context context;
    public NewAccountDAO(Context context)  {
        this.context = context;
    }

    @Override
    public List<String> getAccountNumbersList() {
        DB_Helper db_helper = DB_Helper.getInstance(context);
        SQLiteDatabase db = db_helper.getReadableDatabase();
        String[] resultString = {
                DB_Helper.COLUMN_NAME_ACCOUNT_NO
        };
        String sortOrder =DB_Helper.COLUMN_NAME_ACCOUNT_NO;
        Cursor c = db.query(DB_Helper.TABLE_NAME_ACCOUNT, resultString,null,null,null,null,sortOrder,null);

        ArrayList<String> results = new ArrayList<>();
        while (c.moveToNext())
        {
            results.add(c.getString(c.getColumnIndex(DB_Helper.COLUMN_NAME_ACCOUNT_NO)));
        }
        return results;
    }

    @Override
    public List<Account> getAccountsList() {
        return null;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return null;
    }

    @Override
    public void addAccount(Account account) {

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

    }
}
