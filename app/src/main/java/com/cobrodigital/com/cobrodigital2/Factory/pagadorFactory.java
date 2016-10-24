package com.cobrodigital.com.cobrodigital2.Factory;

import android.content.Context;

import com.cobrodigital.com.cobrodigital2.Model.Pagador;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

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

    public pagadorFactory getInstance(){
        if(instance==null)
            instance=new pagadorFactory();
        return instance;
    }
    public void factory(Context context) throws ClassNotFoundException {
        OrmLiteSqliteOpenHelper base= OpenHelperManager.getHelper(context,Pagador.class);
        try{
            this.dao=base.getDao(Pagador.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void guardar(Pagador objeto) throws SQLException {
        dao.create(objeto);

    }
    public List<Pagador> select() throws SQLException {
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
