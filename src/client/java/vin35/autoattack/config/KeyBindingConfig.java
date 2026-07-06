package vin35.autoattack.config;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import vin35.autoattack.AutoAttack;

public class KeyBindingConfig extends AutoAttack {

    private static final KeyMapping.Category AUTOATTACK_CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("autoattack", "autoattack"));

    private static KeyMapping preventsHittingBlocksKeyBinding;
    private static KeyMapping afkAttackKeyBinding;

    @Override
    public void onInitializeClient() {
        preventsHittingBlocksKeyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.autoattack.preventsHittingBlocks", // The translation key of the keybinding's name
                InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_H, // The keycode of the key
                AUTOATTACK_CATEGORY // The keybinding's category.
        ));
        afkAttackKeyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.autoattack.afkAttack", // The translation key of the keybinding's name
                InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_K, // The keycode of the key
                AUTOATTACK_CATEGORY // The keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            if (preventsHittingBlocksKeyBinding.consumeClick() && mc.player != null) {
                if (AutoAttackConfig.preventsHittingBlocks) {
                    mc.player.sendSystemMessage(Component.translatable("text.KeyBindingConfig.preventsHittingBlocks.OFF"));
                    AutoAttackConfig.preventsHittingBlocks = false;
                } else {
                    mc.player.sendSystemMessage(Component.translatable("text.KeyBindingConfig.preventsHittingBlocks.ON"));
                    AutoAttackConfig.preventsHittingBlocks = true;
                }
            }
            if (afkAttackKeyBinding.consumeClick() && mc.player != null){
                if (AutoAttackConfig.afkAttack) {
                    mc.player.sendSystemMessage(Component.translatable("text.KeyBindingConfig.afkAttack.OFF"));
                    AutoAttackConfig.afkAttack = false;
                } else {
                    mc.player.sendSystemMessage(Component.translatable("text.KeyBindingConfig.afkAttack.ON"));
                    AutoAttackConfig.afkAttack = true;
                }
            }
        });
    }
}
