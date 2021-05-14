package ltotj.minecraft.man10bingo;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryGUI {

    public Inventory inv;

    public InventoryGUI(int i, String name){
        inv= Bukkit.createInventory(null, i, Component.text(name));
    }

    public void setPanel(int slot,int num){
        if(num==0){
            setItem(slot,Material.BARRIER,"未設定");
            return;
        }
        setItem(slot,Material.valueOf(GlobalClass.config.getString("items."+num+".material")),"§e§l"+num+"§dに対応");
    }

    public void renameItem(int slot,String name){
        ItemStack item=inv.getItem(slot);
        if(item==null)return;
        ItemMeta meta=item.getItemMeta();
        meta.displayName(Component.text(name));
        item.setItemMeta(meta);
        inv.setItem(slot,item);
    }

    public void reloreItem(int slot,String... lore){
        ItemStack item=inv.getItem(slot);
        if(item==null)return;
        ItemMeta meta=item.getItemMeta();
        meta.lore(listToComponent(lore));
        item.setItemMeta(meta);
        inv.setItem(slot,item);
    }

    public void addLore(int slot,String... lore){
        ItemStack item=inv.getItem(slot);
        if(item==null)return;
        ItemMeta meta=item.getItemMeta();
        List<Component> clore=meta.lore();
        clore.addAll(listToComponent(lore));
        meta.lore(clore);
        item.setItemMeta(meta);
        inv.setItem(slot,item);
    }

    public void enchantItem(int slot){
        ItemStack item=inv.getItem(slot);
        if(item==null)return;
        item.addUnsafeEnchantment(Enchantment.LUCK,1);
        inv.setItem(slot,item);
    }

    public void setItem(int slot,Material material,String name,String... lore){
        inv.setItem(slot,createGuiItem(material,name,lore));
    }

    public void setItem(int slot,int amount,Material material,String name,String... lore){
        inv.setItem(slot,createGuiItem(amount,material,name,lore));
    }

    protected ItemStack createGuiItem(final Material material, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the lore of the item
        meta.lore(listToComponent(lore));

        item.setItemMeta(meta);

        return item;
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.displayName(Component.text(name));

        // Set the lore of the item
        meta.lore(listToComponent(lore));

        item.setItemMeta(meta);

        return item;
    }

    protected ItemStack createGuiItem(int amount,final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.displayName(Component.text(name));

        // Set the lore of the item
        meta.lore(listToComponent(lore));

        item.setItemMeta(meta);

        return item;
    }

    private List<Component> listToComponent(String... lore){
        List<Component> components=new ArrayList<>();
        for (String s : lore) components.add(Component.text(s));
        return components;
    }
}
