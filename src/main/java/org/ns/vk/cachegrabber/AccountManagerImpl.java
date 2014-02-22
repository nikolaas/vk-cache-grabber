package org.ns.vk.cachegrabber;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ns.vk.cachegrabber.api.Account;
import org.ns.vk.cachegrabber.api.AccountManager;
import org.ns.vk.cachegrabber.store.DataStore;
import org.ns.vk.cachegrabber.store.DataStoreFactory;

/**
 *
 * @author stupak
 */
public class AccountManagerImpl implements AccountManager {

    public static final String ACCOUNT_STORAGE = "accountStorage";
    public static final String CURRENT_ACCOUNT = "currentAccount";
    
    private final Lock lock = new ReentrantLock();
    private Future<Account> current;
    private final DataStoreFactory dataStoreFactory;
    private DataStore<Account> accountStore;
    
    private final Callable<Account> getAccountFunction = new Callable<Account>() {

        @Override
        public Account call() throws Exception {
            Account account = null;
            DataStore<Account> accountStorage = getAccountStorage();
            if ( accountStorage != null ) {
                try {
                    account = accountStorage.get(CURRENT_ACCOUNT);
                } catch (IOException ex) {
                    Logger.getLogger(AccountManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if ( account == null ) {
                //просим у пользователя
            }
            if ( account != null ) {
                saveAccount(account);
            }
            return account;
        }
    };

    public AccountManagerImpl(DataStoreFactory dataStoreFactory) {
        this.dataStoreFactory = dataStoreFactory;
    }
    
    @Override
    public Account getCurrentAccount() {
        if ( current == null ) {
            newTask();
        }
        Account account = null;
        try {
            account = current.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(AccountManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return account;
    }
    
    private void newTask() {
        lock.lock();
        try {
            if ( current == null ) {
                current = new FutureTask(getAccountFunction);
            }
        } finally {
            lock.unlock();
        }
    }

    private void saveAccount(Account account) {
        DataStore<Account> accountStorage = getAccountStorage();
        if ( accountStorage != null ) {
            try {
                accountStorage.set(CURRENT_ACCOUNT, account);
            } catch (IOException ex) {
                Logger.getLogger(AccountManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private DataStore<Account> getAccountStorage() {
        if ( accountStore == null ) {
            if ( dataStoreFactory != null ) {
                try {
                    accountStore = dataStoreFactory.getDataStore(ACCOUNT_STORAGE);
                } catch (IOException ex) {
                    Logger.getLogger(AccountManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return accountStore;
    }
    
    
}
