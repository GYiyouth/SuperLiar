package act;

import RuleAlgorithm.Alive;
import characters.Village;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by geyao on 16/8/25.
 * JButton button = new JButton("click me");
 * button.setEnabled(false);//变灰
 *
 * JTextField field = new JTextField("****");
 * field.setEditable(false);//不能编辑
 */
public class MyButton extends JButton {
    private Village village;
    private final int index;
    public MyButton( int i){ // i 号玩家的按钮

        this.setSize(50, 20);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVisible(true);
        this.setBorderPainted(false); // 不显示边框
        this.setText((i+1)+"号");
        if (Alive.intPlayers.contains(i)){
            index = i;
            this.village = Alive.Players.get(i);
        }else { // 玩家不存在
            index = -1;
            this.village = null;
            this.setEnabled(false);
        }
    }

    public int getNumber(){
        return index;
    }

    public void refresh(){ // 检查玩家存活状态, 确定按钮可用情况
        if (Alive.intPlayers.contains(getNumber()) )
            this.setEnabled(true); // 玩家存活, 按钮可按
        else
            this.setEnabled(false);

    }
    public void refresh(ArrayList intplayers){ // 根据特定数组检查玩家存活状态, 确定按钮可用情况
        if (intplayers.contains(getNumber()) )
            this.setEnabled(true); // 玩家存活, 按钮可按
        else
            this.setEnabled(false);
    }
}
