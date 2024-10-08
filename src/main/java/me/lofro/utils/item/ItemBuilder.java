package me.lofro.utils.item;

import me.lofro.utils.DataContainer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import me.lofro.utils.ChatColorFormatter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemBuilder {

    protected ItemStack is;

    protected ItemMeta im;

    public ItemBuilder() {
        this(Material.AIR);
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(ItemStack itemStack) {
        this.is = itemStack;
    }

    public ItemBuilder setAmount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder setCustomModelData(int data) {
        this.im = this.is.getItemMeta();
        this.im.setCustomModelData(data);
        this.is.setItemMeta(this.im);
        return this;
    }

    public int getCustomModelData() {
        this.im = this.is.getItemMeta();
        return this.im.getCustomModelData();
    }

    public ItemBuilder setID(String id) {
        this.im = this.is.getItemMeta();
        this.im.getPersistentDataContainer().set(DataContainer.key(id), PersistentDataType.BYTE, (byte) 1);
        this.is.setItemMeta(this.im);
        return this;
    }

    public boolean hasID(String id) {
        return DataContainer.has(this.is.getItemMeta(), id, PersistentDataType.BYTE);
    }

    public ItemBuilder setUnbreakable(boolean b) {
        this.im = this.is.getItemMeta();
        this.im.setUnbreakable(b);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        this.im = this.is.getItemMeta();
        this.im.displayName(ChatColorFormatter.stringToComponent(name));
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        this.im = this.is.getItemMeta();
        if (this.im instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) this.im).addStoredEnchant(enchantment, level, true);
        } else {
            this.im.addEnchant(enchantment, level, true);
        }
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addEnchants(Map<Enchantment, Integer> enchantments) {
        this.im = this.is.getItemMeta();
        if (!enchantments.isEmpty())
            for (Enchantment enchantment : enchantments.keySet())
                this.im.addEnchant(enchantment, enchantments.get(enchantment), true);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag... itemFlag) {
        this.im = this.is.getItemMeta();
        this.im.addItemFlags(itemFlag);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setLore(List<Component> lore) {
        this.im = this.is.getItemMeta();
        this.im.lore(lore);
        this.is.setItemMeta(this.im);
        return this;
    }


    public ItemBuilder setLore(String... lore) {
        this.setLore(Arrays.stream(lore).map(ChatColorFormatter::stringToComponent).collect(Collectors.toList()));
        return this;
    }


    public <T, Z> ItemBuilder addData(String key, PersistentDataType<T, Z> type, Z value) {
        this.im = this.is.getItemMeta();
        im.getPersistentDataContainer().set(DataContainer.key(key), type, value);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addData(String key, String value) {
        this.addData(key, PersistentDataType.STRING, value);
        return this;
    }

    public ItemBuilder addData(String key, byte value) {
        this.addData(key, PersistentDataType.BYTE, value);
        return this;
    }

    public ItemBuilder addAttributeModifier(Attribute attribute, AttributeModifier attributeModifier) {

        this.im = this.is.getItemMeta();
        this.im.addAttributeModifier(attribute, attributeModifier);
        this.is.setItemMeta(this.im);
        return this;

    }

    public ItemBuilder addAttributeModifier(Attribute attribute, double value, AttributeModifier.Operation operation, EquipmentSlot slot) {
        this.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attribute.getKey().getKey(),value, operation, slot));
        return this;
    }

    public ItemStack build() {
        return this.is;
    }
}
