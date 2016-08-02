package characters;

import act.Kill;

/**
 * Created by geyao on 16/7/7.
 */
public class Hunter extends Village implements Kill {

    public Hunter(  ){ // 猎人构造函数
        super();
        this.setIdentity(2);
    }

    public int kill( Village village){ // 猎人击杀函数
        if(village.getalive()){
            village.die();
            return 4;
        }
        else return 0;
    }
}
