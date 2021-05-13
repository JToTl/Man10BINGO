package ltotj.minecraft.man10bingo;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class EventList implements Listener {

    List<Component> invNameList=new ArrayList<>();

    public EventList(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private boolean matchName(ItemStack item, String name) {
        return item!=null&&item.getItemMeta()!=null&&item.getItemMeta().displayName() != null&&item.getItemMeta().displayName().equals(Component.text(name));
    }

    private void playSoundPl(Player player, Sound sound,float pitch){
        player.playSound(player.getLocation(),sound,2,pitch);
    }

    @EventHandler
    public void ClickGUI(InventoryClickEvent e){
        ItemStack item=e.getCurrentItem();
        if(item==null)return;
        Player player=(Player)e.getWhoClicked();
        if(matchName(e.getInventory().getItem(22),"フリースポット")||e.getView().title().equals(Component.text("アイテム（数字）一覧：１〜４５"))||e.getView().title().equals(Component.text("アイテム（数字）一覧：４６〜７５")))e.setCancelled(true);
        if(GlobalClass.bingo==null||!GlobalClass.bingo.playerList.containsKey(player.getUniqueId()))return;
        BINGO.BINGOData bingoData = GlobalClass.bingo.playerList.get(player.getUniqueId());
        int slot=e.getSlot();
        if(matchName(item,"§4自分のビンゴに戻る")){
            player.openInventory(bingoData.bingoCard.inv.inv);
            e.setCancelled(true);
        }
        else if(e.getView().title().equals(Component.text(player.getName()+"のBINGOカード"))){
            e.setCancelled(true);
            if(GlobalClass.bingo.canEdit) {
                int ins=slot-(2+(slot/9)*9)+10*(slot/9);
                if(Math.abs(ins/10 -2)<3&&Math.abs(ins-10*(ins/10)-2)<3&&ins!=22){
                    bingoData.bingoCard.editPos = ins;
                    player.openInventory(bingoData.editBoard.inv1.inv);
                }
                else if(matchName(item,"§dボードをランダムに作成する")){
                    bingoData.setRandomNum();
                    playSoundPl(player, Sound.BLOCK_CHAIN_STEP, 0);
                }
            }
        }
        else if(e.getView().title().equals(Component.text("アイテム（数字）一覧：１〜４５"))){
            e.setCancelled(true);
            if(slot<45){
                if(GlobalClass.bingo.canEdit){
                    if(!item.getType().equals(Material.BARRIER)) {
                        bingoData.setNumber(slot + 1);
                        playSoundPl(player, Sound.BLOCK_CHAIN_STEP, 0);
                    }
                    else{
                        player.sendMessage("使用中です");
                    }
                }
                else player.sendMessage("既にビンゴが始まっています");
                player.openInventory(bingoData.bingoCard.inv.inv);
            }
            else if(matchName(item,"§a４６〜７５へ"))player.openInventory(bingoData.editBoard.inv2.inv);
        }
        else if(e.getView().title().equals(Component.text("アイテム（数字）一覧：４６〜７５"))){
            e.setCancelled(true);
            if (slot < 30){
                if(GlobalClass.bingo.canEdit) {
                    if(!item.getType().equals(Material.BARRIER)) {
                        bingoData.setNumber(slot + 46);
                        playSoundPl(player, Sound.BLOCK_CHAIN_STEP, 0);
                    }
                    else {
                        player.sendMessage("使用中です");
                    }
                }
                else player.sendMessage("既にビンゴが始まっています");
                player.openInventory(bingoData.bingoCard.inv.inv);
            }
            else if(matchName(item,"§a１〜４５へ"))player.openInventory(bingoData.editBoard.inv1.inv);
        }
    }
}
