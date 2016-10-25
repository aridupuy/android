package com.cobrodigital.com.cobrodigital2.Factory;

import android.content.Context;

import com.cobrodigital.com.cobrodigital2.Model.Model;
import com.cobrodigital.com.cobrodigital2.Model.Pagador;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Ariel on 23/10/16.
 */
public class pagadorFactory
{
    /**
     /**
     * Created by Ariel on 23/10/16.
     */
    private pagadorFactory instance;
    private Dao dao;
    public pagadorFactory getInstance(Context context) throws ClassNotFoundException, SQLException {
        if(instance==null)
            instance=new pagadorFactory(context);
        return instance;
    }
    public pagadorFactory(Context context) throws ClassNotFoundException, SQLException {
        OrmLiteSqliteOpenHelper helper= OpenHelperManager.getHelper(context,Model.class);
        TableUtils.createTableIfNotExists(helper.getConnectionSource(),Pagador.class);
        this.dao=helper.getDao(Pagador.class);
    }
    public void guardar(Pagador objeto) throws SQLException {
        dao.create(objeto);

    }
    public List<Pagador> select() throws SQLException {
        System.out.println(dao);
        return dao.queryForAll();
    }
    public List<Pagador> select(Map<String,Pagador> Pagadores) throws SQLException{
        return dao.queryForFieldValues(Pagadores);
    }
    public  boolean delete(Pagador object) throws SQLException {
        if(dao.delete(object)!=0)
            return true;
        return false;
    }
}
