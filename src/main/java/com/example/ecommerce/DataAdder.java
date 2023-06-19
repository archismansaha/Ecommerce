package com.example.ecommerce;

import java.io.*;
import java.util.*;
public class DataAdder {
    public static void main(String[] args) throws Exception {
        DBconnector conn = new DBconnector();
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\shubh\\Downloads\\Compressed\\archive\\data.csv"));
        String line = "";
        int c = 0;
        try{
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                try{
                    String[] product = line.split(",");
                    String name = product[2];
                    int price = (int)(Float.parseFloat(product[5]) * 80);
                    float rating = (float) (2 + Math.random() * (3));
                    conn.execute("insert into products(p_name, price, p_rating) values('"+name+"', "+price+", "+rating+")");
                    System.out.println(c + " Product Added");
                    c++;
                } catch (Exception e){
                    continue;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}