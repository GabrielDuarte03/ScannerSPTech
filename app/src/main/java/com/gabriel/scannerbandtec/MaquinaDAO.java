package com.gabriel.scannerbandtec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MaquinaDAO {


    private Conexao conexao;
    private SQLiteDatabase banco;

    public MaquinaDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();

    }

    public long inserir(Maquina maquina){

        ContentValues values = new ContentValues();
        values.put("hsl",maquina.getHsl());
        values.put("modelo",maquina.getModelo());
        values.put("serial",maquina.getSerial());
        values.put("patrimonio",maquina.getPatrimonio());

        return banco.insert("tbMaquina",null,values);
    }

    public List<Maquina> listar(){
        List<Maquina> maquinas = new ArrayList<>();
        Cursor cursor = banco.query("tbMaquina",new String[]{"idMaquina","hsl","modelo","serial","patrimonio"},null,null,null,null,null);

        while(cursor.moveToNext()){
            Maquina maquina = new Maquina();
            maquina.setIdMaquina(cursor.getInt(0));
            maquina.setHsl(cursor.getString(1));
            maquina.setModelo(cursor.getString(2));
            maquina.setSerial(cursor.getString(3));
            maquina.setPatrimonio(cursor.getString(4));

            maquinas.add(maquina);

        }
        return maquinas;
    }
}
