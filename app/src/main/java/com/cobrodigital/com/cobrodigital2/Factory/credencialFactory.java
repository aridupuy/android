package com.cobrodigital.com.cobrodigital2.Factory;

import android.content.Context;

import com.cobrodigital.com.cobrodigital2.Model.Credencial;
import com.cobrodigital.com.cobrodigital2.Model.Model;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
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
public class credencialFactory {
    private credencialFactory instance;
    private Dao dao;

    public credencialFactory getInstance(Context context) throws ClassNotFoundException, SQLException {
        if(instance==null)
            instance=new credencialFactory(context);
        return instance;
    }
    public credencialFactory(Context context) throws ClassNotFoundException, SQLException {
        OrmLiteSqliteOpenHelper helper= OpenHelperManager.getHelper(context,Model.class);
        TableUtils.createTableIfNotExists(helper.getConnectionSource(),Credencial.class);
        this.dao=helper.getDao(Credencial.class);
    }
    public void guardar(Credencial objeto) throws SQLException {
        dao.create(objeto);

    }
    public List<Credencial> select() throws SQLException {
        System.out.println(dao);
        return dao.queryForAll();
    }
    public List<Credencial> select(Map<String,Credencial> Credenciales) throws SQLException{
        return dao.queryForFieldValues(Credenciales);
    }
    public  boolean delete(Credencial object) throws SQLException {
        if(dao.delete(object)!=0)
            return true;
        return false;
    }
}
