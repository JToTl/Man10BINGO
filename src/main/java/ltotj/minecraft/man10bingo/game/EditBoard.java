package ltotj.minecraft.man10bingo.game;

import ltotj.minecraft.man10bingo.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EditBoard {

    public InventoryGUI inv1,inv2;
    Player player;

    public EditBoard(Player player){
        this.player=player;
        inv1=new InventoryGUI(54,"アイテム（数字）一覧：１〜４５");
        inv2=new InventoryGUI(54,"アイテム（数字）一覧：４６〜７５");
        for(int i=0;i<8;i++){
            inv1.setItem(45+i, Material.WHITE_STAINED_GLASS_PANE,"");
            inv2.setItem(45+i, Material.WHITE_STAINED_GLASS_PANE,"");
        }
        reset();
        inv1.setItem(53,Material.LIME_STAINED_GLASS_PANE,"§a４６〜７５へ");
        inv2.setItem(53,Material.LIME_STAINED_GLASS_PANE,"§a１〜４５へ");
        inv1.setItem(49,Material.RED_STAINED_GLASS_PANE,"§4自分のビンゴに戻る");
        inv2.setItem(49,Material.RED_STAINED_GLASS_PANE,"§4自分のビンゴに戻る");
    }

    public void putUsedIcon(int num){
        if(num<=45)inv1.setItem(num-1,Material.BARRIER,"§4使用中");
        else if(num<=75) inv2.setItem(num-46,Material.BARRIER,"§4使用中");
    }

    public void removeUsedIcon(int num){
        if(num<=45){
            inv1.setPanel(num-1,num);
            inv1.addLore(num-1,"§bクリックで設定");
        }
        else if(num<=75){
            inv2.setPanel(num-46,num);
            inv2.addLore(num-46,"§bクリックで設定");
        }
    }

    public void reset(){
        for(int i=1;i<46;i++){
            inv1.setPanel(i-1,i);
            inv1.addLore(i-1,"§bクリックで設定");
        }
        for(int i=46;i<76;i++){
            inv2.setPanel(i-46,i);
            inv2.addLore(i-46,"§bクリックで設定");
        }
    }
}
