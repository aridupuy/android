package com.cobrodigital.com.cobrodigital2.Factory;

import android.content.Context;

import com.cobrodigital.com.cobrodigital2.Model.Boleta;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Ariel on 23/10/16.
 */
public class boletaFactory {
    /**
     /**
     * Created by Ariel on 23/10/16.
     */
    private boletaFactory instance;
    private Dao dao;

    public boletaFactory getInstance(){
        if(instance==null)
            instance=new boletaFactory();
        return instance;
    }
    public void factory(Context context) throws ClassNotFoundException {
        OrmLiteSqliteOpenHelper base= OpenHelperManager.getHelper(context,Boleta.class);
        try{
            this.dao=base.getDao(Boleta.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void guardar(Boleta objeto) throws SQLException {
        dao.create(objeto);

    }
    public List<Boleta> select() throws SQLException {
        return dao.queryForAll();
    }
    public List<Boleta> select(Map<String,Boleta> Boletaes) throws SQLException{
        return dao.queryForFieldValues(Boletaes);
    }
    public  boolean delete(Boleta object) throws SQLException {
        if(dao.delete(object)!=0)
            return true;
        return false;
    }
}
