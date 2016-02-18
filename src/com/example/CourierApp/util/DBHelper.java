package com.example.CourierApp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.CourierApp.dao.DaoMaster;
import com.example.CourierApp.dao.PersonDao;
import com.example.CourierApp.entity.Person;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.List;

/**
 * Created by liuzwei on 2015/3/13.
 */
public class DBHelper {
    private static Context mContext;
    private static DBHelper instance;
    private static DaoMaster.DevOpenHelper helper;
    private PersonDao personDao;
    private static SQLiteDatabase db;
    private static DaoMaster daoMaster;
    private DBHelper(){}
    public static DBHelper getInstance(Context context){
        if (instance == null){
            instance = new DBHelper();
            if (mContext == null){
                mContext = context;
            }
            helper = new DaoMaster.DevOpenHelper(context, "person_db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            instance.personDao = daoMaster.newSession().getPersonDao();
        }
        return instance;
    }

    public List<Person> selectPersons(){
        return personDao.loadAll();
    }

    public Person findByPhone(String phone){
        QueryBuilder qb = personDao.queryBuilder();
        qb.where(PersonDao.Properties.Phone.eq(phone));
        return (Person)qb.unique();
    }

    public void  addPerson(Person person){
        personDao.insert(person);
    }
}
