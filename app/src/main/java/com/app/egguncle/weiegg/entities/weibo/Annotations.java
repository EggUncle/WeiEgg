package com.app.egguncle.weiegg.entities.weibo;

import java.io.Serializable;

/**
 * Created by egguncle on 16.10.13.
 */
public class Annotations  implements Serializable
{
    private int shooting;

    private String client_mblogid;

    public void setShooting(int shooting){
        this.shooting = shooting;
    }
    public int getShooting(){
        return this.shooting;
    }
    public void setClient_mblogid(String client_mblogid){
        this.client_mblogid = client_mblogid;
    }
    public String getClient_mblogid(){
        return this.client_mblogid;
    }
}
