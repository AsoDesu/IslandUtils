package net.asodev.islandutils.modules.crafting.state;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.asodev.islandutils.modules.crafting.CraftingMenuType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;

import static net.asodev.islandutils.util.ChatUtils.iconsFontStyle;

public class CraftingItem {

    private Component title;
    private Item type;
    private int customModelData;

    private long finishesCrafting;
    private CraftingMenuType craftingMenuType;
    private boolean hasSentNotif = false;
    private int slot;

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("title", Component.Serializer.toJson(title, RegistryAccess.EMPTY));
        object.addProperty("type", BuiltInRegistries.ITEM.getKey(type).toString());
        object.addProperty("customModelData", customModelData);
        object.addProperty("finishesCrafting", finishesCrafting);
        object.addProperty("craftingMenuType", craftingMenuType.name());
        object.addProperty("hasSentNotif", hasSentNotif);
        object.addProperty("slot", slot);
        return object;
    }
    public static CraftingItem fromJson(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        CraftingItem item = new CraftingItem();

        String jsonTitle = object.get("title").getAsString();
        item.setTitle( Component.Serializer.fromJson(jsonTitle, RegistryAccess.EMPTY) );

        ResourceLocation typeKey = ResourceLocation.parse(object.get("type").getAsString());
        item.setType( BuiltInRegistries.ITEM.get(typeKey) );

        item.setCustomModelData( object.get("customModelData").getAsInt() );


        String craftingTypeString = object.get("craftingMenuType").getAsString();
        item.setCraftingMenuType( CraftingMenuType.valueOf(craftingTypeString.toUpperCase()) );

        item.setFinishesCrafting( object.get("finishesCrafting").getAsLong() );
        item.setHasSentNotif( object.get("hasSentNotif").getAsBoolean() );
        item.setSlot( object.get("slot").getAsInt() );

        return item;
    }

    public ItemStack getStack() {
        ItemStack stack = new ItemStack(type);
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(customModelData));
        return stack;
    }
    public boolean isComplete() {
        return System.currentTimeMillis() >= this.getFinishesCrafting();
    }

    public Component getTitle() {
        return title;
    }
    public Component getTypeIcon() {
        String icon = this.getCraftingMenuType() == CraftingMenuType.FORGE ? "\ue006" : "\ue007";
        return Component.literal(icon).withStyle(iconsFontStyle);
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

    public int getCustomModelData() {
        return customModelData;
    }
    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
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
                ", customModelData=" + customModelData +
                ", finishesCrafting=" + finishesCrafting +
                ", craftingMenuType=" + craftingMenuType +
                ", hasSentNotif=" + hasSentNotif +
                ", slot=" + slot +
                '}';
    }
}
