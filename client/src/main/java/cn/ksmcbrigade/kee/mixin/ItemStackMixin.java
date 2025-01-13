package cn.ksmcbrigade.kee.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Shadow
	public abstract Item getItem();

	@Shadow
	public abstract void setDamage(int damage);

	@Shadow
	public int size;
	@Unique
	private File file = new File("kee-config.json");

	@Inject(method = "startUsing",at = @At("TAIL"))
	private void using(World player, PlayerEntity par2, CallbackInfoReturnable<ItemStack> cir) throws IOException {
		if(this.getItem().getTranslationKey().contains("grass")){
			par2.heal(health());
			this.size--;
		}
	}

	@Unique
	private int health() throws IOException {
		if(!file.exists()){
			JsonObject object = new JsonObject();
			object.addProperty("hunger_points",3);
			FileUtils.writeStringToFile(file,object.toString());
		}
		JsonObject obj = JsonParser.parseString(FileUtils.readFileToString(file)).getAsJsonObject();
		return obj.get("hunger_points").getAsInt();
	}
}
