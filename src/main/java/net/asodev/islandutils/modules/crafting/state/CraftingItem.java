package net.asodev.islandutils.modules.crafting.state;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.asodev.islandutils.modules.crafting.CraftingMenuType;
import net.asodev.islandutils.util.FontUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CraftingItem {

    private Component title;
    private Item type;
    private ResourceLocation itemModel;

    private long finishesCrafting;
    private CraftingMenuType craftingMenuType;
    private boolean hasSentNotif = false;
    private int slot;

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.add("title", ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, title).getOrThrow());
        object.addProperty("type", BuiltInRegistries.ITEM.getKey(type).toString());
        object.addProperty("itemModel", itemModel.toString());
        object.addProperty("finishesCrafting", finishesCrafting);
        object.addProperty("craftingMenuType", craftingMenuType.name());
        object.addProperty("hasSentNotif", hasSentNotif);
        object.addProperty("slot", slot);
        return object;
    }

    public static CraftingItem fromJson(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        CraftingItem item = new CraftingItem();

        JsonElement jsonTitle = object.get("title");
        item.setTitle(ComponentSerialization.CODEC.decode(JsonOps.INSTANCE, jsonTitle).getOrThrow().getFirst());

        ResourceLocation typeKey = ResourceLocation.parse(object.get("type").getAsString());
        Holder.Reference<Item> itemType = BuiltInRegistries.ITEM.get(typeKey)
                .orElseThrow(() -> new IllegalStateException("Item with type " + typeKey + " does not exist."));
        item.setType(itemType.value());

        item.setItemModel(ResourceLocation.parse(object.get("itemModel").getAsString()));

        String craftingTypeString = object.get("craftingMenuType").getAsString();
        item.setCraftingMenuType(CraftingMenuType.valueOf(craftingTypeString.toUpperCase()));

        item.setFinishesCrafting(object.get("finishesCrafting").getAsLong());
        item.setHasSentNotif(object.get("hasSentNotif").getAsBoolean());
        item.setSlot(object.get("slot").getAsInt());

        return item;
    }

    public ItemStack getStack() {
        ItemStack stack = new ItemStack(type);
        stack.set(DataComponents.ITEM_MODEL, getItemModel());
        return stack;
    }

    public boolean isComplete() {
        return System.currentTimeMillis() >= this.getFinishesCrafting();
    }

    public Component getTitle() {
        return title;
    }

    public Component getTypeIcon() {
        return this.getCraftingMenuType() == CraftingMenuType.FORGE ? FontUtils.FUSION_CRAFTING : FontUtils.CRAFTING;
    }

    public void setTitle(Component title) {
        this.title = title;
    }

    public Item getType() {
        return type;
    }

    public void setType(Item type) {
        this.type = type;
    }

    public ResourceLocation getItemModel() {
        return itemModel;
    }

    public void setItemModel(ResourceLocation itemModel) {
        this.itemModel = itemModel;
    }

    public long getFinishesCrafting() {
        return finishesCrafting;
    }

    public void setFinishesCrafting(long finishesCrafting) {
        this.finishesCrafting = finishesCrafting;
    }

    public CraftingMenuType getCraftingMenuType() {
        return craftingMenuType;
    }

    public void setCraftingMenuType(CraftingMenuType craftingMenuType) {
        this.craftingMenuType = craftingMenuType;
    }

    public boolean hasSentNotif() {
        return hasSentNotif;
    }

    public void setHasSentNotif(boolean hasSentNotif) {
        this.hasSentNotif = hasSentNotif;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public String toString() {
        return "CraftingItem{" +
                "title=" + title +
                ", type=" + type +
                ", itemModel=" + itemModel.toString() +
                ", finishesCrafting=" + finishesCrafting +
                ", craftingMenuType=" + craftingMenuType +
                ", hasSentNotif=" + hasSentNotif +
                ", slot=" + slot +
                '}';
    }
}
