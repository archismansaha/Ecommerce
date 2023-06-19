package com.example.ecommerce;


import java.sql.ResultSet;

public class OrderExecution {
    int u_id;
    int quantity;
    Product product;

    public OrderExecution(int u_id, int quantity, Product product) {
        this.u_id = u_id;
        this.quantity = quantity;
        this.product = product;
    }

    public boolean execute(){
        DBconnector conn = new DBconnector();
        int grp_id;
        ResultSet res = conn.getQueryTable("select max(grp_o_id) from orders");
        try {
            if(res.next()){
                grp_id = res.getInt(1) + 1;
            } else{
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        int update = conn.execute("insert into orders(grp_o_id, u_id, p_id, status, bill_amt, quantity) values("+grp_id+", "+u_id+", "+product.getId()+", 'Order Placed', "+product.getPrice()*quantity+", "+quantity+")");
        try {
            if(update != -1)
                return true;
            else return false;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean addToCart(){
        DBconnector conn = new DBconnector();
        int update = conn.execute("insert into cart(u_id, p_id, quantity) values("+u_id+", "+product.getId()+", "+quantity+")");
        try{
            if(update != -1)
                return true;
            else return false;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}