package ltotj.minecraft.man10bingo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("プレイヤー以外は実行できません");
            return true;
        }
        Player player=(Player)sender;
        if(args.length!=0&&player.hasPermission("bingo.op")){
            switch (args[0]) {
                case "set":
                    if(GlobalClass.bingo!=null){
                        player.sendMessage("既にビンゴが設置されています  /bingo end  で消去可能です");
                    }
                    else if(args.length>1&&args[1].matches("[+-]?\\d*(\\.\\d+)?")&&Integer.parseInt(args[1])>0) {
                        GlobalClass.bingo = new BINGO(Integer.parseInt(args[1]));
                        for(Player p:Bukkit.getServer().getOnlinePlayers()){
                            if(p.hasPermission("bingo.op")){
                                p.sendMessage("目標ライン数"+args[1]+"のビンゴを設置しました");
                            }
                        }
                    }
                    else{
                        player.sendMessage("コマンドが不正です");
                    }
                    break;
                case "roll":
                    if (GlobalClass.bingo == null) {
                        player.sendMessage("現在ビンゴは開催されていません");
                    } else if (!GlobalClass.bingo.rollBINGO()) {
                        player.sendMessage("ビンゴが開始されていないか、現在ロール中です");
                    }
                    break;
                case "start":
                    if (GlobalClass.bingo == null) {
                        player.sendMessage("現在ビンゴは開催されていません");
                    } else if (GlobalClass.bingo.canEdit) {
                        GlobalClass.bingo.startBINGO();
                    }
                    break;
                case "end":
                    if (GlobalClass.bingo == null) {
                        player.sendMessage("現在ビンゴは開催されていません");
                    } else if (GlobalClass.bingo.canRoll) {
                        player.sendMessage("現在ロール中です  ロール終了後にもう一度入力してください");
                    } else {
                        GlobalClass.bingo = null;
                        player.sendMessage("ビンゴを削除しました");
                    }
                    break;
                case "view":
                    if (GlobalClass.bingo == null) {
                        player.sendMessage("現在ビンゴは開催されていません");
                    } else if (args.length > 1) {
                        Player pl = Bukkit.getPlayer(args[1]);
                        if (pl == null || !GlobalClass.bingo.playerList.containsKey(pl.getUniqueId())) {
                            player.sendMessage(args[2] + "はビンゴに参加していません");
                        }
                        player.openInventory(GlobalClass.bingo.playerList.get(pl.getUniqueId()).bingoCard.inv.inv);
                    }
                    break;
                case "number":
                    if (args.length < 2 || !args[1].matches("[+-]?\\d*(\\.\\d+)?")) {
                        player.sendMessage("/bingo number 1〜75");
                    }
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item.getType().equals(Material.AIR)) {
                        player.sendMessage("アイテム持ってください");
                    } else if(args.length<3){
                        GlobalClass.config.getConfig().set("items." + args[1] + ".material", String.valueOf(item.getType()));
                        GlobalClass.config.getConfig().set("items." + args[1] + ".name", item.getType().name());
                        GlobalClass.config.saveConfig();
                        player.sendMessage(args[1] + "に" + item.getType().name() + "を設定しました");
                    }
                    else{
                        GlobalClass.config.getConfig().set("items." + args[1] + ".material", String.valueOf(item.getType()));
                        GlobalClass.config.getConfig().set("items." + args[1] + ".name", args[2]);
                        GlobalClass.config.saveConfig();
                        player.sendMessage(args[1] + "に" + item.getType().name() + "、" + args[2] + "を設定しました");
                    }
                    break;
            }
        }
        else if(GlobalClass.bingo==null||!GlobalClass.bingo.isRunning){
            player.sendMessage("ただいまビンゴは開催されていません");
        }
        else {
            if(GlobalClass.bingo.addPlayer(player)){
                player.openInventory(GlobalClass.bingo.playerList.get(player.getUniqueId()).bingoCard.inv.inv);
            }
            else if(GlobalClass.bingo.playerList.containsKey(player.getUniqueId())){
                player.openInventory(GlobalClass.bingo.playerList.get(player.getUniqueId()).bingoCard.inv.inv);
            }
            else player.sendMessage("参加できませんでした");
        }
        return true;
    }
}
