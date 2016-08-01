package people;

import act.Kill;

/**
 * Created by geyao on 16/7/7.
 */
public class Hunter extends Player implements Kill {

    public Hunter( int number ){ // 猎人构造函数
        super(number);
        this.setIdentity(2);
    }

    public int kill( Player player ){ // 猎人击杀函数
        if(player.getalive()){
            player.die();
            return 4;
        }
        else return 0;
    }
}
