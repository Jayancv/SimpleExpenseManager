package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
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
public class PersistentAccountDAO implements AccountDAO {

    private Context context;
    private DB_Helper db_helper;

    public PersistentAccountDAO(Context context)  {
        this.context = context;
        db_helper = DB_Helper.getInstance(context);

    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = db_helper.getReadableDatabase();

        String[] columString = {
                DB_Helper.ACCOUNT_NO
        };
        String sortOrder =DB_Helper.ACCOUNT_NO;
        Cursor c = db.query(DB_Helper.TABLE_NAME_ACCOUNT, columString,null,null,null,null,sortOrder,null);

        ArrayList<String> results = new ArrayList<>();
        while (c.moveToNext())
        {
            results.add(c.getString(c.getColumnIndex(DB_Helper.ACCOUNT_NO)));     //add account no s one by one
        }
        return results;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = db_helper.getReadableDatabase();

        String[] columString = {
                DB_Helper.ACCOUNT_NO,
                DB_Helper.BANK_NAME,
                DB_Helper.ACCOUNT_HOLDER_NAME,
                DB_Helper.BALANCE
        };
        String sortOrder = DB_Helper.ACCOUNT_NO;
        Cursor c = db.query(DB_Helper.TABLE_NAME_ACCOUNT, columString,null,null,null,null,sortOrder,null);

        ArrayList<Account> results = new ArrayList<>();
        while (c.moveToNext())
        {
            Account account = new Account(c.getString(c.getColumnIndex(DB_Helper.ACCOUNT_NO)),
                    c.getString(c.getColumnIndex(DB_Helper.BANK_NAME)),
                    c.getString(c.getColumnIndex(DB_Helper.ACCOUNT_HOLDER_NAME)),
                    c.getFloat(c.getColumnIndex(DB_Helper.BALANCE)));

            results.add(account);
        }
        return results;


    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = db_helper.getReadableDatabase();

        String[] columString = {
                DB_Helper.ACCOUNT_NO,
                DB_Helper.BANK_NAME,
                DB_Helper.ACCOUNT_HOLDER_NAME,
                DB_Helper.BALANCE
        };
        String sortOrder = DB_Helper.ACCOUNT_NO + " DESC";
        Cursor c = db.query(DB_Helper.TABLE_NAME_ACCOUNT, columString,DB_Helper.ACCOUNT_NO + "='" + accountNo +"'",null,null,null,sortOrder,null);

        if(c.moveToNext()) {
            Account ac = new Account(c.getString(c.getColumnIndex(DB_Helper.ACCOUNT_NO)),
                    c.getString(c.getColumnIndex(DB_Helper.BANK_NAME)),
                    c.getString(c.getColumnIndex(DB_Helper.ACCOUNT_HOLDER_NAME)),
                    c.getFloat(c.getColumnIndex(DB_Helper.BALANCE)));

            return  ac;
        }
        else {
            throw new InvalidAccountException("Account number invalid");
        }
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = db_helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_Helper.ACCOUNT_NO,account.getAccountNo());
        values.put(DB_Helper.ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        values.put(DB_Helper.BANK_NAME,account.getBankName());
        values.put(DB_Helper.BALANCE,account.getBalance());

        db.insert(DB_Helper.TABLE_NAME_ACCOUNT,null,values); db_helper = DB_Helper.getInstance(context);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = db_helper.getWritableDatabase();

        String selection = DB_Helper.ACCOUNT_NO + "= ?'";
        String[] selectionArgs = { accountNo };

        db.delete(DB_Helper.TABLE_NAME_ACCOUNT,selection,selectionArgs);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = db_helper.getWritableDatabase();
        Account ac = getAccount(accountNo);
        ContentValues values = new ContentValues();

        switch (expenseType)
        {
            case EXPENSE:
                values.put(DB_Helper.BALANCE,ac.getBalance() - amount);
                break;
            case INCOME:
                values.put(DB_Helper.BALANCE,ac.getBalance() + amount);
                break;
        }

        String selection = DB_Helper.ACCOUNT_NO + " = ?";
        String[] selectionArgs = { accountNo };

        db.update(DB_Helper.TABLE_NAME_ACCOUNT,values, selection,selectionArgs);


    }
}
