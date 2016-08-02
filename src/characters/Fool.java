package characters;

/**
 * Created by geyao on 16/8/1.
 */
public class Fool extends Village {
    private int sheild = 1; // 免票死次数
    public Fool(){ // 构造函数
        super();
        this.setIdentity(2);
    }

    public int getSheild(){ // 查询免死次数
        return sheild;
    }

    public boolean useSheild(){ // 使用免死权利
        if (sheild == 1){
            sheild--;
            return true;
        }
        return false;
    }

}
